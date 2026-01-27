import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Check, X } from 'lucide-react';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { getRoleBadgeVariant, getStatusBadgeVariant } from '../../utils/badge-variants';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestCardProps {
    request: RoleRequest;
    showActions?: boolean;
    onReviewClick?: (request: RoleRequest, action: 'APPROVED' | 'REJECTED') => void;
    isReviewing?: boolean;
}

export const RoleRequestCard = ({
                                    request,
                                    showActions = false,
                                    onReviewClick,
                                    isReviewing = false,
                                }: RoleRequestCardProps) => {
    const t = useTranslations('admin.roleRequests');
    const { formatDate } = useFormatLocalizedDate();

    return (
        <Card className="border-border bg-white">
            <CardContent className="p-6">
                <div className="space-y-4">
                    <div className="flex items-start justify-between">
                        <div className="space-y-1">
                            <div className="flex items-center gap-2">
                                <p className="font-semibold text-foreground">{request.username}</p>
                                <Badge variant={getRoleBadgeVariant(request.requestedRole)}>
                                    {request.requestedRole}
                                </Badge>
                            </div>
                            <p className="text-sm text-muted-foreground">{request.email}</p>
                        </div>
                        {!showActions && (
                            <Badge variant={getStatusBadgeVariant(request.status)}>
                                {t(`status.${request.status.toLowerCase()}` as any)}
                            </Badge>
                        )}
                    </div>

                    <div className="space-y-1">
                        <p className="text-sm font-medium text-foreground">{t('reason')}</p>
                        <p className="text-sm text-muted-foreground">{request.reason}</p>
                    </div>

                    {request.adminComment && (
                        <div className="space-y-1 rounded-lg bg-muted/50 p-3">
                            <p className="text-sm font-medium text-foreground">{t('adminComment')}</p>
                            <p className="text-sm text-muted-foreground">{request.adminComment}</p>
                        </div>
                    )}

                    <div className="flex flex-wrap gap-4 text-xs text-muted-foreground">
                        <span>{t('requestedOn', { date: formatDate(request.createdAt) })}</span>
                        {request.reviewedAt && request.reviewerUsername && (
                            <span>
                {t('reviewedBy', {
                    reviewer: request.reviewerUsername,
                    date: formatDate(request.reviewedAt),
                })}
              </span>
                        )}
                    </div>

                    {showActions && onReviewClick && (
                        <div className="flex gap-2 pt-2">
                            <Button
                                size="sm"
                                variant="default"
                                className="flex-1 gap-2"
                                onClick={() => onReviewClick(request, 'APPROVED')}
                                disabled={isReviewing}
                            >
                                <Check className="size-4" />
                                {t('approve')}
                            </Button>
                            <Button
                                size="sm"
                                variant="destructive"
                                className="flex-1 gap-2"
                                onClick={() => onReviewClick(request, 'REJECTED')}
                                disabled={isReviewing}
                            >
                                <X className="size-4" />
                                {t('reject')}
                            </Button>
                        </div>
                    )}
                </div>
            </CardContent>
        </Card>
    );
};