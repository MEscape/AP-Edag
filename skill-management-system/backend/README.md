# Backend - Skill Management System

## Quick Start

### Prerequisites

- Java 25
- Maven 3.6+

### Environment Variables

Create a `.env` file or set the following environment variables:

```bash
# =============================================================================
# DEVELOPMENT ENVIRONMENT - ACTIVE CONFIGURATION
# =============================================================================
# This is your working development environment file
# Copy values from here and update with your actual local setup
# =============================================================================

# -----------------------------------------------------------------------------
# APPLICATION PROFILE
# -----------------------------------------------------------------------------
SPRING_PROFILES_ACTIVE="dev"

# -----------------------------------------------------------------------------
# DATABASE CONFIGURATION
# -----------------------------------------------------------------------------
# PostgreSQL connection for local development
DATABASE_URL="jdbc:postgresql://localhost:5433/skillmanagement"
DATABASE_USERNAME="postgres"
DATABASE_PASSWORD="change-me"

# -----------------------------------------------------------------------------
# KEYCLOAK CONFIGURATION
# -----------------------------------------------------------------------------
# Local Keycloak instance
KEYCLOAK_URL="http://keycloak.dev.local"

# OAuth2 Client Credentials (Backend API)
OAUTH2_CLIENT_ID="skill-management-app"
OAUTH2_CLIENT_SECRET="change-me"

# Keycloak OAuth2 Endpoints
KEYCLOAK_AUTH_URL="http://keycloak.dev.local/realms/skill-management/protocol/openid-connect/auth"
KEYCLOAK_TOKEN_URL="http://keycloak.dev.local/realms/skill-management/protocol/openid-connect/token"
KEYCLOAK_INTROSPECTION_URI="http://keycloak.dev.local/realms/skill-management/protocol/openid-connect/token/introspect"

# Keycloak Webhook User Credentials
WEBHOOK_USERNAME="keycloak-webhook"
WEBHOOK_PASSWORD="change-me"

# Admin Client
KEYCLOAK_MASTER_REALM="master"
KEYCLOAK_TARGET_REALM="skill-management"
KEYCLOAK_ADMIN_USERNAME="admin"
KEYCLOAK_ADMIN_PASSWORD="change-me"

# -----------------------------------------------------------------------------
# FRONTEND APPLICATION
# -----------------------------------------------------------------------------
# Local NextJs development server
FRONTEND_URL="http://localhost:3000"

# -----------------------------------------------------------------------------
# API DOCUMENTATION
# -----------------------------------------------------------------------------
# Enable Swagger UI and API docs in development
API_DOCS_ENABLED="true"
SWAGGER_UI_ENABLED="true"
```

### Running the Application

```bash
# Install dependencies and run
mvn clean install
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application will start on `http://localhost:8080`

### API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Troubleshooting

### Database Connection Issues

- Ensure PostgreSQL is running
- Check database credentials and URL
- Verify database exists: `CREATE DATABASE skillmanagement;`

### OAuth2/Keycloak Issues

- Verify Keycloak is running and accessible
- Check client configuration in Keycloak admin console
- Ensure client secret matches configuration

### Build Issues

```bash
# Clean and rebuild
mvn clean compile
# Skip tests if needed
mvn clean install -DskipTests
```

## Development

### Profiles

- `dev` - Development with verbose logging
- `prod` - Production optimized settings

### Database Migration

Flyway migrations are in `src/main/resources/db/migration/`

### Code Quality

```bash
mvn checkstyle:check    # Code style
mvn test               # Run tests
mvn jacoco:report      # Coverage report
```
