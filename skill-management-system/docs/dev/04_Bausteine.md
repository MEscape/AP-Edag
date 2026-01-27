# Kapitel 4: Bausteine und Komponenten

## 4.1 Übersicht

Das System folgt einer **Hexagonal Architecture** (Backend) und **Komponentenarchitektur** (Frontend) für klare Trennung zwischen Geschäftslogik, Infrastruktur und Präsentation.

---

## 4.2 Backend-Komponenten (Hexagonal Architecture)

### Package-Struktur

```
com.edag.skillmanagementsystem/
├── domain/                   # Domain Layer (Geschäftslogik)
│   ├── model/               # Domain Models (POJOs)
│   ├── port/inbound/        # Service Interfaces
│   ├── port/outbound/       # Repository Interfaces
│   └── exception/           # Domain Exceptions
├── application/service/      # Service Implementierungen
└── infrastructure/
    ├── adapter/inbound/web/ # REST Controller
    └── adapter/outbound/    # JPA, Keycloak
```

### Domain Services (Inbound Ports)

| Service | Verantwortlichkeit |
|---------|-------------------|
| **ProfileService** | Profilverwaltung |
| **SkillManagementService** | Skill-Management |
| **EmployeeDiscoveryService** | Mitarbeitersuche |
| **ProjectManagementService** | Projektverwaltung |
| **RoleRequestService** | Rollenverwaltung |
| **AnalyticsService** | Analytics & Statistiken |
| **OptionsService** | Referenzdaten |

### Infrastructure Layer

#### REST Controllers

| Controller | Endpoint | Funktionen |
|------------|----------|------------|
| **ProfileController** | `/api/profile` | Profil abrufen/aktualisieren, Skills verwalten |
| **EmployeeDiscoveryController** | `/api/employees` | Mitarbeiter suchen |
| **ProjectManagementController** | `/api/projects` | Projekte erstellen/verwalten |
| **AnalyticsController** | `/api/analytics` | Dashboard-Daten |

#### JPA Repositories

**Optimierungen:**
- **EntityGraphs** zur Vermeidung von N+1-Problemen
- **Custom Queries** mit JPQL
- **Pagination Support** für große Datenmengen

### Security & Configuration

- **OAuth2 Resource Server** mit JWT-Validation
- **Role-Based Access Control** (RBAC)
- **Method Security** mit `@PreAuthorize`
- **CORS-Konfiguration** für Frontend

---

## 4.3 Frontend-Komponenten

### Verzeichnisstruktur

```
frontend/src/
├── app/[locale]/           # Next.js App Router
│   ├── (authenticated)/    # Protected Routes
│   └── (public)/          # Public Routes
├── screens/                # Feature Screens
│   ├── home/              # Dashboard
│   ├── profile/           # Profil
│   ├── discover-employees/# Suche
│   └── my-projects/       # Projekte
├── components/
│   ├── ui/                # shadcn/ui Components
│   └── navigation/        # Navigation
├── store/
│   └── api/               # RTK Query Slices
├── hooks/                 # Custom Hooks
└── locales/               # i18n Übersetzungen
```

### Screen Components

| Screen | Route | Beschreibung | Rolle |
|--------|-------|--------------|-------|
| **Home** | `/home` | Dashboard mit Statistiken | USER |
| **Profile** | `/profile` | Profil bearbeiten | USER |
| **Discover Employees** | `/discover-employees` | Mitarbeiter suchen | USER |
| **My Projects** | `/my-projects` | Projekte verwalten | USER |
| **Admin** | `/admin` | Benutzerverwaltung | ADMIN |

### State Management (Redux Toolkit)

**RTK Query API Slices:**
- Automatisches Caching
- Optimistic Updates
- Type-Safe Hooks

```typescript
export const profileApi = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: '/api/profile' }),
  tagTypes: ['Profile', 'Skills'],
  endpoints: (builder) => ({
    getProfile: builder.query<Profile, void>({
      query: () => '/',
      providesTags: ['Profile'],
    }),
  }),
})
```

---

## 4.4 Datenbank-Komponenten

### Trigger-Funktionen

| Trigger | Tabelle | Funktion |
|---------|---------|----------|
| **update_updated_at** | Alle | Timestamp-Update |
| **update_total_skills** | employee_skills | Statistik-Update |
| **log_activity** | employee_skills, projects | Audit-Logging |

### Flyway-Migrationen

15 Migrationen (V1-V15) für versionierte Schema-Evolution.

---

## 4.5 Schnittstellen

### Frontend ↔ Backend (REST API)

- **Protocol:** HTTP/REST
- **Format:** JSON
- **Authentication:** OAuth2 Bearer Token
- **API Documentation:** OpenAPI 3.0 / Swagger UI

### Backend ↔ Keycloak (OAuth2)

- **OAuth2 Resource Server:** Spring Security
- **Token Validation:** JWT mit JWKS
- **User Sync:** Webhook für Events

### Backend ↔ PostgreSQL (JPA)

- **Connection Pool:** HikariCP
- **DDL:** Validate (Flyway managed)
- **Optimierungen:** Batch Inserts, EntityGraphs

---

## 4.6 Kubernetes-Deployment

### Namespace-Struktur

```
├── skill-management-system-dev/
│   ├── frontend (1 replica)
│   ├── backend (1 replica)
│   ├── keycloak (1 replica)
│   └── postgres (StatefulSet)
└── skill-management-system-prod/
    ├── frontend (1 replicas)
    ├── backend (1 replicas)
    └── ...
```

### Kustomize-Struktur

```
k8s/app/
├── base/                # Basis-Konfiguration
│   ├── frontend/
│   ├── backend/
│   └── keycloak/
└── overlays/            # Umgebungsspezifisch
    ├── dev/
    └── prod/
```

---

## 4.7 Zusammenfassung

**Backend:**
- ✅ Hexagonal Architecture (klare Trennung)
- ✅ 10 Domain Services
- ✅ 10 REST Controllers
- ✅ JPA Repositories mit Optimierungen

**Frontend:**
- ✅ 9 Screen Components
- ✅ shadcn/ui für UI
- ✅ Redux Toolkit + RTK Query
- ✅ Next.js 16 App Router

**Datenbank:**
- ✅ 11 Tabellen
- ✅ 6 Trigger-Funktionen
- ✅ 15 Flyway-Migrationen

**Schnittstellen:**
- ✅ REST API (Frontend ↔ Backend)
- ✅ OAuth2/JWT (Authentifizierung)
- ✅ JPA/Hibernate (Datenbank-Zugriff)
- ✅ Kubernetes (Orchestrierung)

Diese Architektur ermöglicht hohe Wartbarkeit, Testbarkeit und Skalierbarkeit.
