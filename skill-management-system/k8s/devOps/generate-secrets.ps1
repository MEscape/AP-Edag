# DevOps Infrastructure Kubernetes Secret Generator (PowerShell)
# Usage: .\generate-secrets.ps1 [dev|prod]

$EnvFile = ".env"

if (-not (Test-Path $EnvFile)) {
    Write-Host "Error: $EnvFile not found. Please create it from .env" -ForegroundColor Red
    exit 1
}

Write-Host "Generating DevOps Kubernetes secrets from $EnvFile for prod environment..." -ForegroundColor Green

# Read environment variables from .env file
$envVars = @{ }
Get-Content $EnvFile | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.*)$') {
        $key = $matches[1].Trim()
        $value = $matches[2].Trim()

        # Remove surrounding quotes if present
        if ($value.StartsWith('"') -and $value.EndsWith('"')) {
            $value = $value.Substring(1, $value.Length - 2)
        }

        # Store the cleaned value
        $envVars[$key] = $value
    }
}

$namespace = "skill-management-devops-prod"

# Create namespace if it doesn't exist
kubectl create namespace $namespace --dry-run=client -o yaml | kubectl apply -f -

# Generate PostgreSQL SonarQube secret
Write-Host "Creating postgres-sonar-secret..." -ForegroundColor Cyan
kubectl create secret generic postgres-sonar-secret `
    --from-literal=POSTGRES_PASSWORD="$($envVars['POSTGRES_PASSWORD'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

# Generate Jenkins secret
Write-Host "Creating jenkins-secret..." -ForegroundColor Cyan
kubectl create secret generic jenkins-secret `
    --from-literal=JENKINS_ADMIN_USER="$($envVars['JENKINS_ADMIN_USER'])" `
    --from-literal=JENKINS_ADMIN_PASSWORD="$($envVars['JENKINS_ADMIN_PASSWORD'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

# Generate SonarQube secret
Write-Host "Creating sonarqube-secret..." -ForegroundColor Cyan
kubectl create secret generic sonarqube-secret `
    --from-literal=SONAR_JDBC_PASSWORD="$($envVars['POSTGRES_PASSWORD'])" `
    --from-literal=SONAR_ADMIN_PASSWORD="$($envVars['SONAR_ADMIN_PASSWORD'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

# Generate Docker Registry secret
Write-Host "Creating docker-registry-secret..." -ForegroundColor Cyan
kubectl create secret generic docker-registry-secret `
    --from-literal=REGISTRY_HTTP_SECRET="$($envVars['REGISTRY_HTTP_SECRET'])" `
    --from-literal=REGISTRY_AUTH_HTPASSWD="$($envVars['REGISTRY_AUTH_HTPASSWD'])" `
    --from-literal=REGISTRY_USER="$($envVars['REGISTRY_USER'])" `
    --from-literal=REGISTRY_PASSWORD="$($envVars['REGISTRY_PASSWORD'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

# Generate Bitbucket secret
Write-Host "Creating bitbucket-secret..." -ForegroundColor Cyan
kubectl create secret generic bitbucket-secret `
    --from-literal=BITBUCKET_USERNAME="$($envVars['BITBUCKET_USERNAME'])" `
    --from-literal=BITBUCKET_PASSWORD="$($envVars['BITBUCKET_PASSWORD'])" `
    --from-literal=BITBUCKET_REPO_HTTP_URL="$($envVars['BITBUCKET_REPO_HTTP_URL'])" `
    --from-literal=BITBUCKET_SERVER_URL="$($envVars['BITBUCKET_SERVER_URL'])" `
    --from-literal=BITBUCKET_REPO_OWNER="$($envVars['BITBUCKET_REPO_OWNER'])" `
    --from-literal=BITBUCKET_REPO_NAME="$($envVars['BITBUCKET_REPO_NAME'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -


# Create ConfigMap with corporate CA certificates
Write-Host "Preparing corporate CA certificates ConfigMap (corporate-ca-certs)..." -ForegroundColor Cyan

$corporateBaseUrl = $envVars['CORPORATE_CA_BASE_URL']
$corporatePemFilesCsv = $envVars['CORPORATE_CA_PEM_FILES'] # comma-separated list of .pem filenames

# Default certificate directory under this script folder
$certDir = Join-Path -Path $PSScriptRoot -ChildPath 'certs'

# Ensure directory exists
New-Item -ItemType Directory -Force -Path $certDir | Out-Null

$files = $corporatePemFilesCsv.Split(',') | ForEach-Object { $_.Trim() } | Where-Object { $_ -ne '' }
if ($files.Count -eq 0) {
    Write-Host "Error: CORPORATE_CA_PEM_FILES parsed to empty list." -ForegroundColor Red
    exit 1
}

Write-Host "Downloading corporate CA certificates..." -ForegroundColor Cyan
$downloadErrors = @()
foreach ($file in $files) {
    $url = ("{0}/{1}" -f $corporateBaseUrl.TrimEnd('/'), $file)
    $dest = Join-Path $certDir $file

    if (Test-Path $dest) {
        Write-Host "Skipped (already exists): $file" -ForegroundColor Yellow
        continue
    }

    try {
        Invoke-WebRequest -Uri $url -UseBasicParsing -OutFile $dest -ErrorAction Stop
        Write-Host "Downloaded: $file" -ForegroundColor Green
    } catch {
        $downloadErrors += "Failed: $file ($url) - $_"
    }
}

if ($downloadErrors.Count -gt 0) {
    Write-Host "Error: One or more certificate downloads failed:" -ForegroundColor Red
    $downloadErrors | ForEach-Object { Write-Host $_ -ForegroundColor Red }
    exit 1
}

# Create or update the ConfigMap from the certs directory
Write-Host "Creating/Updating ConfigMap 'corporate-ca-certs' from $certDir" -ForegroundColor Cyan
try {
    kubectl create configmap corporate-ca-certs `
        --from-file=$certDir `
        --namespace=$namespace `
        --dry-run=client -o yaml | kubectl apply -f -

    Write-Host "ConfigMap 'corporate-ca-certs' applied." -ForegroundColor Green
    Write-Host "Verify with: kubectl get configmap corporate-ca-certs -n $namespace" -ForegroundColor Green
} catch {
    Write-Host "Failed to create ConfigMap 'corporate-ca-certs'. $_" -ForegroundColor Red
    exit 1
}

Write-Host "DevOps secrets generated successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Created secrets:" -ForegroundColor Cyan
kubectl get secrets -n $namespace | Where-Object { $_ -match "(postgres-sonar-secret|jenkins-secret|sonarqube-secret|docker-registry-secret|bitbucket-secret)" }
