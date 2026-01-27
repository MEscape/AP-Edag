# Root Setup Script for Skill Management System (PowerShell)
# Usage: .\setup.ps1

param(
  [switch]$SkipDeploy
)

$ErrorActionPreference = "Stop"

function New-RandomBase64([int]$length = 32) {
  $bytes = New-Object byte[] $length
  $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
  $rng.GetBytes($bytes)
  [System.Convert]::ToBase64String($bytes)
}

function New-RandomSecret() {
  [Guid]::NewGuid().ToString('N')
}

function Ensure-Command($name, $friendlyName) {
  if (-not (Get-Command $name -ErrorAction SilentlyContinue)) {
    Write-Host "Warning: $friendlyName ($name) not found in PATH" -ForegroundColor Yellow
    return $false
  }
  return $true
}

function Write-EnvFile($path, [object]$vars) {
  $dir = Split-Path -Parent $path
  if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir | Out-Null }
  $lines = @()
  if ($vars -is [System.Collections.IDictionary]) {
    foreach ($k in $vars.Keys) { $lines += "$k=$($vars[$k])" }
  } elseif ($vars -is [System.Collections.IEnumerable]) {
    foreach ($item in $vars) { $lines += "$item" }
  } else {
    throw "Unsupported vars type for Write-EnvFile: $($vars.GetType().FullName)"
  }
  Set-Content -Path $path -Value ($lines -join "`n") -Encoding UTF8
  Write-Host "Wrote $path" -ForegroundColor Green
}

Write-Host "Initializing Skill Management System..." -ForegroundColor Cyan

# 1) Dependencies
$hasPython = Ensure-Command -name "python" -friendlyName "Python"
if ($hasPython) {
  Write-Host "Installing pre-commit and commitizen via pip..." -ForegroundColor Cyan
  python -m pip install --user pre-commit commitizen | Out-Null
} else {
  Write-Host "Python is required to install pre-commit hooks. Please install Python and rerun." -ForegroundColor Red
  exit 1
}

Write-Host "Installing Git hooks (pre-commit, commit-msg)..." -ForegroundColor Cyan
pre-commit install
pre-commit install --hook-type commit-msg

# 2) Gather base inputs (with sensible defaults)
Write-Host "`n=== GENERAL CONFIGURATION ===" -ForegroundColor Cyan
$defaultKeycloakClientId = "skill-management-app"
$defaultDevHostKeycloak = "keycloak.dev.local"
$defaultAppUrl = "http://localhost:3000"
$defaultApiUrl = "http://localhost:8080/api"
$defaultDbUser = "postgres"
$defaultDbName = "skillmanagement"
$defaultRepoName = "skill-management-system"
$defaultWebhookUsername = "keycloak-webhook"

$KEYCLOAK_CLIENT_ID = Read-Host "KEYCLOAK_CLIENT_ID [default: $defaultKeycloakClientId]"
if (-not $KEYCLOAK_CLIENT_ID) { $KEYCLOAK_CLIENT_ID = $defaultKeycloakClientId }

Write-Host "`n=== DEVELOPMENT ENVIRONMENT CREDENTIALS ===" -ForegroundColor Cyan

# Development credentials
$KEYCLOAK_CLIENT_SECRET_DEV = Read-Host "Keycloak client secret (dev) [default: random-secret]"
if (-not $KEYCLOAK_CLIENT_SECRET_DEV) { $KEYCLOAK_CLIENT_SECRET_DEV = New-RandomSecret }

$NEXTAUTH_SECRET_DEV = Read-Host "NextAuth secret (dev) [default: random-32-secret]"
if (-not $NEXTAUTH_SECRET_DEV) { $NEXTAUTH_SECRET_DEV = New-RandomBase64 32 }

$POSTGRES_USER_DEV = Read-Host "Postgres user (dev) [default: $defaultDbUser]"
if (-not $POSTGRES_USER_DEV) { $POSTGRES_USER_DEV = $defaultDbUser }

$POSTGRES_PASSWORD_DEV = Read-Host "Postgres password (dev) [default: random-secret]"
if (-not $POSTGRES_PASSWORD_DEV) { $POSTGRES_PASSWORD_DEV = New-RandomSecret }

$KC_DB_PASSWORD_DEV = Read-Host "Keycloak DB password (dev) [default: random-secret]"
if (-not $KC_DB_PASSWORD_DEV) { $KC_DB_PASSWORD_DEV = New-RandomSecret }

$POSTGRES_DB_DEV = Read-Host "Postgres DB name (dev) [default: $defaultDbName]"
if (-not $POSTGRES_DB_DEV) { $POSTGRES_DB_DEV = $defaultDbName }

$KEYCLOAK_ADMIN_DEV = Read-Host "Keycloak admin user (dev) [default: admin]"
if (-not $KEYCLOAK_ADMIN_DEV) { $KEYCLOAK_ADMIN_DEV = "admin" }

$KEYCLOAK_ADMIN_PASSWORD_DEV = Read-Host "Keycloak admin password (dev) [default: random-secret]"
if (-not $KEYCLOAK_ADMIN_PASSWORD_DEV) { $KEYCLOAK_ADMIN_PASSWORD_DEV = New-RandomSecret }

$WEBHOOK_USERNAME_DEV = Read-Host "Webhook username (dev) [default: $defaultWebhookUsername]"
if (-not $WEBHOOK_USERNAME_DEV) { $WEBHOOK_USERNAME_DEV = $defaultWebhookUsername }

$WEBHOOK_PASSWORD_DEV = Read-Host "Webhook password (dev) [default: random-secret]"
if (-not $WEBHOOK_PASSWORD_DEV) { $WEBHOOK_PASSWORD_DEV = New-RandomSecret }

Write-Host "`n=== PRODUCTION ENVIRONMENT CREDENTIALS ===" -ForegroundColor Cyan

# Production credentials
$KEYCLOAK_CLIENT_SECRET_PROD = Read-Host "Keycloak client secret (prod) [default: random-secret]"
if (-not $KEYCLOAK_CLIENT_SECRET_PROD) { $KEYCLOAK_CLIENT_SECRET_PROD = New-RandomSecret }

$NEXTAUTH_SECRET_PROD = Read-Host "NextAuth secret (prod) [default: random-32-secret]"
if (-not $NEXTAUTH_SECRET_PROD) { $NEXTAUTH_SECRET_PROD = New-RandomBase64 32 }

$POSTGRES_USER_PROD = Read-Host "Postgres user (prod) [default: $defaultDbUser]"
if (-not $POSTGRES_USER_PROD) { $POSTGRES_USER_PROD = $defaultDbUser }

$POSTGRES_PASSWORD_PROD = Read-Host "Postgres password (prod) [default: random-secret]"
if (-not $POSTGRES_PASSWORD_PROD) { $POSTGRES_PASSWORD_PROD = New-RandomSecret }

$KC_DB_PASSWORD_PROD = Read-Host "Keycloak DB password (prod) [default: random-secret]"
if (-not $KC_DB_PASSWORD_PROD) { $KC_DB_PASSWORD_PROD = New-RandomSecret }

$POSTGRES_DB_PROD = Read-Host "Postgres DB name (prod) [default: $defaultDbName]"
if (-not $POSTGRES_DB_PROD) { $POSTGRES_DB_PROD = $defaultDbName }

$KEYCLOAK_ADMIN_PROD = Read-Host "Keycloak admin user (prod) [default: admin]"
if (-not $KEYCLOAK_ADMIN_PROD) { $KEYCLOAK_ADMIN_PROD = "admin" }

$KEYCLOAK_ADMIN_PASSWORD_PROD = Read-Host "Keycloak admin password (prod) [default: random-secret]"
if (-not $KEYCLOAK_ADMIN_PASSWORD_PROD) { $KEYCLOAK_ADMIN_PASSWORD_PROD = New-RandomSecret }

$WEBHOOK_USERNAME_PROD = Read-Host "Webhook username (prod) [default: $defaultWebhookUsername]"
if (-not $WEBHOOK_USERNAME_PROD) { $WEBHOOK_USERNAME_PROD = $defaultWebhookUsername }

$WEBHOOK_PASSWORD_PROD = Read-Host "Webhook password (prod) [default: random-secret]"
if (-not $WEBHOOK_PASSWORD_PROD) { $WEBHOOK_PASSWORD_PROD = New-RandomSecret }

$REGISTRY_USER = Read-Host "Docker registry user [default: registry]"
if (-not $REGISTRY_USER) { $REGISTRY_USER = "registry" }

$REGISTRY_PASSWORD = Read-Host "Docker registry password [default: random-secret]"
if (-not $REGISTRY_PASSWORD) { $REGISTRY_PASSWORD = New-RandomSecret }

$REGISTRY_HTTP_SECRET = Read-Host "Docker registry HTTP secret [default: random-secret]"
if (-not $REGISTRY_HTTP_SECRET) { $REGISTRY_HTTP_SECRET = New-RandomSecret }

Write-Host "`n=== DEVOPS ENVIRONMENT CREDENTIALS ===" -ForegroundColor Cyan

# DevOps-specific credentials (separate from dev/prod)
$POSTGRES_PASSWORD_DEVOPS = Read-Host "Postgres password (devops) [default: random-secret]"
if (-not $POSTGRES_PASSWORD_DEVOPS) { $POSTGRES_PASSWORD_DEVOPS = New-RandomSecret }

$SONAR_ADMIN_PASSWORD = Read-Host "Sonar admin password (devops) [default: random-secret]"
if (-not $SONAR_ADMIN_PASSWORD) { $SONAR_ADMIN_PASSWORD = New-RandomSecret }

$JENKINS_ADMIN_USER = Read-Host "Jenkins admin user [default: admin]"
if (-not $JENKINS_ADMIN_USER) { $JENKINS_ADMIN_USER = "admin" }

$JENKINS_ADMIN_PASSWORD = Read-Host "Jenkins admin password (devops) [default: random-secret]"
if (-not $JENKINS_ADMIN_PASSWORD) { $JENKINS_ADMIN_PASSWORD = New-RandomSecret }

Write-Host "`n=== BITBUCKET CONFIGURATION ===" -ForegroundColor Cyan
$BITBUCKET_USERNAME = Read-Host "Bitbucket username (leave empty to skip)"
$BITBUCKET_PASSWORD = ""
$BITBUCKET_SERVER_URL = ""
$BITBUCKET_REPO_OWNER = ""
$BITBUCKET_REPO_NAME = ""
$BITBUCKET_REPO_HTTP_URL = ""

if ($BITBUCKET_USERNAME) {
  $BITBUCKET_PASSWORD = Read-Host "Bitbucket password/token" -AsSecureString
  $BITBUCKET_PASSWORD = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto(
    [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($BITBUCKET_PASSWORD)
  )

  $BITBUCKET_SERVER_URL = Read-Host "Bitbucket server URL [default: https://csp.edag.de/bitbucket]"
  if (-not $BITBUCKET_SERVER_URL) { $BITBUCKET_SERVER_URL = "https://csp.edag.de/bitbucket" }

  $BITBUCKET_REPO_OWNER = "~$BITBUCKET_USERNAME"

  $BITBUCKET_REPO_NAME = Read-Host "Bitbucket repository name [default: $defaultRepoName]"
  if (-not $BITBUCKET_REPO_NAME) { $BITBUCKET_REPO_NAME = $defaultRepoName }

  if ($BITBUCKET_SERVER_URL -and $BITBUCKET_REPO_OWNER -and $BITBUCKET_REPO_NAME) {
    $BITBUCKET_REPO_HTTP_URL = "$BITBUCKET_SERVER_URL/scm/$BITBUCKET_REPO_OWNER/$BITBUCKET_REPO_NAME.git"
  }
}

Write-Host "`n=== CORPORATE CA CONFIGURATION ===" -ForegroundColor Cyan
$CORPORATE_CA_BASE_URL = Read-Host "Corporate CA base URL [default: https://crl.edag.de]"
if (-not $CORPORATE_CA_BASE_URL) { $CORPORATE_CA_BASE_URL = "https://crl.edag.de" }

$CORPORATE_CA_PEM_FILES = Read-Host "Corporate CA PEM files [default: EDAG_Engineering_R1.pem,EDAG_Engineering_R1_SUBCA1.pem,EDAG_Engineering_R1_SUBCA2.pem,EDAG_Engineering_R1_SUBCA3.pem,EDAG_Engineering_R1_SUBCA4.pem,EDAG_Engineering_R1_SUBCA5.pem]"
if (-not $CORPORATE_CA_PEM_FILES) { $CORPORATE_CA_PEM_FILES = "EDAG_Engineering_R1.pem,EDAG_Engineering_R1_SUBCA1.pem,EDAG_Engineering_R1_SUBCA2.pem,EDAG_Engineering_R1_SUBCA3.pem,EDAG_Engineering_R1_SUBCA4.pem,EDAG_Engineering_R1_SUBCA5.pem" }

# Generate htpasswd for registry (if htpasswd is available)
$REGISTRY_AUTH_HTPASSWD = ""
$hasHtpasswd = Ensure-Command -name "htpasswd" -friendlyName "htpasswd"
if ($hasHtpasswd) {
  try {
    $REGISTRY_AUTH_HTPASSWD = & htpasswd -nb $REGISTRY_USER $REGISTRY_PASSWORD 2>$null
    if ($REGISTRY_AUTH_HTPASSWD) {
      Write-Host "Generated htpasswd hash for registry authentication" -ForegroundColor Green
    }
  } catch {
    Write-Host "Warning: Could not generate htpasswd hash. You may need to set REGISTRY_AUTH_HTPASSWD manually." -ForegroundColor Yellow
  }
} else {
  Write-Host "Warning: htpasswd not found. REGISTRY_AUTH_HTPASSWD will be empty." -ForegroundColor Yellow
  Write-Host "To generate manually, run: htpasswd -nb $REGISTRY_USER <password>" -ForegroundColor Yellow
}

# 3) Write .env files
Write-Host "`n=== Creating environment files ===" -ForegroundColor Cyan

# frontend/.env (development)
$frontendEnv = @(
  "# =============================================================================",
  "# DEVELOPMENT ENVIRONMENT - ACTIVE CONFIGURATION",
  "# =============================================================================",
  "# This is your working development environment file",
  "# Copy values from here and update with your actual local setup",
  "# =============================================================================",
  "",
  "# -----------------------------------------------------------------------------",
  "# Application Configuration",
  "# -----------------------------------------------------------------------------",
  "NODE_ENV=development",
  "NEXT_PUBLIC_APP_URL=$defaultAppUrl",
  "",
  "# -----------------------------------------------------------------------------",
  "# NextAuth.js Configuration",
  "# -----------------------------------------------------------------------------",
  "NEXTAUTH_URL=$defaultAppUrl",
  "# Generate with: openssl rand -base64 32",
  "NEXTAUTH_SECRET=$NEXTAUTH_SECRET_DEV",
  "",
  "# -----------------------------------------------------------------------------",
  "# Keycloak Configuration",
  "# -----------------------------------------------------------------------------",
  "# Keycloak realm URL",
  "KEYCLOAK_ISSUER=http://$defaultDevHostKeycloak/realms/skill-management",
  "",
  "# Keycloak client credentials",
  "KEYCLOAK_CLIENT_ID=$KEYCLOAK_CLIENT_ID",
  "KEYCLOAK_CLIENT_SECRET=$KEYCLOAK_CLIENT_SECRET_DEV",
  "",
  "# -----------------------------------------------------------------------------",
  "# API Configuration",
  "# -----------------------------------------------------------------------------",
  "# Backend API base URL",
  "NEXT_PUBLIC_API_URL=$defaultApiUrl"
)
Write-EnvFile -path "frontend/.env.test" -vars $frontendEnv

# backend/.env (development)
$backendEnv = @(
  "# =============================================================================",
  "# DEVELOPMENT ENVIRONMENT - ACTIVE CONFIGURATION",
  "# =============================================================================",
  "# This is your working development environment file",
  "# Copy values from here and update with your actual local setup",
  "# =============================================================================",
  "",
  "# -----------------------------------------------------------------------------",
  "# APPLICATION PROFILE",
  "# -----------------------------------------------------------------------------",
  "SPRING_PROFILES_ACTIVE=dev",
  "",
  "# -----------------------------------------------------------------------------",
  "# DATABASE CONFIGURATION",
  "# -----------------------------------------------------------------------------",
  "# PostgreSQL connection for local development",
  "DATABASE_URL=jdbc:postgresql://localhost:5433/$POSTGRES_DB_DEV",
  "DATABASE_USERNAME=$POSTGRES_USER_DEV",
  "DATABASE_PASSWORD=$POSTGRES_PASSWORD_DEV",
  "",
  "# -----------------------------------------------------------------------------",
  "# KEYCLOAK CONFIGURATION",
  "# -----------------------------------------------------------------------------",
  "# Local Keycloak instance",
  "KEYCLOAK_URL=http://$defaultDevHostKeycloak",
  "",
  "# OAuth2 Client Credentials (Backend API)",
  "OAUTH2_CLIENT_ID=$KEYCLOAK_CLIENT_ID",
  "OAUTH2_CLIENT_SECRET=$KEYCLOAK_CLIENT_SECRET_DEV",
  "",
  "# Keycloak OAuth2 Endpoints",
  "KEYCLOAK_AUTH_URL=http://$defaultDevHostKeycloak/realms/skill-management/protocol/openid-connect/auth",
  "KEYCLOAK_TOKEN_URL=http://$defaultDevHostKeycloak/realms/skill-management/protocol/openid-connect/token",
  "KEYCLOAK_INTROSPECTION_URI=http://$defaultDevHostKeycloak/realms/skill-management/protocol/openid-connect/token/introspect",
  "",
  "# Keycloak Webhook User Credentials",
  "WEBHOOK_USERNAME=$WEBHOOK_USERNAME_DEV",
  "WEBHOOK_PASSWORD=$WEBHOOK_PASSWORD_DEV",
  "",
  "# Admin Client",
  "KEYCLOAK_MASTER_REALM=master",
  "KEYCLOAK_TARGET_REALM=skill-management",
  "KEYCLOAK_ADMIN_USERNAME=$KEYCLOAK_ADMIN_DEV",
  "KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD_DEV",
  "",
  "# -----------------------------------------------------------------------------",
  "# FRONTEND APPLICATION",
  "# -----------------------------------------------------------------------------",
  "# Local NextJs development server",
  "FRONTEND_URL=$defaultAppUrl",
  "",
  "# -----------------------------------------------------------------------------",
  "# API DOCUMENTATION",
  "# -----------------------------------------------------------------------------",
  "# Enable Swagger UI and API docs in development",
  "API_DOCS_ENABLED=true",
  "SWAGGER_UI_ENABLED=true"
)
Write-EnvFile -path "backend/.env.test" -vars $backendEnv

# k8s/app/.env.dev
$appDevEnv = @(
  "# Skill Management System - Development Environment Variables",
  "# This file contains development secrets - DO NOT commit to version control",
  "",
  "# PostgreSQL Configuration",
  "POSTGRES_USER=$POSTGRES_USER_DEV",
  "POSTGRES_PASSWORD=$POSTGRES_PASSWORD_DEV",
  "POSTGRES_DB=$POSTGRES_DB_DEV",
  "",
  "# Keycloak Configuration",
  "KC_DB_PASSWORD=$KC_DB_PASSWORD_DEV",
  "KEYCLOAK_ADMIN=$KEYCLOAK_ADMIN_DEV",
  "KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD_DEV",
  "",
  "# Keycloak Client Configuration",
  "KEYCLOAK_CLIENT_ID=$KEYCLOAK_CLIENT_ID",
  "KEYCLOAK_CLIENT_SECRET=$KEYCLOAK_CLIENT_SECRET_DEV",
  "",
  "# Webhook Configuration",
  "WEBHOOK_USERNAME=$WEBHOOK_USERNAME_DEV",
  "WEBHOOK_PASSWORD=$WEBHOOK_PASSWORD_DEV"
)
Write-EnvFile -path "k8s/app/.env.dev.test" -vars $appDevEnv

# k8s/app/.env.prod
$appProdEnv = @(
  "# Skill Management System - Production Environment Variables",
  "# This file contains production secrets - DO NOT commit to version control",
  "# IMPORTANT: Update all passwords and secrets before deploying to production!",
  "",
  "# PostgreSQL Configuration",
  "POSTGRES_USER=$POSTGRES_USER_PROD",
  "POSTGRES_PASSWORD=$POSTGRES_PASSWORD_PROD",
  "POSTGRES_DB=$POSTGRES_DB_PROD",
  "",
  "# Keycloak Configuration",
  "KC_DB_PASSWORD=$KC_DB_PASSWORD_PROD",
  "KEYCLOAK_ADMIN=$KEYCLOAK_ADMIN_PROD",
  "KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD_PROD",
  "",
  "# Keycloak Client Configuration",
  "KEYCLOAK_CLIENT_ID=$KEYCLOAK_CLIENT_ID",
  "KEYCLOAK_CLIENT_SECRET=$KEYCLOAK_CLIENT_SECRET_PROD",
  "",
  "# NextJS Configuration",
  "NEXTAUTH_SECRET=$NEXTAUTH_SECRET_PROD",
  "",
  "# Docker Registry Configuration",
  "REGISTRY_USER=$REGISTRY_USER",
  "REGISTRY_PASSWORD=$REGISTRY_PASSWORD",
  "",
  "# Webhook Configuration",
  "WEBHOOK_USERNAME=$WEBHOOK_USERNAME_PROD",
  "WEBHOOK_PASSWORD=$WEBHOOK_PASSWORD_PROD"
)
Write-EnvFile -path "k8s/app/.env.prod.test" -vars $appProdEnv

# k8s/devOps/.env (prod infra)
$devOpsEnv = @(
  "# Skill Management DevOps - Production Environment Variables",
  "# This file contains production secrets - DO NOT commit to version control",
  "# IMPORTANT: Update all passwords and secrets before deploying to production!",
  "",
  "# PostgreSQL Configuration (DevOps-specific)",
  "POSTGRES_PASSWORD=$POSTGRES_PASSWORD_DEVOPS",
  "",
  "# Jenkins Configuration",
  "JENKINS_ADMIN_USER=$JENKINS_ADMIN_USER",
  "JENKINS_ADMIN_PASSWORD=$JENKINS_ADMIN_PASSWORD",
  "",
  "# SonarQube Configuration",
  "SONAR_ADMIN_PASSWORD=$SONAR_ADMIN_PASSWORD",
  "",
  "# Docker Registry Configuration",
  "REGISTRY_HTTP_SECRET=$REGISTRY_HTTP_SECRET",
  "",
  "# Registry Authentication",
  "# The REGISTRY_AUTH_HTPASSWD value below is in htpasswd format",
  "# Generated for user: $REGISTRY_USER",
  "REGISTRY_AUTH_HTPASSWD=$REGISTRY_AUTH_HTPASSWD",
  "REGISTRY_USER=$REGISTRY_USER",
  "REGISTRY_PASSWORD=$REGISTRY_PASSWORD",
  "",
  "# Bitbucket Configuration",
  "BITBUCKET_USERNAME=$BITBUCKET_USERNAME",
  "BITBUCKET_PASSWORD=$BITBUCKET_PASSWORD",
  "BITBUCKET_REPO_HTTP_URL=$BITBUCKET_REPO_HTTP_URL",
  "BITBUCKET_SERVER_URL=$BITBUCKET_SERVER_URL",
  "BITBUCKET_REPO_OWNER=$BITBUCKET_REPO_OWNER",
  "BITBUCKET_REPO_NAME=$BITBUCKET_REPO_NAME",
  "",
  "# Corporate CA Configuration",
  "CORPORATE_CA_BASE_URL=$CORPORATE_CA_BASE_URL",
  "CORPORATE_CA_PEM_FILES=$CORPORATE_CA_PEM_FILES"
)
Write-EnvFile -path "k8s/devOps/.env.test" -vars $devOpsEnv

# 4) Generate Kubernetes secrets
Write-Host "`n=== Generating Kubernetes secrets ===" -ForegroundColor Cyan
try {
  Push-Location "k8s/app"
  Write-Host "Generating dev secrets..." -ForegroundColor Cyan
  ./generate-secrets.ps1 dev
  Write-Host "Generating prod secrets..." -ForegroundColor Cyan
  ./generate-secrets.ps1 prod
} finally { Pop-Location }

try {
  Push-Location "k8s/devOps"
  Write-Host "Generating devOps secrets..." -ForegroundColor Cyan
  ./generate-secrets.ps1
} finally { Pop-Location }

# 5) Deploy (optional)
if (-not $SkipDeploy) {
  Write-Host "`n=== Deploying environments ===" -ForegroundColor Cyan
  try {
    Push-Location "k8s/devOps"
    Write-Host "Deploying devOps (prod)..." -ForegroundColor Cyan
    ./deploy.ps1
  } finally { Pop-Location }

  try {
    Push-Location "k8s/app"
    Write-Host "Deploying app (dev)..." -ForegroundColor Cyan
    ./deploy.ps1 dev
  } finally { Pop-Location }

  try {
    Push-Location "k8s/app"
    Write-Host "Deploying app (prod)..." -ForegroundColor Cyan
    ./deploy.ps1 prod
  } finally { Pop-Location }
} else {
  Write-Host "`nSkipping deployments as requested." -ForegroundColor Yellow
}

# 6) Summary of generated credentials
Write-Host "`n=============================================================================" -ForegroundColor Cyan
Write-Host "SETUP COMPLETE!" -ForegroundColor Green
Write-Host "=============================================================================" -ForegroundColor Cyan

Write-Host "`n=== GENERATED CREDENTIALS SUMMARY ===" -ForegroundColor Yellow
Write-Host "`nDEVELOPMENT:" -ForegroundColor Cyan
Write-Host "  Postgres Password: $POSTGRES_PASSWORD_DEV"
Write-Host "  Keycloak Admin Password: $KEYCLOAK_ADMIN_PASSWORD_DEV"
Write-Host "  Keycloak Client Secret: $KEYCLOAK_CLIENT_SECRET_DEV"

Write-Host "`nPRODUCTION:" -ForegroundColor Cyan
Write-Host "  Postgres Password: $POSTGRES_PASSWORD_PROD"
Write-Host "  Keycloak Admin Password: $KEYCLOAK_ADMIN_PASSWORD_PROD"
Write-Host "  Keycloak Client Secret: $KEYCLOAK_CLIENT_SECRET_PROD"

Write-Host "`nDEVOPS:" -ForegroundColor Cyan
Write-Host "  Postgres Password: $POSTGRES_PASSWORD_DEVOPS"
Write-Host "  Jenkins Admin Password: $JENKINS_ADMIN_PASSWORD"
Write-Host "  SonarQube Admin Password: $SONAR_ADMIN_PASSWORD"
Write-Host "  Registry User: $REGISTRY_USER"
Write-Host "  Registry Password: $REGISTRY_PASSWORD"

if ($BITBUCKET_USERNAME) {
  Write-Host "`nBITBUCKET:" -ForegroundColor Cyan
  Write-Host "  Username: $BITBUCKET_USERNAME"
  Write-Host "  Repo URL: $BITBUCKET_REPO_HTTP_URL"
}

Write-Host "`n=== NEXT STEPS ===" -ForegroundColor Cyan
Write-Host "1. Start backend dev:" -ForegroundColor White
Write-Host "   cd backend" -ForegroundColor Gray
Write-Host "   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev" -ForegroundColor Gray

Write-Host "`n2. Start frontend dev:" -ForegroundColor White
Write-Host "   cd frontend" -ForegroundColor Gray
Write-Host "   npm run dev" -ForegroundColor Gray

Write-Host "`n3. Configure local hosts (Windows):" -ForegroundColor White
Write-Host "   Edit: C:\Windows\System32\drivers\etc\hosts" -ForegroundColor Gray
Write-Host "   Add the following lines:" -ForegroundColor Gray
Write-Host "   127.0.0.1   keycloak.dev.local" -ForegroundColor Gray
Write-Host "   127.0.0.1   auth.skill-management.edag.com" -ForegroundColor Gray
Write-Host "   127.0.0.1   skill-management.edag.com" -ForegroundColor Gray
Write-Host "   127.0.0.1   api.skill-management.edag.com" -ForegroundColor Gray
Write-Host "   127.0.0.1   jenkins.skill-management-devops.edag.com" -ForegroundColor Gray
Write-Host "   127.0.0.1   sonarqube.skill-management-devops.edag.com" -ForegroundColor Gray
Write-Host "   127.0.0.1   registry.skill-management-devops.edag.com" -ForegroundColor Gray

Write-Host "`n4. Review Kubernetes namespaces:" -ForegroundColor White
Write-Host "   - skill-management-system-dev" -ForegroundColor Gray
Write-Host "   - skill-management-system-prod" -ForegroundColor Gray
Write-Host "   - skill-management-devops-prod" -ForegroundColor Gray

Write-Host "`n5. IMPORTANT: Store all generated passwords securely!" -ForegroundColor Yellow
Write-Host "   Consider using a password manager or secure vault." -ForegroundColor Yellow

Write-Host "`n=============================================================================" -ForegroundColor Cyan
