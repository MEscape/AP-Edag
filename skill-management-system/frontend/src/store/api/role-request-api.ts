import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export type RoleType = 'USER' | 'MANAGER' | 'ADMIN';
export type RequestStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface RoleRequest {
  id: string;
  userId: string;
  username: string;
  email: string;
  requestedRole: RoleType;
  status: RequestStatus;
  reason: string;
  reviewedBy: string | null;
  reviewerUsername: string | null;
  reviewedAt: string | null;
  adminComment: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateRoleRequestRequest {
  requestedRole: RoleType;
  reason: string;
}

// ============================================================================
// RTK Query API
// ============================================================================

export const roleRequestApi = createApi({
  reducerPath: 'roleRequestApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['RoleRequest'],
  endpoints: (builder) => ({
    // Get my role requests
    getMyRoleRequests: builder.query<RoleRequest[], void>({
      query: () => '/v1/role-requests/me',
      providesTags: ['RoleRequest'],
    }),

    // Create role request
    createRoleRequest: builder.mutation<RoleRequest, CreateRoleRequestRequest>({
      query: (body) => ({
        url: '/v1/role-requests',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['RoleRequest'],
    }),

    // Cancel role request
    cancelRoleRequest: builder.mutation<void, string>({
      query: (requestId) => ({
        url: `/v1/role-requests/${requestId}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['RoleRequest'],
    }),
  }),
});

export const {
  useGetMyRoleRequestsQuery,
  useCreateRoleRequestMutation,
  useCancelRoleRequestMutation,
} = roleRequestApi;
