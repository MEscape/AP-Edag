'use client';

import { useTranslations } from 'next-intl';
import { Badge } from '@/components/ui/badge';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import {
  getRoleRequestStatusIcon,
  getRoleRequestRoleBadgeVariant,
  getRoleRequestStatusBadgeVariant,
} from '../../utils/role-variants';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestHistoryItemProps {
  request: RoleRequest;
}

export const RoleRequestHistoryItem = ({ request }: RoleRequestHistoryItemProps) => {
  const t = useTranslations('dashboard');
  const { formatDate } = useFormatLocalizedDate();
  const StatusIcon = getRoleRequestStatusIcon(request.status);

  return (
      <div className="rounded-lg border border-border bg-muted/30 p-4">
        <div className="space-y-3">
          <div className="flex flex-wrap items-center gap-2">
            <Badge variant={getRoleRequestRoleBadgeVariant(request.requestedRole)} className="font-semibold">
              {t(`roleRequest.roles.${request.requestedRole.toLowerCase()}` as any)}
            </Badge>
            <Badge variant={getRoleRequestStatusBadgeVariant(request.status)} className="gap-1 font-semibold">
              <StatusIcon className="size-3" />
              {t(`roleRequest.status.${request.status.toLowerCase()}` as any)}
            </Badge>
          </div>

          <div>
            <p className="text-sm font-medium text-muted-foreground">
              {t('roleRequest.yourReason')}
            </p>
            <p className="mt-1 text-sm text-foreground">{request.reason}</p>
          </div>

          {request.adminComment && (
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t('roleRequest.adminComment')}
                </p>
                <p className="mt-1 text-sm text-foreground">{request.adminComment}</p>
              </div>
          )}

          <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-muted-foreground">
            <p>
              {t('roleRequest.requestedOn')}{' '}
              {formatDate(request.createdAt, { day: '2-digit', month: 'short', year: 'numeric' })}
            </p>
            {request.reviewedAt && (
                <p>
                  {t('roleRequest.reviewedOn')}{' '}
                  {formatDate(request.reviewedAt, { day: '2-digit', month: 'short', year: 'numeric' })}
                </p>
            )}
            {request.reviewedBy && (
                <p>
                  {t('roleRequest.reviewedBy')} {request.reviewerUsername}
                </p>
            )}
          </div>
        </div>
      </div>
  );
};