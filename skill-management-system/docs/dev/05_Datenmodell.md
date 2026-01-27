# Kapitel 5: Datenmodell

## 5.1 Übersicht

Das Datenmodell umfasst **14 Haupttabellen** in PostgreSQL 15 mit Triggers, Constraints und Indizes für Datenintegrität und Performance. Die Struktur wurde über **15 Flyway-Migrationen** evolutionär entwickelt.

---

## 5.2 Entitäten im Detail

### users (Benutzer)

Zentrale Identitätstabelle, synchronisiert mit Keycloak.

| Spalte | Typ | Constraints | Beschreibung |
|--------|-----|-------------|--------------|
| id | UUID | PRIMARY KEY | Eindeutige Benutzer-ID |
| username | VARCHAR(128) | NOT NULL, UNIQUE | Benutzername |
| email | VARCHAR(320) | NOT NULL, UNIQUE | E-Mail-Adresse |
| first_name | VARCHAR(128) | NULL | Vorname |
| last_name | VARCHAR(128) | NULL | Nachname |
| created_at | TIMESTAMP | NOT NULL | Erstellungszeitpunkt |
| updated_at | TIMESTAMP | NOT NULL | Letztes Update |

**Indizes:** `idx_users_email`, `idx_users_username`
**Beziehungen:** 1:1 zu employee_profiles, 1:N zu activities

### employee_profiles (Mitarbeiterprofile)

Erweiterte Profilinformationen (1:1 zu users).

| Spalte | Typ | Constraints |
|--------|-----|-------------|
| user_id | UUID | PRIMARY KEY, FK → users |
| position_id | UUID | FK → positions |
| location_id | UUID | FK → locations |
| availability | VARCHAR(50) | NOT NULL, DEFAULT 'UNAVAILABLE' |
| years_of_experience | NUMERIC(3,1) | NOT NULL, CHECK >= 0 |
| bio | TEXT | CHECK LENGTH <= 500 |

**Trigger:** `update_updated_at`, `initialize_user_statistics`, `log_profile_activity`

### employee_skills (Mitarbeiter-Skills)

Junction-Tabelle (N:M) mit Proficiency-Metadaten.

| Spalte | Typ | Constraints |
|--------|-----|-------------|
| id | UUID | PRIMARY KEY |
| employee_id | UUID | FK → employee_profiles, NOT NULL |
| skill_id | UUID | FK → skills, NOT NULL |
| proficiency_score | INTEGER | CHECK 0-100 |
| years_of_experience | NUMERIC(3,1) | CHECK >= 0 |
| last_used | DATE | NULL |

**Constraints:** UNIQUE (employee_id, skill_id)
**Trigger:** `update_total_skills`, `log_employee_skill_activity`

### skills (Skills/Technologien)

Master-Katalog aller Skills.

| Spalte | Typ | Constraints |
|--------|-----|-------------|
| id | UUID | PRIMARY KEY |
| name | VARCHAR(128) | NOT NULL, UNIQUE |
| category_id | UUID | FK → skill_categories |
| is_active | BOOLEAN | NOT NULL, DEFAULT true |

### projects (Projekte)

Projektinformationen mit N:M zu Mitarbeitern.

| Spalte | Typ | Constraints |
|--------|-----|-------------|
| id | UUID | PRIMARY KEY |
| created_by_user_id | UUID | FK → users |
| name | VARCHAR(255) | NOT NULL |
| status | VARCHAR(32) | DEFAULT 'PLANNED' |
| start_date | DATE | NOT NULL |
| end_date | DATE | CHECK >= start_date |
| team_size | INTEGER | CHECK > 0 |

**Status:** PLANNED, ACTIVE, ON_HOLD, COMPLETED, CANCELLED
**Trigger:** `update_project_counts`

### project_members (Projektmitglieder)

Junction-Tabelle (N:M) mit Rollenzuordnung.

| Spalte | Typ | Constraints |
|--------|-----|-------------|
| id | UUID | PRIMARY KEY |
| project_id | UUID | FK → projects, NOT NULL |
| employee_id | UUID | FK → employee_profiles, NOT NULL |
| position_id | UUID | FK → positions |

**Constraints:** UNIQUE (project_id, employee_id)

### user_statistics (Statistiken)

Aggregierte Statistiken (automatisch durch Triggers).

| Spalte | Typ | Beschreibung |
|--------|-----|--------------|
| user_id | UUID | PRIMARY KEY, FK → users |
| total_skills | INTEGER | Anzahl Skills |
| total_projects | INTEGER | Anzahl Projekte |
| active_projects | INTEGER | Aktive Projekte |
| average_skill_score | NUMERIC(5,2) | Durchschnitt |

**Aktualisierung:** Automatisch durch Triggers

### activities (Aktivitätslog)

Immutable Audit-Log.

| Spalte | Typ | Beschreibung |
|--------|-----|--------------|
| id | UUID | PRIMARY KEY |
| user_id | UUID | FK → users |
| type | VARCHAR(64) | Aktivitätstyp |
| timestamp | TIMESTAMP | Zeitstempel |

**Typen:** SKILL_ADDED, PROFILE_UPDATED, PROJECT_CREATED, etc.

---

## 5.3 Beziehungstypen

### 1:1-Beziehungen
- users ↔ employee_profiles
- users ↔ user_statistics

### 1:N-Beziehungen
- skill_categories → skills
- positions → employee_profiles
- users → activities

### N:M-Beziehungen
- employee_profiles ↔ skills (via employee_skills)
- projects ↔ employee_profiles (via project_members)
- projects ↔ skills (via project_skills)

---

## 5.4 Constraints

### Check Constraints

```sql
-- Proficiency zwischen 0 und 100
CHECK (proficiency_score >= 0 AND proficiency_score <= 100)

-- Jahre nicht negativ
CHECK (years_of_experience >= 0)

-- Enddatum nach Startdatum
CHECK (end_date IS NULL OR end_date >= start_date)
```

### Unique Constraints

- users: username, email
- skills: name
- employee_skills: (employee_id, skill_id)
- project_members: (project_id, employee_id)

---

## 5.5 Indizes

### Primary Key Indizes
Alle Tabellen nutzen **UUID** mit `gen_random_uuid()`.

### Foreign Key Indizes
```sql
CREATE INDEX idx_employee_profiles_location ON employee_profiles(location_id);
CREATE INDEX idx_employee_skills_employee ON employee_skills(employee_id);
CREATE INDEX idx_activities_user_timestamp ON activities(user_id, timestamp DESC);
```

### Query-Optimierung
```sql
CREATE INDEX idx_employee_skills_skill_proficiency
ON employee_skills(skill_id, proficiency_score DESC);
```

---

## 5.6 Database Triggers

### Timestamp-Management

```sql
CREATE FUNCTION update_updated_at_column() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

Trigger auf allen Tabellen: `trigger_users_updated_at`, etc.

### Statistik-Automatisierung

```sql
CREATE FUNCTION update_total_skills() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_statistics (user_id, total_skills, average_skill_score)
    SELECT employee_id, COUNT(*), AVG(proficiency_score)
    FROM employee_skills WHERE employee_id = NEW.employee_id
    ON CONFLICT (user_id) DO UPDATE SET
        total_skills = EXCLUDED.total_skills,
        average_skill_score = EXCLUDED.average_skill_score;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

### Activity-Logging

```sql
CREATE FUNCTION log_employee_skill_activity() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO activities (user_id, type, entity_id, entity_type)
    VALUES (NEW.employee_id, 'SKILL_ADDED', NEW.skill_id, 'SKILL');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

---

## 5.7 Flyway-Migrationen Übersicht

| Version | Beschreibung | Tabellen |
|---------|--------------|----------|
| **V1** | Basistabellen | users, positions, locations, skills |
| **V2** | Mitarbeiterprofile | employee_profiles, employee_skills |
| **V3** | Projekte | projects, project_skills |
| **V4** | Analytics | user_statistics, activities |
| **V5-V8** | Triggers | Timestamp, Statistik, Activity-Logging |
| **V9** | Initiale Daten | Referenzdaten |
| **V11** | Refactoring | project_members (N:M) |
| **V14** | Rollenanfragen | role_requests |

---

## 5.8 Zusammenfassung

**Charakteristiken:**
- **11 Haupttabellen** für Domänenmodelle
- **3 Junction-Tabellen** für N:M-Beziehungen
- **UUID-basierte** Primary Keys
- **6 Trigger-Funktionen** für Automatisierung
- **34 Indizes** für Performance
- **15 Flyway-Migrationen** für Evolution

**Design-Prinzipien:**
- ✅ 3. Normalform für Konsistenz
- ✅ Referenzielle Integrität
- ✅ Denormalisierung wo nötig (user_statistics)
- ✅ Audit-Trail (activities)
- ✅ Automatisierung (Triggers)
- ✅ Performance (Strategische Indizes)
