# Skill Management DevOps Kubernetes Deployment Script (PowerShell)
# Usage: .\deploy.ps1

$ErrorActionPreference = "Stop"

$Environment = "prod"
$Namespace = "skill-management-devops-$Environment"

Write-Host "Deploying Skill Management DevOps to $Environment environment..." -ForegroundColor Green

# Check if kubectl is available
try {
    kubectl version --client --output=json | Out-Null
} catch {
    Write-Host "Error: kubectl is not installed or not in PATH" -ForegroundColor Red
    exit 1
}

Write-Host "WARNING: You are about to deploy to PRODUCTION!" -ForegroundColor Yellow
Write-Host "Make sure you have updated the passwords in .env and generated the secrets" -ForegroundColor Yellow
$response = Read-Host "Continue? (y/N)"
if ($response -ne "y" -and $response -ne "Y") {
    Write-Host "Deployment cancelled" -ForegroundColor Red
    exit 1
}

Write-Host "Validating Kustomize configuration..." -ForegroundColor Cyan
try {
    kubectl kustomize "base/" | Out-Null
    Write-Host "Configuration is valid" -ForegroundColor Green
} catch {
    Write-Host "Error: Kustomize validation failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host "Applying Kubernetes manifests..." -ForegroundColor Cyan
try {
    kubectl apply -k "base/"
} catch {
    Write-Host "Error: Failed to apply manifests" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host "Waiting for deployments to be ready..." -ForegroundColor Cyan
try {
    kubectl wait --for=condition=available --timeout=300s deployment --all -n $Namespace
} catch {
    Write-Host "Warning: Some deployments may not be ready yet" -ForegroundColor Yellow
}

Write-Host "Deployment Status:" -ForegroundColor Cyan
kubectl get pods,services,ingress -n $Namespace

Write-Host ""
Write-Host "Deployment completed successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Access URLs:" -ForegroundColor Cyan
Write-Host "   Jenkins:         https://jenkins.skill-management-devops.edag.com" -ForegroundColor White
Write-Host "   SonarQube:       https://sonarqube.skill-management-devops.edag.com" -ForegroundColor White
Write-Host "   Docker Registry: https://registry.skill-management-devops.edag.com" -ForegroundColor White
Write-Host ""
