'use client';

import { useTranslations } from 'next-intl';
import { Badge } from '@/components/ui/badge';
import { useSession } from 'next-auth/react';

export const RoleBadge = () => {
    const t = useTranslations('dashboard');
    const { data: session } = useSession();

    const roles = session?.user?.roles || [];
    const isAdmin = roles.includes('admin');
    const isManager = roles.includes('manager');

    if (isAdmin) {
        return (
            <Badge
                variant="outline"
                className="border-0 bg-purple-500/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-wide text-purple-700"
            >
                {t('roles.admin')}
            </Badge>
        );
    }

    if (isManager) {
        return (
            <Badge
                variant="outline"
                className="border-0 bg-green-500/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-wide text-green-700"
            >
                {t('roles.manager')}
            </Badge>
        );
    }

    return (
        <Badge
            variant="outline"
            className="border-0 bg-blue-500/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-wide text-blue-700"
        >
            {t('roles.user')}
        </Badge>
    );
};