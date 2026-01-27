import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

// Analytics Types
export interface UserStatistics {
  id: string;
  userId: string;
  totalSkills: number;
  totalProjects: number;
  totalRecommendations: number;
  activeProjects: number;
  averageSkillScore: number | null;
}

export interface SkillDevelopmentDataPoint {
  id: string;
  userId: string;
  month: string;
  totalSkills: number;
  skillsAdded: number;
  skillsUpdated: number;
  skillsRemoved: number;
  averageProficiency: number;
  topCategory: string;
}

export interface SkillDevelopmentTrends {
  dataPoints: SkillDevelopmentDataPoint[];
}

export type ActivityType =
// Skill-related
    | 'SKILL_ADDED'
    | 'SKILL_UPDATED'
    | 'SKILL_REMOVED'

    // Project lifecycle
    | 'PROJECT_CREATED'
    | 'PROJECT_STARTED'
    | 'PROJECT_COMPLETED'
    | 'PROJECT_PLANNED'
    | 'PROJECT_UPDATED'
    | 'PROJECT_DELETED'

    // Project members
    | 'PROJECT_MEMBER_ADDED'
    | 'PROJECT_MEMBER_REMOVED'
    | 'PROJECT_MEMBER_ROLE_CHANGED'

    // Project skills/technologies
    | 'PROJECT_SKILL_ADDED'
    | 'PROJECT_SKILL_REMOVED'

    // Role request events
    | 'ROLE_REQUEST_CREATED'
    | 'ROLE_REQUEST_APPROVED'
    | 'ROLE_REQUEST_REJECTED'
    | 'ROLE_REQUEST_CANCELLED'
    | 'ROLE_REQUEST_REVIEWED'
    | 'ROLE_REQUEST_UPDATED'

    // Profile events
    | 'PROFILE_CREATED'
    | 'PROFILE_UPDATED'

    // User account events
    | 'USER_ACCOUNT_CREATED'
    | 'USER_EMAIL_CHANGED'
    | 'USER_NAME_CHANGED'
    | 'USER_ACCOUNT_DELETED';

export interface Activity {
  id: string;
  userId: string;
  activityType: ActivityType;
  relatedEntityId: string | null;
  relatedEntityType: string | null;
  timestamp: string;
}

export interface PageMetadata {
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface RecentActivitiesResponse {
  activities: Activity[];
  metadata: PageMetadata;
}

// Project Types
export interface Project {
  id: string;
  name: string;
  description: string;
  status: 'ACTIVE' | 'COMPLETED' | 'PLANNED';
  startDate: string;
  endDate: string | null;
  position: string;
  client: string;
  teamSize: number | null;
  technologies: string[];
}

export interface RecentProjectsResponse {
  projects: Project[];
  metadata: PageMetadata;
}

// ============================================================================
// Api Integration
// ============================================================================

export const dashboardApi = createApi({
  reducerPath: 'dashboardApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['Statistics', 'SkillTrends', 'Activities', 'Projects'],
  endpoints: (builder) => ({
    // Get user statistics
    getUserStatistics: builder.query<UserStatistics, string>({
      query: (userId = 'me') => ({
        url: `/v1/analytics/users/${userId}/statistics`,
        method: 'GET',
      }),
      providesTags: ['Statistics'],
    }),

    // Get skill development trends
    getSkillDevelopmentTrends: builder.query<SkillDevelopmentTrends, string>({
      query: (userId = 'me') => ({
        url: `/v1/analytics/users/${userId}/skill-trends`,
        method: 'GET',
      }),
      providesTags: ['SkillTrends'],
    }),

    // Get recent activities with pagination
    getRecentActivities: builder.query<
        RecentActivitiesResponse,
        { userId?: string; page?: number; size?: number }
    >({
      query: ({ userId = 'me', page = 0, size = 20 }) => {
        const params = new URLSearchParams({
          page: page.toString(),
          size: size.toString(),
        });
        return {
          url: `/v1/analytics/users/${userId}/activities?${params.toString()}`,
          method: 'GET',
        };
      },
      providesTags: ['Activities'],
    }),

    // Get recent projects with pagination
    getRecentProjects: builder.query<
        RecentProjectsResponse,
        { userId?: string; page?: number; size?: number }
    >({
      query: ({ userId = 'me', page = 0, size = 20 }) => {
        const params = new URLSearchParams({
          page: page.toString(),
          size: size.toString(),
        });
        return {
          url: `/v1/projects/users/${userId}?${params.toString()}`,
          method: 'GET',
        };
      },
      providesTags: ['Projects'],
    }),
  }),
});

export const {
  useGetUserStatisticsQuery,
  useGetSkillDevelopmentTrendsQuery,
  useGetRecentActivitiesQuery,
  useGetRecentProjectsQuery,
} = dashboardApi;
