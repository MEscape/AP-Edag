'use client';

import { useTranslations } from 'next-intl';
import { Shield } from 'lucide-react';
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from '@/components/ui/card';
import { RoleRequestPendingItem } from './role-request-pending-item';
import { RoleRequestHistoryItem } from './role-request-history-item';
import { EmptyState } from '@/components/ui/empty-state';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestListProps {
    requests: RoleRequest[];
    pendingRequests: RoleRequest[];
    historyRequests: RoleRequest[];
    isLoading: boolean;
    isError: boolean;
    isCancelling: boolean;
    onCancel: (requestId: string) => Promise<{ success: boolean; error?: string }>;
}

export const RoleRequestList = ({
                                    requests,
                                    pendingRequests,
                                    historyRequests,
                                    isLoading,
                                    isError,
                                    isCancelling,
                                    onCancel,
                                }: RoleRequestListProps) => {
    const t = useTranslations('dashboard');

    if (isLoading || !requests || requests.length === 0) {
        return null;
    }

    if (isError) {
        return (
            <Card className="border-border bg-white">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                        <Shield className="size-5 text-primary" />
                        {t('roleRequest.myRequests')}
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <p className="text-center text-muted-foreground">{t('roleRequest.loadError')}</p>
                </CardContent>
            </Card>
        );
    }

    return (
        <Card className="border-border bg-white">
            <CardHeader>
                <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                    {t('roleRequest.myRequests')}
                </CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('roleRequest.myRequestsDescription')}
                </CardDescription>
            </CardHeader>
            <CardContent>
                {requests.length === 0 ? (
                    <EmptyState icon={Shield} message={t('roleRequest.noRequests')} />
                ) : (
                    <div className="space-y-4">
                        {pendingRequests.length > 0 && (
                            <div className="space-y-3">
                                <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">
                                    {t('roleRequest.pending')}
                                </h3>
                                {pendingRequests.map((request) => (
                                    <RoleRequestPendingItem
                                        key={request.id}
                                        request={request}
                                        isCancelling={isCancelling}
                                        onCancel={onCancel}
                                    />
                                ))}
                            </div>
                        )}

                        {historyRequests.length > 0 && (
                            <div className="space-y-3">
                                <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">
                                    {t('roleRequest.history')}
                                </h3>
                                {historyRequests.map((request) => (
                                    <RoleRequestHistoryItem key={request.id} request={request} />
                                ))}
                            </div>
                        )}
                    </div>
                )}
            </CardContent>
        </Card>
    );
};
