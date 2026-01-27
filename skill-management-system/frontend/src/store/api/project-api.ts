import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export interface ProjectMember {
  employeeId: string;
  employeeName: string;
  positionId: string;
  positionName: string;
}

export interface Project {
  id: string;
  name: string;
  description: string;
  status: 'ACTIVE' | 'COMPLETED' | 'PLANNED';
  startDate: string;
  endDate: string | null;
  client: string;
  teamSize: number;
  technologies: string[];
  createdByUserId: string;
  members: ProjectMember[];
}

export interface ProjectSearchFilters {
  searchTerm?: string;
  statusList?: string[];
  employeeIds?: string[];
  skillIds?: string[];
}

export interface ProjectSearchResponse {
  projects: Project[];
  metadata: {
    pageNumber: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
  };
}

export interface RecentProjectsResponse {
  projects: Project[];
  metadata: {
    pageNumber: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
  };
}

export interface ProjectMemberRequest {
  employeeId: string;
  positionId: string;
}

export interface CreateProjectRequest {
  name: string;
  description?: string;
  status: 'ACTIVE' | 'COMPLETED' | 'PLANNED';
  startDate: string;
  endDate?: string | null;
  client?: string;
  technologies: string[];
  members: ProjectMemberRequest[];
}

export interface UpdateProjectRequest {
  name?: string;
  description?: string;
  status?: string;
  startDate?: string;
  endDate?: string | null;
  client?: string;
  technologyIds?: string[];
  members?: ProjectMemberRequest[];
}

export interface TechnologyFilterOption {
  id: string;
  name: string;
}

export interface ProjectFilterOptions {
  skills: TechnologyFilterOption[];
}

// ============================================================================
// Api Integration
// ============================================================================

export const projectApi = createApi({
  reducerPath: 'projectApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['Project', 'ProjectList', 'FilterOptions'],
  endpoints: (builder) => ({
    // Get user projects (for profile/dashboard)
    getUserProjects: builder.query<
      RecentProjectsResponse,
      { userId: string; page?: number; size?: number }
    >({
      query: ({ userId, page = 0, size = 20 }) => ({
        url: `/v1/projects/users/${userId}`,
        method: 'GET',
        params: { page, size },
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.projects.map(({ id }) => ({ type: 'Project' as const, id })),
              { type: 'ProjectList', id: 'USER' },
            ]
          : [{ type: 'ProjectList', id: 'USER' }],
    }),

    // Search projects (for managers)
    searchProjects: builder.query<
      ProjectSearchResponse,
      {
        filters?: ProjectSearchFilters;
        page?: number;
        size?: number;
        sortBy?: string;
        sortOrder?: 'asc' | 'desc';
      }
    >({
      query: ({ filters, page = 0, size = 20, sortBy = 'startDate', sortOrder = 'desc' }) => ({
        url: '/v1/projects/search',
        method: 'GET',
        params: {
          searchTerm: filters?.searchTerm,
          statusList: filters?.statusList,
          employeeIds: filters?.employeeIds,
          skillIds: filters?.skillIds,
          page,
          size,
          sortBy,
          sortOrder,
        },
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.projects.map(({ id }) => ({ type: 'Project' as const, id })),
              { type: 'ProjectList', id: 'MANAGER' },
            ]
          : [{ type: 'ProjectList', id: 'MANAGER' }],
    }),

    // Get project by ID
    getProjectById: builder.query<Project, string>({
      query: (projectId) => ({
        url: `/v1/projects/${projectId}`,
        method: 'GET',
      }),
      providesTags: (_result, _error, projectId) => [{ type: 'Project', id: projectId }],
    }),

    // Get filter options
    getFilterOptions: builder.query<ProjectFilterOptions, void>({
      query: () => ({
        url: '/v1/projects/filter-options',
        method: 'GET',
      }),
      providesTags: [{ type: 'FilterOptions', id: 'LIST' }],
    }),

    // Create project
    createProject: builder.mutation<Project, CreateProjectRequest>({
      query: (data) => ({
        url: '/v1/management/projects',
        method: 'POST',
        body: data,
      }),
      invalidatesTags: [
        { type: 'ProjectList', id: 'MANAGER' },
        { type: 'ProjectList', id: 'USER' },
        { type: 'FilterOptions', id: 'LIST' },
      ],
    }),

    // Update project
    updateProject: builder.mutation<Project, { projectId: string; data: UpdateProjectRequest }>({
      query: ({ projectId, data }) => ({
        url: `/v1/management/projects/${projectId}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { projectId }) => [
        { type: 'Project', id: projectId },
        { type: 'ProjectList', id: 'MANAGER' },
        { type: 'ProjectList', id: 'USER' },
      ],
    }),

    // Delete project
    deleteProject: builder.mutation<void, string>({
      query: (projectId) => ({
        url: `/v1/management/projects/${projectId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, projectId) => [
        { type: 'Project', id: projectId },
        { type: 'ProjectList', id: 'MANAGER' },
        { type: 'ProjectList', id: 'USER' },
        { type: 'FilterOptions', id: 'LIST' },
      ],
    }),
  }),
});

export const {
  useGetUserProjectsQuery,
  useSearchProjectsQuery,
  useGetProjectByIdQuery,
  useGetFilterOptionsQuery,
  useCreateProjectMutation,
  useUpdateProjectMutation,
  useDeleteProjectMutation,
} = projectApi;
