'use client';

import { useEffect, useRef } from 'react';
import { useSession } from 'next-auth/react';
import { useRouter } from 'next/navigation';
import { useGetMyRoleRequestsQuery } from '@/store/api/role-request-api';
import { toast } from 'sonner';
import { useTranslations } from 'next-intl';
import { logger } from '@/libs/logger';

/**
 * Hook that automatically updates the session when a role request is approved.
 */
export function useSessionSync() {
  const t = useTranslations('dashboard.roleRequest');
  const { data: session, update: updateSession } = useSession();
  const router = useRouter();

  const { data: roleRequests } = useGetMyRoleRequestsQuery(undefined, {
    skip: !session?.user,
  });
  const isUpdatingRef = useRef(false);
  const processedRequestRef = useRef<string | null>(null);

  useEffect(() => {
    if (!roleRequests || !session?.user || isUpdatingRef.current) return;

    // Find approved request with role not yet in session
    const pendingRoleUpdate = roleRequests.find(
      (request) =>
        request.status === 'APPROVED' &&
        request.id !== processedRequestRef.current &&
        !session.user.roles.some(role => role.toUpperCase() === request.requestedRole.toUpperCase())
    );

    if (!pendingRoleUpdate) return;

    logger.info('Role approved but not in session, updating...', {
      requestId: pendingRoleUpdate.id,
      requestedRole: pendingRoleUpdate.requestedRole,
      currentRoles: session.user.roles,
    });

    isUpdatingRef.current = true;
    processedRequestRef.current = pendingRoleUpdate.id;

    updateSession()
      .then(() => {
        logger.info('Session updated with new role');
        // Refresh router to sync with middleware
        router.refresh();
        toast.success(t('roleUpdated', { role: pendingRoleUpdate.requestedRole }));
      })
      .catch((error) => {
        logger.error('Failed to update session', { error: String(error) });
        toast.error(t('sessionUpdateError'));
        processedRequestRef.current = null; // Reset on error
      })
      .finally(() => {
        isUpdatingRef.current = false;
      });
  }, [roleRequests, session?.user?.roles, updateSession, router, t]);
}
