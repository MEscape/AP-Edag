import type { Action, Middleware } from '@reduxjs/toolkit';
import { isRejectedWithValue } from '@reduxjs/toolkit';
import { Env } from '@/libs/env';
import { logger } from '@/libs/logger';

type RTKQueryMeta = {
  arg?: {
    endpointName?: string;
  };
};

type RTKQueryError = {
  status?: number | string;
  data?: unknown;
};

type RTKQueryAction = {
  meta?: RTKQueryMeta;
  payload?: RTKQueryError;
} & Action;

export const rtkQueryLoggerMiddleware: Middleware = () => (next) => (action) => {
  const typedAction = action as RTKQueryAction;

  // Only log in development or if it's an error
  const shouldLog = Env.NODE_ENV === 'development' || isRejectedWithValue(typedAction);

  if (shouldLog) {
    // Log API errors
    if (isRejectedWithValue(typedAction)) {
      logger.error('API Error', {
        endpoint: typedAction.meta?.arg?.endpointName,
        error: typedAction.payload,
        status: typedAction.payload?.status,
      });
    } else if (Env.NODE_ENV === 'development') {
      // Log API requests (development only)
      if (typedAction.type?.includes('executeQuery/pending')) {
        logger.debug('API Request', {
          endpoint: typedAction.meta?.arg?.endpointName,
          type: 'query',
        });
      } else if (typedAction.type?.includes('executeMutation/pending')) {
        logger.debug('API Mutation', {
          endpoint: typedAction.meta?.arg?.endpointName,
          type: 'mutation',
        });
      }
    }
  }

  return next(action);
};
