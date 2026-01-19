# Backend-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [Architektur-Überblick](#architektur-überblick)
2. [Technologie-Stack](#technologie-stack)
3. [Projekt-Struktur](#projekt-struktur)
4. [Domain Layer](#domain-layer)
5. [Application Layer](#application-layer)
6. [Infrastructure Layer](#infrastructure-layer)
7. [REST-API Endpunkte](#rest-api-endpunkte)
8. [Build & Deployment](#build--deployment)

---

## Architektur-Überblick

Das Backend folgt der **Hexagonalen Architektur** (Ports & Adapters Pattern), auch bekannt als **Clean Architecture**. Dieser Ansatz trennt strikte die Business-Logik von technischen Details.

### Architektur-Prinzipien

```
┌─────────────────────────────────────────────────────────────────┐
│                     INFRASTRUCTURE LAYER                         │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │              Inbound Adapters (Web)                        │ │
│  │  - REST Controllers                                        │ │
│  │  - DTOs (Data Transfer Objects)                           │ │
│  │  - Exception Handlers                                      │ │
│  │  - Security Configuration                                  │ │
│  └─────────────────────┬──────────────────────────────────────┘ │
│                        │                                         │
│  ┌─────────────────────▼──────────────────────────────────────┐ │
│  │                  APPLICATION LAYER                          │ │
│  │  ┌──────────────────────────────────────────────────────┐  │ │
│  │  │           Service Implementations                     │  │ │
│  │  │  - Business Logic Orchestration                      │  │ │
│  │  │  - Transaction Management                            │  │ │
│  │  │  - Use-Case Implementation                           │  │ │
│  │  └─────────────────┬────────────────────────────────────┘  │ │
│  │                    │                                        │ │
│  │  ┌─────────────────▼────────────────────────────────────┐  │ │
│  │  │              DOMAIN LAYER (Core)                     │  │ │
│  │  │  - Domain Models (Records)                          │  │ │
│  │  │  - Port Interfaces (Inbound & Outbound)            │  │ │
│  │  │  - Domain Exceptions                                │  │ │
│  │  │  - Business Rules                                   │  │ │
│  │  └─────────────────┬────────────────────────────────────┘  │ │
│  │                    │                                        │ │
│  └────────────────────┼────────────────────────────────────────┘ │
│                       │                                          │
│  ┌────────────────────▼──────────────────────────────────────┐  │
│  │           Outbound Adapters (Persistence)                 │  │
│  │  - JPA Repositories                                       │  │
│  │  - Entity Mappers                                         │  │
│  │  - Database Entities                                      │  │
│  │  - Specifications (Query Builder)                         │  │
│  │  - External API Clients (Keycloak)                       │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

**Abhängigkeitsregel:**
- **Domain** hat KEINE Abhängigkeiten (Pure Business Logic)
- **Application** hängt nur von **Domain** ab
- **Infrastructure** hängt von **Application** und **Domain** ab
- Alle Abhängigkeiten zeigen nach INNEN (zur Domain)

---

## Technologie-Stack

### Core Framework
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.7</version>
</parent>
```

### Java Version
```xml
<properties>
    <java.version>25</java.version>
</properties>
```

### Hauptabhängigkeiten

| Dependency | Version | Zweck |
|------------|---------|-------|
| **spring-boot-starter-web** | 3.5.7 | REST API, MVC |
| **spring-boot-starter-data-jpa** | 3.5.7 | ORM, Datenbankzugriff |
| **spring-boot-starter-oauth2-resource-server** | 3.5.7 | OAuth2 Token-Validierung |
| **spring-boot-starter-actuator** | 3.5.7 | Health-Checks, Metrics |
| **postgresql** | Latest | PostgreSQL JDBC Driver |
| **flyway-core** | Latest | Datenbankmigrationen |
| **flyway-database-postgresql** | Latest | Flyway PostgreSQL Support |
| **springdoc-openapi-starter-webmvc-ui** | 2.8.13 | Swagger/OpenAPI UI |
| **keycloak-admin-client** | 26.0.7 | Keycloak API Integration |
| **lombok** | Latest | Boilerplate-Reduktion |
| **spring-boot-starter-test** | 3.5.7 | Testing (JUnit, Mockito) |
| **spring-boot-testcontainers** | 3.5.7 | Integration Tests |

### Code-Qualität Tools

```xml
<!-- Google Checkstyle -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.6.0</version>
    <configuration>
        <configLocation>google_checks.xml</configLocation>
    </configuration>
</plugin>

<!-- Spotless (Code Formatting) -->
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <java>
            <googleJavaFormat/>
        </java>
    </configuration>
</plugin>

<!-- JaCoCo (Test Coverage) -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
</plugin>
```

---

## Projekt-Struktur

```
backend/src/main/java/com/edag/skillmanagementsystem/
│
├── SkillManagementSystemApplication.java    # Spring Boot Main
│
├── domain/                                   # DOMAIN LAYER
│   ├── model/                               # Domain-Entitäten
│   │   ├── user/                            # User, UserProfile, AvailabilityStatus
│   │   ├── employee/                        # Employee, EmployeeSearchCriteria
│   │   ├── skill/                           # ProfileSkill
│   │   ├── project/                         # Project, ProjectStatus, ProjectMember
│   │   ├── role/                            # RoleRequest, RoleType, RequestStatus
│   │   ├── analytics/                       # Activity, UserStatistics, SkillDevelopment
│   │   └── option/                          # Skill, SkillCategory, Location, Position
│   │
│   ├── port/                                # Port-Interfaces
│   │   ├── inbound/                         # Use-Case Interfaces (Services)
│   │   │   ├── ProfileService.java
│   │   │   ├── SkillManagementService.java
│   │   │   ├── EmployeeDiscoveryService.java
│   │   │   ├── ProjectManagementService.java
│   │   │   ├── ProjectDiscoveryService.java
│   │   │   ├── AnalyticsService.java
│   │   │   ├── RoleRequestService.java
│   │   │   ├── UserAdminService.java
│   │   │   ├── UserSyncService.java
│   │   │   └── OptionsService.java
│   │   │
│   │   └── outbound/                        # Repository Interfaces
│   │       ├── UserRepository.java
│   │       ├── ProfileRepository.java
│   │       ├── ProfileSkillRepository.java
│   │       ├── EmployeeRepository.java
│   │       ├── ProjectRepository.java
│   │       ├── AnalyticsRepository.java
│   │       ├── ActivityRepository.java
│   │       ├── RoleRequestRepository.java
│   │       ├── OptionsRepository.java
│   │       └── KeycloakAdminClient.java
│   │
│   └── exception/                           # Domain-Exceptions
│       ├── shared/                          # Basis-Exceptions
│       ├── user/                            # User-spezifische Exceptions
│       ├── skill/                           # Skill-spezifische Exceptions
│       ├── project/                         # Projekt-spezifische Exceptions
│       ├── role/                            # Rollen-spezifische Exceptions
│       └── analytics/                       # Analytics-spezifische Exceptions
│
├── application/                             # APPLICATION LAYER
│   ├── service/                             # Service-Implementierungen
│   │   ├── ProfileServiceImpl.java
│   │   ├── SkillManagementServiceImpl.java
│   │   ├── EmployeeDiscoveryServiceImpl.java
│   │   ├── ProjectManagementServiceImpl.java
│   │   ├── ProjectDiscoveryServiceImpl.java
│   │   ├── AnalyticsServiceImpl.java
│   │   ├── RoleRequestServiceImpl.java
│   │   ├── UserAdminServiceImpl.java
│   │   ├── UserSyncServiceImpl.java
│   │   └── OptionsServiceImpl.java
│   │
│   ├── config/                              # Spring-Konfigurationen
│   │   ├── security/                        # Security-Konfiguration
│   │   │   ├── ResourceServerSecurityConfig.java
│   │   │   ├── KeycloakAuthoritiesConverter.java
│   │   │   ├── CorsSecurityConfig.java
│   │   │   └── WebhookSecurityConfig.java
│   │   ├── openapi/                         # Swagger-Konfiguration
│   │   │   └── SwaggerOpenApiConfig.java
│   │   └── i18n/                            # Internationalisierung
│   │       └── I18nConfig.java
│   │
│   └── security/                            # Security-Komponenten
│       └── CustomAccessDeniedHandler.java
│
└── infrastructure/                          # INFRASTRUCTURE LAYER
    ├── adapter/
    │   ├── inbound/                         # Inbound Adapters
    │   │   └── web/                         # REST-Schicht
    │   │       ├── controller/              # REST-Controller
    │   │       │   ├── ProfileController.java
    │   │       │   ├── SkillsController.java
    │   │       │   ├── EmployeeDiscoveryController.java
    │   │       │   ├── ProjectManagementController.java
    │   │       │   ├── ProjectDiscoverController.java
    │   │       │   ├── AnalyticsController.java
    │   │       │   ├── RoleRequestController.java
    │   │       │   ├── UserAdminController.java
    │   │       │   ├── OptionsController.java
    │   │       │   └── KeycloakWebhookController.java
    │   │       │
    │   │       ├── dto/                     # Data Transfer Objects
    │   │       │   ├── profile/
    │   │       │   ├── skill/
    │   │       │   ├── employee/
    │   │       │   ├── project/
    │   │       │   ├── analytics/
    │   │       │   ├── role/
    │   │       │   ├── option/
    │   │       │   └── webhook/
    │   │       │
    │   │       ├── exception/               # Exception-Handler
    │   │       │   ├── GlobalExceptionHandler.java
    │   │       │   ├── UserExceptionHandler.java
    │   │       │   ├── SkillExceptionHandler.java
    │   │       │   ├── ProjectExceptionHandler.java
    │   │       │   ├── RoleExceptionHandler.java
    │   │       │   └── AnalyticsExceptionHandler.java
    │   │       │
    │   │       └── security/                # Web-Security-Utils
    │   │           └── AuthorizationUtils.java
    │   │
    │   └── outbound/                        # Outbound Adapters
    │       ├── persistence/                 # Datenbank-Adapter
    │       │   └── jpa/
    │       │       ├── model/               # JPA-Entities
    │       │       │   ├── base/            # Basis-Entities
    │       │       │   ├── user/
    │       │       │   ├── employee/
    │       │       │   ├── project/
    │       │       │   ├── role/
    │       │       │   ├── analytics/
    │       │       │   └── option/
    │       │       │
    │       │       ├── repository/          # JPA-Repositories
    │       │       │   ├── user/
    │       │       │   ├── employee/
    │       │       │   ├── project/
    │       │       │   ├── role/
    │       │       │   ├── analytics/
    │       │       │   └── option/
    │       │       │
    │       │       ├── specification/       # JPA-Specifications
    │       │       │   ├── EmployeeSpecification.java
    │       │       │   └── ProjectSpecification.java
    │       │       │
    │       │       └── datasource/          # DataSource (Mapper)
    │       │           ├── user/
    │       │           ├── profile/
    │       │           ├── employee/
    │       │           ├── project/
    │       │           ├── role/
    │       │           ├── analytics/
    │       │           └── option/
    │       │
    │       └── keycloak/                    # Keycloak-Adapter
    │           └── KeycloakAdminClientImpl.java
    │
    ├── properties/                          # Configuration Properties
    │   ├── SecurityProperties.java
    │   ├── KeycloakAdminProperties.java
    │   └── SpringDocProperties.java
    │
    └── validation/                          # Custom Validators
        ├── ValidDateRange.java
        └── ValidDateRangeValidator.java
```

---

## Domain Layer

### Domain Models (Java Records)

Das Backend nutzt **Java 25 Records** für immutable Domain-Objekte. Records sind kompakt, typsicher und unveränderlich.

#### User Model
```java
@Builder
public record User(
    UUID id,
    String username,
    String email,
    String firstName,
    String lastName,
    Instant createdAt,
    Instant updatedAt
) {}
```

#### Employee Model
```java
@Builder
public record Employee(
    UUID userId,
    String firstName,
    String lastName,
    String email,
    String position,
    String location,
    AvailabilityStatus availability,
    double yearsOfExperience,
    List<ProfileSkill> skills,      // Top 3 Skills für Übersichten
    int totalProjects,
    int totalSkills
) {}
```

#### ProfileSkill Model
```java
@Builder
public record ProfileSkill(
    UUID id,                    // Beziehungs-ID (employee_skills.id)
    String skillName,
    String category,
    int proficiencyScore,       // 0-100
    double yearsOfExperience,
    LocalDate lastUsed
) {}
```

#### Project Model
```java
@Builder
public record Project(
    UUID id,
    String name,
    String description,
    ProjectStatus status,       // PLANNED, ACTIVE, ON_HOLD, COMPLETED, CANCELLED
    LocalDate startDate,
    LocalDate endDate,
    String client,
    Integer teamSize,
    Set<String> technologies,
    UUID createdByUserId,
    Set<ProjectMemberName> members
) {}
```

### Port Interfaces

#### Inbound Ports (Use-Cases)

**ProfileService** - Profilverwaltung
```java
public interface ProfileService {
  UserProfile getProfile(UUID userId);
  
  UserProfile updateProfile(
      UUID userId, 
      UUID positionId, 
      UUID locationId,
      AvailabilityStatus availability, 
      Double yearsOfExperience, 
      String bio
  );
}
```

**SkillManagementService** - Skill-Verwaltung
```java
public interface SkillManagementService {
  ProfileSkill addSkill(
      UUID userId, 
      UUID skillId, 
      UUID categoryId,
      int score, 
      double yearsOfExperience, 
      LocalDate lastUsed
  );
  
  ProfileSkill updateSkill(
      UUID userId, 
      UUID skillId,
      Integer score, 
      Double yearsOfExperience, 
      LocalDate lastUsed
  );
  
  void deleteSkill(UUID userId, UUID skillId);
}
```

**EmployeeDiscoveryService** - Mitarbeitersuche
```java
public interface EmployeeDiscoveryService {
  Page<Employee> searchEmployees(
      EmployeeSearchCriteria criteria, 
      Pageable pageable
  );
  
  Employee getEmployeeById(UUID id);
  
  EmployeeFilterOptions getFilterOptions();
}
```

#### Outbound Ports (Repositories)

**UserRepository**
```java
public interface UserRepository {
  Optional<User> findById(UUID id);
  Optional<User> findByUsername(String username);
  User save(User user);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
}
```

**EmployeeRepository**
```java
public interface EmployeeRepository {
  Optional<Employee> findById(UUID id);
  Page<Employee> findByCriteria(
      EmployeeSearchCriteria criteria, 
      Pageable pageable
  );
}
```

### Domain Exceptions

Hierarchische Exception-Struktur für sauberes Error-Handling:

```java
// Basis-Exception
public abstract class DomainException extends RuntimeException {
  protected DomainException(String message) {
    super(message);
  }
}

// Resource-Exceptions
public class ResourceNotFoundException extends DomainException {}
public class ResourceAlreadyExistsException extends DomainException {}

// User-Exceptions
public class UserNotFoundException extends ResourceNotFoundException {}
public class ProfileNotFoundException extends ResourceNotFoundException {}
public class EmployeeNotFoundException extends ResourceNotFoundException {}

// Skill-Exceptions
public class SkillNotFoundException extends ResourceNotFoundException {}
public class DuplicateSkillException extends ResourceAlreadyExistsException {}
public class InactiveSkillException extends InvalidRequestException {}

// Project-Exceptions
public class ProjectNotFoundException extends ResourceNotFoundException {}
public class InvalidProjectDataException extends InvalidRequestException {}
```

---

## Application Layer

### Service-Implementierungen

Die Services implementieren die Use-Cases und orchestrieren die Business-Logik.

#### EmployeeDiscoveryServiceImpl

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeDiscoveryServiceImpl implements EmployeeDiscoveryService {

  private final EmployeeRepository employeeRepository;
  private final OptionsRepository optionsRepository;

  @Override
  public Page<Employee> searchEmployees(
      final EmployeeSearchCriteria criteria, 
      final Pageable pageable) {
    
    log.debug("Searching employees with criteria: {}", criteria);
    
    Page<Employee> results = employeeRepository.findByCriteria(criteria, pageable);
    
    log.info("Found {} employees out of {} total",
        results.getNumberOfElements(),
        results.getTotalElements());
    
    return results;
  }

  @Override
  public Employee getEmployeeById(final UUID id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @Override
  public EmployeeFilterOptions getFilterOptions() {
    List<Location> locations = optionsRepository.findAllActiveLocations();
    List<Skill> skills = optionsRepository.findAllActiveSkills();
    List<SkillCategory> categories = optionsRepository.findAllActiveSkillCategories();
    
    return new EmployeeFilterOptions(skills, locations, categories);
  }
}
```

**Key-Points:**
- `@Transactional(readOnly = true)` für Read-Operationen (Performance)
- Logging auf DEBUG und INFO-Level
- Exception-Handling mit Domain-Exceptions
- Delegation an Repository

#### SkillManagementServiceImpl

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SkillManagementServiceImpl implements SkillManagementService {

  private final ProfileRepository profileRepository;
  private final ProfileSkillRepository profileSkillRepository;
  private final OptionsRepository optionsRepository;

  @Override
  public ProfileSkill addSkill(
      UUID userId, UUID skillId, UUID categoryId,
      int score, double yearsOfExperience, LocalDate lastUsed) {
    
    log.debug("Adding skill {} to user {}", skillId, userId);
    
    // 1. Validierung: Skill existiert und ist aktiv
    Skill skill = optionsRepository.findActiveSkillById(skillId)
        .orElseThrow(() -> new SkillNotFoundException(skillId));
    
    // 2. Duplikat-Check
    if (profileSkillRepository.existsByUserIdAndSkillId(userId, skillId)) {
      throw new DuplicateSkillException(skillId);
    }
    
    // 3. Skill hinzufügen
    ProfileSkill profileSkill = ProfileSkill.builder()
        .skillName(skill.name())
        .category(skill.category())
        .proficiencyScore(score)
        .yearsOfExperience(yearsOfExperience)
        .lastUsed(lastUsed)
        .build();
    
    ProfileSkill saved = profileSkillRepository.save(userId, profileSkill);
    
    log.info("Successfully added skill {} to user {}", skillId, userId);
    
    return saved;
  }
  
  // weitere Methoden...
}
```

**Key-Points:**
- `@Transactional` für Write-Operationen
- Validierung vor Datenänderungen
- Business-Rules im Service (Duplikat-Check, Aktiv-Check)
- Strukturiertes Logging

---

## Infrastructure Layer

### REST-Controller

#### EmployeeDiscoveryController

```java
@RestController
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Discovery")
public class EmployeeDiscoveryController {

  private final EmployeeDiscoveryService employeeDiscoveryService;
  private final AuthorizationUtils authorizationUtils;

  @Operation(summary = "Search employees")
  @ApiResponse(responseCode = "200", description = "Success")
  @GetMapping("/search")
  public ResponseEntity<EmployeeSearchResponseDto> searchEmployees(
      @ParameterObject @ModelAttribute EmployeeSearchFiltersDto filtersDto,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "asc") String sortOrder) {

    EmployeeSearchCriteria criteria = filtersDto.toDomain();
    
    Sort sort = sortOrder.equalsIgnoreCase("desc")
        ? Sort.by(mapSortField(sortBy)).descending()
        : Sort.by(mapSortField(sortBy)).ascending();
    
    Pageable pageable = PageRequest.of(page, size, sort);
    
    Page<Employee> results = employeeDiscoveryService.searchEmployees(criteria, pageable);
    
    return ResponseEntity.ok(EmployeeSearchResponseDto.from(results));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<EmployeeResponseDto> getEmployeeById(
      @PathVariable String userId,
      Principal principal) {
    
    UUID resolvedId = authorizationUtils.resolveUserId(userId, principal);
    Employee employee = employeeDiscoveryService.getEmployeeById(resolvedId);
    
    return ResponseEntity.ok(EmployeeResponseDto.from(employee));
  }
}
```

**Features:**
- `@Tag` für OpenAPI-Gruppierung
- `@Operation` und `@ApiResponse` für Swagger-Dokumentation
- DTO-Konvertierung (Request → Domain, Domain → Response)
- Support für "me" Alias (über `AuthorizationUtils`)
- Pagination und Sorting
- Multi-Filter Support

### DTOs (Data Transfer Objects)

#### EmployeeSearchFiltersDto
```java
public record EmployeeSearchFiltersDto(
    String searchTerm,
    List<UUID> skillIds,
    List<UUID> skillCategoryIds,
    List<UUID> locationIds,
    List<AvailabilityStatus> availability,
    Double minExperience
) {
  public EmployeeSearchCriteria toDomain() {
    return EmployeeSearchCriteria.builder()
        .searchTerm(searchTerm)
        .skillIds(skillIds)
        .skillCategoryIds(skillCategoryIds)
        .locationIds(locationIds)
        .availability(availability)
        .minExperience(minExperience)
        .build();
  }
}
```

#### EmployeeResponseDto
```java
public record EmployeeResponseDto(
    String id,
    String firstName,
    String lastName,
    String email,
    String position,
    String location,
    String availability,
    double yearsOfExperience,
    List<EmployeeSkillDto> skills,
    int skillCount,
    int totalProjects
) {
  public static EmployeeResponseDto from(Employee employee) {
    return new EmployeeResponseDto(
        employee.userId().toString(),
        employee.firstName(),
        employee.lastName(),
        employee.email(),
        employee.position(),
        employee.location(),
        employee.availability().name().toLowerCase(),
        employee.yearsOfExperience(),
        employee.skills().stream()
            .map(EmployeeSkillDto::from)
            .toList(),
        employee.totalSkills(),
        employee.totalProjects()
    );
  }
}
```

### JPA-Entities

#### EmployeeEntity
```java
@Entity
@Table(name = "employee_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends BaseAuditEntity {

  @Id
  @Column(name = "user_id")
  private UUID userId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_id")
  private PositionEntity position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  private LocationEntity location;

  @Enumerated(EnumType.STRING)
  @Column(name = "availability", nullable = false)
  private AvailabilityStatus availability;

  @Column(name = "years_of_experience", nullable = false)
  private Double yearsOfExperience;

  @Column(name = "bio", length = 500)
  private String bio;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmployeeSkillEntity> skills = new ArrayList<>();
}
```

### JPA Specifications (Dynamic Queries)

Für komplexe Suchfilter verwendet das Backend **JPA Specifications**:

```java
public class EmployeeSpecification {

  public static Specification<EmployeeEntity> buildSearchSpec(
      EmployeeSearchCriteria criteria) {
    
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Full-text search
      if (criteria.searchTerm() != null) {
        String pattern = "%" + criteria.searchTerm().toLowerCase() + "%";
        predicates.add(cb.or(
            cb.like(cb.lower(root.get("user").get("firstName")), pattern),
            cb.like(cb.lower(root.get("user").get("lastName")), pattern),
            cb.like(cb.lower(root.get("user").get("email")), pattern)
        ));
      }

      // Skill-Filter
      if (criteria.skillIds() != null && !criteria.skillIds().isEmpty()) {
        Join<EmployeeEntity, EmployeeSkillEntity> skillJoin = root.join("skills");
        predicates.add(skillJoin.get("skill").get("id").in(criteria.skillIds()));
      }

      // Location-Filter
      if (criteria.locationIds() != null && !criteria.locationIds().isEmpty()) {
        predicates.add(root.get("location").get("id").in(criteria.locationIds()));
      }

      // Availability-Filter
      if (criteria.availability() != null && !criteria.availability().isEmpty()) {
        predicates.add(root.get("availability").in(criteria.availability()));
      }

      // Min Experience
      if (criteria.minExperience() != null) {
        predicates.add(cb.greaterThanOrEqualTo(
            root.get("yearsOfExperience"), 
            criteria.minExperience()
        ));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
```

**Vorteile:**
- Typsicher (Compile-Time Checks)
- Wiederverwendbar
- Kombinierbar (AND/OR-Logik)
- Performance durch optimierte SQL-Queries

---

## REST-API Endpunkte

### Übersicht aller Controller

| Controller | Base-Path | Beschreibung |
|------------|-----------|--------------|
| **ProfileController** | `/v1/profiles` | Profilverwaltung |
| **SkillsController** | `/v1/profiles/{userId}/skills` | Skill-Management |
| **EmployeeDiscoveryController** | `/v1/employees` | Mitarbeitersuche |
| **ProjectManagementController** | `/v1/projects` | Projektverwaltung (Manager/Admin) |
| **ProjectDiscoverController** | `/v1/projects/search` | Projektsuche |
| **AnalyticsController** | `/v1/analytics` | Analytics & Statistiken |
| **RoleRequestController** | `/v1/role-requests` | Rollenanfragen |
| **UserAdminController** | `/v1/admin/users` | User-Administration (Admin) |
| **OptionsController** | `/v1/options` | Stammdaten (Skills, Locations, etc.) |
| **KeycloakWebhookController** | `/webhooks/keycloak` | Keycloak Event-Webhooks |

### API-Beispiele

#### 1. Mitarbeitersuche mit Filtern

**Request:**
```http
GET /v1/employees/search?searchTerm=john&skillIds=uuid1,uuid2&locationIds=uuid3&availability=available&minExperience=3&page=0&size=20&sortBy=experience&sortOrder=desc
Authorization: Bearer <token>
Accept-Language: de
```

**Response:**
```json
{
  "employees": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@edag.com",
      "position": "Senior Software Engineer",
      "location": "Fulda",
      "availability": "available",
      "yearsOfExperience": 5.5,
      "skills": [
        {
          "id": "skill-uuid-1",
          "name": "Java",
          "score": 90,
          "yearsOfExperience": 5.0
        }
      ],
      "skillCount": 12,
      "totalProjects": 8
    }
  ],
  "metadata": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 45,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

#### 2. Skill zu Profil hinzufügen

**Request:**
```http
POST /v1/profiles/me/skills
Authorization: Bearer <token>
Content-Type: application/json

{
  "skillId": "123e4567-e89b-12d3-a456-426614174000",
  "categoryId": "234e5678-e89b-12d3-a456-426614174001",
  "score": 85,
  "yearsOfExperience": 3.5,
  "lastUsed": "2025-11-20"
}
```

**Response:**
```json
{
  "id": "345e6789-e89b-12d3-a456-426614174002",
  "skillName": "Spring Boot",
  "category": "Backend",
  "proficiencyScore": 85,
  "yearsOfExperience": 3.5,
  "lastUsed": "2025-11-20"
}
```

#### 3. Profil aktualisieren

**Request:**
```http
PUT /v1/profiles/me
Authorization: Bearer <token>
Content-Type: application/json

{
  "positionId": "456e7890-e89b-12d3-a456-426614174003",
  "locationId": "567e8901-e89b-12d3-a456-426614174004",
  "availability": "partially_available",
  "yearsOfExperience": 6.0,
  "bio": "Passionate software engineer with focus on backend development."
}
```

---

## Build & Deployment

### Multi-Stage Dockerfile

```dockerfile
# Stage 1: Build
FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /workspace

COPY mvnw mvnw.cmd ./
COPY .mvn/ .mvn/
COPY pom.xml ./
RUN chmod +x ./mvnw

COPY src/ src/
RUN ./mvnw -B --no-transfer-progress clean package

# Stage 2: Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=builder /workspace/target/*.jar /app/app.jar
RUN chown appuser:appgroup /app/app.jar

EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

USER appuser
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

**Optimierungen:**
- Multi-Stage Build (kleineres finales Image)
- Non-Root User (Security Best Practice)
- JVM Memory Management (`MaxRAMPercentage`)
- Layer-Caching durch separate COPY-Befehle

### Maven Build

```bash
# Lokaler Build
./mvnw clean package

# Mit Tests
./mvnw clean verify

# Code-Formatting
./mvnw spotless:apply

# Checkstyle-Prüfung
./mvnw checkstyle:check

# Test-Coverage
./mvnw jacoco:report
```

### Application Configuration

**application.yml** (Base)
```yaml
spring:
  application:
    name: skill-management-system
  
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
  
  security:
    oauth2:
      resourceserver:
        opaquetoken:
          introspection-uri: ${KEYCLOAK_INTROSPECTION_URI}
          client-id: ${OAUTH2_CLIENT_ID}
          client-secret: ${OAUTH2_CLIENT_SECRET}

server:
  servlet:
    context-path: /api
```

**application-dev.yml**
```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    com.edag.skillmanagementsystem: DEBUG
    org.springframework.security: DEBUG
```

---

## Zusammenfassung

### Stärken der Backend-Architektur

✅ **Klare Trennung der Verantwortlichkeiten** (Hexagonale Architektur)  
✅ **Hohe Testbarkeit** (Pure Domain-Logic ohne Framework-Abhängigkeiten)  
✅ **Typsicherheit** (Java 25 Records, keine NPEs durch moderne API)  
✅ **Erweiterbarkeit** (Neue Features durch neue Ports/Adapters)  
✅ **Performance** (Optimierte Queries, Connection-Pooling, Caching-Strategy)  
✅ **Security** (OAuth2, CORS, Validation, SQL-Injection-Prevention)  
✅ **Code-Qualität** (Checkstyle, Spotless, JaCoCo, SonarQube)  
✅ **API-Dokumentation** (OpenAPI/Swagger, automatisch generiert)  
✅ **Internationalisierung** (Multi-Language Support über Accept-Language Header)  

### Best Practices

- **Records statt Klassen** für immutable Domain-Objekte
- **Builder-Pattern** für komplexe Objekt-Konstruktion
- **Specifications** für dynamische Queries
- **DTOs** für API-Contracts (Entkopplung von Domain)
- **Exceptions** für Business-Fehler (nicht Strings)
- **Logging** auf mehreren Ebenen (DEBUG, INFO, ERROR)
- **Transactions** sinnvoll einsetzen (readOnly für Lesezugriffe)
- **Validation** mit Jakarta Validation (`@Valid`, Custom Validators)

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Frontend-Analyse →](./02_FRONTEND_ANALYSE.md)
