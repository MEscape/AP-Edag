'use client';

import { useTranslations } from 'next-intl';
import { Loader2, Trash2 } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import {
  getRoleRequestStatusIcon,
  getRoleRequestRoleBadgeVariant,
  getRoleRequestStatusBadgeVariant,
} from '../../utils/role-variants';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestPendingItemProps {
  request: RoleRequest;
  isCancelling: boolean;
  onCancel: (requestId: string) => Promise<{ success: boolean; error?: string }>;
}

export const RoleRequestPendingItem = ({
                                         request,
                                         isCancelling,
                                         onCancel,
                                       }: RoleRequestPendingItemProps) => {
  const t = useTranslations('dashboard');
  const { formatDate } = useFormatLocalizedDate();
  const StatusIcon = getRoleRequestStatusIcon(request.status);

  return (
      <div className="rounded-lg border border-border bg-white p-4 shadow-sm transition-all hover:border-primary/20 hover:shadow-md">
        <div className="flex items-start justify-between gap-4">
          <div className="flex-1 space-y-3">
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

            <p className="text-xs text-muted-foreground">
              {t('roleRequest.requestedOn')}{' '}
              {formatDate(request.createdAt, { day: '2-digit', month: 'short', year: 'numeric' })}
            </p>
          </div>

          <Button
              variant="ghost"
              size="sm"
              onClick={() => onCancel(request.id)}
              disabled={isCancelling}
              className="shrink-0 text-destructive hover:bg-destructive/10 hover:text-destructive"
          >
            {isCancelling ? (
                <Loader2 className="size-4 animate-spin" />
            ) : (
                <>
                  <Trash2 className="mr-2 size-4" />
                  {t('roleRequest.cancel')}
                </>
            )}
          </Button>
        </div>
      </div>
  );
};