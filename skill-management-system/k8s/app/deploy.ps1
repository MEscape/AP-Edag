# Skill Management System Kubernetes Deployment Script (PowerShell)
# Usage: .\deploy.ps1 [dev|prod]

param(
    [Parameter(Position=0)]
    [ValidateSet("dev", "prod")]
    [string]$Environment
)

$ErrorActionPreference = "Stop"

# Default image tag
$ImageTag = "latest"

# Environment parameter is mandatory
if (-not $Environment) {
    Write-Host "Error: Environment parameter is required!" -ForegroundColor Red
    Write-Host "Usage: .\deploy.ps1 [dev|prod]" -ForegroundColor Yellow
    exit 1
}

$Namespace = "skill-management-system-$Environment"

Write-Host "Deploying Skill Management System to $Environment environment..." -ForegroundColor Green

# Check if kubectl is available
try {
    kubectl version --client --output=json | Out-Null
} catch {
    Write-Host "Error: kubectl is not installed or not in PATH" -ForegroundColor Red
    exit 1
}

# Fix line endings in init script (CRITICAL for Alpine Linux)
$InitScriptPath = Join-Path $PSScriptRoot "base\postgres\init-multiple-databases.sh"
if (Test-Path $InitScriptPath) {
    Write-Host "Fixing line endings in init-multiple-databases.sh..." -ForegroundColor Cyan
    try {
        (Get-Content $InitScriptPath -Raw) -replace "`r`n","`n" | Set-Content $InitScriptPath -NoNewline
        Write-Host "Line endings fixed successfully." -ForegroundColor Green
    } catch {
        Write-Host "Warning: Could not fix line endings in init script" -ForegroundColor Yellow
        Write-Host $_.Exception.Message -ForegroundColor Yellow
    }
} else {
    Write-Host "Warning: init-multiple-databases.sh not found at $InitScriptPath" -ForegroundColor Yellow
}

# Production warning
if ($Environment -eq "prod") {
    Write-Host "WARNING: You are about to deploy to PRODUCTION!" -ForegroundColor Yellow
    Write-Host "Make sure you have updated the passwords in .env.prod and generated the secrets" -ForegroundColor Yellow
    $response = Read-Host "Continue? (y/N)"
    if ($response -ne "y" -and $response -ne "Y") {
        Write-Host "Deployment cancelled" -ForegroundColor Red
        exit 1
    }

    # --- PROD: Build & Push Images to Registry ---
    Write-Host "Checking Docker and DevOps registry availability..." -ForegroundColor Cyan
    try {
        docker version | Out-Null
    } catch {
        Write-Host "Error: Docker is not installed or not in PATH" -ForegroundColor Red
        exit 1
    }

    # Registry configuration
    $RegistryExternalHost = "registry.skill-management-devops.edag.com"
    $RegistryInternalService = "prod-sms-docker-registry-service"
    $DevOpsNamespace = "skill-management-devops-prod"
    $AppNamespace = "skill-management-system-prod"

    # Verify DevOps registry service exists
    try {
        kubectl get svc $RegistryInternalService -n $DevOpsNamespace | Out-Null
        Write-Host "Registry service found in namespace $DevOpsNamespace" -ForegroundColor Green
    } catch {
        Write-Host "Error: DevOps Docker registry service not found (prod must be up)." -ForegroundColor Red
        Write-Host "Hint: run k8s/devOps/deploy.ps1 first." -ForegroundColor Yellow
        exit 1
    }

    # Load registry credentials from app .env.prod (current directory)
    $AppProdEnvPath = Join-Path $PSScriptRoot ".env.prod"
    if (-not (Test-Path $AppProdEnvPath)) {
        Write-Host "Error: .env.prod not found at $AppProdEnvPath" -ForegroundColor Red
        Write-Host "Hint: create it via setup.ps1 or manually." -ForegroundColor Yellow
        exit 1
    }

    $envVars = @{ }
    Get-Content $AppProdEnvPath | ForEach-Object {
        if ($_ -match '^([^#][^=]+)=(.*)$') {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()
            # Remove surrounding quotes if present
            if ($value.StartsWith('"') -and $value.EndsWith('"')) {
                $value = $value.Substring(1, $value.Length - 2)
            } elseif ($value.StartsWith("'") -and $value.EndsWith("'")) {
                $value = $value.Substring(1, $value.Length - 2)
            }
            $envVars[$key] = $value
        }
    }

    $REGISTRY_USER = $envVars['REGISTRY_USER']
    $REGISTRY_PASSWORD = $envVars['REGISTRY_PASSWORD']
    if (-not $REGISTRY_USER -or -not $REGISTRY_PASSWORD) {
        Write-Host "Error: REGISTRY_USER or REGISTRY_PASSWORD missing in .env.prod" -ForegroundColor Red
        exit 1
    }

    # Docker login to external registry hostname (via Traefik HTTPS)
    Write-Host "Logging in to Docker registry $RegistryExternalHost..." -ForegroundColor Cyan
    try {
        $loginOutput = $REGISTRY_PASSWORD | docker login $RegistryExternalHost -u $REGISTRY_USER --password-stdin 2>&1
        if ($LASTEXITCODE -ne 0) {
            throw "Docker login failed with exit code $LASTEXITCODE : $loginOutput"
        }
        Write-Host "Docker login successful." -ForegroundColor Green
    } catch {
        Write-Host "Error: Docker login failed for $RegistryExternalHost" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        exit 1
    }

    # Determine repo root and build contexts
    $RepoRoot = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
    $BackendDir = Join-Path $RepoRoot "backend"
    $FrontendDir = Join-Path $RepoRoot "frontend"

    if (-not (Test-Path $BackendDir) -or -not (Test-Path $FrontendDir)) {
        Write-Host "Error: Could not locate backend or frontend directories." -ForegroundColor Red
        Write-Host "Checked: $BackendDir and $FrontendDir" -ForegroundColor Yellow
        exit 1
    }

    # Use external host for building/pushing, internal service for pod image pull
    $BackendImageExternal = "$RegistryExternalHost/skill-management-backend"
    $FrontendImageExternal = "$RegistryExternalHost/skill-management-frontend"

    # Build Backend
    Write-Host "Building backend image: ${BackendImageExternal}:${ImageTag}" -ForegroundColor Cyan
    try {
        Push-Location $BackendDir
        Write-Host "Current directory: $(Get-Location)" -ForegroundColor Gray

        docker build -t "${BackendImageExternal}:${ImageTag}" -t "${BackendImageExternal}:latest" .

        if ($LASTEXITCODE -ne 0) {
            throw "Docker build failed with exit code $LASTEXITCODE"
        }
        Write-Host "Backend image built successfully." -ForegroundColor Green
    } catch {
        Write-Host "Error: Backend image build failed" -ForegroundColor Red
        Write-Host "Working directory was: $BackendDir" -ForegroundColor Yellow
        Write-Host $_.Exception.Message -ForegroundColor Red
        Pop-Location
        exit 1
    } finally {
        Pop-Location
    }

    # Build Frontend
    Write-Host "Building frontend image: ${FrontendImageExternal}:${ImageTag}" -ForegroundColor Cyan
    try {
        Push-Location $FrontendDir
        Write-Host "Current directory: $(Get-Location)" -ForegroundColor Gray

        docker build -t "${FrontendImageExternal}:${ImageTag}" -t "${FrontendImageExternal}:latest" .

        if ($LASTEXITCODE -ne 0) {
            throw "Docker build failed with exit code $LASTEXITCODE"
        }

        Write-Host "Frontend image built successfully." -ForegroundColor Green
    } catch {
        Write-Host "Error: Frontend image build failed" -ForegroundColor Red
        Write-Host "Working directory was: $FrontendDir" -ForegroundColor Yellow
        Write-Host $_.Exception.Message -ForegroundColor Red
        Pop-Location
        exit 1
    } finally {
        Pop-Location
    }

    # Push images
    Write-Host "Pushing images to $RegistryExternalHost..." -ForegroundColor Cyan
    try {
        Write-Host "Pushing backend:${ImageTag}..." -ForegroundColor Cyan
        $pushOutput = docker push "${BackendImageExternal}:${ImageTag}" 2>&1
        if ($LASTEXITCODE -ne 0) {
            throw "Push failed: $pushOutput"
        }


        Write-Host "Pushing frontend:${ImageTag}..." -ForegroundColor Cyan
        $pushOutput = docker push "${FrontendImageExternal}:${ImageTag}" 2>&1
        if ($LASTEXITCODE -ne 0) {
            throw "Push failed: $pushOutput"
        }

        Write-Host "All images pushed successfully." -ForegroundColor Green
    } catch {
        Write-Host "Error: Failed to push one or more images" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        exit 1
    }

    # Logout
    try {
        docker logout $RegistryExternalHost 2>&1 | Out-Null
    } catch {
        Write-Host "Warning: Docker logout failed (non-critical)" -ForegroundColor Yellow
    }
}

Write-Host "Validating Kustomize configuration..." -ForegroundColor Cyan
try {
    $kustomizeOutput = kubectl kustomize "overlays/$Environment/" 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Kustomize validation failed: $kustomizeOutput"
    }
    Write-Host "Configuration is valid" -ForegroundColor Green
} catch {
    Write-Host "Error: Kustomize validation failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host "Applying Kubernetes manifests..." -ForegroundColor Cyan
try {
    kubectl apply -k "overlays/$Environment/"
    if ($LASTEXITCODE -ne 0) {
        throw "kubectl apply failed with exit code $LASTEXITCODE"
    }
} catch {
    Write-Host "Error: Failed to apply manifests" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

# After applying, update images for PROD to the pushed tags and wait for rollout
if ($Environment -eq "prod") {
    Write-Host "Updating deployments to use registry images with tag '$ImageTag'..." -ForegroundColor Cyan
    $AppNamespace = "skill-management-system-prod"
    $BackendDeployment = "prod-sms-backend"
    $FrontendDeployment = "prod-sms-frontend"
    $DevOpsNamespace = "skill-management-devops-prod"
    $RegistryInternalService = "prod-sms-docker-registry-service"

    # Use fully qualified internal service name for cluster DNS resolution
    $BackendImageExternal = "$RegistryExternalHost/skill-management-backend"
    $FrontendImageExternal = "$RegistryExternalHost/skill-management-frontend"

    try {
        Write-Host "Updating backend deployment..." -ForegroundColor Cyan
        kubectl -n $AppNamespace set image deployment/$BackendDeployment backend="${BackendImageExternal}:${ImageTag}"
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to update backend deployment"
        }

        Write-Host "Waiting for backend rollout..." -ForegroundColor Cyan
        kubectl -n $AppNamespace rollout status deployment/$BackendDeployment --timeout=300s
        if ($LASTEXITCODE -ne 0) {
            throw "Backend rollout failed or timed out"
        }

        Write-Host "Updating frontend deployment..." -ForegroundColor Cyan
        kubectl -n $AppNamespace set image deployment/$FrontendDeployment frontend="${FrontendImageExternal}:${ImageTag}"
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to update frontend deployment"
        }

        Write-Host "Waiting for frontend rollout..." -ForegroundColor Cyan
        kubectl -n $AppNamespace rollout status deployment/$FrontendDeployment --timeout=300s
        if ($LASTEXITCODE -ne 0) {
            throw "Frontend rollout failed or timed out"
        }

        Write-Host "Deployments updated and rolled out successfully." -ForegroundColor Green
    } catch {
        Write-Host "Error: Failed to update images or wait for rollout" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red

        # Show pod status for debugging
        Write-Host "`nPod status for debugging:" -ForegroundColor Yellow
        kubectl get pods -n $AppNamespace
        kubectl describe pods -n $AppNamespace | Select-String -Pattern "Error|Failed|ImagePull" -Context 2,2

        exit 1
    }
}

# --- DEV-ONLY: Traefik Postgres Port Patch (only once) ---
if ($Environment -eq "dev") {
    Write-Host "Checking Traefik for PostgreSQL port patch..." -ForegroundColor Cyan
    try {
        $traefikService = kubectl get service traefik -n kube-system -o yaml 2>&1 | Select-String "5433"

        if (-not $traefikService) {
            Write-Host "Patching Traefik for PostgreSQL port 5433..." -ForegroundColor Yellow

            kubectl patch deployment traefik -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--entrypoints.postgres.address=:5433/tcp"}]'
            kubectl patch deployment traefik -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/ports/-", "value": {"containerPort": 5433, "name": "postgres", "protocol": "TCP"}}]'
            kubectl patch service traefik -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/ports/-", "value": {"name": "postgres", "port": 5433, "protocol": "TCP", "targetPort": 5433}}]'

            Write-Host "Traefik successfully patched for PostgreSQL." -ForegroundColor Green
            Write-Host "Waiting for Traefik to restart..." -ForegroundColor Cyan
            Start-Sleep -Seconds 10
        } else {
            Write-Host "Traefik already patched. Skipping..." -ForegroundColor Gray
        }
    } catch {
        Write-Host "Warning: Could not patch Traefik." -ForegroundColor Yellow
        Write-Host $_.Exception.Message -ForegroundColor Yellow
    }
}
# -----------------------------------------------------------

Write-Host "Waiting for deployments to be ready..." -ForegroundColor Cyan
try {
    kubectl wait --for=condition=available --timeout=300s deployment --all -n $Namespace
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Warning: Some deployments may not be ready yet" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Warning: Some deployments may not be ready yet" -ForegroundColor Yellow
}

Write-Host "Deployment Status:" -ForegroundColor Cyan
kubectl get pods,services,ingress -n $Namespace

Write-Host ""
Write-Host "Deployment completed successfully!" -ForegroundColor Green
Write-Host ""

# Environment-specific access info
if ($Environment -eq "dev") {
    Write-Host "Access URLs:" -ForegroundColor Cyan
    Write-Host "   Keycloak Admin: http://keycloak.dev.local/admin" -ForegroundColor White
} elseif ($Environment -eq "prod") {
    Write-Host "Access URLs:" -ForegroundColor Cyan
    Write-Host "   Keycloak Admin: http://auth.skill-management.edag.com/admin" -ForegroundColor White
    Write-Host "   Frontend:       http://skill-management.edag.com" -ForegroundColor White
    Write-Host "   Backend:        http://api.skill-management.edag.com/swagger-ui/index.html" -ForegroundColor White
}
