import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export interface ProfileSkill {
    id: string;
    skillName: string;
    category: string;
    proficiencyScore: number;
    yearsOfExperience: number;
    lastUsed: string; // LocalDate from backend as ISO string
}

export interface ProjectMember {
    employeeId: string;
    employeeName: string;
    positionId: string;
    positionName: string;
}

export interface ProfileProject {
    id: string;
    name: string;
    description: string;
    status: 'ACTIVE' | 'COMPLETED' | 'PLANNED';
    startDate: string;
    endDate: string | null;
    client: string;
    members: ProjectMember[];
    teamSize: number | null;
    technologies: string[];
}

export interface UserProfile {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    positionId: string;
    position: string;
    locationId: string;
    location: string;
    yearsOfExperience: number;
    availability: 'available' | 'partially_available' | 'unavailable';
    joinDate: string;
    bio: string;
    skills: ProfileSkill[];
    projects: ProfileProject[];
    totalProjects: number;
    activeProjects: number;
    totalSkills: number;
    averageSkillScore: number;
    completionRate: number;
    lastUpdated: string;
}

export interface AddSkillRequest {
    categoryId: string; // UUID
    skillId: string; // UUID
    score: number;
    yearsOfExperience: number;
    lastUsed: string; // LocalDate as ISO string (YYYY-MM-DD)
}

export interface UpdateSkillRequest {
    score: number;
    yearsOfExperience?: number;
    lastUsed?: string; // LocalDate as ISO string (YYYY-MM-DD)
}

export interface UpdateProfileRequest {
    positionId?: string; // UUID
    locationId?: string; // UUID
    availability?: 'available' | 'partially_available' | 'unavailable';
    yearsOfExperience?: number;
    bio?: string;
}

// ============================================================================
// Api Integration
// ============================================================================

export const profileApi = createApi({
    reducerPath: 'profileApi',
    baseQuery: baseQueryWithLocale,
    tagTypes: ['Profile', 'Skills', 'Options'],
    endpoints: (builder) => ({
        // Get profile
        getProfile: builder.query<UserProfile, string>({
            query: (userId = 'me') => ({
                url: `/v1/profiles/${userId}`,
                method: 'GET',
            }),
            providesTags: (_result, _error, userId) =>
                [{ type: 'Profile', id: userId }],
        }),

        // Update profile
        updateProfile: builder.mutation<
            UserProfile,
            { userId: string; data: UpdateProfileRequest }
        >({
            query: ({ userId, data }) => ({
                url: `/v1/profiles/${userId}`,
                method: 'PUT',
                body: data,
            }),
            invalidatesTags: (_result, _error, { userId }) => [{ type: 'Profile', id: userId }],
        }),

        // Add skill
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

        // Update skill
        updateSkill: builder.mutation<
            ProfileSkill,
            { userId: string; skillId: string; data: UpdateSkillRequest }
        >({
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

        // Delete skill
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
