# CI/CD-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [CI/CD-Architektur](#cicd-architektur)
2. [Jenkins Setup](#jenkins-setup)
3. [Pipeline-Struktur](#pipeline-struktur)
4. [Build-Prozesse](#build-prozesse)
5. [Quality Gates](#quality-gates)
6. [Docker Registry](#docker-registry)
7. [Deployment-Automation](#deployment-automation)
8. [Best Practices](#best-practices)

---

## CI/CD-Architektur

### Übersicht

```
┌─────────────────────────────────────────────────────────────────┐
│                    Developer Workflow                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ git push
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Bitbucket Repository                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ webhook trigger
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                         Jenkins                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  1. Checkout Code                                        │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  2. Backend: Build & Test (Maven)                       │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  3. Backend: SonarQube Analysis ────────►                │   │
│  └──────────────────────────────────────────┼───────────────┘   │
│  ┌──────────────────────────────────────────┼───────────────┐   │
│  │  4. Backend: Quality Gate Wait           │               │   │
│  └──────────────────────────────────────────┼───────────────┘   │
│                                              │                   │
│  ┌──────────────────────────────────────────▼───────────────┐   │
│  │                    SonarQube                             │   │
│  │  - Code Quality Analysis                                 │   │
│  │  - Security Vulnerabilities                              │   │
│  │  - Code Coverage                                         │   │
│  └──────────────────────────────────────────┬───────────────┘   │
│                                              │                   │
│  ┌──────────────────────────────────────────┼───────────────┐   │
│  │  5. Frontend: Build & Test (npm)         │               │   │
│  └──────────────────────────────────────────┼───────────────┘   │
│  ┌──────────────────────────────────────────▼───────────────┐   │
│  │  6. Frontend: SonarQube Analysis                         │   │
│  └──────────────────────────────────────────┬───────────────┘   │
│  ┌──────────────────────────────────────────┼───────────────┐   │
│  │  7. Frontend: Quality Gate Wait          │               │   │
│  └──────────────────────────────────────────┴───────────────┘   │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  8. Docker Build & Push (only main branch)              │   │
│  └──────────────────────────────────────────┬───────────────┘   │
│                                              │                   │
│                                              │                   │
└──────────────────────────────────────────────┼───────────────────┘
                                               │
                                               │ push images
                                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Docker Registry                               │
│  - registry.skill-management-devops.edag.com                    │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ trigger deploy job
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Kubernetes Cluster                            │
│  - skill-management-system-dev                                  │
│  - skill-management-system-prod                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Jenkins Setup

### Jenkins Configuration as Code (JCasC)

**Deployment**: Kubernetes Deployment in `skill-management-devops-prod` Namespace

**Image**: `jenkins/jenkins:2.534-jdk21`

### InitContainers

**1. Corporate CA Certificates Import**:
```yaml
initContainers:
- name: install-ca-certs
  image: jenkins/jenkins:2.534-jdk21
  command:
    - sh
    - -c
    - |
      mkdir -p /var/jenkins_home/cacerts
      cp /opt/java/openjdk/lib/security/cacerts /var/jenkins_home/cacerts/cacerts
      
      for cert in /var/jenkins_certs/*.pem; do
        cert_name=$(basename "$cert" .pem)
        keytool -import -trustcacerts -noprompt \
          -alias "$cert_name" \
          -file "$cert" \
          -keystore /var/jenkins_home/cacerts/cacerts \
          -storepass changeit
      done
  volumeMounts:
    - name: jenkins-home
      mountPath: /var/jenkins_home
    - name: corporate-ca-certs
      mountPath: /var/jenkins_certs
```

**Zweck**: EDAG Corporate CA Zertifikate importieren für HTTPS-Verbindungen zu internen Services

**2. Plugin Installation**:
```yaml
- name: install-plugins
  image: jenkins/jenkins:2.534-jdk21
  command:
    - sh
    - -c
    - |
      mkdir -p /var/jenkins_home/plugins
      jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt \
        --plugin-download-directory /var/jenkins_home/plugins
  volumeMounts:
    - name: jenkins-home
      mountPath: /var/jenkins_home
    - name: plugins-list
      mountPath: /usr/share/jenkins/ref/plugins.txt
      subPath: plugins.txt
```

**Installierte Plugins** (aus `plugins.txt`):
```
kubernetes:4250.v3c41b_4b_80b_4b_
kubernetes-credentials-provider:1.262.v2670ef7ea_0c5
docker-workflow:580.vc0c340686b_54
git:5.5.2
sonar:2.17.2
bitbucket:2.0.0
configuration-as-code:1810.v9b_c30a_249a_4c
```

### Environment Variables

```yaml
env:
- name: JAVA_OPTS
  value: "-Djenkins.install.runSetupWizard=false -Djavax.net.ssl.trustStore=/var/jenkins_home/cacerts/cacerts"
- name: CASC_JENKINS_CONFIG
  value: "/var/jenkins_config"

# Registry Credentials
- name: REGISTRY_USER
  valueFrom:
    secretKeyRef:
      name: docker-registry-secret
      key: REGISTRY_USER
- name: REGISTRY_PASSWORD
  valueFrom:
    secretKeyRef:
      name: docker-registry-secret
      key: REGISTRY_PASSWORD

# SonarQube Token
- name: SONARQUBE_TOKEN
  valueFrom:
    secretKeyRef:
      name: sonarqube-secret
      key: SONARQUBE_TOKEN

# Bitbucket Credentials
- name: BITBUCKET_USERNAME
  valueFrom:
    secretKeyRef:
      name: bitbucket-secret
      key: BITBUCKET_USERNAME
- name: BITBUCKET_PASSWORD
  valueFrom:
    secretKeyRef:
      name: bitbucket-secret
      key: BITBUCKET_PASSWORD
```

### RBAC (ServiceAccount)

**jenkins-serviceaccount.yaml**:
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: jenkins
  namespace: skill-management-devops-prod
```

**jenkins-rbac.yaml**:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: jenkins-role
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log", "pods/exec"]
  verbs: ["create", "delete", "get", "list", "patch", "update", "watch"]
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get", "list"]

---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: jenkins-rolebinding
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: jenkins-role
subjects:
- kind: ServiceAccount
  name: jenkins
  namespace: skill-management-devops-prod
```

**Zweck**: Jenkins kann Pods für Pipeline-Agents erstellen (Kubernetes Plugin)

---

## Pipeline-Struktur

### Jenkinsfile

**Datei**: `Jenkinsfile` (Root des Projekts)

```groovy
pipeline {
  agent none  // Dynamische Agent-Zuweisung

  options {
    disableConcurrentBuilds()  // Keine parallelen Builds
    timeout(time: 1, unit: 'HOURS')
  }

  environment {
    REGISTRY_EXTERNAL = 'registry.skill-management-devops.edag.com'
    BACKEND_IMAGE_EXTERNAL = "${REGISTRY_EXTERNAL}/skill-management-backend"
    FRONTEND_IMAGE_EXTERNAL = "${REGISTRY_EXTERNAL}/skill-management-frontend"
  }

  stages { ... }
}
```

### Agent-Strategie

**Kubernetes Pod Templates**:

**Universal Agent** (für Checkout, Quality Gate Wait):
```groovy
agent { label 'universal' }
```

**Backend Agent** (Maven + Docker):
```groovy
agent { label 'backend' }
// Containers: maven:3.9-eclipse-temurin-21, docker:dind
```

**Frontend Agent** (Node.js + Docker):
```groovy
agent { label 'frontend' }
// Containers: node:22-alpine, docker:dind
```

---

## Build-Prozesse

### Stage 1: Checkout

```groovy
stage('Checkout') {
  agent { label 'universal' }
  steps {
    checkout scm  // SCM = Bitbucket
  }
}
```

**Trigger**: Webhook von Bitbucket bei Push

---

### Stage 2-4: Backend Pipeline

**Stage 2: Test & Package**

```groovy
stage('Backend: Build & Analyze') {
  agent { label 'backend' }
  stages {
    stage('Test & Package') {
      steps {
        container('maven') {
          sh '''#!/bin/bash
            set -euxo pipefail
            cd backend
            chmod +x ./mvnw
            ./mvnw -B --no-transfer-progress clean test package
          '''
        }
      }
    }
  }
}
```

**Maven-Befehle**:
- `clean`: Vorherige Builds entfernen
- `test`: Unit-Tests ausführen (JUnit 5)
- `package`: JAR erstellen

**Artefakte**:
- `backend/target/skill-management-system-0.0.1-SNAPSHOT.jar`
- `backend/target/site/jacoco/index.html` (Coverage Report)

---

**Stage 3: SonarQube Analysis**

```groovy
stage('SonarQube Analysis') {
  steps {
    container('maven') {
      withSonarQubeEnv('SonarQube') {
        sh '''#!/bin/bash
          set -euxo pipefail
          cd backend
          ./mvnw -B --no-transfer-progress sonar:sonar \
            -Dsonar.projectKey=sms-backend \
            -Dsonar.projectName="SMS Backend"
        '''
      }
    }
  }
}
```

**SonarQube Maven Plugin**: `org.sonarsource.scanner.maven:sonar-maven-plugin`

**Analysierte Metriken**:
- **Bugs**: Fehler im Code
- **Vulnerabilities**: Sicherheitslücken
- **Code Smells**: Wartbarkeit
- **Coverage**: Test-Abdeckung (JaCoCo)
- **Duplications**: Code-Duplikate

**Konfiguration** (`pom.xml`):
```xml
<properties>
  <sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
  <sonar.coverage.jacoco.xmlReportPaths>
    ${project.build.directory}/site/jacoco/jacoco.xml
  </sonar.coverage.jacoco.xmlReportPaths>
</properties>
```

---

**Stage 4: Quality Gate**

```groovy
stage('Backend: Quality Gate') {
  agent { label 'universal' }
  steps {
    timeout(time: 10, unit: 'MINUTES') {
      waitForQualityGate abortPipeline: true
    }
  }
}
```

**Quality Gate Definition** (SonarQube):
```yaml
Conditions:
  - Coverage >= 80%
  - Maintainability Rating = A
  - Reliability Rating = A
  - Security Rating = A
  - Security Hotspots Reviewed >= 100%
```

**Verhalten**: Pipeline bricht ab (`abortPipeline: true`) wenn Quality Gate fehlschlägt

---

### Stage 5-7: Frontend Pipeline

**Stage 5: Install & Build**

```groovy
stage('Frontend: Build & Analyze') {
  agent { label 'frontend' }
  stages {
    stage('Install & Build') {
      steps {
        container('nodejs') {
          sh '''#!/bin/bash
            set -euxo pipefail
            cd frontend
            npm ci
            export SKIP_ENV_VALIDATION=true
            npm run build
          '''
        }
      }
    }
  }
}
```

**npm Befehle**:
- `npm ci`: Clean Install (reproduzierbar, basiert auf package-lock.json)
- `npm run build`: Next.js Production Build

**Build-Artefakte**:
- `frontend/.next/`: Next.js Build Output
- `frontend/.next/standalone/`: Standalone Server
- `frontend/.next/static/`: Static Assets

**Environment Variable**:
- `SKIP_ENV_VALIDATION=true`: Umgeht `.env` Validierung im CI

---

**Stage 6: SonarQube Analysis**

```groovy
stage('SonarQube Analysis') {
  steps {
    container('nodejs') {
      withSonarQubeEnv('SonarQube') {
        sh '''#!/bin/bash
          set -euxo pipefail
          cd frontend
          npx sonar-scanner \
            -Dsonar.projectKey=sms-frontend \
            -Dsonar.projectName="SMS Frontend" \
            -Dsonar.sources=./src
        '''
      }
    }
  }
}
```

**sonar-project.properties** (Frontend-Root):
```properties
sonar.projectKey=sms-frontend
sonar.projectName=SMS Frontend
sonar.projectVersion=1.0
sonar.sources=src
sonar.sourceEncoding=UTF-8
sonar.javascript.lcov.reportPaths=coverage/lcov.info
sonar.exclusions=**/*.test.ts,**/*.test.tsx,**/node_modules/**
```

**Analysierte Dateien**:
- TypeScript/TSX Files
- Code Complexity
- TypeScript Errors (via ESLint)

---

**Stage 7: Quality Gate**

```groovy
stage('Frontend: Quality Gate') {
  agent { label 'universal' }
  steps {
    timeout(time: 10, unit: 'MINUTES') {
      waitForQualityGate abortPipeline: true
    }
  }
}
```

---

### Stage 8: Build & Push Docker Images

**Condition**: Nur auf `main` Branch

```groovy
stage('Build & Push Images') {
  when {
    branch 'main'
  }
  parallel {
    stage('Backend: Build & Push Image') { ... }
    stage('Frontend: Build & Push Image') { ... }
  }
}
```

**Backend Image Build**:
```groovy
stage('Backend: Build & Push Image') {
  agent { label 'backend' }
  steps {
    container('docker') {
      script {
        docker.withRegistry("https://${REGISTRY_EXTERNAL}", 'docker-registry') {
          dir('backend') {
            def backendImage = docker.build("${BACKEND_IMAGE_EXTERNAL}:${BUILD_NUMBER}")
            backendImage.push()
            backendImage.push('latest')
            echo "Backend image pushed: ${BACKEND_IMAGE_EXTERNAL}:${BUILD_NUMBER}"
          }
        }
      }
    }
  }
}
```

**Docker Build**:
- **Context**: `backend/` Verzeichnis
- **Dockerfile**: Multi-Stage (Build + Runtime)
- **Tags**: `${BUILD_NUMBER}` + `latest`

**Backend Dockerfile**:
```dockerfile
# Build Stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

**Frontend Image Build**:
```groovy
stage('Frontend: Build & Push Image') {
  agent { label 'frontend' }
  steps {
    container('docker') {
      script {
        docker.withRegistry("https://${REGISTRY_EXTERNAL}", 'docker-registry') {
          dir('frontend') {
            def frontendImage = docker.build("${FRONTEND_IMAGE_EXTERNAL}:${BUILD_NUMBER}")
            frontendImage.push()
            frontendImage.push('latest')
            echo "Frontend image pushed: ${FRONTEND_IMAGE_EXTERNAL}:${BUILD_NUMBER}"
          }
        }
      }
    }
  }
}
```

**Frontend Dockerfile**:
```dockerfile
# Dependencies Stage
FROM node:22-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

# Build Stage
FROM node:22-alpine AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
ENV SKIP_ENV_VALIDATION=true
RUN npm run build

# Runtime Stage
FROM node:22-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
COPY --from=builder /app/public ./public
EXPOSE 3000
CMD ["node", "server.js"]
```

---

### Stage 9: Trigger Deploy

```groovy
stage('Trigger Deploy') {
  when {
    branch 'main'
  }
  agent { label 'universal' }
  steps {
    script {
      build job: 'skill-management-deploy'
    }
  }
}
```

**Deployment Job**: Separater Jenkins-Job der Kubernetes Apply ausführt

---

## Quality Gates

### SonarQube-Integration

**SonarQube Deployment**: `k8s/devOps/base/sonarqube/`

**Database**: PostgreSQL (separates StatefulSet)

**SonarQube Configuration**:
```yaml
env:
- name: SONAR_JDBC_URL
  value: "jdbc:postgresql://postgres-sonar-service:5432/sonarqube"
- name: SONAR_ES_BOOTSTRAP_CHECKS_DISABLE
  value: "true"
```

### Quality Profiles

**Backend (Java)**:
- **Profile**: Sonar way (Built-in)
- **Rules**: 600+ aktivierte Regeln
- **Categories**: Bug, Vulnerability, Code Smell, Security Hotspot

**Frontend (TypeScript)**:
- **Profile**: Sonar way (Built-in)
- **Rules**: 400+ aktivierte Regeln
- **Linting**: ESLint Integration

### Quality Gates

**Standard Gate**:
```
Conditions:
  On Overall Code:
    - Coverage < 80.0%  → FAIL
    - Duplicated Lines (%) > 3.0%  → FAIL
    - Maintainability Rating worse than A  → FAIL
    - Reliability Rating worse than A  → FAIL
    - Security Rating worse than A  → FAIL
    - Security Hotspots Reviewed < 100%  → FAIL
```

**Pipeline Integration**:
```groovy
withSonarQubeEnv('SonarQube') {
  // SonarQube Analysis
}

waitForQualityGate abortPipeline: true
```

---

## Docker Registry

### Setup

**Deployment**: `k8s/devOps/base/docker-registry/`

**Image**: `registry:2`

**Authentication**: htpasswd

**Storage**: PersistentVolumeClaim (20Gi)

### Configuration

```yaml
env:
- name: REGISTRY_AUTH
  value: "htpasswd"
- name: REGISTRY_AUTH_HTPASSWD_REALM
  value: "Registry Realm"
- name: REGISTRY_AUTH_HTPASSWD_PATH
  value: "/auth/htpasswd"
- name: REGISTRY_STORAGE_FILESYSTEM_ROOTDIRECTORY
  value: "/var/lib/registry"
```

**htpasswd Secret**:
```bash
htpasswd -Bbn <username> <password> | base64
```

### Registry Access

**URL**: `registry.skill-management-devops.edag.com`

**Credentials**: Stored in Jenkins (`docker-registry` Credential)

**Push Command** (manual):
```bash
docker login registry.skill-management-devops.edag.com
docker tag skill-management-backend:latest registry.skill-management-devops.edag.com/skill-management-backend:latest
docker push registry.skill-management-devops.edag.com/skill-management-backend:latest
```

---

## Deployment-Automation

### Deploy Job

**Job**: `skill-management-deploy`

**Trigger**: Manuell oder via Build-Job

**Script**:
```groovy
stage('Deploy to Dev') {
  steps {
    container('kubectl') {
      sh '''
        kubectl apply -k k8s/app/overlays/dev
        kubectl rollout status deployment/dev-sms-keycloak -n skill-management-system-dev
      '''
    }
  }
}

stage('Deploy to Prod') {
  when {
    expression { return params.DEPLOY_TO_PROD }
  }
  steps {
    input message: 'Deploy to Production?', ok: 'Deploy'
    container('kubectl') {
      sh '''
        kubectl apply -k k8s/app/overlays/prod
        kubectl rollout status deployment/prod-sms-keycloak -n skill-management-system-prod
      '''
    }
  }
}
```

**Kustomize Apply**:
- Development: Automatisch nach erfolgreichem Build
- Production: Manueller Approval-Step

---

## Best Practices

### Pipeline Design

✅ **Agent-Strategie**: `agent none` mit dynamischen Agents  
✅ **Parallel Stages**: Backend & Frontend parallel builden  
✅ **Fail Fast**: Quality Gates stoppen Pipeline bei Fehlern  
✅ **Branch Protection**: Images nur von `main` pushen  
✅ **Timeouts**: Verhindert hängende Builds  
✅ **Atomic Builds**: `disableConcurrentBuilds()`  

### Security

✅ **Credentials**: Alle Secrets in Kubernetes Secrets  
✅ **Registry Auth**: htpasswd für Docker Registry  
✅ **RBAC**: Minimale Berechtigungen für Jenkins ServiceAccount  
✅ **TLS**: HTTPS für alle externen Endpoints  
✅ **Vulnerability Scanning**: SonarQube Security Analysis  

### Monitoring

✅ **Build Status**: Jenkins Dashboard  
✅ **Quality Metrics**: SonarQube Dashboard  
✅ **Deployment Status**: `kubectl rollout status`  
✅ **Logs**: Jenkins Console Output + Kubernetes Logs  

### Optimization

✅ **Docker Layer Caching**: Multi-Stage Builds  
✅ **Maven Dependencies**: Cached in separate layer  
✅ **npm ci**: Reproduzierbare Installs  
✅ **Kubernetes Agents**: Ephemeral, nur für Build-Dauer  
✅ **Persistent Volumes**: Jenkins Home, Registry Storage  

---

## Pipeline-Flow Diagramm

```
[Git Push] 
    │
    ├─► [Checkout]
    │
    ├─► [Backend Build & Test]
    │       │
    │       └─► [SonarQube Analysis] ──► [Quality Gate] ──✓ Pass
    │                                                      ✗ Fail → ABORT
    │
    ├─► [Frontend Build & Test]
    │       │
    │       └─► [SonarQube Analysis] ──► [Quality Gate] ──✓ Pass
    │                                                      ✗ Fail → ABORT
    │
    └─► [Docker Build & Push] (only main)
            │
            ├─► [Backend Image] → Registry
            │
            ├─► [Frontend Image] → Registry
            │
            └─► [Trigger Deploy Job]
                    │
                    ├─► [Deploy to Dev] (automatic)
                    │
                    └─► [Deploy to Prod] (manual approval)
```

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Integration-Analyse →](./07_INTEGRATION_ANALYSE.md)
