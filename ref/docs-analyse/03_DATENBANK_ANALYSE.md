# Datenbank-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [Datenbank-Architektur](#datenbank-architektur)
2. [Tabellen-Schema](#tabellen-schema)
3. [Flyway-Migrationen](#flyway-migrationen)
4. [Triggers & Functions](#triggers--functions)
5. [Indizes & Performance](#indizes--performance)
6. [Datenintegrität](#datenintegrität)
7. [Stammdaten](#stammdaten)

---

## Datenbank-Architektur

### Technologie
- **DBMS**: PostgreSQL 15 (Alpine)
- **ORM**: Spring Data JPA + Hibernate
- **Migration-Tool**: Flyway
- **Dialect**: PostgreSQL Dialect
- **Connection Pool**: HikariCP

### Datenbankstruktur

```
skill_management_db
│
├── Stammdaten (Reference Data)
│   ├── users                    # Benutzerverwaltung
│   ├── positions                # Job-Positionen
│   ├── locations                # Standorte
│   ├── skill_categories         # Skill-Kategorien
│   └── skills                   # Skills-Katalog
│
├── Profildaten (Profile Data)
│   ├── employee_profiles        # Mitarbeiterprofile
│   └── employee_skills          # Mitarbeiter-Skill-Zuordnung
│
├── Projektdaten (Project Data)
│   ├── projects                 # Projekte
│   ├── project_members          # Projekt-Mitglieder
│   └── project_skills           # Projekt-Skills
│
├── Analytics (Analytics Data)
│   ├── user_statistics          # Aggregierte User-Statistiken
│   ├── skill_development        # Skill-Entwicklung über Zeit
│   └── activities               # Activity-Log
│
└── Verwaltung (Administration)
    └── role_requests            # Rollenanfragen
```

---

## Tabellen-Schema

### 1. Users (Benutzerverwaltung)

**Tabelle: `users`**
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(128) NOT NULL UNIQUE,
    email VARCHAR(320) NOT NULL UNIQUE,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_created_at ON users(created_at);
```

**Zweck**: Zentrale User-Identity, synchronisiert von Keycloak

**Relationen**:
- `1:1` mit `employee_profiles`
- `1:n` mit `projects`
- `1:n` mit `activities`
- `1:1` mit `user_statistics`

---

### 2. Positions (Job-Rollen)

**Tabelle: `positions`**
```sql
CREATE TABLE positions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_positions_active ON positions(is_active);
CREATE INDEX idx_positions_name ON positions(name);
```

**Zweck**: Master-Katalog für Positionen/Rollen

**Beispiel-Daten**:
- Senior Software Engineer
- Backend Developer
- Frontend Developer
- DevOps Engineer
- Solution Architect
- Project Manager

---

### 3. Locations (Standorte)

**Tabelle: `locations`**
```sql
CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_locations_active ON locations(is_active);
CREATE INDEX idx_locations_name ON locations(name);
```

**Zweck**: Standort-Katalog

**Beispiel-Daten**:
- Fulda
- Berlin
- München
- Hamburg
- Remote

---

### 4. Skill Categories (Kategorien)

**Tabelle: `skill_categories`**
```sql
CREATE TABLE skill_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_skill_categories_active ON skill_categories(is_active);
CREATE INDEX idx_skill_categories_name ON skill_categories(name);
```

**Kategorien** (13 Hauptkategorien):
1. Frontend Development
2. Backend Development
3. Mobile Development
4. Database & Data Engineering
5. Cloud & Infrastructure
6. DevOps & CI/CD
7. Architecture & Design
8. Testing & Quality Assurance
9. Monitoring & Observability
10. Security & Authentication
11. Project Management & Agile
12. Machine Learning & AI
13. Business Intelligence & Analytics

---

### 5. Skills (Skill-Katalog)

**Tabelle: `skills`**
```sql
CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL UNIQUE,
    category_id UUID NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_skills_category FOREIGN KEY (category_id)
        REFERENCES skill_categories(id) ON DELETE RESTRICT
);

CREATE INDEX idx_skills_category_active ON skills(category_id, is_active);
```

**Beispiel-Skills** (200+ Skills im System):

**Frontend**: React, Angular, Vue.js, Next.js, TypeScript, JavaScript, TailwindCSS, Redux, etc.

**Backend**: Java, Spring Boot, Node.js, Python, Django, FastAPI, Go, Rust, GraphQL, REST API, etc.

**DevOps**: Docker, Kubernetes, Jenkins, GitHub Actions, Terraform, Ansible, ArgoCD, etc.

**Database**: PostgreSQL, MySQL, MongoDB, Redis, Elasticsearch, Kafka, etc.

---

### 6. Employee Profiles (Mitarbeiterprofile)

**Tabelle: `employee_profiles`**
```sql
CREATE TABLE employee_profiles (
    user_id UUID PRIMARY KEY,
    position_id UUID,
    location_id UUID,
    availability VARCHAR(50) NOT NULL DEFAULT 'UNAVAILABLE',
    years_of_experience NUMERIC(3,1) NOT NULL DEFAULT 0.0,
    bio TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_position FOREIGN KEY (position_id)
        REFERENCES positions(id) ON DELETE RESTRICT,
    CONSTRAINT fk_employee_location FOREIGN KEY (location_id)
        REFERENCES locations(id) ON DELETE RESTRICT,
    CONSTRAINT chk_years_of_experience CHECK (years_of_experience >= 0),
    CONSTRAINT chk_bio_length CHECK (LENGTH(bio) <= 500)
);

CREATE INDEX idx_employee_profiles_location ON employee_profiles(location_id);
CREATE INDEX idx_employee_profiles_position ON employee_profiles(position_id);
CREATE INDEX idx_employee_profiles_availability ON employee_profiles(availability);
CREATE INDEX idx_employee_profiles_experience ON employee_profiles(years_of_experience);
```

**Availability-Status**:
- `AVAILABLE` - Verfügbar für neue Projekte
- `PARTIALLY_AVAILABLE` - Teilweise verfügbar
- `UNAVAILABLE` - Nicht verfügbar

**Constraints**:
- Bio maximal 500 Zeichen
- Erfahrung >= 0 Jahre
- 1:1 mit User (CASCADE Delete)

---

### 7. Employee Skills (Skill-Zuordnungen)

**Tabelle: `employee_skills`**
```sql
CREATE TABLE employee_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    proficiency_score INTEGER NOT NULL,
    years_of_experience NUMERIC(3,1) NOT NULL DEFAULT 0.0,
    last_used DATE,
    
    CONSTRAINT fk_employee_skill_employee FOREIGN KEY (employee_id)
        REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_skill_skill FOREIGN KEY (skill_id)
        REFERENCES skills(id) ON DELETE CASCADE,
    CONSTRAINT uk_employee_skill UNIQUE (employee_id, skill_id),
    CONSTRAINT chk_proficiency_score CHECK (proficiency_score >= 0 AND proficiency_score <= 100),
    CONSTRAINT chk_skill_years_experience CHECK (years_of_experience >= 0)
);

CREATE INDEX idx_employee_skills_employee ON employee_skills(employee_id);
CREATE INDEX idx_employee_skills_skill ON employee_skills(skill_id);
CREATE INDEX idx_employee_skills_skill_proficiency ON employee_skills(skill_id, proficiency_score DESC);
```

**Proficiency Score**: 0-100
- 0-20: Grundkenntnisse
- 21-40: Fortgeschritten
- 41-60: Kompetent
- 61-80: Expert
- 81-100: Master

**Business Rules**:
- Ein User kann ein Skill nur einmal haben (UNIQUE Constraint)
- Score zwischen 0-100
- Automatische Trigger-Updates für `user_statistics`

---

### 8. Projects (Projekte)

**Tabelle: `projects`**
```sql
CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(32) NOT NULL DEFAULT 'PLANNED',
    start_date DATE NOT NULL,
    end_date DATE,
    position_id UUID NOT NULL,
    client VARCHAR(255),
    team_size INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_project_employee FOREIGN KEY (employee_id)
        REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_project_position FOREIGN KEY (position_id)
        REFERENCES positions(id) ON DELETE RESTRICT,
    CONSTRAINT chk_project_dates CHECK (end_date IS NULL OR end_date >= start_date),
    CONSTRAINT chk_project_team_size CHECK (team_size IS NULL OR team_size > 0),
    CONSTRAINT chk_description_length CHECK (LENGTH(description) <= 2000)
);

CREATE INDEX idx_projects_employee_start_date ON projects(employee_id, start_date DESC);
CREATE INDEX idx_projects_employee_status ON projects(employee_id, status);
CREATE INDEX idx_projects_start_date ON projects(start_date DESC);
CREATE INDEX idx_projects_status ON projects(status);
```

**Project Status**:
- `PLANNED` - Geplant
- `ACTIVE` - Aktiv
- `ON_HOLD` - Pausiert
- `COMPLETED` - Abgeschlossen
- `CANCELLED` - Abgebrochen

---

### 9. Project Members (Team-Zuordnung)

**Tabelle: `project_members`**
```sql
CREATE TABLE project_members (
    project_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    position_id UUID NOT NULL,
    
    PRIMARY KEY (project_id, employee_id),
    
    CONSTRAINT fk_project_member_project FOREIGN KEY (project_id)
        REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_member_employee FOREIGN KEY (employee_id)
        REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_project_member_position FOREIGN KEY (position_id)
        REFERENCES positions(id) ON DELETE RESTRICT
);

CREATE INDEX idx_project_members_employee ON project_members(employee_id);
```

**Zweck**: N:M Beziehung zwischen Projects und Employees

---

### 10. User Statistics (Analytics)

**Tabelle: `user_statistics`**
```sql
CREATE TABLE user_statistics (
    user_id UUID PRIMARY KEY,
    total_skills INTEGER NOT NULL DEFAULT 0,
    total_projects INTEGER NOT NULL DEFAULT 0,
    total_recommendations INTEGER NOT NULL DEFAULT 0,
    active_projects INTEGER NOT NULL DEFAULT 0,
    average_skill_score NUMERIC(5,2) DEFAULT 0.0,
    last_calculated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_user_stats_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_statistics_user UNIQUE (user_id),
    CONSTRAINT chk_total_skills CHECK (total_skills >= 0),
    CONSTRAINT chk_total_projects CHECK (total_projects >= 0),
    CONSTRAINT chk_active_projects CHECK (active_projects >= 0 AND active_projects <= total_projects),
    CONSTRAINT chk_average_skill_score CHECK (average_skill_score IS NULL OR (average_skill_score >= 0 AND average_skill_score <= 100))
);

CREATE INDEX idx_user_stats_last_calculated ON user_statistics(last_calculated);
```

**Automatische Updates**: Via Triggers bei Änderungen in `employee_skills` und `projects`

---

### 11. Activities (Audit-Log)

**Tabelle: `activities`**
```sql
CREATE TABLE activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type VARCHAR(64) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    entity_id UUID,
    entity_type VARCHAR(50),
    
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_activities_user_timestamp ON activities(user_id, timestamp DESC);
CREATE INDEX idx_activities_type ON activities(type);
CREATE INDEX idx_activities_entity ON activities(entity_type, entity_id);

-- Performance-Optimierung für high-traffic table
ALTER TABLE activities SET (
    autovacuum_vacuum_scale_factor = 0.05,
    autovacuum_analyze_scale_factor = 0.02
);
```

**Activity Types**:
- `CREATED_SKILL`
- `UPDATED_SKILL`
- `DELETED_SKILL`
- `UPDATED_PROFILE`
- `CREATED_PROJECT`
- `UPDATED_PROJECT`
- `COMPLETED_PROJECT`

---

## Flyway-Migrationen

### Migration-Übersicht

| Migration | Beschreibung |
|-----------|--------------|
| **V1** | Basis-Tabellen (users, positions, locations, skill_categories, skills) |
| **V2** | Employee Profiles & Skills |
| **V3** | Projects & Project Skills |
| **V4** | Analytics Tabellen (user_statistics, skill_development, activities) |
| **V5** | Update-Timestamp Triggers |
| **V6** | User Statistics Triggers |
| **V7** | Activity Logging Triggers |
| **V8** | Skill Development Triggers |
| **V9** | Initial Reference Data (Stammdaten) |
| **V10** | Employee Profile Auto-Creation |
| **V11** | Project Members Refactoring |
| **V12** | Project Team Size Trigger |
| **V13** | Team Size Constraint Removal |
| **V14** | Role Requests Table |
| **V15** | Extended Activity Logging |

### Flyway-Konfiguration

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    validate-on-migrate: true
    locations: classpath:db/migration
```

**Vorteile**:
- Versionierte Datenbank-Änderungen
- Automatische Migration beim Start
- Rollback-Sicherheit
- Team-Synchronisation

---

## Triggers & Functions

### 1. Update Timestamp Trigger

**Automatisches Update von `updated_at`**

```sql
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Angewendet auf alle Haupt-Tabellen
CREATE TRIGGER trigger_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- ... (für alle Tabellen mit updated_at)
```

---

### 2. User Statistics Trigger

**Automatische Aktualisierung bei Skill-Änderungen**

```sql
CREATE OR REPLACE FUNCTION update_total_skills()
    RETURNS TRIGGER AS $$
DECLARE
    v_employee_id UUID;
    v_total_skills INTEGER;
    v_avg_score NUMERIC(5,2);
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_employee_id := OLD.employee_id;
    ELSE
        v_employee_id := NEW.employee_id;
    END IF;

    SELECT COUNT(*), AVG(proficiency_score)
    INTO v_total_skills, v_avg_score
    FROM employee_skills
    WHERE employee_id = v_employee_id;

    INSERT INTO user_statistics (user_id, total_skills, average_skill_score, last_calculated)
    VALUES (v_employee_id, COALESCE(v_total_skills, 0), v_avg_score, CURRENT_TIMESTAMP)
    ON CONFLICT (user_id) DO UPDATE
        SET total_skills = COALESCE(v_total_skills, 0),
            average_skill_score = v_avg_score,
            last_calculated = CURRENT_TIMESTAMP;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger bei INSERT, UPDATE, DELETE
CREATE TRIGGER trigger_update_total_skills_insert
    AFTER INSERT ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_total_skills();
```

**Effekt**: 
- `total_skills` wird automatisch aktualisiert
- `average_skill_score` wird neu berechnet
- Keine manuelle Aggregation nötig

---

### 3. Activity Logging Trigger

**Automatisches Logging bei Skill-Operationen**

```sql
CREATE OR REPLACE FUNCTION log_skill_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'CREATED_SKILL';
        INSERT INTO activities (user_id, type, entity_id, entity_type)
        VALUES (NEW.employee_id, v_activity_type, NEW.id, 'EMPLOYEE_SKILL');
    ELSIF TG_OP = 'UPDATE' THEN
        v_activity_type := 'UPDATED_SKILL';
        INSERT INTO activities (user_id, type, entity_id, entity_type)
        VALUES (NEW.employee_id, v_activity_type, NEW.id, 'EMPLOYEE_SKILL');
    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'DELETED_SKILL';
        INSERT INTO activities (user_id, type, entity_id, entity_type)
        VALUES (OLD.employee_id, v_activity_type, OLD.id, 'EMPLOYEE_SKILL');
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;
```

**Vorteile**:
- Vollständige Audit-Trail
- Keine Änderung im Application-Code
- Performance-optimiert (nach INSERT/UPDATE/DELETE)

---

## Indizes & Performance

### Strategie

1. **Primary Keys**: Alle Tabellen mit UUID als PK
2. **Foreign Keys**: Automatische Indizes auf FK-Spalten
3. **Lookup-Felder**: Indizes auf häufig gefilterte Spalten
4. **Composite Indizes**: Für Multi-Column Queries
5. **Partial Indizes**: Für `is_active = true` Filter

### Wichtige Performance-Indizes

```sql
-- Employee Search Performance
CREATE INDEX idx_employee_profiles_location ON employee_profiles(location_id);
CREATE INDEX idx_employee_profiles_position ON employee_profiles(position_id);
CREATE INDEX idx_employee_profiles_availability ON employee_profiles(availability);

-- Skill Search Performance
CREATE INDEX idx_employee_skills_skill_proficiency 
    ON employee_skills(skill_id, proficiency_score DESC);

-- Project Timeline Queries
CREATE INDEX idx_projects_employee_start_date 
    ON projects(employee_id, start_date DESC);

-- Activity Log Queries
CREATE INDEX idx_activities_user_timestamp 
    ON activities(user_id, timestamp DESC);
```

### Query-Optimierungen

**Full-Text Search** mit `pg_trgm` Extension:
```sql
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Erlaubt LIKE '%search%' Queries mit Indizes
CREATE INDEX idx_users_name_trgm ON users USING gin (
    (first_name || ' ' || last_name) gin_trgm_ops
);
```

---

## Datenintegrität

### Constraints

1. **NOT NULL**: Pflichtfelder
2. **UNIQUE**: Eindeutige Werte (username, email, skill name)
3. **CHECK**: Validierung (score 0-100, dates, lengths)
4. **FOREIGN KEY**: Referentielle Integrität
5. **CASCADE/RESTRICT**: Löschverhalten

### Cascade-Strategien

```sql
-- CASCADE: Abhängige Daten mitlöschen
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

-- RESTRICT: Löschen verhindern wenn Abhängigkeiten existieren
FOREIGN KEY (category_id) REFERENCES skill_categories(id) ON DELETE RESTRICT
```

**Beispiel**:
- User wird gelöscht → Employee Profile, Skills, Projects werden mitgelöscht (CASCADE)
- Skill Category löschen → Blockiert wenn Skills existieren (RESTRICT)

---

## Stammdaten

### Initial Data Loading (V9 Migration)

**200+ Skills** werden beim ersten Start geladen:

```sql
-- Frontend Development (18 Skills)
INSERT INTO skills (name, category_id, is_active) VALUES
    ('React', '11111111-...', TRUE),
    ('Angular', '11111111-...', TRUE),
    ('Vue.js', '11111111-...', TRUE),
    -- ...

-- Backend Development (21 Skills)
INSERT INTO skills (name, category_id, is_active) VALUES
    ('Java', '22222222-...', TRUE),
    ('Spring Boot', '22222222-...', TRUE),
    ('Node.js', '22222222-...', TRUE),
    -- ...

-- DevOps & CI/CD (14 Skills)
INSERT INTO skills (name, category_id, is_active) VALUES
    ('Docker', '66666666-...', TRUE),
    ('Kubernetes', '66666666-...', TRUE),
    ('Jenkins', '66666666-...', TRUE),
    -- ...
```

**Positions**:
- Software Engineer
- Senior Software Engineer
- Lead Developer
- Solution Architect
- DevOps Engineer
- Project Manager
- Product Owner
- Scrum Master

**Locations**:
- Fulda
- Berlin
- München
- Hamburg
- Stuttgart
- Remote

---

## ER-Diagramm (Vereinfacht)

```
┌─────────────┐
│   users     │
└──────┬──────┘
       │ 1:1
       │
┌──────▼──────────────┐
│ employee_profiles   │
└──────┬──────────────┘
       │ 1:n
       │
┌──────▼──────────────┐      ┌──────────────┐
│ employee_skills     │─────►│   skills     │
└─────────────────────┘ n:1  └──────┬───────┘
                                     │ n:1
                              ┌──────▼──────────────┐
                              │ skill_categories    │
                              └─────────────────────┘

┌──────────────┐
│  projects    │
└──────┬───────┘
       │ n:1
       │
┌──────▼──────────────┐      ┌──────────────┐
│ project_members     │─────►│ positions    │
└─────────────────────┘ n:1  └──────────────┘

┌──────────────┐      ┌──────────────┐
│ locations    │      │ activities   │
└──────────────┘      └──────────────┘
```

---

## Best Practices

✅ **UUID als Primary Keys** - Verteilte Systeme, keine Kollisionen  
✅ **Audit-Felder** (created_at, updated_at) - Nachvollziehbarkeit  
✅ **Soft-Deletes** für Referenzdaten (is_active flag)  
✅ **Check Constraints** - Datenvalidierung auf DB-Ebene  
✅ **Triggers** für berechnete Felder - Konsistenz garantiert  
✅ **Indizes** auf Foreign Keys und Filter-Spalten  
✅ **Normalisierung** - 3NF, keine Redundanzen  
✅ **Versionierung** mit Flyway - Reproduzierbar, nachvollziehbar  

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Security-Analyse →](./04_SECURITY_ANALYSE.md)
