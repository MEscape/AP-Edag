import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithLocale } from '@/store/api/base-api';

// ============================================================================
// Type Definitions
// ============================================================================

export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  createdAt: string;
  updatedAt: string;
}

export interface PaginatedUsers {
  users: User[];
  metadata: PageMetadata;
}

export interface PageMetadata {
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface RoleRequest {
  id: string;
  userId: string;
  username: string;
  email: string;
  requestedRole: 'USER' | 'MANAGER' | 'ADMIN';
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  reason: string;
  reviewedBy: string | null;
  reviewerUsername: string | null;
  reviewedAt: string | null;
  adminComment: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface PaginatedRoleRequests {
  requests: RoleRequest[];
  metadata: PageMetadata;
}

export interface ReviewRoleRequestRequest {
  adminComment?: string;
}

export interface GetUsersParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}

export interface SearchUsersParams {
  searchTerm: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}

export interface GetRoleRequestsParams {
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  page?: number;
  size?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

// ============================================================================
// RTK Query API
// ============================================================================

export const adminApi = createApi({
  reducerPath: 'adminApi',
  baseQuery: baseQueryWithLocale,
  tagTypes: ['User', 'RoleRequest'],
  endpoints: (builder) => ({
    // ========== User Management ==========
    getAllUsers: builder.query<PaginatedUsers, GetUsersParams | undefined>({
      query: (params) => ({
        url: '/v1/admin/users',
        params: {
          page: params?.page ?? 0,
          size: params?.size ?? 10,
          sortBy: params?.sortBy ?? 'createdAt',
          sortDirection: params?.sortDirection ?? 'desc',
        },
      }),
      providesTags: ['User'],
    }),

    searchUsers: builder.query<PaginatedUsers, SearchUsersParams>({
      query: ({ searchTerm, page = 0, size = 10, sortBy = 'username', sortDirection = 'asc' }) => ({
        url: '/v1/admin/users/search',
        params: { searchTerm, page, size, sortBy, sortDirection },
      }),
      providesTags: ['User'],
    }),

    // ========== Role Request Management ==========
    getRoleRequestsByStatus: builder.query<PaginatedRoleRequests, GetRoleRequestsParams | undefined>({
      query: (params) => ({
        url: '/v1/role-requests',
        params: {
          status: params?.status,
          page: params?.page ?? 0,
          size: params?.size ?? 20,
          sortBy: params?.sortBy ?? 'createdAt',
          sortOrder: params?.sortOrder ?? 'desc',
        },
      }),
      providesTags: ['RoleRequest'],
    }),

    approveRoleRequest: builder.mutation<RoleRequest, { requestId: string; body?: ReviewRoleRequestRequest }>({
      query: ({ requestId, body }) => ({
        url: `/v1/role-requests/${requestId}/approve`,
        method: 'POST',
        body: body ?? {},
      }),
      invalidatesTags: ['RoleRequest', 'User'],
    }),

    rejectRoleRequest: builder.mutation<RoleRequest, { requestId: string; body?: ReviewRoleRequestRequest }>({
      query: ({ requestId, body }) => ({
        url: `/v1/role-requests/${requestId}/reject`,
        method: 'POST',
        body: body ?? {},
      }),
      invalidatesTags: ['RoleRequest', 'User'],
    }),
  }),
});

export const {
  useGetAllUsersQuery,
  useSearchUsersQuery,
  useGetRoleRequestsByStatusQuery,
  useApproveRoleRequestMutation,
  useRejectRoleRequestMutation,
} = adminApi;