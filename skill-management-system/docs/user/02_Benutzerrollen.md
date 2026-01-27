# 2. Benutzerrollen

## 2.1 Rollenübersicht

Das Skill Management System verwendet **rollenbasierte Zugriffskontrolle (RBAC)** mit 3 Hauptrollen:

---

### **User (Mitarbeiter)**

**Standardrolle für alle Benutzer**

**Berechtigungen:**
- ✅ Eigenes Profil bearbeiten (Position, Standort, Bio, Verfügbarkeit)
- ✅ Skills hinzufügen, bearbeiten, löschen
- ✅ Mitarbeitersuche mit Filtern nutzen
- ✅ Fremde Profile ansehen (schreibgeschützt)
- ✅ Eigene Projekte ansehen
- ✅ Dashboard und Statistiken einsehen
- ❌ Projekte erstellen oder Teams zusammenstellen
- ❌ Fremde Profile bearbeiten
- ❌ Rollenanfragen genehmigen

**Typische Nutzer:** Alle EDAG-Mitarbeiter

**Hauptaufgaben:**
- Skill-Profil aktuell halten
- Nach Kollegen mit bestimmten Skills suchen
- Eigene Verfügbarkeit setzen

---

### **Manager (Projektleiter)**

**Erweiterte Rolle für Projektverantwortliche**

**Berechtigungen:**
- ✅ Alle User-Berechtigungen
- ✅ Projekte erstellen und bearbeiten
- ✅ Team-Mitglieder zu Projekten hinzufügen/entfernen
- ✅ Projektsuche mit erweiterten Filtern
- ✅ Projektdetails verwalten (Status, Zeitraum, Beschreibung)
- ❌ Rollenanfragen genehmigen
- ❌ Benutzerverwaltung
- ❌ System-Einstellungen ändern

**Typische Nutzer:** Projektleiter, Team-Leads, Abteilungsleiter

**Hauptaufgaben:**
- Passende Teammitglieder für Projekte finden
- Teams zusammenstellen basierend auf Skills
- Projekt-Status verwalten (PLANNED, ACTIVE, COMPLETED)

---

### **Admin (Administrator)**

**Vollzugriff für IT und HR**

**Berechtigungen:**
- ✅ Alle User-Berechtigungen
- ✅ Rollenanfragen genehmigen/ablehnen
- ✅ Benutzerrollen direkt in Keycloak zuweisen
- ✅ System-Überwachung und Wartung

**Typische Nutzer:** IT-Administratoren, System-Verantwortliche

**Hauptaufgaben:**
- Rollenanfragen zeitnah bearbeiten (1-3 Werktage)
- Benutzerverwaltung (Profile ansehen, Rollen zuweisen)
- Technische Probleme beheben
- Skill-Katalog erweitern (zukünftig)

---

## 2.2 Rollenzuweisung

**Standardrolle:** Jeder neue Benutzer erhält automatisch die Rolle **User** bei Erstanmeldung.

**Rollenerhöhung:**
- Über **Rollenanfrage** im System (Einstellungen → "Rollenanfrage stellen")
- Begründung erforderlich (z.B. "Neue Position als Projektleiter")
- Admin-Genehmigung notwendig (1-3 Werktage)
- Automatische Keycloak-Rollenzuweisung bei Genehmigung

**Wichtig:** Rollen werden in Keycloak verwaltet. Direkte Änderungen nur durch IT-Administratoren.

---

## 2.3 Rollenhierarchie

```
Admin
└── Manager
    └── User
```

**Berechtigungen sind rollenbasiert und nicht vollständig kumulativ:**
- **Admin** besitzt administrative Rechte (z. B. Benutzer- und Stammdatenverwaltung), jedoch **nicht automatisch alle Manager-Funktionen**
- **Manager** verfügt über projektbezogene Rechte, wie **Projekte suchen und erstellen**, sowie User-Rechte
- **User** hat ausschließlich Basis-Rechte

**Wichtige Regeln:**
- Berechtigungen werden **explizit pro Rolle definiert**
- Rollen erben **keine vollständigen Rechte** voneinander
- **Ein Benutzer = Eine Rolle** (keine Mehrfachrollen)
```

---

## 2.4 Kontakt für Rollenwechsel

**Rollenanfrage über System:**
Einstellungen → "Rollenanfrage stellen" → Automatischer Workflow
