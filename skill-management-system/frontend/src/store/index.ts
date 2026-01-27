import { configureStore } from '@reduxjs/toolkit';
import { Env } from '@/libs/env';
import { rtkQueryLoggerMiddleware } from '@/store/middleware/api-logger';
import { logtapeReduxMiddleware } from '@/store/middleware/logger';
import { dashboardApi } from '@/store/api/dashboard-api';
import { employeeApi } from '@/store/api/employee-api';
import { profileApi } from '@/store/api/profile-api';
import { projectApi } from '@/store/api/project-api';
import { roleRequestApi } from '@/store/api/role-request-api';
import { adminApi } from '@/store/api/admin-api';
import {optionApi} from "@/store/api/option-api";

export const store = configureStore({
  reducer: {
    // RTK Query APIs
    [dashboardApi.reducerPath]: dashboardApi.reducer,
    [employeeApi.reducerPath]: employeeApi.reducer,
    [profileApi.reducerPath]: profileApi.reducer,
    [projectApi.reducerPath]: projectApi.reducer,
    [roleRequestApi.reducerPath]: roleRequestApi.reducer,
    [adminApi.reducerPath]: adminApi.reducer,
    [optionApi.reducerPath]: optionApi.reducer,
  },

  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: {
          ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'],
          ignoredPaths: ['api.queries', 'api.mutations'],
        },
      })
          .concat(dashboardApi.middleware) // Dashboard API middleware
          .concat(employeeApi.middleware) // Employee API middleware
          .concat(profileApi.middleware) // Profile API middleware
          .concat(projectApi.middleware) // Project API middleware
          .concat(roleRequestApi.middleware) // Role Request API middleware
          .concat(adminApi.middleware) // Admin API middleware
          .concat(optionApi.middleware) // Option API middleware
          .concat(rtkQueryLoggerMiddleware) // RTK Query specific logging
          .concat(logtapeReduxMiddleware), // General Redux logging

  devTools: Env.NODE_ENV === 'development',
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
