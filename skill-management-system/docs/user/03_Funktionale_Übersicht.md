# 3. Funktionale Übersicht

## 3.1 System-Übersicht

Das Skill Management System ist eine Web-Anwendung zur Verwaltung von Mitarbeiter-Kompetenzen bei EDAG. Zentrale Funktionen: Profilverwaltung, Skill-Erfassung, Mitarbeitersuche und Projektmanagement.

**Architektur:** Next.js Frontend + Spring Boot Backend + PostgreSQL + Keycloak

**Zugriff:** https://app.skill-management.edag.com (EDAG-Netzwerk)

---

## 3.2 Dashboard

### Startseite nach Login

**Anzeige:**
- **Willkommen-Nachricht** mit Benutzername
- **Statistik-Karten:**
  - Total Skills (Anzahl hinzugefügter Skills)
  - Durchschnittlicher Proficiency Score
  - Anzahl aktiver Projekte
  - Letzte Aktivität (Datum)
- **Skills Chart:** Die 5 besten Skills visualisiert
- **Schnellzugriff-Buttons:** Profil bearbeiten, Rollen anfragen, Mitarbeiter suchen

---

## 3.3 Profilverwaltung

### Mein Profil

**Stammdaten (aus Keycloak, nicht editierbar):**
- Name (Vor- und Nachname)
- E-Mail-Adresse

**Editierbare Felder:**
- **Position:** Dropdown (Software Engineer, Projektleiter, Teamleiter, etc.)
- **Standort:** Dropdown (Fulda, Berlin, München, Remote, etc.)
- **Biografie:** Freitext (max. 500 Zeichen) - optional
- **Erfahrung:** Zahlenfeld
- **Verfügbarkeit:** Dropdown (Verfügbar, Begrenzt verfügbar, Nicht verfügbar)

**Aktionen:**
- "Bearbeiten" → Formular mit Validierung
- "Speichern" → PUT `/api/v1/profiles/me`
- Toast-Benachrichtigung bei Erfolg/Fehler

---

### Fremde Profile

**Aufruf:** Über Mitarbeitersuche → "Profil anzeigen"

**Ansicht (schreibgeschützt):**
- Alle Stammdaten
- Biografie
- Skills mit Scores und Erfahrungsjahren
- Statistiken (Total Skills, Durchschnittsscore)
- Liste aktiver Projekte

**Keine Bearbeitungsmöglichkeit** für fremde Profile.

---

## 3.4 Skill-Management

### Skill hinzufügen

**Dialog:**
- **Skill auswählen:** Dropdown mit Suche (200+ Skills aus 13 Kategorien)
- **Proficiency Score:** Slider 0-100
- **Erfahrungsjahre:** Eingabefeld (Dezimalzahl, z.B. 2.5)

**Validierung:**
- Skill muss ausgewählt sein
- Score zwischen 0-100
- Jahre ≥ 0
- Kein Duplikat (ein Skill nur einmal pro Profil)

**API:** POST `/api/v1/profiles/me/skills`

**Datenbank-Effekt:** Trigger `update_total_skills()` aktualisiert automatisch `user_statistics.total_skills` und `user_statistics.average_proficiency`

---

### Skill-Kategorien (13)

1. **Frontend Development:** Next.JS, React, TypeScript, HTML/CSS, Tailwind CSS
2. **Backend Development:** Java, Spring Boot, Maven
3. **DevOps:** Docker, Kubernetes, Jenkins, BitBucket, SonarQube
4. **Datenbanken:** PostgreSQL, Flyway
5. **Security:** OAuth2, JWT, OIDC
6. **UI/UX Design:** Figma, ShadCN
7. **Sprachen:** Deutsch, Englisch

---

### Skill bearbeiten

**Aktion:** ✏️ Icon → Dialog mit aktualisierten Werten

**API:** PUT `/api/v1/profiles/me/skills/{id}`

---

### Skill löschen

**Aktion:** 🗑️ Icon → Bestätigungsdialog

**API:** DELETE `/api/v1/profiles/me/skills/{id}`

**Datenbank-Effekt:** Trigger aktualisiert Statistiken + Activity-Log

---

## 3.5 Mitarbeitersuche

### Suchfilter

**Verfügbare Filter:**
- **Skills:** Mehrfachauswahl (UND-Verknüpfung)
- **Standort:** Mehrfachauswahl (UND-Verknüpfung)
- **Position:** Mehrfachauswahl (UND-Verknüpfung)


**Technologie:** JPA Specifications für dynamische Filterung

**Performance:** PostgreSQL-Indizes auf `location`, `availability`, `skill_id` → Antwortzeit <300ms

---

### Suchergebnisse

**Darstellung:** Card-Layout mit Pagination (20 pro Seite)

**Pro Mitarbeiter:**
- Name, Position, Standort
- Verfügbarkeit-Status
- Skill-Badges
- "Profil anzeigen" Button

**Sortierung:** Name (alphabetisch)

---

### Filter-Kombinationen

**Beispiel 1:** Java + Kubernetes in Fulda
**Beispiel 2:** DevOps-Engineers (Docker + AWS) an allen Standorten

**UND-Logik bei Skills:** Mehrere Skills = Mitarbeiter muss ALLE haben (restriktiv)

**Tipp:** Für mehr Ergebnisse weniger Skills auswählen oder einzeln suchen

---

## 3.6 Projektmanagement (Manager)

### Projekte erstellen

**Nur für:** Manager

**Felder:**
- **Name:** Pflicht, max. 100 Zeichen
- **Beschreibung:** Optional, max. 1000 Zeichen
- **Startdatum:** Datepicker
- **Enddatum:** Optional, Datepicker
- **Status:** PLANNED | ACTIVE | COMPLETED
- **Kunde:** Optional, max. 100 Zeichen
- **Technologien:** Custom Dropdown Textfield, Min 1 Auswahl
- **Teammitglieder:** Mitarbeitersuche vernetzung mit Auswahlfunktion

**API:** POST `/api/v1/projects`

---

### Projekt-Status

| Status | Beschreibung | Nächster Status |
|--------|--------------|-----------------|
| **PLANNED** | In Planung | → ACTIVE |
| **ACTIVE** | Läuft | → COMPLETED oder ON_HOLD |
| **COMPLETED** | Abgeschlossen | (Endstatus) |

---

### Projektsuche

**Filter:**
- Status (Mehrfachauswahl)
- Skills (projektrelevante Skills)
- Mitarbeiter (Mehrfachauswahl)

**Nur für Manager**

---

## 3.7 Rollenanfragen

### Rollenanfrage stellen (User)

**Ablauf:**
1. Einstellungen → "Rollenanfrage stellen"
2. Gewünschte Rolle (Manager | Admin) + Begründung
3. API: POST `/api/v1/role-requests`
4. Status: Ausstehend ⏳

**Dauer:** 1-3 Werktage Bearbeitung

---

### Rollenanfragen genehmigen (Admin)

**Admin-Panel:**
- Liste aller ausstehenden Anfragen
- Pro Anfrage: Benutzername, Rolle, Begründung, Datum

**Aktionen:**
- **Genehmigen:** API: POST `/api/v1/role-requests/{id}/approve` → Automatische Keycloak-Rollenzuweisung
- **Ablehnen:** API: POST `/api/v1/role-requests/{id}/reject` → Optional: Ablehnungsgrund

---

## 3.8 Activity-Log

**Zweck:** Nachvollziehbarkeit aller Änderungen

**Protokolliert:** Skill hinzugefügt/gelöscht/bearbeitet, Profil aktualisiert, Projekt beigetreten/verlassen

**Technologie:** DB-Trigger `log_skill_activity()` → `activities` Tabelle

**Zugriff:** Dashboard Startseite → "Letzte Aktivitäten"

**Statistiken:** `user_statistics` (total_skills, average_proficiency, total_projects, last_activity_at) - automatisch via Trigger

---

## 3.9 Admin-Funktionen

**Übersicht:** Alle Benutzer mit Name, E-Mail, Rolle, Registrierungsdatum, Letzte Aktivität

**Aktionen:** Rolle ändern (Keycloak), Profil ansehen, Benutzer deaktivieren

---

## 3.10 Internationalisierung

**Sprachen:** 🇩🇪 Deutsch | 🇬🇧 Englisch  

**Spracherkennung:**  
- Automatische Erkennung der **System- bzw. Browsersprache**
- Fallback auf Englisch, falls die Sprache nicht unterstützt wird

**Technologie:**  
- Frontend: `next-intl`  
- Backend: Auswertung des `Accept-Language` Headers  

**✅ Übersetzt:**  
- UI-Elemente  
- Fehlermeldungen  
- Validierungen  

**❌ Nicht übersetzt:**  
- Biografien  
- Projektbeschreibungen  
- Skill-Namen (Englisch als Standardsprache)

---

## 3.11 Performance & Browser

**Performance:**
Seite: <2s | API: ~150ms | Suche: ~250ms | Skill add: ~300ms

**Browser:**
✅ Chrome 100+ | ✅ Edge 100+ | ✅ Firefox 95+ | ⚠️ Safari 15+

---

## 3.12 Datenschutz & Sicherheit

**Authentifizierung:** OAuth2 Authorization Code Flow mit Keycloak (Token-basiert, 24h Session) - **KEIN SSO**

**Autorisierung:** RBAC (user/manager/admin) mit Spring `@PreAuthorize`

**Datenschutz:** DSGVO-konform, On-Premise, HTTPS (TLS 1.3), nur angemeldete EDAG-Mitarbeiter, Audit-Log, Profil-Löschung auf Anfrage
