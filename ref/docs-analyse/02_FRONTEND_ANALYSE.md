# Frontend-Analyse - Skill Management System

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md)

---

## Inhaltsverzeichnis

1. [Technologie-Stack](#technologie-stack)
2. [Projekt-Struktur](#projekt-struktur)
3. [State Management](#state-management)
4. [API-Integration](#api-integration)
5. [Routing & Navigation](#routing--navigation)
6. [Authentication](#authentication)
7. [Komponenten-Architektur](#komponenten-architektur)
8. [Internationalisierung](#internationalisierung)
9. [Styling](#styling)
10. [Build & Deployment](#build--deployment)

---

## Technologie-Stack

### Core Framework

```json
{
  "next": "16.0.0",
  "react": "19.2.0",
  "react-dom": "19.2.0"
}
```

### State Management & Data Fetching

```json
{
  "@reduxjs/toolkit": "2.9.2",
  "react-redux": "9.2.0"
}
```

**RTK Query** ist integriert für:
- Automatisches Caching
- Request Deduplication
- Optimistic Updates
- Cache Invalidierung
- Background Refetching

### UI Framework & Komponenten

```json
{
  "@radix-ui/react-checkbox": "^1.3.3",
  "@radix-ui/react-dialog": "^1.1.15",
  "@radix-ui/react-dropdown-menu": "^2.1.16",
  "@radix-ui/react-label": "^2.1.8",
  "@radix-ui/react-progress": "^1.1.8",
  "@radix-ui/react-select": "^2.2.6",
  "@radix-ui/react-tabs": "^1.1.13",
  "lucide-react": "^0.552.0",
  "class-variance-authority": "^0.7.1",
  "tailwind-merge": "^3.3.1",
  "tailwindcss-animate": "^1.0.7"
}
```

Diese bilden **shadcn/ui** - ein Komponentensystem basierend auf Radix UI.

### Styling

```json
{
  "tailwindcss": "^3.4.0",
  "autoprefixer": "^10.4.21",
  "postcss": "^8.5.6"
}
```

### Authentication

```json
{
  "next-auth": "^4.24.13",
  "@auth/core": "^0.34.3"
}
```

### Internationalisierung

```json
{
  "next-intl": "^4.4.0"
}
```

### Visualization

```json
{
  "recharts": "^3.4.1"
}
```

### Validation

```json
{
  "zod": "^4.1.12"
}
```

### Development Tools

```json
{
  "typescript": "^5.9.3",
  "eslint": "^9.39.0",
  "@antfu/eslint-config": "^6.2.0",
  "knip": "^5.66.4",
  "sonarqube-scanner": "^4.3.2"
}
```

---

## Projekt-Struktur

```
frontend/
├── public/                           # Statische Assets
├── src/
│   ├── app/                         # Next.js 14 App Router
│   │   ├── api/                     # API Routes
│   │   │   ├── auth/               
│   │   │   │   └── [...nextauth]/  # NextAuth.js Route Handler
│   │   │   └── health/             # Health-Check Endpoint
│   │   │
│   │   ├── fonts/                  # Custom Fonts
│   │   ├── [locale]/               # Internationalisierte Routen
│   │   │   ├── (auth)/             # Auth-Layout-Gruppe
│   │   │   │   └── login/
│   │   │   │
│   │   │   ├── (dashboard)/        # Dashboard-Layout-Gruppe
│   │   │   │   ├── dashboard/
│   │   │   │   │   ├── home/
│   │   │   │   │   ├── discover/
│   │   │   │   │   │   ├── employees/
│   │   │   │   │   │   └── projects/
│   │   │   │   │   ├── profile/
│   │   │   │   │   ├── projects/
│   │   │   │   │   ├── activities/
│   │   │   │   │   └── admin/
│   │   │   │   │
│   │   │   │   ├── layout.tsx      # Dashboard Layout
│   │   │   │   └── page.tsx        # Dashboard Root
│   │   │   │
│   │   │   ├── layout.tsx          # Locale Layout
│   │   │   └── page.tsx            # Home/Landing Page
│   │   │
│   │   ├── global-error.tsx        # Global Error Boundary
│   │   ├── robots.ts               # SEO Robots Config
│   │   └── sitemap.ts              # SEO Sitemap
│   │
│   ├── components/                  # Wiederverwendbare UI-Komponenten
│   │   ├── ui/                     # shadcn/ui Basis-Komponenten
│   │   │   ├── button.tsx
│   │   │   ├── card.tsx
│   │   │   ├── dialog.tsx
│   │   │   ├── dropdown-menu.tsx
│   │   │   ├── input.tsx
│   │   │   ├── label.tsx
│   │   │   ├── select.tsx
│   │   │   ├── tabs.tsx
│   │   │   └── ...
│   │   │
│   │   ├── layout/                 # Layout-Komponenten
│   │   │   ├── header.tsx
│   │   │   ├── sidebar.tsx
│   │   │   ├── navigation.tsx
│   │   │   └── footer.tsx
│   │   │
│   │   └── shared/                 # Geteilte Business-Komponenten
│   │       ├── skill-badge.tsx
│   │       ├── user-avatar.tsx
│   │       ├── loading-spinner.tsx
│   │       └── ...
│   │
│   ├── screens/                     # Feature-spezifische Screens
│   │   ├── home/                   # Dashboard Home
│   │   ├── profile/                # Benutzerprofil
│   │   ├── discover-employees/     # Mitarbeitersuche
│   │   ├── discover-projects/      # Projektsuche
│   │   ├── my-projects/            # Meine Projekte
│   │   ├── my-activities/          # Meine Aktivitäten
│   │   ├── project-details/        # Projekt-Details
│   │   ├── admin/                  # Admin-Panel
│   │   └── welcome/                # Welcome-Screen
│   │
│   ├── store/                       # Redux State Management
│   │   ├── api/                    # RTK Query API Slices
│   │   │   ├── base-api.ts        # Base Query mit Auth
│   │   │   ├── dashboard-api.ts   # Dashboard Endpunkte
│   │   │   ├── employee-api.ts    # Employee Discovery
│   │   │   ├── profile-api.ts     # Profil & Skills
│   │   │   ├── project-api.ts     # Projekt-Management
│   │   │   ├── role-request-api.ts # Rollenanfragen
│   │   │   ├── admin-api.ts       # Admin-Funktionen
│   │   │   └── option-api.ts      # Stammdaten
│   │   │
│   │   ├── middleware/             # Custom Middleware
│   │   │   ├── api-logger.ts      # API-Request-Logging
│   │   │   └── logger.ts          # Redux-Logging
│   │   │
│   │   ├── hooks.ts                # Typed Redux Hooks
│   │   └── index.ts                # Store Configuration
│   │
│   ├── types/                       # TypeScript Type Definitions
│   │   ├── api.d.ts                # API-Response Types
│   │   ├── auth.d.ts               # Auth Types
│   │   └── models.d.ts             # Domain Model Types
│   │
│   ├── libs/                        # Utilities & Konfigurationen
│   │   ├── auth.ts                 # NextAuth Configuration
│   │   ├── env.ts                  # Environment Variables (Zod)
│   │   ├── i18n.ts                 # i18n Configuration
│   │   ├── i18nRouting.ts          # i18n Routing Config
│   │   └── logger.ts               # LogTape Configuration
│   │
│   ├── locales/                     # Übersetzungen
│   │   ├── de.json                 # Deutsch
│   │   └── en.json                 # Englisch
│   │
│   ├── hooks/                       # Custom React Hooks
│   │   ├── use-toast.ts
│   │   ├── use-debounce.ts
│   │   └── ...
│   │
│   ├── utils/                       # Helper-Funktionen
│   │   ├── helpers.ts
│   │   ├── date-utils.ts
│   │   └── ...
│   │
│   ├── styles/                      # Global Styles
│   │   └── globals.css
│   │
│   └── proxy.ts                     # Next.js Middleware (Auth)
│
├── components.json                  # shadcn/ui Config
├── next.config.ts                   # Next.js Configuration
├── tsconfig.json                    # TypeScript Config
├── tailwind.config.ts               # Tailwind Config
├── postcss.config.js                # PostCSS Config
├── eslint.config.mjs                # ESLint Config
├── knip.config.ts                   # Dependency Analysis
├── package.json
└── Dockerfile                       # Docker Build
```

---

## State Management

### Redux Store Configuration

**src/store/index.ts**
```typescript
import { configureStore } from '@reduxjs/toolkit';
import { dashboardApi } from '@/store/api/dashboard-api';
import { employeeApi } from '@/store/api/employee-api';
import { profileApi } from '@/store/api/profile-api';
import { projectApi } from '@/store/api/project-api';
import { roleRequestApi } from '@/store/api/role-request-api';
import { adminApi } from '@/store/api/admin-api';
import { optionApi } from '@/store/api/option-api';

export const store = configureStore({
  reducer: {
    [dashboardApi.reducerPath]: dashboardApi.reducer,
    [employeeApi.reducerPath]: employeeApi.reducer,
    [profileApi.reducerPath]: profileApi.reducer,
    [projectApi.reducerPath]: projectApi.reducer,
    [roleRequestApi.reducerPath]: roleRequestApi.reducer,
    [adminApi.reducerPath]: adminApi.reducer,
    [optionApi.reducerPath]: optionApi.reducer,
  },
  
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(dashboardApi.middleware)
      .concat(employeeApi.middleware)
      .concat(profileApi.middleware)
      .concat(projectApi.middleware)
      .concat(roleRequestApi.middleware)
      .concat(adminApi.middleware)
      .concat(optionApi.middleware)
      .concat(rtkQueryLoggerMiddleware)
      .concat(logtapeReduxMiddleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

**Features:**
- Separierte API-Slices für verschiedene Domänen
- Custom Middleware für Logging
- Type-Safe mit TypeScript

### Typed Hooks

**src/store/hooks.ts**
```typescript
import { useDispatch, useSelector, useStore } from 'react-redux';
import type { RootState, AppDispatch } from './index';

export const useAppDispatch = useDispatch.withTypes<AppDispatch>();
export const useAppSelector = useSelector.withTypes<RootState>();
export const useAppStore = useStore.withTypes<typeof store>();
```

---

## API-Integration

### Base Query Configuration

**src/store/api/base-api.ts**
```typescript
import { fetchBaseQuery } from "@reduxjs/toolkit/query";
import { getSession, signOut } from "next-auth/react";

const baseQuery = fetchBaseQuery({
  baseUrl: getBaseUrl(),
  prepareHeaders: async (headers) => {
    // Locale aus Document
    const locale = document.documentElement.lang || 'en';
    headers.set('Accept-Language', locale);

    // NextAuth Session (Access Token)
    const session = await getSession();
    if (session?.accessToken) {
      headers.set('Authorization', `Bearer ${session.accessToken}`);
    }

    return headers;
  },
});

export const baseQueryWithLocale: BaseQueryFn = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  // Automatischer Logout bei 401
  if (result.error?.status === 401) {
    await signOut({ redirect: true, callbackUrl: '/' });
  }

  return result;
};
```

**Features:**
- Automatisches Hinzufügen von Authorization-Header
- Locale-Header für i18n
- Automatischer Logout bei 401-Fehler
- Token aus NextAuth-Session

### Employee API Slice

**src/store/api/employee-api.ts**
```typescript
export const employeeApi = createApi({
  reducerPath: 'employeeApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['Employees', 'EmployeeDetail', 'FilterOptions'],
  
  endpoints: (builder) => ({
    // Mitarbeiter suchen mit Filtern
    searchEmployees: builder.query<EmployeeSearchResponse, EmployeeSearchParams>({
      query: (params) => {
        const searchParams = buildEmployeeSearchParams(params);
        return {
          url: `/v1/employees/search?${searchParams.toString()}`,
          method: 'GET',
        };
      },
      providesTags: ['Employees'],
    }),

    // Filter-Optionen abrufen
    getFilterOptions: builder.query<EmployeeFilterOptions, void>({
      query: () => '/v1/employees/filter-options',
      providesTags: ['FilterOptions'],
    }),
  }),
});

export const {
  useSearchEmployeesQuery,
  useGetFilterOptionsQuery,
} = employeeApi;
```

**Type Definitions:**
```typescript
export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  position: string;
  location: string;
  availability: 'available' | 'partially_available' | 'unavailable';
  skills: EmployeeSkill[];
  skillCount: number;
  totalProjects: number;
  yearsOfExperience: number;
}

export interface EmployeeSearchFilters {
  searchTerm?: string;
  skillIds?: string[];
  skillCategoryIds?: string[];
  locationIds?: string[];
  availability?: ('available' | 'partially_available' | 'unavailable')[];
  minExperience?: number;
}

export interface EmployeeSearchParams extends PaginationParams, SortingParams {
  filters?: EmployeeSearchFilters;
}
```

### Profile API Slice

**src/store/api/profile-api.ts**
```typescript
export const profileApi = createApi({
  reducerPath: 'profileApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['Profile', 'Skills', 'Options'],
  
  endpoints: (builder) => ({
    // Profil abrufen
    getProfile: builder.query<UserProfile, string>({
      query: (userId = 'me') => `/v1/profiles/${userId}`,
      providesTags: (_result, _error, userId) => [{ type: 'Profile', id: userId }],
    }),

    // Profil aktualisieren
    updateProfile: builder.mutation<UserProfile, { userId: string; data: UpdateProfileRequest }>({
      query: ({ userId, data }) => ({
        url: `/v1/profiles/${userId}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { userId }) => [{ type: 'Profile', id: userId }],
    }),

    // Skill hinzufügen
    addSkill: builder.mutation<ProfileSkill, { userId: string; skill: AddSkillRequest }>({
      query: ({ userId, skill }) => ({
        url: `/v1/profiles/${userId}/skills`,
        method: 'POST',
        body: skill,
      }),
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Profile', id: userId },
        'Skills',
      ],
    }),

    // Skill aktualisieren
    updateSkill: builder.mutation<ProfileSkill, { userId: string; skillId: string; data: UpdateSkillRequest }>({
      query: ({ userId, skillId, data }) => ({
        url: `/v1/profiles/${userId}/skills/${skillId}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Profile', id: userId },
        'Skills',
      ],
    }),

    // Skill löschen
    deleteSkill: builder.mutation<void, { userId: string; skillId: string }>({
      query: ({ userId, skillId }) => ({
        url: `/v1/profiles/${userId}/skills/${skillId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Profile', id: userId },
        'Skills',
      ],
    }),
  }),
});

export const {
  useGetProfileQuery,
  useUpdateProfileMutation,
  useAddSkillMutation,
  useUpdateSkillMutation,
  useDeleteSkillMutation,
} = profileApi;
```

**Cache-Invalidierung:**
- Mutations invalidieren automatisch relevante Queries
- `providesTags` und `invalidatesTags` für granulares Caching
- Optimistic Updates möglich

### Verwendung in Komponenten

```typescript
function EmployeeSearchScreen() {
  const [filters, setFilters] = useState<EmployeeSearchFilters>({});
  const [page, setPage] = useState(0);

  // Automatisches Fetching, Caching, Revalidierung
  const { data, isLoading, error } = useSearchEmployeesQuery({
    filters,
    page,
    size: 20,
    sortBy: 'name',
    sortOrder: 'asc',
  });

  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;

  return (
    <div>
      <EmployeeFilters filters={filters} onChange={setFilters} />
      <EmployeeList employees={data.employees} />
      <Pagination 
        current={page} 
        total={data.metadata.totalPages} 
        onChange={setPage} 
      />
    </div>
  );
}
```

**Vorteile:**
- Deklarativ (keine useEffect für Fetching)
- Automatisches Caching
- Loading & Error States
- Automatic Refetching
- Request Deduplication

---

## Routing & Navigation

### Next.js 14 App Router

Das Frontend nutzt den **App Router** mit folgenden Features:

**Layouts (Nested Routing)**
```
app/[locale]/
  ├── layout.tsx                 # Root Layout (i18n)
  ├── (auth)/
  │   ├── layout.tsx            # Auth Layout (minimal)
  │   └── login/page.tsx
  │
  └── (dashboard)/
      ├── layout.tsx            # Dashboard Layout (Sidebar, Header)
      └── dashboard/
          ├── home/page.tsx
          ├── profile/page.tsx
          ├── discover/
          │   ├── employees/page.tsx
          │   └── projects/page.tsx
          └── admin/page.tsx
```

**Layout-Gruppen** `(auth)` und `(dashboard)`:
- Organisieren Routen ohne URL-Segment
- Verschiedene Layouts für verschiedene Bereiche
- Vermeiden von Code-Duplikation

### Route-Schutz (Middleware)

**src/proxy.ts**
```typescript
import { withAuth } from 'next-auth/middleware';
import createIntlMiddleware from 'next-intl/middleware';
import { routing } from '@/libs/i18nRouting';

const intlMiddleware = createIntlMiddleware(routing);

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
        const isPublicRoute = publicRoutes.some(route => pathname.startsWith(route));
        if (isPublicRoute) return true;

        // Authentifizierung erforderlich
        if (!token) return false;

        // Rollen-Check
        const userRoles = token.roles as string[] | undefined;
        if (!userRoles || userRoles.length === 0) return false;

        // Manager-Routes
        const managerRoutes = ['/dashboard/discover/projects'];
        const isManagerRoute = managerRoutes.some(route => pathname.startsWith(route));
        if (isManagerRoute && !userRoles.includes('manager')) {
          return false;
        }

        // Admin-Routes
        const adminRoutes = ['/dashboard/admin'];
        const isAdminRoute = adminRoutes.some(route => pathname.startsWith(route));
        if (isAdminRoute && !userRoles.includes('admin')) {
          return false;
        }

        return true;
      },
    },
  }
);

export const config = {
  matcher: [
    '/((?!api|_next|_vercel|.*\\..*).*)',
    '/api/(.*)',
  ],
};
```

**Middleware-Chain:**
1. **NextAuth** prüft Authentifizierung
2. **Rollen-basierte Autorisierung**
3. **next-intl** für Locale-Routing

---

## Authentication

### NextAuth Configuration

**src/libs/auth.ts**
```typescript
import { NextAuthOptions } from 'next-auth';
import KeycloakProvider from 'next-auth/providers/keycloak';

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
        token.roles = account.roles; // Von Keycloak
      }
      return token;
    },
    
    async session({ session, token }) {
      session.accessToken = token.accessToken;
      session.roles = token.roles;
      return session;
    },
  },
  
  pages: {
    signIn: '/login',
  },
};
```

### Auth-Flow

```
┌──────────┐
│  User    │
└────┬─────┘
     │ 1. Navigiert zu /dashboard
     ▼
┌────────────────────┐
│   Middleware       │ ◄── src/proxy.ts
│   (withAuth)       │
└────┬───────────────┘
     │ 2. Keine Session? → Redirect zu /login
     │ 3. Session vorhanden? → Rollen-Check
     ▼
┌────────────────────┐
│   Login-Page       │
│   (/login)         │
└────┬───────────────┘
     │ 4. Klick auf "Login"
     ▼
┌────────────────────┐
│   Keycloak         │ ◄── OAuth2 Flow
│   (Identity Server)│
└────┬───────────────┘
     │ 5. User authentifiziert
     │ 6. Redirect mit Code
     ▼
┌────────────────────┐
│   NextAuth         │ ◄── /api/auth/callback
│   (Callback)       │
└────┬───────────────┘
     │ 7. Token-Exchange
     │ 8. Session erstellen
     ▼
┌────────────────────┐
│   Dashboard        │
│   (geschützt)      │
└────────────────────┘
```

---

## Komponenten-Architektur

### shadcn/ui System

Das Frontend verwendet **shadcn/ui** - kein NPM-Package, sondern kopierte Komponenten.

**Vorteile:**
- Vollständige Kontrolle über Code
- Keine Vendor-Lock-in
- Tree-Shaking (nur genutzte Komponenten)
- Einfach anpassbar

**Installation einer Komponente:**
```bash
npx shadcn-ui@latest add button
```

Dies kopiert die Komponente nach `src/components/ui/button.tsx`.

### Beispiel: Button-Komponente

**src/components/ui/button.tsx**
```typescript
import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "@/utils/helpers"

const buttonVariants = cva(
  "inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none disabled:pointer-events-none disabled:opacity-50",
  {
    variants: {
      variant: {
        default: "bg-primary text-primary-foreground hover:bg-primary/90",
        destructive: "bg-destructive text-destructive-foreground hover:bg-destructive/90",
        outline: "border border-input hover:bg-accent",
        secondary: "bg-secondary text-secondary-foreground hover:bg-secondary/80",
        ghost: "hover:bg-accent hover:text-accent-foreground",
        link: "underline-offset-4 hover:underline text-primary",
      },
      size: {
        default: "h-10 px-4 py-2",
        sm: "h-9 rounded-md px-3",
        lg: "h-11 rounded-md px-8",
        icon: "h-10 w-10",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
)

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button"
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    )
  }
)
Button.displayName = "Button"

export { Button, buttonVariants }
```

**Verwendung:**
```typescript
<Button variant="default" size="lg">
  Click me
</Button>

<Button variant="destructive" onClick={handleDelete}>
  Delete
</Button>

<Button variant="outline" disabled>
  Disabled
</Button>
```

### Feature-Komponenten

**Beispiel: Employee Card**
```typescript
interface EmployeeCardProps {
  employee: Employee;
  onClick?: () => void;
}

export function EmployeeCard({ employee, onClick }: EmployeeCardProps) {
  return (
    <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={onClick}>
      <CardHeader>
        <div className="flex items-center gap-4">
          <Avatar>
            <AvatarFallback>
              {employee.firstName[0]}{employee.lastName[0]}
            </AvatarFallback>
          </Avatar>
          <div>
            <CardTitle>{employee.firstName} {employee.lastName}</CardTitle>
            <CardDescription>{employee.position}</CardDescription>
          </div>
        </div>
      </CardHeader>
      
      <CardContent>
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <MapPin className="h-4 w-4" />
            <span>{employee.location}</span>
          </div>
          
          <div className="flex items-center gap-2">
            <Briefcase className="h-4 w-4" />
            <span>{employee.yearsOfExperience} Jahre Erfahrung</span>
          </div>
          
          <Badge variant={getAvailabilityVariant(employee.availability)}>
            {employee.availability}
          </Badge>
        </div>
        
        <Separator className="my-4" />
        
        <div className="space-y-2">
          <h4 className="text-sm font-medium">Top Skills</h4>
          <div className="flex flex-wrap gap-2">
            {employee.skills.slice(0, 3).map(skill => (
              <Badge key={skill.id} variant="outline">
                {skill.name} ({skill.score})
              </Badge>
            ))}
          </div>
        </div>
      </CardContent>
      
      <CardFooter>
        <Button variant="outline" className="w-full">
          Profil ansehen
        </Button>
      </CardFooter>
    </Card>
  );
}
```

---

## Internationalisierung

### next-intl Setup

**src/libs/i18n.ts**
```typescript
import { getRequestConfig } from 'next-intl/server';
import { routing } from './i18nRouting';

export default getRequestConfig(async ({ locale }) => {
  // Validierung
  if (!routing.locales.includes(locale as any)) {
    locale = routing.defaultLocale;
  }

  return {
    messages: (await import(`../locales/${locale}.json`)).default,
  };
});
```

**src/libs/i18nRouting.ts**
```typescript
import { defineRouting } from 'next-intl/routing';

export const routing = defineRouting({
  locales: ['de', 'en'],
  defaultLocale: 'de',
  localePrefix: 'always',
});
```

### Übersetzungsdateien

**src/locales/de.json**
```json
{
  "common": {
    "loading": "Lädt...",
    "error": "Ein Fehler ist aufgetreten",
    "save": "Speichern",
    "cancel": "Abbrechen",
    "delete": "Löschen",
    "edit": "Bearbeiten"
  },
  "navigation": {
    "home": "Startseite",
    "profile": "Profil",
    "employees": "Mitarbeiter",
    "projects": "Projekte",
    "admin": "Administration"
  },
  "profile": {
    "title": "Mein Profil",
    "editProfile": "Profil bearbeiten",
    "addSkill": "Skill hinzufügen",
    "skills": "Skills",
    "projects": "Projekte"
  }
}
```

### Verwendung

```typescript
import { useTranslations } from 'next-intl';

export function ProfilePage() {
  const t = useTranslations('profile');

  return (
    <div>
      <h1>{t('title')}</h1>
      <Button>{t('editProfile')}</Button>
    </div>
  );
}
```

**Vorteile:**
- Type-Safe Translations (mit TypeScript)
- Automatisches Locale-Switching
- SEO-freundlich (separate URLs pro Sprache)
- Server & Client Support

---

## Styling

### TailwindCSS Configuration

**tailwind.config.ts**
```typescript
import type { Config } from 'tailwindcss';

const config: Config = {
  darkMode: ['class'],
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
    './src/screens/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        border: 'hsl(var(--border))',
        input: 'hsl(var(--input))',
        ring: 'hsl(var(--ring))',
        background: 'hsl(var(--background))',
        foreground: 'hsl(var(--foreground))',
        primary: {
          DEFAULT: 'hsl(var(--primary))',
          foreground: 'hsl(var(--primary-foreground))',
        },
        secondary: {
          DEFAULT: 'hsl(var(--secondary))',
          foreground: 'hsl(var(--secondary-foreground))',
        },
        // ... weitere Farben
      },
      borderRadius: {
        lg: 'var(--radius)',
        md: 'calc(var(--radius) - 2px)',
        sm: 'calc(var(--radius) - 4px)',
      },
    },
  },
  plugins: [require('tailwindcss-animate')],
};

export default config;
```

### CSS Variables (Design-Tokens)

**src/styles/globals.css**
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  :root {
    --background: 0 0% 100%;
    --foreground: 222.2 84% 4.9%;
    
    --card: 0 0% 100%;
    --card-foreground: 222.2 84% 4.9%;
    
    --primary: 222.2 47.4% 11.2%;
    --primary-foreground: 210 40% 98%;
    
    --secondary: 210 40% 96.1%;
    --secondary-foreground: 222.2 47.4% 11.2%;
    
    --radius: 0.5rem;
  }

  .dark {
    --background: 222.2 84% 4.9%;
    --foreground: 210 40% 98%;
    /* ... Dark Mode Farben */
  }
}
```

**Vorteile:**
- Theme-Switching (Light/Dark Mode)
- Konsistente Design-Sprache
- Zentrale Verwaltung
- CSS Custom Properties für Runtime-Änderungen

---

## Build & Deployment

### Next.js Configuration

**next.config.ts**
```typescript
import type { NextConfig } from 'next';
import createNextIntlPlugin from 'next-intl/plugin';
import './src/libs/env';

const baseConfig: NextConfig = {
  devIndicators: {
    position: 'bottom-right',
  },
  poweredByHeader: false,
  reactStrictMode: true,
  reactCompiler: true,  // React 19 Compiler
  experimental: {
    turbopackFileSystemCacheForDev: true  // Turbopack Caching
  }
};

let configWithPlugins = createNextIntlPlugin('./src/libs/i18n.ts')(baseConfig);

// Bundle Analyzer (optional)
if (process.env.ANALYZE === 'true') {
  const withBundleAnalyzer = require('@next/bundle-analyzer');
  configWithPlugins = withBundleAnalyzer()(configWithPlugins);
}

export default configWithPlugins;
```

### Multi-Stage Dockerfile

```dockerfile
# Stage 1: Dependencies
FROM node:20-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --only=production

# Stage 2: Build
FROM node:20-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
ENV NEXT_TELEMETRY_DISABLED 1
ENV SKIP_ENV_VALIDATION true
RUN npm run build

# Stage 3: Runtime
FROM node:20-alpine AS runner
WORKDIR /app

ENV NODE_ENV production
ENV NEXT_TELEMETRY_DISABLED 1

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /app/public ./public
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs
EXPOSE 3000
ENV PORT 3000

CMD ["node", "server.js"]
```

### Build-Scripts

```json
{
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "eslint .",
    "lint:fix": "eslint . --fix",
    "check:types": "tsc --noEmit --pretty",
    "check:deps": "knip",
    "check:i18n": "i18n-check -l src/locales -s en -u src -f next-intl"
  }
}
```

### Environment Variables

**src/libs/env.ts** (Zod Validation)
```typescript
import { createEnv } from '@t3-oss/env-nextjs';
import { z } from 'zod';

export const Env = createEnv({
  server: {
    KEYCLOAK_CLIENT_ID: z.string(),
    KEYCLOAK_CLIENT_SECRET: z.string(),
    KEYCLOAK_ISSUER: z.string().url(),
    NEXTAUTH_SECRET: z.string(),
    NEXTAUTH_URL: z.string().url(),
  },
  client: {
    NEXT_PUBLIC_API_URL: z.string().url(),
  },
  runtimeEnv: {
    KEYCLOAK_CLIENT_ID: process.env.KEYCLOAK_CLIENT_ID,
    KEYCLOAK_CLIENT_SECRET: process.env.KEYCLOAK_CLIENT_SECRET,
    KEYCLOAK_ISSUER: process.env.KEYCLOAK_ISSUER,
    NEXTAUTH_SECRET: process.env.NEXTAUTH_SECRET,
    NEXTAUTH_URL: process.env.NEXTAUTH_URL,
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
  },
});
```

---

## Zusammenfassung

### Stärken der Frontend-Architektur

✅ **Moderne Stack** (Next.js 16, React 19, TypeScript)  
✅ **Type-Safety** (End-to-End mit Zod Validation)  
✅ **Performance** (React Compiler, Turbopack, SSR, Image Optimization)  
✅ **Developer Experience** (Hot Reload, TypeScript, ESLint, Prettier)  
✅ **State Management** (RTK Query mit automatischem Caching)  
✅ **UI-Konsistenz** (shadcn/ui mit Radix Primitives)  
✅ **Internationalisierung** (next-intl mit Type-Safe Translations)  
✅ **Security** (NextAuth, Role-Based Access Control)  
✅ **SEO-Ready** (App Router, Metadata API, Sitemap)  
✅ **Accessibility** (Radix UI ARIA-konform)  

### Best Practices

- **Feature-Based Structure** statt Technical Structure
- **Colocated Components** (nahe am Verwendungsort)
- **Server Components** wo möglich (Performance)
- **Client Components** nur bei Interaktivität
- **RTK Query** für API-Calls (kein manuelles Fetching)
- **Typed Hooks** für Redux (Type-Safety)
- **CSS-in-JS vermeiden** (TailwindCSS für Performance)
- **Environment-Validation** mit Zod (zur Build-Time)

---

[← Zurück zur Übersicht](../SYSTEM_ANALYSE.md) | [Weiter: Datenbank-Analyse →](./03_DATENBANK_ANALYSE.md)
