# Skill Management System - Vollständige Systemanalyse

> **Analysiert am:** 23. November 2025  
> **Projekt:** Betriebliche Projektarbeit - Fachinformatiker Anwendungsentwicklung  
> **Auszubildender:** Marvin Eschenbach  
> **Unternehmen:** EDAG Engineering GmbH, Fulda

---

## Dokumentationsstruktur

Diese Systemanalyse ist in mehrere Dokumente aufgeteilt, um die Komplexität des Systems übersichtlich darzustellen:

1. **[01_BACKEND_ANALYSE.md](./docs-analyse/01_BACKEND_ANALYSE.md)** - Backend-Architektur und Implementierung
2. **[02_FRONTEND_ANALYSE.md](./docs-analyse/02_FRONTEND_ANALYSE.md)** - Frontend-Architektur und Implementierung
3. **[03_DATENBANK_ANALYSE.md](./docs-analyse/03_DATENBANK_ANALYSE.md)** - Datenbankstruktur und Migrationen
4. **[04_SECURITY_ANALYSE.md](./docs-analyse/04_SECURITY_ANALYSE.md)** - Sicherheitskonzept und Authentifizierung
5. **[05_KUBERNETES_ANALYSE.md](./docs-analyse/05_KUBERNETES_ANALYSE.md)** - Kubernetes-Cluster und Deployment
6. **[06_CI_CD_ANALYSE.md](./docs-analyse/06_CI_CD_ANALYSE.md)** - CI/CD Pipeline und Automatisierung
7. **[07_INTEGRATION_ANALYSE.md](./docs-analyse/07_INTEGRATION_ANALYSE.md)** - Frontend-Backend Integration und Datenflüsse
8. **[08_GESAMTARCHITEKTUR.md](./docs-analyse/08_GESAMTARCHITEKTUR.md)** - Gesamtarchitektur und Zusammenfassung

---

## Executive Summary

Das **Skill Management System** ist eine vollständig entwickelte, produktionsreife Webanwendung zur effizienten Verwaltung von Mitarbeiterkompetenzen und projektbezogener Ressourcenplanung bei EDAG Engineering GmbH.

### Technologie-Stack

| Schicht | Technologie | Version |
|---------|-------------|---------|
| **Frontend** | Next.js | 16.0.0 |
| **Frontend Framework** | React | 19.2.0 |
| **UI-Bibliothek** | shadcn/ui + Radix UI | Latest |
| **State Management** | Redux Toolkit + RTK Query | 2.9.2 |
| **Styling** | TailwindCSS | 3.4.0 |
| **Backend** | Spring Boot | 3.5.7 |
| **Programmiersprache** | Java | 25 |
| **Datenbank** | PostgreSQL | 15 |
| **Migration-Tool** | Flyway | Latest |
| **Authentication** | Keycloak | 23.0 |
| **OAuth2** | Spring Security OAuth2 Resource Server | - |
| **Container Runtime** | Docker | Latest |
| **Orchestrierung** | Kubernetes | Latest |
| **CI/CD** | Jenkins | Latest |
| **Code-Qualität** | SonarQube | Latest |
| **API-Dokumentation** | SpringDoc OpenAPI | 2.8.13 |

### Architektur-Überblick

```
┌─────────────────────────────────────────────────────────────────┐
│                        KUBERNETES CLUSTER                        │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                     INGRESS CONTROLLER                     │  │
│  │        (skill-management.edag.com, TLS/SSL)               │  │
│  └────────────────────┬──────────────────┬───────────────────┘  │
│                       │                  │                       │
│  ┌────────────────────▼─────┐  ┌────────▼──────────────────┐   │
│  │   FRONTEND SERVICE       │  │   BACKEND SERVICE         │   │
│  │   (Next.js)              │  │   (Spring Boot)           │   │
│  │   - React 19             │  │   - Java 25               │   │
│  │   - Server-Side Rendering│  │   - REST API              │   │
│  │   - i18n (DE/EN)         │  │   - OAuth2 Resource Server│   │
│  │   Port: 3000             │  │   Port: 8080              │   │
│  └──────────────────────────┘  └───────────┬───────────────┘   │
│                                             │                    │
│  ┌──────────────────────────────────────────▼───────────────┐   │
│  │              KEYCLOAK SERVICE                             │   │
│  │              (Identity & Access Management)               │   │
│  │              Port: 8080                                   │   │
│  └──────────────────────────────────────────┬───────────────┘   │
│                                             │                    │
│  ┌──────────────────────────────────────────▼───────────────┐   │
│  │              POSTGRESQL STATEFULSET                       │   │
│  │              - skill_management_db                        │   │
│  │              - keycloak_db                                │   │
│  │              Port: 5432                                   │   │
│  └───────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘

                              ▲
                              │
                    ┌─────────┴──────────┐
                    │   JENKINS CI/CD    │
                    │   - Build Pipeline │
                    │   - SonarQube Scan │
                    │   - Docker Build   │
                    │   - K8s Deploy     │
                    └────────────────────┘
```

### Kernfunktionalitäten

1. **Mitarbeiter-Skill-Management**
   - Hinzufügen, Bearbeiten und Löschen von Skills
   - Proficiency-Bewertung (0-100 Punkte)
   - Erfahrungsjahre pro Skill
   - Letzte Verwendung tracken

2. **Mitarbeitersuche & Discovery**
   - Volltext-Suche über Namen und E-Mail
   - Multi-Filter: Skills, Kategorien, Standorte, Verfügbarkeit
   - Pagination und Sortierung
   - Erweiterte Such-Algorithmen

3. **Projekt-Management**
   - Projekthistorie pro Mitarbeiter
   - Team-Zusammensetzung
   - Verwendete Technologien
   - Projektstatus-Tracking

4. **Analytics & Reporting**
   - User-Statistiken (Skills, Projekte, Durchschnittswerte)
   - Skill-Entwicklung über Zeit
   - Activity-Logging
   - Dashboard-Visualisierungen

5. **Rollenverwaltung**
   - User (Standard-Berechtigung)
   - Manager (erweiterte Projekt-Rechte)
   - Admin (volle System-Kontrolle)
   - Rollenanfragen und Genehmigungen

### Design-Prinzipien

**Backend: Hexagonale Architektur (Ports & Adapters)**
```
domain/
  ├── model/          # Domain-Entitäten (Pure Business Logic)
  ├── port/
  │   ├── inbound/    # Use-Cases (Service-Interfaces)
  │   └── outbound/   # Repository-Interfaces
application/
  ├── service/        # Service-Implementierungen
  └── config/         # Spring-Konfigurationen
infrastructure/
  ├── adapter/
  │   ├── inbound/    # REST-Controller, DTOs
  │   └── outbound/   # JPA-Repositories, External APIs
```

**Frontend: Feature-Based Architecture**
```
src/
  ├── app/            # Next.js App Router (Routing)
  ├── components/     # Wiederverwendbare UI-Komponenten
  ├── screens/        # Feature-spezifische Seiten
  ├── store/          # Redux State + RTK Query APIs
  ├── types/          # TypeScript Type-Definitionen
  ├── libs/           # Utilities und Konfigurationen
  └── locales/        # i18n Übersetzungen (DE/EN)
```

### Besondere Merkmale

✅ **Vollständige Typsicherheit**: TypeScript im Frontend, Java 25 Records im Backend  
✅ **Internationalisierung**: Deutsch & Englisch (next-intl)  
✅ **Echtzeit-Synchronisation**: RTK Query mit automatischem Cache-Invalidierung  
✅ **Automatische Datenmigration**: Flyway für versionierte DB-Migrationen  
✅ **Code-Qualität**: Checkstyle, Spotless, ESLint, SonarQube Integration  
✅ **Security-First**: OAuth2, Keycloak, CORS, CSP-Headers  
✅ **Container-Native**: Multi-Stage Docker Builds, Kubernetes-Ready  
✅ **CI/CD Automation**: Jenkins Pipeline mit automatisierten Tests  
✅ **Observability**: Spring Actuator, Logging, Health-Checks  

### Projekt-Status

**Stand:** November 2025  
**Version:** 0.0.1-SNAPSHOT  
**Status:** ✅ Produktionsreif (MVP)  
**Test-Coverage:** Backend mit Unit- und Integrationstests  
**Deployment:** Lokaler Kubernetes-Cluster (Kind/Minikube)  

---

## Nächste Schritte

Für eine detaillierte Analyse der einzelnen Komponenten, lesen Sie bitte die verlinkten Dokumente im Abschnitt **Dokumentationsstruktur**.

---

**© 2025 EDAG Engineering GmbH - Ausbildungsprojekt**
