import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export interface EmployeeSkill {
  id: string;
  name: string;
  score: number;
  yearsOfExperience: number;
}

export interface PageMetadata {
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  positionId: string;
  position: string;
  locationId: string;
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

export interface PaginationParams {
  page?: number;
  size?: number;
}

export interface SortingParams {
  sortBy?: 'name' | 'experience' | 'projects' | 'skills';
  sortOrder?: 'asc' | 'desc';
}

export interface EmployeeSearchParams extends PaginationParams, SortingParams {
  filters?: EmployeeSearchFilters;
}

export interface EmployeeSearchResponse {
  employees: Employee[];
  metadata: PageMetadata;
}

// Filter option item types
export interface SkillFilterOption {
  id: string;
  name: string;
  categoryId: string;
  categoryName: string;
}

export interface LocationFilterOption {
  id: string;
  name: string;
}

export interface SkillCategoryFilterOption {
  id: string;
  name: string;
}

export interface EmployeeFilterOptions {
  skills: SkillFilterOption[];
  locations: LocationFilterOption[];
  skillCategories: SkillCategoryFilterOption[];
}

// ============================================================================
// Helper Functions
// ============================================================================

// Helper function to build search params
function buildEmployeeSearchParams(params: EmployeeSearchParams): URLSearchParams {
  const searchParams = new URLSearchParams();

  // Add pagination parameters
  if (params.page !== undefined) {
    searchParams.append('page', params.page.toString());
  }
  if (params.size !== undefined) {
    searchParams.append('size', params.size.toString());
  }

  // Add sorting parameters
  if (params.sortBy) {
    searchParams.append('sortBy', params.sortBy);
  }
  if (params.sortOrder) {
    searchParams.append('sortOrder', params.sortOrder);
  }

  // Add filter parameters
  if (params.filters) {
    const { searchTerm, skillIds, skillCategoryIds, locationIds, availability, minExperience } =
        params.filters;

    if (searchTerm) {
      searchParams.append('searchTerm', searchTerm);
    }
    if (minExperience !== undefined) {
      searchParams.append('minExperience', minExperience.toString());
    }

    // Array-based filters with IDs
    if (skillIds) {
      skillIds.forEach((id) => searchParams.append('skillIds', id));
    }
    if (skillCategoryIds) {
      skillCategoryIds.forEach((id) => searchParams.append('skillCategoryIds', id));
    }
    if (locationIds) {
      locationIds.forEach((id) => searchParams.append('locationIds', id));
    }
    if (availability) {
      availability.forEach((status) => searchParams.append('availability', status));
    }
  }

  return searchParams;
}

// ============================================================================
// API Integration
// ============================================================================

export const employeeApi = createApi({
  reducerPath: 'employeeApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['Employees', 'EmployeeDetail', 'FilterOptions'],
  endpoints: (builder) => ({
    /**
     * Search for employees with filters and pagination
     */
    searchEmployees: builder.query<EmployeeSearchResponse, EmployeeSearchParams>({
      query: (params) => {
        const searchParams = buildEmployeeSearchParams(params);
        const queryString = searchParams.toString();

        return {
          url: `/v1/employees/search${queryString ? '?' + queryString : ''}`,
          method: 'GET',
        };
      },
      providesTags: ['Employees'],
    }),

    /**
     * Get filter options for search
     */
    getFilterOptions: builder.query<EmployeeFilterOptions, void>({
      query: () => ({
        url: '/v1/employees/filter-options',
        method: 'GET',
      }),
      providesTags: ['FilterOptions'],
    }),
  }),
});

export const {
  useSearchEmployeesQuery,
  useGetFilterOptionsQuery,
} = employeeApi;
