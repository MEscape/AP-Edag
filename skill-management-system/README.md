# Skill Management System – Betriebliche Projektarbeit

> **Projektbezeichnung:** Entwicklung eines webbasierten Skill Management Systems zur effizienten projektbezogenen Ressourceneinsatzplanung
> **Auszubildender:** Marvin Eschenbach
> **Unternehmen:** EDAG Engineering GmbH – Standort Fulda
> **Ausbildungsberuf:** Fachinformatiker für Anwendungsentwicklung
> **Projektzeitraum:** 28.10.2025 - 31.11.2025
> **Projektbetreuer:** [Christian Henning](mailto:christian.henning@edag.com)

---

## **Wichtiger Hinweis**

> Führe **zuerst [`setup.ps1`](./setup.ps1)** aus – es setzt **Env-Variablen**, **Secrets**, **Hooks** und den **Kubernetes-Cluster** auf.
> Optional: `-SkipDeploy`, um das **Deployment zu überspringen**.

---

## Einleitung

Im Rahmen meiner betrieblichen Projektarbeit wird ein webbasiertes **Skill Management System** entwickelt, das Projektverantwortlichen bei EDAG ermöglicht, geeignete Mitarbeitende auf Basis ihrer Fähigkeiten, Projekterfahrungen und Bewertungen gezielt für Kundenprojekte auszuwählen.

Das System stellt eine zentrale Plattform bereit, um Skills zu erfassen, zu bewerten und zu visualisieren.
Dadurch wird der Ressourceneinsatz im Unternehmen effizienter, transparenter und datenbasiert gesteuert.

---

## Dokumentationsstruktur

Die Dokumentation ist in zwei Hauptbereiche unterteilt:

1. **Benutzerdokumentation** – beschreibt die Nutzung der Anwendung
2. **Entwicklerdokumentation** – beschreibt Konzept, Architektur, Umsetzung und Tests

---

## Benutzerdokumentation

| Kapitel | Inhalt |
|----------|--------|
| [01_Einführung.md](docs/user/01_Einführung.md) | Überblick über das System und die Zielsetzung |
| [02_Benutzerrollen.md](docs/user/02_Benutzerrollen.md) | Beschreibung der Rollen und Berechtigungen (Mitarbeiter, Projektleiter, Admin) |
| [03_Funktionale_Übersicht.md](docs/user/03_Funktionale_Übersicht.md) | Überblick über die Hauptfunktionen (Skillverwaltung, Suche, Bewertungen, Visualisierung) |
| [04_Benutzeranleitung.md](docs/user/04_Benutzeranleitung.md) | Schritt-für-Schritt-Anleitung zur Nutzung der Anwendung |
| [05_FAQ_und_Support.md](docs/user/05_FAQ_und_Support.md) | Häufige Fragen, Kontakt und Hilfestellung |

---

## Entwicklerdokumentation

Die Struktur orientiert sich an den zentralen Ideen des **arc42-Modells**, jedoch vereinfacht für eine praxisgerechte Projektdokumentation im Ausbildungsrahmen.

| Kapitel | Inhalt |
|----------|--------|
| [01_Projektkontext.md](docs/dev/01_Projektkontext.md) | Ausgangssituation, Zielsetzung, Nutzen |
| [02_Anforderungen.md](docs/dev/02_Anforderungen.md) | Funktionale & nicht-funktionale Anforderungen |
| [03_Systemarchitektur.md](docs/dev/03_Systemarchitektur.md) | Überblick über die Architektur (Frontend, Backend, Datenbank, CI/CD) |
| [04_Bausteine.md](docs/dev/04_Bausteine.md) | Beschreibung der Hauptkomponenten und ihrer Schnittstellen |
| [05_Datenmodell.md](docs/dev/05_Datenmodell.md) | Entitäten, Beziehungen und Datenflüsse |
| [06_Technologien_und_Tools.md](docs/dev/06_Technologien_und_Tools.md) | Verwendete Frameworks, Libraries, Tools und Entwicklungsumgebung |
| [07_Umsetzung.md](docs/dev/07_Umsetzung.md) | Implementierung der Kernfunktionen (Auth, Skills, Bewertung, Filter, Visualisierung) |
| [08_Ergebnisse_und_Fazit.md](docs/dev/09_Ergebnisse_und_Fazit.md) | Bewertung des Projekts, Lessons Learned, Ausblick |

---

## Projektstruktur

```text

skill-management-system/
│
├─ frontend/            # Next.js + ShadCN (UI)
├─ backend/             # Spring Boot (REST API und Datenbank)
├─ design/              # Figma-Prototypen, UI-Konzepte
├─ docs/
│   ├─ user/            # Benutzerdokumentation
│   └─ dev/             # Entwicklerdokumentation
│
├─ k8s/                 # Kubernetes Manifeste & Kustomize-Overlays
├─ Jenkinsfile          # CI/CD Pipeline für Jenkins
└─ README.md            # Diese Datei

```

---

## Kurzüberblick zum Technologie-Stack

| Schicht | Technologie | Beschreibung |
|----------|--------------|---------------|
| **Frontend** | Next.js, TailwindCSS, ShadCN/UI | Weboberfläche mit modernen UI-Komponenten |
| **Backend** | Spring Boot (Java), REST API | Business-Logik, Authentifizierung, Datenzugriff |
| **Datenbank** | PostgreSQL | Speicherung von Skill- und Bewertungsdaten |
| **DevOps** | Docker, Jenkins, Kubernetes, Kustomize | Lokale Entwicklungsumgebung & CI/CD |
| **Dokumentation** | Markdown, Mermaid, Figma | Für technische & visuelle Dokumentation |

---

## Kontakt

**Projektbetreuer:**
Christian Henning
📞 +49 (661) 6000-97327
<!-- markdownlint-disable-next-line MD034 -->
✉️ christian.henning@edag.com

---

> © EDAG Engineering GmbH – Ausbildungsprojekt Fachinformatiker Anwendungsentwicklung
> Standort Fulda, 2025
