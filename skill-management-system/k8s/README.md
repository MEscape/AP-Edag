# Kubernetes - Skill Management System

## Quick Start

- Run scripts:
  - `./app/generate-secrets.ps1 dev` → generate secrets for Dev
  - `./app/generate-secrets.ps1 prod` → generate secrets for Prod
  - `./app/deploy.ps1 dev` → deploy application to Dev
  - `./app/deploy.ps1 prod` → deploy application to Prod
  - `./devOps/generate-secrets.ps1` → generate DevOps infrastructure secrets (Prod)
  - `./devOps/deploy.ps1` → deploy DevOps infrastructure (Prod)
- Alternatively use the root setup script `./setup.ps1` (creates .envs, generates secrets, and can run deployments).

## Prerequisites

- `kubectl` installed and configured (cluster access)
- Kustomize available (`kubectl kustomize`)
- Correct Kubernetes context and permissions

## Structure

```text

k8s/
├─ app/           # Application (Keycloak, Backend, Frontend)
│  ├─ base/       # Base resources
│  ├─ overlays/   # Environments (dev, prod)
│  ├─ generate-secrets.ps1
│  └─ deploy.ps1
└─ devOps/        # DevOps infrastructure (Jenkins, SonarQube, Registry)
   ├─ base/
   ├─ generate-secrets.ps1
   └─ deploy.ps1

```

## Environment Variables

Create and maintain three environment files. Use the same sectioned style as in the backend and keep shared secrets consistent across services.

### App Dev (`k8s/app/.env.dev`)

```bash
# Skill Management System - Development Environment Variables
# This file contains development secrets - DO NOT commit to version control

# PostgreSQL Configuration
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="change-me"
POSTGRES_DB="skillmanagement"

# Keycloak Configuration
KEYCLOAK_ADMIN="admin"
KEYCLOAK_ADMIN_PASSWORD="change-me"
KC_DB_PASSWORD="change-me"

# Keycloak Client Configuration
KEYCLOAK_CLIENT_ID="skill-management-app"
KEYCLOAK_CLIENT_SECRET="change-me"

# Webhook Configuration
WEBHOOK_USERNAME="keycloak-webhook"
WEBHOOK_PASSWORD="change-me"
```

### App Prod (`k8s/app/.env.prod`)

```bash
# Skill Management System - Production Environment Variables
# This file contains production secrets - DO NOT commit to version control
# IMPORTANT: Update all passwords and secrets before deploying to production!

# PostgreSQL Configuration
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="change-me"
POSTGRES_DB="skillmanagement"

# Keycloak Configuration
KEYCLOAK_ADMIN="admin"
KEYCLOAK_ADMIN_PASSWORD="change-me"
KC_DB_PASSWORD="change-me"

# Keycloak Client Configuration
KEYCLOAK_CLIENT_ID="skill-management-app"
KEYCLOAK_CLIENT_SECRET="change-me"

# NextJS Configuration
NEXTAUTH_SECRET="change-me"

# Docker Registry Configuration
REGISTRY_USER="admin"
REGISTRY_PASSWORD="change-me"

# Webhook Configuration
WEBHOOK_USERNAME="keycloak-webhook"
WEBHOOK_PASSWORD="change-me"
```

### DevOps Prod (`k8s/devOps/.env`)

```bash
# Skill Management DevOps - Production Environment Variables
# This file contains production secrets - DO NOT commit to version control
# IMPORTANT: Update all passwords and secrets before deploying to production!

# PostgreSQL Configuration
POSTGRES_PASSWORD="change-me"

# Jenkins Configuration
JENKINS_ADMIN_USER="admin"
JENKINS_ADMIN_PASSWORD="change-me"

# SonarQube Configuration
SONAR_ADMIN_PASSWORD="change-me"

# Docker Registry Configuration
REGISTRY_HTTP_SECRET="change-me"
# Registry Authentication (htpasswd format)
# Username: admin
# Password: change-me
REGISTRY_AUTH_HTPASSWD="admin:change-me"
REGISTRY_USER="admin"
REGISTRY_PASSWORD="change-me"

# Bitbucket Configuration
BITBUCKET_USERNAME="me90678"
BITBUCKET_PASSWORD="change-me"
BITBUCKET_REPO_HTTP_URL="https://csp.edag.de/bitbucket/scm/~me90678/skill-management-system.git"
BITBUCKET_SERVER_URL="https://csp.edag.de/bitbucket"
BITBUCKET_REPO_OWNER="~me90678"
BITBUCKET_REPO_NAME="skill-management-system"

# Corporate CA Configuration
CORPORATE_CA_BASE_URL="https://crl.edag.de"
CORPORATE_CA_PEM_FILES="EDAG_Engineering_R1.pem,EDAG_Engineering_R1_SUBCA1.pem,EDAG_Engineering_R1_SUBCA2.pem,EDAG_Engineering_R1_SUBCA3.pem,EDAG_Engineering_R1_SUBCA4.pem,EDAG_Engineering_R1_SUBCA5.pem"
```

## Deployments

- Validate configurations: `kubectl kustomize overlays/dev/` or `overlays/prod/`
- Deploy application:
  - Dev: `./app/deploy.ps1 dev`
  - Prod: `./app/deploy.ps1 prod` (with safety prompt)
- Deploy DevOps:
  - Prod: `./devOps/deploy.ps1` (with safety prompt)

## Namespaces

- `skill-management-system-dev`
- `skill-management-system-prod`
- `skill-management-devops-prod`

## Access & Hosts

- Keycloak Dev: `http://keycloak.dev.local`
- Keycloak Prod: `http://auth.skill-management.edag.com`
- Frontend Prod: `http://skill-management.edag.com`
- Backend Prod: `http://api.skill-management.edag.com/swagger-ui/index.html`
- Jenkins: `https://jenkins.skill-management-devops.edag.com`
- SonarQube: `https://sonarqube.skill-management-devops.edag.com`
- Registry: `https://registry.skill-management-devops.edag.com`

Windows hosts file: `C:\Windows\System32\drivers\etc\hosts` – add the domains above (with Ingress IP if needed).

## Troubleshooting

- `kubectl` in PATH? → `kubectl version --client`
- Secrets present? → `kubectl get secrets -n <namespace>`
- Kustomize valid? → `kubectl kustomize overlays/<env>/`
- CertManager installed? → `kubectl get deployment cert-manager -n cert-manager`
