'use client';

import { useTranslations } from 'next-intl';
import { useSession } from 'next-auth/react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { QuickActionsList } from './quick-actions-list';
import type { RoleType } from '@/store/api/role-request-api';

interface QuickActionsProps {
    onCreateRoleRequest: (params: {
        requestedRole: RoleType;
        reason: string;
    }) => Promise<{ success: boolean; error?: string }>;
    isCreating: boolean;
}

export const QuickActions = ({ onCreateRoleRequest, isCreating }: QuickActionsProps) => {
    const t = useTranslations('dashboard.quickActions');
    const { data: session } = useSession();
    const userRoles = session?.user?.roles || [];

    return (
        <Card className="border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg">
            <CardHeader>
                <CardTitle className="text-xl font-bold text-foreground">{t('title')}</CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('description')}
                </CardDescription>
            </CardHeader>
            <CardContent>
                <QuickActionsList
                    userRoles={userRoles}
                    onCreateRoleRequest={onCreateRoleRequest}
                    isCreating={isCreating}
                />
            </CardContent>
        </Card>
    );
};