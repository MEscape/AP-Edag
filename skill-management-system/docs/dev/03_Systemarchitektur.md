# 3. Systemarchitektur

## 3.1 Architekturüberblick

Das Skill Management System folgt einer **modernen Microservices-ähnlichen Architektur** mit klarer Trennung zwischen Frontend, Backend und unterstützenden Services.

### Schichtenmodell

- **Presentation Layer:** Next.js Frontend (React 19 + TypeScript)
- **Application Layer:** Spring Boot REST API (Java 25), Keycloak IAM
- **Data Layer:** PostgreSQL 15
- **Infrastructure Layer:** Kubernetes, Jenkins, SonarQube, Docker Registry

---

## 3.2 Komponentenarchitektur

### C4-Model: Context Diagram

**Akteure:**
- Mitarbeiter/Manager/Admin (EDAG Benutzer)

**System:**
- Frontend (Next.js) ↔ Backend (Spring Boot) ↔ PostgreSQL
- Frontend ↔ Keycloak (OAuth2 Login)
- Backend ↔ Keycloak (Token Validation)

---

## 3.3 Backend-Architektur (Hexagonal Architecture)

### Schichten-Modell

**Domain Layer (Business Logic):**
- Domain Models (Java Records)
- Port Interfaces (inbound/outbound)
- Domain Exceptions

**Application Layer (Use Cases):**
- Service Implementierungen
- Security Config, OpenAPI Config

**Infrastructure Layer (Adapters):**
- REST Controller (Inbound Adapter)
- JPA Repositories (Outbound Adapter)
- Keycloak Admin Client

### Package-Struktur

```
com.edag.skillmanagementsystem/
├── domain/
│   ├── model/               # Domain Models (Records)
│   ├── port/inbound/        # Service Interfaces
│   ├── port/outbound/       # Repository Interfaces
│   └── exception/
├── application/service/     # Service Implementierungen
└── infrastructure/
    ├── adapter/inbound/web/ # REST Controller + DTOs
    └── adapter/outbound/    # JPA + Keycloak
```

### Vorteile

✅ **Unabhängigkeit:** Domain kennt kein Spring, JPA oder REST
✅ **Testbarkeit:** Domain ohne Infrastruktur testbar
✅ **Austauschbarkeit:** REST → GraphQL, JPA → MongoDB
✅ **Klare Verantwortlichkeiten:** Jede Schicht hat eindeutige Aufgabe

---

## 3.4 Frontend-Architektur (Feature-Based)

### Architektur-Übersicht

**Presentation:**
- Pages/Routes (app/[locale]/...)
- Feature Screens (screens/...)
- Shared Components (components/ui/...)

**State Management:**
- Redux Store (@reduxjs/toolkit)
- RTK Query (API Slices)

**Routing & i18n:**
- Next.js Middleware (Auth + i18n)
- next-intl (Translations)

### Folder-Struktur

```
frontend/src/
├── app/[locale]/            # Next.js App Router
│   ├── (auth)/             # Auth Layout
│   └── (dashboard)/        # Dashboard Layout
├── screens/                 # Feature Screens
│   ├── discover-employees/
│   ├── profile/
│   └── my-projects/
├── components/              # Shared Components
│   ├── ui/                 # shadcn/ui
│   └── navigation/
├── store/api/              # RTK Query Slices
├── types/                  # TypeScript Types
└── locales/                # i18n Translations
```

### State Management (RTK Query)

```typescript
export const employeeApi = createApi({
  baseQuery: baseQueryWithAuth,
  tagTypes: ['Employees'],
  endpoints: (builder) => ({
    searchEmployees: builder.query<Response, Params>({
      query: (params) => ({ url: '/v1/employees/search', method: 'POST', body: params }),
      providesTags: ['Employees'],
    }),
  }),
})
```

**Vorteile:** Automatisches Caching, Optimistic Updates, Loading/Error States

---

## 3.5 Datenfluss (End-to-End)

### Beispiel: Skill hinzufügen

1. User klickt "Add Skill" → Frontend validiert
2. Frontend → POST /api/v1/profiles/me/skills (Bearer Token)
3. Next.js Middleware → Token prüfen
4. Backend → Token Introspection (Keycloak)
5. Backend → Business Logic (Service Layer)
6. Backend → INSERT INTO employee_skills (PostgreSQL)
7. PostgreSQL → Trigger: update_total_skills(), log_skill_activity()
8. Backend → 201 Created + DTO
9. Frontend → RTK Query invalidates 'Profile' tag → Re-Fetch
10. Frontend → Toast + UI Update

### OAuth2 Flow

1. User → Login Button
2. Frontend → Redirect zu Keycloak /auth
3. User → Login (Username + Password)
4. Keycloak → Redirect mit Authorization Code
5. NextAuth → Token Exchange (Code → Tokens)
6. NextAuth → Session mit Access Token
7. Frontend → API Request (Bearer Token)
8. Backend → Token Validation (Keycloak)
9. Backend → Response

---

## 3.6 Deployment-Architektur (Kubernetes)

### Namespace-Struktur

```
├── skill-management-system-dev/
│   ├── frontend (1 replica)
│   ├── backend (1 replica)
│   ├── keycloak (1 replica)
│   └── postgres (StatefulSet, 1 replica)
└── skill-management-system-prod/
    ├── frontend (1 replicas)
    ├── backend (1 replicas)
    ├── keycloak (1 replicas)
    └── postgres (StatefulSet, 1 replica)
```

### Kustomize-Struktur

```
k8s/app/
├── base/                    # Basis-Konfiguration
│   ├── frontend/
│   ├── backend/
│   ├── keycloak/
│   └── postgres/
└── overlays/                # Umgebungsspezifisch
    ├── dev/
    └── prod/
```

### Ingress (Traefik)

**Routing:**
- `app.skill-management.edag.com` → Frontend (Port 3000)
- `api.skill-management.edag.com` → Backend (Port 8080)
- `auth.skill-management.edag.com` → Keycloak (Port 8443)

---

## 3.7 Technologie-Stack (Übersicht)

| Schicht | Technologie | Version |
|---------|-------------|---------|
| **Frontend** | Next.js | 16.0.0 |
| | React | 19.2.0 |
| | TypeScript | 5.9.3 |
| | Redux Toolkit | 2.9.2 |
| | TailwindCSS | 3.4.0 |
| **Backend** | Spring Boot | 3.5.7 |
| | Java | 25 |
| | Spring Security | 6.x |
| | Spring Data JPA | 3.x |
| **Database** | PostgreSQL | 15 Alpine |
| **Identity** | Keycloak | 23.0 |
| **Container** | Docker | 24.x |
| | Kubernetes | 1.28+ |
| **CI/CD** | Jenkins | 2.534 |
| | SonarQube | Community |

---

## 3.8 Architektur-Entscheidungen (ADRs)

### ADR-1: Hexagonal Architecture

**Status:** ✅ Akzeptiert
**Vorteile:** Unabhängigkeit, Testbarkeit, Austauschbarkeit
**Nachteile:** Mehr Boilerplate, steilere Lernkurve

### ADR-2: RTK Query

**Status:** ✅ Akzeptiert
**Vorteile:** Weniger Boilerplate, Automatisches Caching
**Nachteile:** Redux Toolkit erforderlich

### ADR-3: Kubernetes

**Status:** ✅ Akzeptiert
**Vorteile:** Production-ready, Skalierbarkeit, Standard bei EDAG
**Nachteile:** Höhere Komplexität

---

## 3.9 Qualitätsattribute & Taktiken

| Qualitätsmerkmal | Taktik | Umsetzung |
|------------------|--------|-----------|
| **Performance** | Caching | RTK Query, PostgreSQL Indizes |
| | Lazy Loading | Next.js Code Splitting |
| | Query Optimization | JPA EntityGraphs |
| **Security** | Defense in Depth | OAuth2 + JWT + Method Security |
| | Input Validation | Jakarta Validation + Zod |
| | Secrets Management | Kubernetes Secrets |
| **Skalierbarkeit** | Stateless Services | JWT statt Sessions |
| | Horizontal Scaling | Kubernetes Replicas |
| | Connection Pooling | HikariCP |
| **Verfügbarkeit** | Health Checks | Spring Actuator + K8s Probes |
| | Auto-Healing | Kubernetes Liveness Probes |
| | Load Balancing | Kubernetes Services |
| **Wartbarkeit** | Clean Architecture | Hexagonal Architecture |
| | Code Quality | SonarQube Quality Gates |
| | Documentation | OpenAPI, arc42 |

---

## 3.10 Zusammenfassung

Das Skill Management System nutzt eine moderne, bewährte Architektur:

- **Backend:** Hexagonal Architecture für Entkopplung und Testbarkeit
- **Frontend:** Feature-basiert mit RTK Query für State Management
- **Deployment:** Kubernetes mit Kustomize für Infrastructure as Code
- **Security:** OAuth2 Keycloak Flow (KEIN SSO - nur OAuth2!)
- **DevOps:** CI/CD mit Jenkins, Quality Gates mit SonarQube

Diese Architektur gewährleistet Skalierbarkeit, Wartbarkeit und hohe Code-Qualität.
