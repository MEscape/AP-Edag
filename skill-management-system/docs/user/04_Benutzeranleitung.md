# 4. Benutzeranleitung

## 4.1 Erste Schritte

### Erstanmeldung

1. Browser öffnen → `https://app.skill-management.edag.com`
2. "Anmelden" klicken → Weiterleitung zu Keycloak
3. EDAG-Zugangsdaten eingeben (Benutzername + Passwort)
4. Bei Erstanmeldung: Profil vervollständigen

**Wichtig:** Session bleibt 24h gültig, dann erneute Anmeldung erforderlich.

---

### Profil vervollständigen

**Pflichtfelder:**
- Position (z.B. "Software Engineer", "Projektleiter")
- Standort (z.B. "Fulda", "Berlin")
- Mindestens 1 Skills hinzufügen
- Biografie (max. 500 Zeichen)
- Verfügbarkeit (Verfügbar, Begrenzt, Nicht verfügbar)

---

## 4.2 Profilverwaltung

### Profil bearbeiten

1. Sidebar → "Mein Profil"
2. "Bearbeiten" Button klicken
3. Felder ändern:
   - **Position:** Dropdown-Auswahl
   - **Standort:** Dropdown-Auswahl
   - **Biografie:** Freitext (max. 500 Zeichen)
   - **Verfügbarkeit:** Verfügbar | Begrenzt verfügbar | Nicht verfügbar
4. "Speichern" → Toast-Benachrichtigung "Profil aktualisiert"

**Tipp:** Name und E-Mail können nicht geändert werden (Keycloak-Daten).

---

### Verfügbarkeit setzen

**Status-Optionen:**
- **Verfügbar:** Offen für neue Projekte
- **Begrenzt verfügbar:** Teilweise für neue Aufgaben verfügbar
- **Nicht verfügbar:** Aktuell ausgelastet

**Verwendung:** Projektleiter filtern nach Verfügbarkeit bei Mitarbeitersuche.

---

## 4.3 Skill-Management

### Skill hinzufügen

1. "Mein Profil" → "Skills" Sektion
2. "+ Skill hinzufügen" Button
3. Dialog:
   - **Skill auswählen:** Dropdown mit 200+ Skills (Suche möglich)
   - **Proficiency Score:** Slider 0-100
   - **Erfahrungsjahre:** Eingabefeld (z.B. 2.5)
4. "Hinzufügen" → Skill erscheint in Liste

**Skill-Kategorien:** Frontend, Backend, DevOps, Datenbanken, Testing, Project Management, Mobile, Security, Data Science, UI/UX, Business Analysis, Soft Skills, Sprachen

---

### Skill bearbeiten

1. Skill in Liste finden
2. ✏️ "Bearbeiten" Icon klicken
3. Score oder Erfahrungsjahre ändern
4. "Speichern"

---

### Skill löschen

1. 🗑️ "Löschen" Icon klicken
2. Bestätigungs-Dialog: "Ja, löschen"

**Achtung:** Gelöschte Skills werden aus Statistiken entfernt (Trigger: `update_total_skills`).

---

### Proficiency Score richtig bewerten

| Score | Level | Beschreibung |
|-------|-------|--------------|
| **0-25** | Grundkenntnisse | Basics bekannt, Anleitung nötig, 1-2 Tutorials |
| **26-50** | Fortgeschritten | Selbstständiges Arbeiten, 1-2 Projekte |
| **51-75** | Sehr gut | Produktiv, 3+ Projekte, kann andere unterstützen |
| **76-100** | Experte | Go-To-Person, 5+ Jahre, komplexe Probleme |

---

## 4.4 Mitarbeitersuche

### Suche starten

1. Sidebar → "Mitarbeiter suchen"
2. Filter setzen:
   - **Skills:** Mehrfachauswahl (UND-Verknüpfung!)
   - **Standort:** Mehrfachauswahl
   - **Position:** Mehrfachauswahl
   - **Verfügbarkeit:** Checkbox
3. "Suchen" → Ergebnisse (20 pro Seite)

---

### Filter kombinieren

**Beispiel 1 - Java-Entwickler in Fulda:**
```
Skills: Java
Standort: Fulda
Position: Software Engineer
```

**Beispiel 2 - Kubernetes-Experten (min. 2 Jahre):**
```
Skills: Kubernetes
Mindest-Erfahrung: 2 Jahre
```

**Beispiel 3 - Verfügbare Full-Stack-Entwickler:**
```
Skills: React, Spring Boot
Verfügbarkeit: ✅
```

**Wichtig:** Mehrere Skills = UND-Verknüpfung (Mitarbeiter muss ALLE Skills haben). Für mehr Ergebnisse: Weniger Skills auswählen.

---

### Suchergebnisse

**Anzeige pro Mitarbeiter:**
- Name, Position, Standort
- Skill-Badges (farbcodiert nach Score)
- Verfügbarkeit-Status
- "Profil anzeigen" Button

---

### Profil eines Mitarbeiters ansehen

1. "Profil anzeigen" klicken
2. Detailansicht:
   - Stammdaten (Name, E-Mail, Position, Standort)
   - Biografie
   - Alle Skills mit Scores und Erfahrungsjahren
   - Statistiken (Total Skills, Durchschnittsscore)
   - Aktive Projekte

**Hinweis:** Fremde Profile sind schreibgeschützt (nur eigenes Profil editierbar).

---

## 4.5 Projektmanagement (Manager)

**Verfügbar für:** Manager + Admin

### Projekt erstellen

1. Sidebar → "Projekte" → "+ Neues Projekt"
2. Formular:
   - **Name:** Projektname (max. 100 Zeichen)
   - **Beschreibung:** Freitext (max. 1000 Zeichen)
   - **Startdatum:** Datepicker
   - **Enddatum:** Datepicker
   - **Status:** PLANNED | ACTIVE | COMPLETED
3. "Erstellen" → Projekt wird angelegt

---

### Team zusammenstellen

1. Projektdetails öffnen → "Team" Tab
2. "+ Mitglied hinzufügen"
3. Mitarbeitersuche (Skills, Standort filtern)
4. Mitarbeiter auswählen → "Hinzufügen"
5. Mitglied erscheint in Team-Liste

**Team-Größe:** Kein Limit (wird automatisch berechnet via Trigger).

---

### Mitglied aus Projekt entfernen

1. Team-Liste → Mitglied finden
2. "Entfernen" Button
3. Bestätigung → Mitglied entfernt

**Hinweis:** Mitarbeiter können sich nicht selbst entfernen (nur Projektleiter/Admin).

---

### Projekt bearbeiten

1. Projektdetails → "Bearbeiten"
2. Felder ändern (Name, Beschreibung, Zeitraum, Status)
3. "Speichern"

**Status-Übergang:**
PLANNED → ACTIVE → COMPLETED (oder ON_HOLD)

---

### Projektsuche

1. Sidebar → "Projekte durchsuchen" (Manager/Admin)
2. Filter:
   - **Status:** PLANNED, ACTIVE, COMPLETED, ON_HOLD
   - **Zeitraum:** Von-Bis
   - **Skills:** Projektrelevante Skills
3. Ergebnisse → Projektdetails anzeigen

---

## 4.6 Rollenanfrage stellen

### Manager-Rolle beantragen

1. Sidebar → "Einstellungen" → "Rollen"
2. "Rollenanfrage stellen" Button
3. Formular:
   - **Gewünschte Rolle:** Manager | Admin
   - **Begründung:** Freitext (z.B. "Neue Position als Projektleiter")
4. "Anfrage absenden"

**Dauer:** 1-3 Werktage Bearbeitung

**Benachrichtigung:** E-Mail bei Genehmigung/Ablehnung

---

### Status prüfen

1. Einstellungen → "Rollen" → "Meine Anfragen"
2. Status:
   - **Ausstehend** ⏳: In Bearbeitung
   - **Genehmigt** ✅: Rolle wurde zugewiesen
   - **Abgelehnt** ❌: Anfrage abgelehnt (mit Grund)

---

## 4.7 Rollenanfragen genehmigen (Admin)

1. Sidebar → "Admin" → "Rollenanfragen"
2. Liste aller Anfragen (ausstehend)
3. Anfrage auswählen → Details ansehen:
   - Benutzername
   - Gewünschte Rolle
   - Begründung
   - Anfragedatum
4. Aktion wählen:
   - **Genehmigen:** Rolle wird automatisch in Keycloak zugewiesen
   - **Ablehnen:** Anfrage ablehnen (optional: Ablehnungsgrund angeben)

**Wichtig:** Bei Genehmigung erfolgt automatische Keycloak-Rollenzuweisung (Admin REST API).

---

## 4.8 Dashboard & Analytics

### Dashboard-Übersicht

**Anzeige:**
- **Total Skills:** Anzahl hinzugefügter Skills
- **Durchschnittsscore:** Durchschnitt aller Proficiency Scores
- **Aktive Projekte:** Anzahl Projekte mit Status ACTIVE
- **Skills Chart:** Die entwicklung der Skills -> Hinzugefügt/Gelöscht etc. (Chart)

**Aktualisierung:** Automatisch via Datenbank-Trigger (real-time).

---

### Activity-Log

1. Sidebar → "Meine Aktivitäten"
2. Chronologische Liste:
   - Skill hinzugefügt/gelöscht
   - Profil aktualisiert
   - Projekt beigetreten/verlassen

**Zweck:** Nachvollziehbarkeit aller Änderungen (Audit-Log).

---

## 4.9 Troubleshooting

### Login-Probleme

**Problem:** "Ungültige Zugangsdaten"
**Lösung:**
- CapsLock prüfen
- Passwort zurücksetzen ("Passwort vergessen?")
- IT kontaktieren bei Sperrung

---

### Langsame Performance

**Problem:** Seite lädt langsam
**Lösung:**
- Internetverbindung prüfen
- Browser-Cache leeren (Strg+Shift+Delete)
- Anderen Browser testen (Chrome/Edge empfohlen)
- VPN-Verbindung prüfen

---

### Skill lässt sich nicht hinzufügen

**Problem:** Fehlermeldung "Skill existiert bereits"
**Lösung:** Skill ist bereits vorhanden → Bearbeiten statt neu hinzufügen

---

### Filter funktionieren nicht

**Problem:** Keine Suchergebnisse trotz bekannter Mitarbeiter
**Lösung:**
- Filter zurücksetzen (zu restriktiv?)
- Weniger Skills auswählen (UND-Verknüpfung!)
- Verschiedene Schreibweisen probieren

---

### Session abgelaufen

**Problem:** "Session expired" Meldung
**Lösung:** Nach 24h normal → Erneut anmelden

---

## 4.10 Best Practices

### Für Mitarbeiter

- ✅ Profil quartalsweise aktualisieren
- ✅ Mindestens 5-10 Kern-Skills pflegen
- ✅ Ehrliche Skill-Bewertung (keine Über-/Unterbewertung)
- ✅ Verfügbarkeit aktuell halten
- ✅ Biografie aussagekräftig gestalten

### Für Projektleiter

- ✅ Realistische Skill-Anforderungen definieren
- ✅ Filter kombinieren (Skills + Standort + Verfügbarkeit)
- ✅ Mitarbeiterprofile vollständig prüfen vor Projektzuordnung
- ✅ Team-Größe angemessen wählen (3-10 Personen optimal)

### Für Admins

- ✅ Rollenanfragen zeitnah bearbeiten (1-3 Werktage)
- ✅ Begründungen prüfen vor Genehmigung
- ✅ Rücksprache mit Vorgesetzten bei Admin-Anfragen
- ✅ Skill-Katalog bei Bedarf erweitern
