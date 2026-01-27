import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export interface SkillCategory {
    id: string;
    name: string;
}

export interface Skill {
    id: string;
    name: string;
    categoryId: string;
    categoryName: string;
}

export interface Position {
    id: string;
    name: string;
}

export interface Location {
    id: string;
    name: string;
}

export interface SkillCategoriesResponse {
    categories: SkillCategory[];
}

export interface SkillOptionsResponse {
    skills: Skill[];
}

export interface PositionOptionsResponse {
    positions: Position[];
}

export interface LocationOptionsResponse {
    locations: Location[];
}

export interface CreateSkillCategoryRequest {
    name: string;
    description?: string;
}

export interface CreateSkillRequest {
    name: string;
    categoryId: string;
}

export interface CreatePositionRequest {
    name: string;
}

export interface CreateLocationRequest {
    name: string;
}

// ============================================================================
// RTK Query API
// ============================================================================

export const optionApi = createApi({
    reducerPath: 'optionApi',
    baseQuery: baseQueryWithLocale,
    tagTypes: ['SkillCategory', 'Skill', 'Position', 'Location'],
    endpoints: (builder) => ({
        // ========== Public Endpoints (Read) ==========

        // Get all skill categories
        getSkillCategories: builder.query<SkillCategoriesResponse, void>({
            query: () => '/v1/options/skill-categories',
            providesTags: ['SkillCategory'],
        }),

        // Get all skills
        getAllSkills: builder.query<SkillOptionsResponse, void>({
            query: () => '/v1/options/skills',
            providesTags: ['Skill'],
        }),

        // Get skills by category
        getSkillsByCategory: builder.query<SkillOptionsResponse, string>({
            query: (categoryId) => `/v1/options/skills/category/${categoryId}`,
            providesTags: ['Skill'],
        }),

        // Get all positions
        getPositions: builder.query<PositionOptionsResponse, void>({
            query: () => '/v1/options/positions',
            providesTags: ['Position'],
        }),

        // Get all locations
        getLocations: builder.query<LocationOptionsResponse, void>({
            query: () => '/v1/options/locations',
            providesTags: ['Location'],
        }),

        // ========== Admin Endpoints (Create) ==========

        // Create skill category (Admin only)
        createSkillCategory: builder.mutation<SkillCategory, CreateSkillCategoryRequest>({
            query: (body) => ({
                url: '/v1/options/skill-categories',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['SkillCategory'],
        }),

        // Create skill (Admin only)
        createSkill: builder.mutation<Skill, CreateSkillRequest>({
            query: (body) => ({
                url: '/v1/options/skills',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Skill'],
        }),

        // Create position (Admin only)
        createPosition: builder.mutation<Position, CreatePositionRequest>({
            query: (body) => ({
                url: '/v1/options/positions',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Position'],
        }),

        // Create location (Admin only)
        createLocation: builder.mutation<Location, CreateLocationRequest>({
            query: (body) => ({
                url: '/v1/options/locations',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Location'],
        }),
    }),
});

export const {
    // Public hooks
    useGetSkillCategoriesQuery,
    useGetAllSkillsQuery,
    useGetSkillsByCategoryQuery,
    useGetPositionsQuery,
    useGetLocationsQuery,

    // Admin hooks
    useCreateSkillCategoryMutation,
    useCreateSkillMutation,
    useCreatePositionMutation,
    useCreateLocationMutation,
} = optionApi;