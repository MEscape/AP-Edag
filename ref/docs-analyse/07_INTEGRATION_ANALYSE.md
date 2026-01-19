# Integration-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Frontend-Backend Integration

### API-Kommunikation

**RTK Query Base API** (`src/store/api/base-api.ts`):
```typescript
export const baseApi = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080',
    prepareHeaders: async (headers, { getState }) => {
      const session = await getSession();
      if (session?.accessToken) {
        headers.set('Authorization', `Bearer ${session.accessToken}`);
      }
      headers.set('Accept-Language', locale);
      return headers;
    },
  }),
  tagTypes: ['Profile', 'Skills', 'Employees', 'Projects', 'Users'],
  endpoints: () => ({}),
});
```

**Auto-Logout bei 401**:
```typescript
export const baseQueryWithLocale: BaseQueryFn = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  if (result.error?.status === 401) {
    await signOut({ redirect: true, callbackUrl: '/' });
  }
  return result;
};
```

---

## REST API Endpoints

### Employee Discovery

**Frontend** (`employee-api.ts`):
```typescript
searchEmployees: builder.query<PaginatedResponse<EmployeeDto>, SearchEmployeesParams>({
  query: (params) => ({
    url: '/v1/employees/search',
    method: 'POST',
    body: {
      skills: params.skills,
      positions: params.positions,
      locations: params.locations,
      availability: params.availability,
      minYearsExperience: params.minYearsExperience,
      page: params.page || 0,
      size: params.size || 20,
    },
  }),
  providesTags: ['Employees'],
}),
```

**Backend** (`EmployeeDiscoveryController.java`):
```java
@PostMapping("/search")
public ResponseEntity<Page<EmployeeSearchResultDto>> searchEmployees(
    @Valid @RequestBody EmployeeSearchRequestDto searchRequest,
    Pageable pageable) {
  
  Page<EmployeeSearchResult> results = employeeDiscoveryService
      .searchEmployees(searchRequest, pageable);
  
  return ResponseEntity.ok(results.map(EmployeeSearchResultDto::from));
}
```

**JPA Specification** (dynamische Queries):
```java
public Specification<Employee> buildSpecification(EmployeeSearchRequestDto request) {
  return (root, query, cb) -> {
    List<Predicate> predicates = new ArrayList<>();
    
    // Skills Filter
    if (!request.skills().isEmpty()) {
      Join<Employee, ProfileSkill> skillsJoin = root.join("skills");
      predicates.add(skillsJoin.get("skill").get("id").in(request.skills()));
    }
    
    // Availability Filter
    if (request.availability() != null) {
      predicates.add(cb.equal(root.get("availability"), request.availability()));
    }
    
    return cb.and(predicates.toArray(new Predicate[0]));
  };
}
```

---

### Profile Management

**Frontend** (`profile-api.ts`):
```typescript
addSkill: builder.mutation<ProfileSkillDto, AddSkillRequest>({
  query: ({ userId, skillData }) => ({
    url: `/v1/profiles/${userId}/skills`,
    method: 'POST',
    body: skillData,
  }),
  invalidatesTags: ['Profile', 'Skills'],
}),
```

**Backend** (`ProfileController.java`):
```java
@PostMapping("/{userId}/skills")
@PreAuthorize("hasRole('USER')")
public ResponseEntity<ProfileSkillResponseDto> addSkill(
    @PathVariable String userId,
    @Valid @RequestBody AddSkillRequestDto request,
    Principal principal) {
  
  authorizationUtils.validateUserAccess(userId, principal);
  UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);
  
  ProfileSkill skill = skillManagementService.addSkill(resolvedUserId, request);
  
  return ResponseEntity.status(HttpStatus.CREATED)
      .body(ProfileSkillResponseDto.from(skill));
}
```

**Service Layer**:
```java
@Transactional
public ProfileSkill addSkill(UUID userId, AddSkillRequestDto request) {
  Employee employee = employeeRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
  
  Skill skill = skillRepository.findById(request.skillId())
      .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
  
  ProfileSkill profileSkill = ProfileSkill.builder()
      .employee(employee)
      .skill(skill)
      .proficiencyScore(request.proficiencyScore())
      .yearsOfExperience(request.yearsOfExperience())
      .lastUsed(request.lastUsed())
      .build();
  
  return profileSkillRepository.save(profileSkill);
}
```

---

## Authentication Flow

### 1. Login (NextAuth + Keycloak)

```
User → Frontend → NextAuth → Keycloak → Authorization Code
     ← Frontend ← NextAuth ← Keycloak ← Access Token + Refresh Token
```

**NextAuth Configuration**:
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
  },
};
```

### 2. API Request

```
Frontend → API Request + Bearer Token → Backend
        ← Response                    ← Spring Security validates Token
```

**Token Validation** (Backend):
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
  return http
      .oauth2ResourceServer(oauth2 -> oauth2
          .opaqueToken(token -> token.introspector(customIntrospector)))
      .build();
}
```

---

## Data Flow Examples

### Employee Search Flow

```
1. User enters search criteria in UI
   └─► Component: EmployeeSearchScreen
   
2. Form submission triggers RTK Query
   └─► Hook: useSearchEmployeesMutation()
   
3. API Request to Backend
   POST /api/v1/employees/search
   Headers: {
     Authorization: Bearer <token>
     Accept-Language: de
   }
   Body: {
     skills: [uuid1, uuid2],
     positions: [uuid3],
     page: 0,
     size: 20
   }
   
4. Backend processes request
   └─► Controller → Service → Repository (JPA Specifications)
   
5. Database Query
   SELECT e.* FROM employee_profiles e
   JOIN employee_skills es ON e.user_id = es.employee_id
   WHERE es.skill_id IN (uuid1, uuid2)
   AND e.position_id = uuid3
   LIMIT 20 OFFSET 0;
   
6. Response mapping
   └─► Entity → DTO → JSON
   
7. Frontend updates state
   └─► Redux Store → Component Re-render → UI Update
```

---

### Skill Addition Flow

```
1. User fills "Add Skill" form
   └─► Component: AddSkillDialog
   
2. Form validation (Zod Schema)
   └─► proficiencyScore: 0-100
   └─► yearsOfExperience: ≥ 0
   
3. API Request
   POST /api/v1/profiles/me/skills
   Body: {
     skillId: uuid,
     proficiencyScore: 85,
     yearsOfExperience: 3.5,
     lastUsed: "2025-11"
   }
   
4. Backend Authorization Check
   └─► validateUserAccess(userId, principal)
   
5. Business Logic
   └─► Validate skill exists
   └─► Check duplicate (UNIQUE constraint)
   └─► Create ProfileSkill entity
   └─► Save to database
   
6. Database Trigger fires
   └─► update_total_skills() → Updates user_statistics
   └─► log_skill_activity() → Creates activity log
   
7. Response
   {
     id: uuid,
     skill: { id, name, category },
     proficiencyScore: 85,
     yearsOfExperience: 3.5
   }
   
8. Frontend cache invalidation
   └─► invalidatesTags: ['Profile', 'Skills']
   └─► Automatic refetch of profile data
   
9. UI Update
   └─► Toast notification: "Skill added successfully"
   └─► Skill appears in profile list
```

---

## Error Handling

### Backend Error Response

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        Instant.now()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidation(
      MethodArgumentNotValidException ex) {
    
    Map<String, String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage
        ));
    
    return ResponseEntity.badRequest().body(new ValidationErrorResponse(errors));
  }
}
```

### Frontend Error Handling

```typescript
const { mutate: addSkill, isError, error } = useAddSkillMutation();

// Error Display
{isError && (
  <Alert variant="destructive">
    <AlertTitle>Error</AlertTitle>
    <AlertDescription>
      {error?.data?.message || 'Failed to add skill'}
    </AlertDescription>
  </Alert>
)}
```

---

## Real-Time Updates

### Keycloak Webhook Integration

**User Sync Flow**:
```
1. Admin creates user in Keycloak
   └─► Keycloak fires USER_CREATED event
   
2. Webhook sends POST request
   POST http://backend:8080/api/v1/webhooks/keycloak
   Body: {
     type: "USER_CREATED",
     userId: uuid,
     email: "new.user@edag.com",
     username: "new.user"
   }
   
3. Backend processes webhook
   └─► UserSyncService.syncNewUser()
   
4. Create User + Employee Profile
   INSERT INTO users (id, username, email) VALUES ...;
   INSERT INTO employee_profiles (user_id) VALUES ...;
   
5. User can now login and use system
```

---

## Internationalization

### Frontend (next-intl)

**Language Selection**:
```typescript
// middleware.ts
export default intlMiddleware({
  locales: ['en', 'de'],
  defaultLocale: 'de',
});

// Usage in components
const t = useTranslations('EmployeeSearch');
<h1>{t('title')}</h1>  // → "Mitarbeitersuche"
```

**Translation Files**:
```json
// src/locales/de.json
{
  "EmployeeSearch": {
    "title": "Mitarbeitersuche",
    "filters": "Filter",
    "results": "Ergebnisse"
  }
}
```

### Backend (Accept-Language Header)

```java
@GetMapping("/skills")
public ResponseEntity<List<SkillDto>> getSkills(
    @RequestHeader(value = "Accept-Language", defaultValue = "de") String locale) {
  
  List<Skill> skills = skillService.getAllSkills();
  // Could translate skill names based on locale
  return ResponseEntity.ok(skills.stream().map(SkillDto::from).toList());
}
```

---

## Performance Optimizations

### Frontend Caching

**RTK Query Automatic Caching**:
```typescript
// Cache Duration: 60 seconds
keepUnusedDataFor: 60,

// Refetch on Focus
refetchOnFocus: true,

// Tag-based Cache Invalidation
invalidatesTags: ['Profile'],
providesTags: (result) => [
  { type: 'Profile', id: result.id },
],
```

### Backend Pagination

```java
@GetMapping("/employees")
public Page<EmployeeDto> getEmployees(
    @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
  
  return employeeService.findAll(pageable).map(EmployeeDto::from);
}
```

### Database Indexing

```sql
-- Frequently searched columns
CREATE INDEX idx_employee_skills_skill_proficiency 
    ON employee_skills(skill_id, proficiency_score DESC);

CREATE INDEX idx_employee_profiles_location 
    ON employee_profiles(location_id);
```

---

## Security Integration

### Role-Based Access

**Frontend Middleware**:
```typescript
if (pathname.startsWith('/dashboard/discover/projects')) {
  return userRoles.includes('manager');
}
```

**Backend Method Security**:
```java
@PreAuthorize("hasRole('MANAGER')")
@PostMapping("/projects")
public ResponseEntity<ProjectDto> createProject(...) { }
```

### CORS Configuration

**Backend**:
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration config = new CorsConfiguration();
  config.setAllowedOrigins(Arrays.asList(
      "http://localhost:3000",
      "https://skill-management.edag.com"
  ));
  config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
  config.setAllowCredentials(true);
  return source;
}
```

---

## Integration Testing

### Backend Integration Tests

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "USER")
  void searchEmployees_ShouldReturnResults() throws Exception {
    mockMvc.perform(post("/api/v1/employees/search")
        .contentType(APPLICATION_JSON)
        .content("""
          {
            "skills": ["uuid"],
            "page": 0,
            "size": 20
          }
        """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }
}
```

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Gesamtarchitektur →](./08_GESAMTARCHITEKTUR.md)
