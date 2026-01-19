# Kubernetes-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [Cluster-Architektur](#cluster-architektur)
2. [Namespace-Strategie](#namespace-strategie)
3. [Application Resources](#application-resources)
4. [DevOps Resources](#devops-resources)
5. [Kustomize Configuration](#kustomize-configuration)
6. [Networking & Ingress](#networking--ingress)
7. [Storage & Persistence](#storage--persistence)
8. [Secrets Management](#secrets-management)
9. [Deployment-Strategie](#deployment-strategie)

---

## Cluster-Architektur

### Gesamt-Übersicht

```
┌─────────────────────────────────────────────────────────────────────┐
│                         KUBERNETES CLUSTER                           │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │          Namespace: skill-management-system-dev                │ │
│  │  - PostgreSQL (StatefulSet)                                    │ │
│  │  - Keycloak (Deployment)                                       │ │
│  │                                                                 │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │          Namespace: skill-management-system-prod               │ │
│  │  - PostgreSQL (StatefulSet)                                    │ │
│  │  - Keycloak (Deployment)                                       │ │
│  │                                                                 │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │          Namespace: skill-management-devops-prod               │ │
│  │  - Jenkins (Deployment + PVC)                                  │ │
│  │  - SonarQube (Deployment + PVC)                                │ │
│  │  - PostgreSQL (StatefulSet) - SonarQube DB                     │ │
│  │  - Docker Registry (Deployment + PVC)                          │ │
│  │                                                                 │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │              Traefik Ingress Controller                        │ │
│  │  - TLS Termination (cert-manager)                              │ │
│  │  - Routing (IngressRoute CRDs)                                 │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Namespace-Strategie

### Namespaces

**Anwendungs-Namespaces**:
```yaml
# Development Environment
skill-management-system-dev

# Production Environment
skill-management-system-prod
```

**DevOps-Namespace**:
```yaml
# CI/CD Tools (shared across environments)
skill-management-devops-prod
```

### Namespace-Isolation

**Labels für Organisation**:
```yaml
labels:
  app.kubernetes.io/part-of: skill-management-system
  app.kubernetes.io/instance: dev|prod
  environment: development|production
```

---

## Application Resources

### PostgreSQL (StatefulSet)

**Datei**: `k8s/app/base/postgres/statefulset.yaml`

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
  labels:
    app: postgres
    component: database
    part-of: skill-management-system
spec:
  serviceName: postgres-service
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
        component: database
    spec:
      containers:
      - name: postgres
        image: postgres:15-alpine
        ports:
        - containerPort: 5432
          name: postgres
        env:
        - name: POSTGRES_DB
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: POSTGRES_DB
        - name: POSTGRES_USER
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: POSTGRES_USER
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: POSTGRES_PASSWORD
        - name: KC_DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: keycloak-secret
              key: KC_DB_PASSWORD
        - name: PGDATA
          value: /var/lib/postgresql/data/pgdata
        volumeMounts:
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
        - name: init-scripts
          mountPath: /docker-entrypoint-initdb.d
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          exec:
            command:
            - /bin/sh
            - -c
            - pg_isready -U $POSTGRES_USER -d $POSTGRES_DB
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          exec:
            command:
            - /bin/sh
            - -c
            - pg_isready -U $POSTGRES_USER -d $POSTGRES_DB
          initialDelaySeconds: 5
          periodSeconds: 5
      volumes:
      - name: init-scripts
        configMap:
          name: postgres-init-scripts
          defaultMode: 0755
  volumeClaimTemplates:
  - metadata:
      name: postgres-storage
    spec:
      accessModes:
        - ReadWriteOnce
      resources:
        requests:
          storage: 5Gi
```

**Features**:
- **StatefulSet** für stabile Network Identity
- **PersistentVolumeClaim** für Datenpersistenz (5Gi)
- **Init-Scripts** via ConfigMap für Keycloak-DB Setup
- **Health Probes** für Monitoring
- **Resource Limits** für Stabilität

---

### Keycloak (Deployment)

**Datei**: `k8s/app/base/keycloak/deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: keycloak
  labels:
    app: keycloak
    component: identity-provider
spec:
  replicas: 1
  selector:
    matchLabels:
      app: keycloak
  template:
    metadata:
      labels:
        app: keycloak
        component: identity-provider
    spec:
      containers:
      - name: keycloak
        image: quay.io/keycloak/keycloak:23.0
        args:
          - start
          - --optimized
          - --import-realm
        ports:
          - containerPort: 8080
            name: http
        env:
          - name: KC_HOSTNAME_STRICT
            value: "false"
          - name: KC_HTTP_ENABLED
            value: "true"
          - name: KC_PROXY
            value: "edge"
          - name: KC_DB_URL
            valueFrom:
              configMapKeyRef:
                name: keycloak-config
                key: KC_DB_URL
          - name: KC_DB_PASSWORD
            valueFrom:
              secretKeyRef:
                name: keycloak-secret
                key: KC_DB_PASSWORD
          - name: KEYCLOAK_ADMIN
            valueFrom:
              secretKeyRef:
                name: keycloak-secret
                key: KEYCLOAK_ADMIN
          - name: KEYCLOAK_ADMIN_PASSWORD
            valueFrom:
              secretKeyRef:
                name: keycloak-secret
                key: KEYCLOAK_ADMIN_PASSWORD
          # Webhook für User-Synchronisation
          - name: KC_SPI_EVENTS_LISTENER_WEBHOOK_EVENT_LISTENER_WEBHOOK_URL
            valueFrom:
              configMapKeyRef:
                name: keycloak-webhook-config
                key: KC_SPI_EVENTS_LISTENER_WEBHOOK_EVENT_LISTENER_WEBHOOK_URL
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /health/live
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /health/ready
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        startupProbe:
          httpGet:
            path: /health/started
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          failureThreshold: 30
        volumeMounts:
          - name: realm-config
            mountPath: /opt/keycloak/data/import/skill-management-realm.json
            subPath: skill-management-realm.json
            readOnly: true
          - name: theme-storage
            mountPath: /opt/keycloak/themes/edag
      volumes:
        - name: realm-config
          configMap:
            name: keycloak-realm
        - name: theme-storage
          configMap:
            name: keycloak-theme
```

**Features**:
- **Realm Import** beim Start (`--import-realm`)
- **Optimized Mode** für Production
- **Custom Theme** (EDAG Branding)
- **Webhook Integration** für User-Sync
- **Multi-Stage Probes** (startup, liveness, readiness)

---

### Services

**PostgreSQL Service (Headless)**:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: postgres-service
  labels:
    app: postgres
spec:
  type: ClusterIP
  clusterIP: None  # Headless Service
  selector:
    app: postgres
  ports:
  - port: 5432
    targetPort: 5432
    name: postgres
```

**Keycloak Service**:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: keycloak-service
  labels:
    app: keycloak
spec:
  type: ClusterIP
  selector:
    app: keycloak
  ports:
  - port: 8080
    targetPort: 8080
    name: http
```

---

## DevOps Resources

### Jenkins (Deployment)

**Datei**: `k8s/devOps/base/jenkins/deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: jenkins
  labels:
    app: jenkins
    component: master
spec:
  replicas: 1
  selector:
    matchLabels:
      app: jenkins
  template:
    metadata:
      labels:
        app: jenkins
    spec:
      serviceAccountName: jenkins
      securityContext:
        fsGroup: 1000
      initContainers:
      # Corporate CA Certificates Import
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
      
      # Plugin Installation
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
      
      containers:
      - name: jenkins
        image: jenkins/jenkins:2.534-jdk21
        ports:
        - containerPort: 8080
          name: http
        - containerPort: 50000
          name: agent
        env:
        - name: JAVA_OPTS
          value: "-Djenkins.install.runSetupWizard=false"
        - name: CASC_JENKINS_CONFIG
          value: "/var/jenkins_config"
        # Secrets für Registry, SonarQube, Bitbucket
        envFrom:
        - secretRef:
            name: jenkins-secret
        - secretRef:
            name: docker-registry-secret
        - secretRef:
            name: sonarqube-secret
        - secretRef:
            name: bitbucket-secret
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        volumeMounts:
          - name: jenkins-home
            mountPath: /var/jenkins_home
          - name: jenkins-config
            mountPath: /var/jenkins_config
      volumes:
        - name: jenkins-home
          persistentVolumeClaim:
            claimName: jenkins-pvc
        - name: jenkins-config
          configMap:
            name: jenkins-casc
```

**Features**:
- **Configuration as Code** (JCasC)
- **Corporate CA Certs** import via InitContainer
- **Plugin Pre-Installation**
- **ServiceAccount** für Kubernetes API Zugriff
- **Persistent Storage** (10Gi)

---

### SonarQube (Deployment)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: sonarqube
spec:
  replicas: 1
  selector:
    matchLabels:
      app: sonarqube
  template:
    spec:
      containers:
      - name: sonarqube
        image: sonarqube:community
        ports:
        - containerPort: 9000
        env:
        - name: SONAR_JDBC_URL
          value: "jdbc:postgresql://postgres-sonar-service:5432/sonarqube"
        - name: SONAR_JDBC_USERNAME
          valueFrom:
            secretKeyRef:
              name: postgres-sonar-secret
              key: POSTGRES_USER
        - name: SONAR_JDBC_PASSWORD
          valueFrom:
            secretKeyRef:
              name: postgres-sonar-secret
              key: POSTGRES_PASSWORD
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        volumeMounts:
        - name: sonarqube-data
          mountPath: /opt/sonarqube/data
        - name: sonarqube-extensions
          mountPath: /opt/sonarqube/extensions
        - name: sonarqube-logs
          mountPath: /opt/sonarqube/logs
      volumes:
      - name: sonarqube-data
        persistentVolumeClaim:
          claimName: sonarqube-data-pvc
      - name: sonarqube-extensions
        persistentVolumeClaim:
          claimName: sonarqube-extensions-pvc
      - name: sonarqube-logs
        persistentVolumeClaim:
          claimName: sonarqube-logs-pvc
```

---

### Docker Registry (Deployment)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: docker-registry
spec:
  replicas: 1
  selector:
    matchLabels:
      app: docker-registry
  template:
    spec:
      containers:
      - name: registry
        image: registry:2
        ports:
        - containerPort: 5000
        env:
        - name: REGISTRY_AUTH
          value: "htpasswd"
        - name: REGISTRY_AUTH_HTPASSWD_REALM
          value: "Registry Realm"
        - name: REGISTRY_AUTH_HTPASSWD_PATH
          value: "/auth/htpasswd"
        - name: REGISTRY_STORAGE_FILESYSTEM_ROOTDIRECTORY
          value: "/var/lib/registry"
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        volumeMounts:
        - name: registry-data
          mountPath: /var/lib/registry
        - name: registry-auth
          mountPath: /auth
          readOnly: true
      volumes:
      - name: registry-data
        persistentVolumeClaim:
          claimName: docker-registry-pvc
      - name: registry-auth
        secret:
          secretName: docker-registry-htpasswd
```

---

## Kustomize Configuration

### Base Configuration

**Datei**: `k8s/app/base/kustomization.yaml`

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
metadata:
  name: skill-management-system-base

resources:
- postgres/
- keycloak/

namespace: skill-management-system

namePrefix: sms-

images:
- name: postgres
  newTag: 15-alpine
- name: quay.io/keycloak/keycloak
  newTag: "23.0"

labels:
- includeSelectors: true
  pairs:
    app.kubernetes.io/managed-by: kustomize
    app.kubernetes.io/part-of: skill-management-system
```

---

### Development Overlay

**Datei**: `k8s/app/overlays/dev/kustomization.yaml`

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
- ../../base
- ingress.yaml
- namespace.yaml

namespace: skill-management-system-dev
namePrefix: dev-

configMapGenerator:
- behavior: merge
  literals:
  - KC_DB_URL=jdbc:postgresql://dev-sms-postgres-service:5433/keycloak
  - BACKEND_HOST=http://localhost:8080
  - FRONTEND_HOST=http://localhost:3000
  name: keycloak-config

- behavior: merge
  literals:
  - KC_SPI_EVENTS_LISTENER_WEBHOOK_EVENT_LISTENER_WEBHOOK_URL=http://host.docker.internal:8080/api/v1/webhooks/keycloak
  name: keycloak-webhook-config

replicas:
- count: 1
  name: postgres
- count: 1
  name: keycloak

labels:
- includeSelectors: true
  pairs:
    app.kubernetes.io/instance: dev
    environment: development

patches:
- path: postgres-patch.yaml
- path: keycloak-patch.yaml
```

**Patches für Development**:
```yaml
# postgres-patch.yaml
- op: replace
  path: /spec/template/spec/containers/0/resources/limits/memory
  value: "256Mi"

# keycloak-patch.yaml
- op: add
  path: /spec/template/spec/containers/0/env/-
  value:
    name: KC_LOG_LEVEL
    value: DEBUG
```

---

### Production Overlay

**Datei**: `k8s/app/overlays/prod/kustomization.yaml`

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
- ../../base
- ingress.yaml
- namespace.yaml

namespace: skill-management-system-prod
namePrefix: prod-

replicas:
- count: 1  # Könnte auf 2+ skaliert werden
  name: postgres
- count: 2  # High Availability
  name: keycloak

labels:
- includeSelectors: true
  pairs:
    app.kubernetes.io/instance: prod
    environment: production

patches:
- path: production-resources.yaml
```

---

## Networking & Ingress

### Traefik IngressRoute

**Datei**: `k8s/devOps/base/ingress.yaml`

```yaml
---
# Self-Signed Certificate Issuer (cert-manager)
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: selfsigned-issuer
spec:
  selfSigned: {}

---
# Certificate für DevOps Services
apiVersion: cert-manager.io/v1
kind: Certificate
metadata:
  name: devops-cert
  namespace: skill-management-devops-prod
spec:
  secretName: devops-tls
  duration: 8760h  # 1 Jahr
  renewBefore: 720h  # 30 Tage
  issuerRef:
    name: selfsigned-issuer
    kind: ClusterIssuer
  commonName: skill-management-devops.edag.com
  dnsNames:
    - jenkins.skill-management-devops.edag.com
    - sonarqube.skill-management-devops.edag.com
    - registry.skill-management-devops.edag.com

---
# Jenkins IngressRoute
apiVersion: traefik.containo.us/v1alpha1
kind: IngressRoute
metadata:
  name: jenkins-https
  namespace: skill-management-devops-prod
spec:
  entryPoints:
    - websecure
  routes:
    - match: Host(`jenkins.skill-management-devops.edag.com`)
      kind: Rule
      services:
        - name: prod-sms-jenkins-service
          port: 8080
  tls:
    secretName: devops-tls

---
# SonarQube IngressRoute
apiVersion: traefik.containo.us/v1alpha1
kind: IngressRoute
metadata:
  name: sonarqube-https
  namespace: skill-management-devops-prod
spec:
  entryPoints:
    - websecure
  routes:
    - match: Host(`sonarqube.skill-management-devops.edag.com`)
      kind: Rule
      services:
        - name: prod-sms-sonarqube-service
          port: 9000
  tls:
    secretName: devops-tls

---
# Docker Registry IngressRoute
apiVersion: traefik.containo.us/v1alpha1
kind: IngressRoute
metadata:
  name: registry-https
  namespace: skill-management-devops-prod
spec:
  entryPoints:
    - websecure
  routes:
    - match: Host(`registry.skill-management-devops.edag.com`)
      kind: Rule
      services:
        - name: prod-sms-docker-registry-service
          port: 5000
  tls:
    secretName: devops-tls
```

**Features**:
- **Traefik CRDs** (IngressRoute statt Ingress)
- **TLS Termination** via cert-manager
- **Subdomain Routing**
- **Shared Certificate** für alle DevOps-Services

---

## Storage & Persistence

### PersistentVolumeClaims

**Jenkins PVC**:
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: jenkins-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: local-path
```

**SonarQube PVCs**:
```yaml
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: sonarqube-data-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 5Gi

---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: sonarqube-extensions-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi

---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: sonarqube-logs-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

**Docker Registry PVC**:
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: docker-registry-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 20Gi
```

**PostgreSQL (via StatefulSet VolumeClaimTemplate)**:
```yaml
volumeClaimTemplates:
- metadata:
    name: postgres-storage
  spec:
    accessModes:
      - ReadWriteOnce
    resources:
      requests:
        storage: 5Gi
```

---

## Secrets Management

### Secret-Generierung

**Script**: `k8s/app/deploy.ps1` & `k8s/devOps/deploy.ps1`

**generate-secrets.ps1**:
```powershell
# Generate random passwords
$DB_PASSWORD = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 32 | % {[char]$_})
$KEYCLOAK_PASSWORD = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 32 | % {[char]$_})

# Encode to Base64
$DB_PASSWORD_B64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($DB_PASSWORD))
$KEYCLOAK_PASSWORD_B64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($KEYCLOAK_PASSWORD))

# Create Secret YAML
@"
apiVersion: v1
kind: Secret
metadata:
  name: postgres-secret
type: Opaque
data:
  POSTGRES_PASSWORD: $DB_PASSWORD_B64
"@ | kubectl apply -f -
```

### Secret-Typen

1. **Opaque Secrets**: Passwords, API Keys
2. **docker-registry**: Registry Credentials
3. **tls**: TLS Certificates (via cert-manager)

---

## Deployment-Strategie

### Rolling Update

**Standard-Strategie**:
```yaml
spec:
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 0
      maxSurge: 1
```

**Vorteile**:
- Zero-Downtime Deployment
- Gradual Rollout
- Automatisches Rollback bei Fehler

### Deployment-Workflow

```bash
# 1. Secrets generieren
./k8s/app/deploy.ps1 -Environment dev -GenerateSecrets

# 2. Kustomize Build & Apply
kubectl apply -k k8s/app/overlays/dev

# 3. Rollout Status
kubectl rollout status deployment/dev-sms-keycloak -n skill-management-system-dev

# 4. Health Check
kubectl get pods -n skill-management-system-dev
```

---

## Best Practices

✅ **Kustomize** für Environment-Management (DRY)  
✅ **StatefulSets** für Stateful Workloads (PostgreSQL)  
✅ **Deployments** für Stateless Workloads (Keycloak, Services)  
✅ **Namespaces** für Environment-Isolation  
✅ **Resource Limits** auf allen Containern  
✅ **Health Probes** (liveness, readiness, startup)  
✅ **Secrets** nie im Git (generiert bei Deployment)  
✅ **ConfigMaps** für Konfiguration  
✅ **Labels** für Organisation und Selektion  
✅ **Service Accounts** für RBAC  
✅ **PVC** für Datenpersistenz  
✅ **Init Containers** für Setup-Tasks  

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: CI/CD-Analyse →](./06_CI_CD_ANALYSE.md)
