import type { Middleware, UnknownAction } from '@reduxjs/toolkit';
import { Env } from '@/libs/env';
import { logger } from '@/libs/logger';

export const logtapeReduxMiddleware: Middleware = (store) => (next) => (action) => {
  // Only log in development
  if (Env.NODE_ENV !== 'development') {
    return next(action);
  }

  const startTime = performance.now();

  // Get previous state
  const prevState = store.getState();

  // Type guard to check if action has expected properties
  const typedAction = action as UnknownAction & {
    payload?: unknown;
    meta?: unknown;
  };

  // Log action
  logger.debug('Action dispatched', {
    type: typedAction.type,
    payload: typedAction.payload,
    meta: typedAction.meta,
  });

  // Execute action
  const result = next(action);

  // Get next state
  const nextState = store.getState();
  const duration = performance.now() - startTime;

  // Log state change
  logger.debug('State updated', {
    action: typedAction.type,
    duration: `${duration.toFixed(2)}ms`,
    // Only log changed slices to avoid noise
    changes: getStateChanges(prevState, nextState),
  });

  return result;
};

// Helper to detect state changes
function getStateChanges(prevState: any, nextState: any): string[] {
  const changes: string[] = [];

  for (const key of Object.keys(nextState)) {
    if (prevState[key] !== nextState[key]) {
      changes.push(key);
    }
  }

  return changes;
}
