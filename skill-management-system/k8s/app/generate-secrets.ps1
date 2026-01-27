# Skill Management System Kubernetes Secret Generator (PowerShell)
# Usage: .\generate-secrets.ps1 [dev|prod]

param(
    [Parameter(Position=0)]
    [ValidateSet("dev", "prod")]
    [string]$Environment
)

$ErrorActionPreference = "Stop"

# Environment parameter is mandatory
if (-not $Environment) {
    Write-Host "Error: Environment parameter is required!" -ForegroundColor Red
    Write-Host "Usage: .\generate-secrets.ps1 [dev|prod]" -ForegroundColor Yellow
    exit 1
}

# Determine .env file based on environment
$EnvFile = ".env.$Environment"

if (-not (Test-Path $EnvFile)) {
    Write-Host "Error: $EnvFile not found. Please create it from .env.example" -ForegroundColor Red
    exit 1
}

Write-Host "Generating Kubernetes secrets from $EnvFile for $Environment environment..." -ForegroundColor Green

# Read environment variables from .env file
$envVars = @{ }
Get-Content $EnvFile | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.*)$') {
        $envVars[$matches[1].Trim()] = $matches[2].Trim()
    }
}

$namespace = "skill-management-system-$Environment"

# Create namespace if it doesn't exist
kubectl create namespace $namespace --dry-run=client -o yaml | kubectl apply -f -

# Generate PostgreSQL secret
Write-Host "Creating postgres-secret..." -ForegroundColor Cyan
kubectl create secret generic postgres-secret `
    --from-literal=POSTGRES_USER="$($envVars['POSTGRES_USER'])" `
    --from-literal=POSTGRES_PASSWORD="$($envVars['POSTGRES_PASSWORD'])" `
    --from-literal=POSTGRES_DB="$($envVars['POSTGRES_DB'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

# Generate Keycloak secret
Write-Host "Creating keycloak-secret..." -ForegroundColor Cyan
kubectl create secret generic keycloak-secret `
    --from-literal=KC_DB_USERNAME="$($envVars['POSTGRES_USER'])" `
    --from-literal=KC_DB_PASSWORD="$($envVars['KC_DB_PASSWORD'])" `
    --from-literal=KEYCLOAK_ADMIN="$($envVars['KEYCLOAK_ADMIN'])" `
    --from-literal=KEYCLOAK_ADMIN_PASSWORD="$($envVars['KEYCLOAK_ADMIN_PASSWORD'])" `
    --from-literal=KEYCLOAK_CLIENT_ID="$($envVars['KEYCLOAK_CLIENT_ID'])" `
    --from-literal=KEYCLOAK_CLIENT_SECRET="$($envVars['KEYCLOAK_CLIENT_SECRET'])" `
    --from-literal=WEBHOOK_USERNAME="$($envVars['WEBHOOK_USERNAME'])" `
    --from-literal=WEBHOOK_PASSWORD="$($envVars['WEBHOOK_PASSWORD'])" `
    --namespace=$namespace `
    --dry-run=client -o yaml | kubectl apply -f -

if ($Environment -eq "prod") {
    # Generate Docker registry secret
    Write-Host "Creating Docker registry imagePullSecret (regcred)..." -ForegroundColor Cyan
    kubectl create secret docker-registry regcred `
        --docker-server="registry.skill-management-devops.edag.com" `
        --docker-username="$($envVars['REGISTRY_USER'])" `
        --docker-password="$($envVars['REGISTRY_PASSWORD'])" `
        --namespace=$namespace `
        --dry-run=client -o yaml | kubectl apply -f -

    # Generate Backend secret
    Write-Host "Creating backend-secret..." -ForegroundColor Cyan
    kubectl create secret generic backend-secret `
        --from-literal=DATABASE_USERNAME="$($envVars['POSTGRES_USER'])" `
        --from-literal=DATABASE_PASSWORD="$($envVars['POSTGRES_PASSWORD'])" `
        --from-literal=OAUTH2_CLIENT_ID="$($envVars['KEYCLOAK_CLIENT_ID'])" `
        --from-literal=OAUTH2_CLIENT_SECRET="$($envVars['KEYCLOAK_CLIENT_SECRET'])" `
        --from-literal=KEYCLOAK_ADMIN_USERNAME="$($envVars['KEYCLOAK_ADMIN'])" `
        --from-literal=KEYCLOAK_ADMIN_PASSWORD="$($envVars['KEYCLOAK_ADMIN_PASSWORD'])" `
        --from-literal=WEBHOOK_USERNAME="$($envVars['WEBHOOK_USERNAME'])" `
        --from-literal=WEBHOOK_PASSWORD="$($envVars['WEBHOOK_PASSWORD'])" `
        --namespace=$namespace `
        --dry-run=client -o yaml | kubectl apply -f -

    # Generate Frontend secret
    Write-Host "Creating frontend-secret..." -ForegroundColor Cyan
    kubectl create secret generic frontend-secret `
        --from-literal=KEYCLOAK_CLIENT_ID="$($envVars['KEYCLOAK_CLIENT_ID'])" `
        --from-literal=KEYCLOAK_CLIENT_SECRET="$($envVars['KEYCLOAK_CLIENT_SECRET'])" `
        --from-literal=NEXTAUTH_SECRET="$($envVars['NEXTAUTH_SECRET'])" `
        --namespace=$namespace `
        --dry-run=client -o yaml | kubectl apply -f -
}

Write-Host "Secrets generated successfully!" -ForegroundColor Green
Write-Host ""

Write-Host "Created secrets:" -ForegroundColor Cyan

$secretNames = @("postgres-secret", "keycloak-secret")
if ($Environment -eq "prod") {
    $secretNames += @("regcred", "backend-secret", "frontend-secret")
}

foreach ($secret in $secretNames) {
    $output = kubectl get secret $secret -n $namespace -o name 2>$null
    if ($LASTEXITCODE -eq 0 -and $output) {
        Write-Host "  - $secret"
    }
}
