# Kapitel 7: Umsetzung und Implementierungsdetails

## 7.1 Übersicht

Dieses Kapitel beschreibt die technische Umsetzung der wichtigsten Features: Authentifizierung (OAuth2 Keycloak Flow), Profilverwaltung, Skill-Management, Employee Discovery, Projektmanagement, Analytics und Internationalisierung.

---

## 7.2 Authentifizierung & Autorisierung

### OAuth2 Flow mit Keycloak

**Frontend (NextAuth.js):**
```typescript
export const authOptions: NextAuthOptions = {
  providers: [KeycloakProvider({
    clientId: process.env.KEYCLOAK_CLIENT_ID!,
    issuer: process.env.KEYCLOAK_ISSUER,
  })],
  callbacks: {
    async jwt({ token, account }) {
      if (account) token.accessToken = account.access_token
      return refreshAccessToken(token)
    },
  },
}
```

**Backend (Spring Security):**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter())
        ));
        return http.build();
    }
}
```

**Method Security:**
```java
@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.claims['sub']")
public ResponseEntity<ProfileDTO> updateProfile(@PathVariable UUID userId) {
    return ResponseEntity.ok(profileService.updateProfile(userId, request));
}
```

---

## 7.3 Profilverwaltung

**Service:**
```java
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {
    @Override
    public EmployeeProfile updateProfile(UUID userId, UpdateProfileCommand command) {
        EmployeeProfileEntity entity = employeeRepository.findByUserIdForUpdate(userId)
            .orElseThrow(() -> new NotFoundException("Profile not found"));
        entity.setPosition(positionRepository.findById(command.positionId()).orElseThrow());
        entity.setAvailability(command.availability());
        return employeeRepository.save(entity);
    }
}
```

**Frontend:**
```tsx
export function ProfileScreen() {
  const { data: profile } = useGetMyProfileQuery()
  const [updateProfile] = useUpdateMyProfileMutation()

  const onSubmit = async (data: UpdateProfileRequest) => {
    await updateProfile(data).unwrap()
    toast.success('Profile updated')
  }

  return <Form onSubmit={form.handleSubmit(onSubmit)}>{/* fields */}</Form>
}
```

---

## 7.4 Skill-Management

**Service:**
```java
@Service
public class SkillManagementServiceImpl {
    @Override
    public EmployeeSkill addSkill(UUID employeeId, AddSkillCommand command) {
        if (employeeSkillRepository.existsByEmployeeIdAndSkillId(employeeId, command.skillId())) {
            throw new DuplicateResourceException("Skill already exists");
        }
        EmployeeSkillEntity entity = EmployeeSkillEntity.builder()
            .employeeId(employeeId)
            .skillId(command.skillId())
            .proficiencyScore(command.proficiencyScore())
            .build();
        return employeeSkillRepository.save(entity);
    }
}
```

**Frontend:**
```tsx
export function AddSkillDialog({ open, onOpenChange }) {
  const [addSkill] = useAddSkillMutation()

  const onSubmit = async (data) => {
    try {
      await addSkill(data).unwrap()
      toast.success('Skill added')
      onOpenChange(false)
    } catch (error) {
      toast.error('Failed to add skill')
    }
  }

  return <Dialog open={open}><Form onSubmit={handleSubmit(onSubmit)} /></Dialog>
}
```

---

## 7.5 Employee Discovery

**JPA Specifications:**
```java
public class EmployeeSpecifications {
    public static Specification<EmployeeProfileEntity> withSearchCriteria(
        EmployeeSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.searchTerm() != null) {
                predicates.add(cb.like(cb.lower(root.get("user").get("firstName")),
                    "%" + criteria.searchTerm().toLowerCase() + "%"));
            }

            if (criteria.skillIds() != null && !criteria.skillIds().isEmpty()) {
                Subquery<Long> skillSubquery = query.subquery(Long.class);
                skillSubquery.select(cb.count(skillRoot))
                    .where(skillRoot.get("skillId").in(criteria.skillIds()));
                predicates.add(cb.equal(skillSubquery, (long) criteria.skillIds().size()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

**Frontend:**
```tsx
export function EmployeeSearchScreen() {
  const [filters, setFilters] = useFilterState()
  const debouncedSearch = useDebounce(filters.searchTerm, 500)

  const { data } = useSearchEmployeesQuery({
    searchTerm: debouncedSearch,
    skillIds: filters.skillIds,
    page: filters.page,
  })

  return (
    <div className="flex gap-6">
      <FilterSidebar filters={filters} onChange={setFilters} />
      <EmployeeGrid employees={data?.content} />
    </div>
  )
}
```

---

## 7.6 Projektmanagement

**Service:**
```java
@Service
@Transactional
public class ProjectManagementServiceImpl {
    @Override
    public Project createProject(CreateProjectCommand command, UUID userId) {
        ProjectEntity entity = ProjectEntity.builder()
            .name(command.name())
            .status(ProjectStatus.PLANNED)
            .createdByUserId(userId)
            .build();
        return projectRepository.save(entity);
    }

    @Override
    public Project addMember(UUID projectId, AddProjectMemberCommand command) {
        if (projectMemberRepository.existsByProjectIdAndEmployeeId(projectId, command.employeeId())) {
            throw new DuplicateResourceException("Employee already member");
        }
        ProjectMemberEntity member = ProjectMemberEntity.builder()
            .projectId(projectId)
            .employeeId(command.employeeId())
            .build();
        projectMemberRepository.save(member);
        return projectRepository.findByIdWithDetails(projectId).orElseThrow();
    }
}
```

---

## 7.7 Analytics & Dashboard

**Query:**
```java
@Query("""
    SELECT new DashboardStats(us.totalSkills, us.averageSkillScore, us.totalProjects,
        (SELECT COUNT(a) FROM ActivityEntity a WHERE a.userId = :userId))
    FROM UserStatisticsEntity us WHERE us.userId = :userId
""")
Optional<DashboardStats> getDashboardStats(@Param("userId") UUID userId);
```

**Frontend:**
```tsx
export function HomeScreen() {
  const { data: stats } = useGetDashboardStatsQuery()

  return (
    <div className="grid grid-cols-4 gap-4">
      <StatsCard title="Total Skills" value={stats.totalSkills} />
      <StatsCard title="Avg Score" value={stats.averageSkillScore} />
      <StatsCard title="Active Projects" value={stats.activeProjects} />
    </div>
  )
}
```

---

## 7.8 Internationalisierung (i18n)

**Backend:**
```java
@Configuration
public class I18nConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        return ms;
    }
}
```

**Frontend:**
```typescript
export default createMiddleware({
  locales: ['en', 'de'],
  defaultLocale: 'en',
})

// Usage
const t = useTranslations('profile')
return <h1>{t('title')}</h1>
```

---

## 7.9 Error-Handling

**Global Exception Handler:**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("Resource not found"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(errors));
    }
}
```

---

## 7.10 Zusammenfassung

### Implementierungsstatus

| Feature | Backend | Frontend | Tests | Status |
|---------|---------|----------|-------|--------|
| **Authentifizierung** | ✅ OAuth2 | ✅ NextAuth | ✅ | ✅ Vollständig |
| **Profilverwaltung** | ✅ CRUD | ✅ Form | ✅ | ✅ Vollständig |
| **Skill-Management** | ✅ CRUD | ✅ Dialog | ✅ | ✅ Vollständig |
| **Employee Discovery** | ✅ JPA Specs | ✅ Filter | ✅ | ✅ Vollständig |
| **Projektmanagement** | ✅ CRUD | ✅ Manage | ✅ | ✅ Vollständig |
| **Analytics** | ✅ Stats | ✅ Dashboard | ✅ | ✅ Vollständig |

### Technische Highlights

**Backend:**
- ✅ Hexagonal Architecture für klare Trennung
- ✅ JPA Specifications für dynamische Queries
- ✅ PostgreSQL Triggers für Automatisierung
- ✅ Method Security mit @PreAuthorize
- ✅ Global Exception Handler mit i18n

**Frontend:**
- ✅ RTK Query für API-Caching
- ✅ Optimistic Updates für bessere UX
- ✅ Form-Validierung mit Zod
- ✅ shadcn/ui für konsistentes Design
- ✅ Debounced Search für Performance

**DevOps:**
- ✅ Jenkins Pipeline mit 9 Stages
- ✅ SonarQube Quality Gates
- ✅ Docker Multi-Stage Builds
- ✅ Kubernetes mit Kustomize
