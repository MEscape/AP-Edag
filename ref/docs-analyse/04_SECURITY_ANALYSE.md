# Security-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [Security-Architektur](#security-architektur)
2. [Keycloak Integration](#keycloak-integration)
3. [OAuth2 & JWT](#oauth2--jwt)
4. [Backend Security](#backend-security)
5. [Frontend Security](#frontend-security)
6. [CORS Configuration](#cors-configuration)
7. [Rollen & Berechtigungen](#rollen--berechtigungen)
8. [Security Best Practices](#security-best-practices)

---

## Security-Architektur

### Übersicht

```
┌──────────────────────────────────────────────────────────────────┐
│                         CLIENT (Browser)                          │
└───────────────────────────┬──────────────────────────────────────┘
                            │
                            │ 1. Login Request
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                    NEXTAUTH.JS (Frontend)                         │
│  - Session Management                                             │
│  - Token Refresh                                                  │
└───────────────────────────┬──────────────────────────────────────┘
                            │
                            │ 2. OAuth2 Authorization Code Flow
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                    KEYCLOAK (Identity Provider)                   │
│  - User Authentication                                            │
│  - Token Issuance (Access Token, Refresh Token)                  │
│  - Role Management                                                │
└───────────────────────────┬──────────────────────────────────────┘
                            │
                            │ 3. Access Token
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                    NEXT.JS MIDDLEWARE                             │
│  - Authentication Check                                           │
│  - Role-Based Access Control                                     │
│  - Route Protection                                               │
└───────────────────────────┬──────────────────────────────────────┘
                            │
                            │ 4. API Request + Bearer Token
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│              SPRING SECURITY (OAuth2 Resource Server)             │
│  - Token Introspection                                            │
│  - Authority Extraction                                           │
│  - Method Security                                                │
└───────────────────────────┬──────────────────────────────────────┘
                            │
                            │ 5. Authorized Request
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                    BACKEND SERVICES                               │
│  - Business Logic                                                 │
│  - Data Access                                                    │
└──────────────────────────────────────────────────────────────────┘
```

---

## Keycloak Integration

### Keycloak-Konfiguration

**Deployment**: Kubernetes StatefulSet
**Version**: 23.0
**Database**: PostgreSQL (separates Schema)
**Realm**: `skill-management`
**Client**: `skill-management-client`

### Realm-Konfiguration

```json
{
  "realm": "skill-management",
  "enabled": true,
  "clients": [
    {
      "clientId": "skill-management-client",
      "enabled": true,
      "protocol": "openid-connect",
      "publicClient": false,
      "redirectUris": [
        "http://localhost:3000/*",
        "https://skill-management.edag.com/*"
      ],
      "webOrigins": ["+"],
      "standardFlowEnabled": true,
      "directAccessGrantsEnabled": true,
      "serviceAccountsEnabled": false
    }
  ],
  "roles": {
    "realm": [
      {
        "name": "user",
        "description": "Standard user role"
      },
      {
        "name": "manager",
        "description": "Manager role with project creation rights"
      },
      {
        "name": "admin",
        "description": "Administrator role with full access"
      }
    ]
  }
}
```

### User-Synchronisation

**Webhook-basierte Synchronisation**:

```java
@RestController
@RequestMapping("/webhooks/keycloak")
public class KeycloakWebhookController {

  private final UserSyncService userSyncService;

  @PostMapping("/events")
  public ResponseEntity<Void> handleKeycloakEvent(
      @RequestBody KeycloakWebhookEventDto event) {
    
    switch (event.type()) {
      case "USER_CREATED" -> userSyncService.syncNewUser(event);
      case "USER_UPDATED" -> userSyncService.updateUser(event);
      case "USER_DELETED" -> userSyncService.deleteUser(event);
    }
    
    return ResponseEntity.ok().build();
  }
}
```

**Vorteile**:
- Automatische User-Synchronisation
- Kein manuelles Anlegen in Backend-DB
- Single Source of Truth (Keycloak)

---

## OAuth2 & JWT

### Authorization Code Flow

```
1. User → Frontend: Klick auf "Login"
2. Frontend → Keycloak: Redirect zu /auth/realms/skill-management/protocol/openid-connect/auth
3. User → Keycloak: Credentials eingeben
4. Keycloak → Frontend: Redirect mit Authorization Code
5. Frontend → Keycloak: Token-Exchange (Code → Tokens)
6. Keycloak → Frontend: Access Token + Refresh Token + ID Token
7. Frontend → Backend: API-Request mit Bearer Token
8. Backend → Keycloak: Token Introspection (Validierung)
9. Keycloak → Backend: Token-Metadaten + Rollen
10. Backend → Frontend: Response
```

### Token-Struktur

**Access Token (Opaque Token)**:
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Decoded Payload**:
```json
{
  "sub": "123e4567-e89b-12d3-a456-426614174000",
  "email": "john.doe@edag.com",
  "preferred_username": "john.doe",
  "given_name": "John",
  "family_name": "Doe",
  "realm_access": {
    "roles": ["user", "manager"]
  },
  "scope": "openid profile email",
  "exp": 1732384200,
  "iat": 1732380600,
  "iss": "https://keycloak.skill-management.edag.com/realms/skill-management"
}
```

### Token-Validierung

**Backend (Opaque Token Introspection)**:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        opaquetoken:
          introspection-uri: ${KEYCLOAK_INTROSPECTION_URI}
          client-id: ${OAUTH2_CLIENT_ID}
          client-secret: ${OAUTH2_CLIENT_SECRET}
```

**Frontend (NextAuth Session)**:
```typescript
export const authOptions: NextAuthOptions = {
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID!,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
      issuer: process.env.KEYCLOAK_ISSUER,
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      if (account) {
        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.roles = account.roles;
      }
      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken;
      session.roles = token.roles;
      return session;
    },
  },
};
```

---

## Backend Security

### Spring Security Configuration

**ResourceServerSecurityConfig.java**:
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerSecurityConfig {

  @Bean
  public SecurityFilterChain opaqueTokenFilterChain(
      HttpSecurity http, 
      OpaqueTokenIntrospector delegate) throws Exception {
    
    return http
        // CSRF deaktiviert (Stateless API)
        .csrf(AbstractHttpConfigurer::disable)
        
        // CORS aktiviert
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        
        // Session Management (Stateless)
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // Security Headers
        .headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
            .contentTypeOptions(contentTypeOptions -> {})
            .httpStrictTransportSecurity(hsts -> 
                hsts.maxAgeInSeconds(31536000).includeSubDomains(true))
            .referrerPolicy(referrer -> 
                referrer.policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))
        
        // Authorization Rules
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/error").permitAll()
            .anyRequest().authenticated())
        
        // OAuth2 Resource Server
        .oauth2ResourceServer(oauth2 -> oauth2
            .opaqueToken(token -> token.introspector(customIntrospector(delegate))))
        
        .build();
  }
}
```

### Authority Extraction

**KeycloakAuthoritiesConverter.java**:
```java
@Component
public class KeycloakAuthoritiesConverter {

  public List<GrantedAuthority> extractAuthoritiesFromClaims(
      Map<String, Object> claims) {
    
    List<GrantedAuthority> authorities = new ArrayList<>();
    
    // Realm Roles extrahieren
    Object realmAccess = claims.get("realm_access");
    if (realmAccess instanceof Map<?, ?> realmAccessMap) {
      Object rolesObj = realmAccessMap.get("roles");
      if (rolesObj instanceof Collection<?> roles) {
        authorities.addAll(roles.stream()
            .map(Object::toString)
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .toList());
      }
    }
    
    // Scopes extrahieren
    Object scopeClaim = claims.get("scope");
    if (scopeClaim instanceof String scopes) {
      authorities.addAll(Arrays.stream(scopes.split(" "))
          .map(s -> new SimpleGrantedAuthority("SCOPE_" + s.toUpperCase()))
          .toList());
    }
    
    return authorities;
  }
}
```

**Ergebnis**:
- Roles: `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN`
- Scopes: `SCOPE_OPENID`, `SCOPE_PROFILE`, `SCOPE_EMAIL`

### Method Security

**Annotation-basierte Autorisierung**:

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/users/{userId}")
public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
  userAdminService.deleteUser(userId);
  return ResponseEntity.noContent().build();
}

@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
@PostMapping("/projects")
public ResponseEntity<ProjectDto> createProject(
    @Valid @RequestBody CreateProjectRequestDto request) {
  // ...
}
```

### Custom Access Control

**AuthorizationUtils.java**:
```java
@Component
public class AuthorizationUtils {

  public void validateUserAccess(String requestedUserId, Principal principal) {
    UUID authenticatedUserId = extractUserIdFromPrincipal(principal);
    UUID requestedId = "me".equals(requestedUserId) 
        ? authenticatedUserId 
        : UUID.fromString(requestedUserId);
    
    if (!authenticatedUserId.equals(requestedId)) {
      throw new AccessDeniedException("User not authorized to access this resource");
    }
  }

  public UUID resolveUserId(String userId, Principal principal) {
    if ("me".equals(userId)) {
      return extractUserIdFromPrincipal(principal);
    }
    return UUID.fromString(userId);
  }
}
```

**Verwendung**:
```java
@PutMapping("/{userId}/skills/{skillId}")
public ResponseEntity<ProfileSkillResponseDto> updateSkill(
    @PathVariable String userId,
    @PathVariable String skillId,
    @Valid @RequestBody UpdateSkillRequestDto request,
    Principal principal) {
  
  // User darf nur eigene Skills ändern
  authorizationUtils.validateUserAccess(userId, principal);
  
  UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);
  ProfileSkill skill = skillManagementService.updateSkill(resolvedUserId, ...);
  
  return ResponseEntity.ok(ProfileSkillResponseDto.from(skill));
}
```

---

## Frontend Security

### Next.js Middleware (Route Protection)

**src/proxy.ts**:
```typescript
export default withAuth(
  (req) => {
    return intlMiddleware(req);
  },
  {
    callbacks: {
      authorized: ({ token, req }) => {
        const { pathname } = req.nextUrl;

        // Public Routes
        const publicRoutes = ['/'];
        if (publicRoutes.some(route => pathname.startsWith(route))) {
          return true;
        }

        // Authenticated?
        if (!token) return false;

        // Roles vorhanden?
        const userRoles = token.roles as string[] | undefined;
        if (!userRoles || userRoles.length === 0) return false;

        // Manager-Routes
        const managerRoutes = ['/dashboard/discover/projects'];
        if (managerRoutes.some(route => pathname.startsWith(route))) {
          return userRoles.includes('manager');
        }

        // Admin-Routes
        const adminRoutes = ['/dashboard/admin'];
        if (adminRoutes.some(route => pathname.startsWith(route))) {
          return userRoles.includes('admin');
        }

        return true;
      },
    },
  }
);
```

**Schutz-Ebenen**:
1. **Authentication**: Token muss vorhanden sein
2. **Authorization**: Rollen müssen existieren
3. **Role-Based Access**: Spezifische Rollen für Routen

### Session Management

**Automatic Token Refresh**:
```typescript
export const baseQueryWithLocale: BaseQueryFn = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  // Bei 401: Automatischer Logout
  if (result.error?.status === 401) {
    await signOut({ redirect: true, callbackUrl: '/' });
  }

  return result;
};
```

### XSS Protection

**Content Security Policy** (Headers):
```typescript
// next.config.ts
const securityHeaders = [
  {
    key: 'X-DNS-Prefetch-Control',
    value: 'on'
  },
  {
    key: 'Strict-Transport-Security',
    value: 'max-age=63072000; includeSubDomains; preload'
  },
  {
    key: 'X-Frame-Options',
    value: 'DENY'
  },
  {
    key: 'X-Content-Type-Options',
    value: 'nosniff'
  },
  {
    key: 'X-XSS-Protection',
    value: '1; mode=block'
  },
  {
    key: 'Referrer-Policy',
    value: 'strict-origin-when-cross-origin'
  }
];
```

---

## CORS Configuration

### Backend CORS

**CorsSecurityConfig.java**:
```java
@Configuration
public class CorsSecurityConfig {

  @Value("${app.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/v1/**", configuration);
    
    return source;
  }
}
```

**application.yml**:
```yaml
app:
  cors:
    allowed-origins:
      - http://localhost:3000
      - https://skill-management.edag.com
```

---

## Rollen & Berechtigungen

### Rollen-Hierarchie

```
ADMIN
  └─ Alle Rechte
  └─ User-Verwaltung
  └─ Rollenanfragen genehmigen
  └─ Alle Projekte sehen/bearbeiten
  └─ System-Konfiguration

MANAGER
  └─ Projekte erstellen/bearbeiten
  └─ Mitarbeiter suchen (erweitert)
  └─ Team-Zusammenstellung
  └─ Eigene Projekte verwalten

USER (Standard)
  └─ Eigenes Profil bearbeiten
  └─ Skills hinzufügen/bearbeiten
  └─ Mitarbeiter suchen (basis)
  └─ Eigene Projekte ansehen
  └─ Rollenanfrage stellen
```

### Permissions-Matrix

| Funktion | User | Manager | Admin |
|----------|------|---------|-------|
| **Profil bearbeiten (eigenes)** | ✅ | ✅ | ✅ |
| **Profil bearbeiten (fremdes)** | ❌ | ❌ | ✅ |
| **Skills hinzufügen (eigene)** | ✅ | ✅ | ✅ |
| **Mitarbeiter suchen** | ✅ | ✅ | ✅ |
| **Mitarbeiter-Details ansehen** | ✅ | ✅ | ✅ |
| **Projekt erstellen** | ❌ | ✅ | ✅ |
| **Projekt bearbeiten (eigenes)** | ❌ | ✅ | ✅ |
| **Projekt bearbeiten (fremdes)** | ❌ | ❌ | ✅ |
| **Projekt löschen** | ❌ | ✅ | ✅ |
| **Projektsuche** | ❌ | ✅ | ✅ |
| **Rollenanfrage stellen** | ✅ | ✅ | ❌ |
| **Rollenanfrage genehmigen** | ❌ | ❌ | ✅ |
| **User löschen** | ❌ | ❌ | ✅ |
| **Stammdaten verwalten** | ❌ | ❌ | ✅ |

### Role Request Flow

```
1. User → Request Manager/Admin Role
2. System → Create RoleRequest (Status: PENDING)
3. Admin → Review Request
4. Admin → Approve/Reject
5. System → Update Keycloak Role (if approved)
6. System → Update RoleRequest Status
7. User → Access to new features
```

---

## Security Best Practices

### Implementierte Maßnahmen

✅ **OAuth2 + OpenID Connect** - Industry Standard  
✅ **Token-basierte Auth** - Stateless, skalierbar  
✅ **Role-Based Access Control** - Granulare Berechtigungen  
✅ **HTTPS Only** - TLS/SSL verschlüsselt  
✅ **CORS konfiguriert** - Nur erlaubte Origins  
✅ **CSRF Protection** - Bei stateful Operations  
✅ **Security Headers** - XSS, Clickjacking Prevention  
✅ **Input Validation** - Jakarta Validation + Custom Validators  
✅ **SQL Injection Prevention** - JPA + Prepared Statements  
✅ **Password Hashing** - Keycloak (bcrypt)  
✅ **Session Management** - Secure Cookie Flags  
✅ **Rate Limiting** - Keycloak + Ingress Level  
✅ **Audit Logging** - Activities Table  
✅ **Principle of Least Privilege** - Minimale Berechtigungen  

### Secrets Management

**Kubernetes Secrets**:
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: sms-backend-secrets
type: Opaque
data:
  DATABASE_PASSWORD: <base64>
  OAUTH2_CLIENT_SECRET: <base64>
  KEYCLOAK_ADMIN_PASSWORD: <base64>
```

**Environment Variables** (niemals im Code):
```yaml
env:
  - name: DATABASE_PASSWORD
    valueFrom:
      secretKeyRef:
        name: sms-backend-secrets
        key: DATABASE_PASSWORD
```

### Vulnerability Scanning

**Dependency Scanning**:
- Maven: `mvn dependency-check:check`
- npm: `npm audit`
- Trivy: Container Image Scanning

**Static Code Analysis**:
- SonarQube: Continuous Inspection
- Checkstyle: Code Quality
- ESLint: Frontend Linting

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Kubernetes-Analyse →](./05_KUBERNETES_ANALYSE.md)
