# Frontend - Skill Management System

## Quick Start

### Prerequisites

- Node.js 18+
- npm or yarn

### Environment Variables

Create a `.env` file in the frontend directory:

```bash
# =============================================================================
# DEVELOPMENT ENVIRONMENT - ACTIVE CONFIGURATION
# =============================================================================
# This is your working development environment file
# Copy values from here and update with your actual local setup
# =============================================================================

# -----------------------------------------------------------------------------
# Application Configuration
# -----------------------------------------------------------------------------
NODE_ENV="development"
NEXT_PUBLIC_APP_URL="http://localhost:3000"

# -----------------------------------------------------------------------------
# NextAuth.js Configuration
# -----------------------------------------------------------------------------
NEXTAUTH_URL="http://localhost:3000"
# Generate with: openssl rand -base64 32
NEXTAUTH_SECRET="change-me"

# -----------------------------------------------------------------------------
# Keycloak Configuration
# -----------------------------------------------------------------------------
# Keycloak realm URL
KEYCLOAK_ISSUER="https://keycloak.dev.local/realms/skill-management"

# Keycloak client credentials
KEYCLOAK_CLIENT_ID="skill-management-app"
KEYCLOAK_CLIENT_SECRET="change-me"

# -----------------------------------------------------------------------------
# API Configuration
# -----------------------------------------------------------------------------
# Backend API base URL
NEXT_PUBLIC_API_URL="http://localhost:8080/api/v1"
```

### Running the Application

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
npm start
```

The application will start on `http://localhost:3000`

### Available Scripts

```bash
npm run dev          # Development server
npm run build        # Production build
npm run start        # Start production server
npm run lint         # Run ESLint
npm run lint:fix     # Fix ESLint issues
npm run check:types  # TypeScript type checking
npm run check:deps   # Check unused dependencies
npm run check:i18n   # Check internationalization
```

## Troubleshooting

### Environment Variable Issues

- Ensure all required variables are set in `.env`
- Restart dev server after changing environment variables
- Check that `NEXTAUTH_SECRET` is 32 characters

### Authentication Issues

- Verify Keycloak is running and accessible
- Check Keycloak client configuration
- Ensure redirect URIs are configured: `http://localhost:3000/api/auth/callback/keycloak`

### Build Issues

```bash
# Clear Next.js cache
rm -rf .next
npm run build

# Type checking issues
npm run check:types

# Dependency issues
rm -rf node_modules package-lock.json
npm install
```

### API Connection Issues

- Ensure backend is running on configured `NEXT_PUBLIC_API_URL`
- Check CORS configuration in backend
- Verify network connectivity between frontend and backend

## Development

### Tech Stack

- Next.js 16 with App Router
- TypeScript
- Tailwind CSS
- NextAuth.js for authentication
- Redux Toolkit for state management
- React 19

### Internationalization

- Supported languages: German (default), English
- Locale files: `src/locales/de.json`, `src/locales/en.json`

### Code Quality

- ESLint with Antfu config
- TypeScript strict mode
- Tailwind CSS with custom theme
