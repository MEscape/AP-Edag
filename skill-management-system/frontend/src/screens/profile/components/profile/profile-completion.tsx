'use client';

import { useTranslations } from 'next-intl';
import { Progress } from '@/components/ui/progress';

export const ProfileCompletion = ({ completionRate }: { completionRate: number }) => {
    const t = useTranslations('profile');

    return (
        <div className="w-full space-y-2 rounded-lg border border-border bg-muted/30 p-4 lg:w-64">
            <div className="flex items-center justify-between text-sm">
                <span className="font-medium">{t('profileCompletion')}</span>
                <span className="font-semibold text-primary">{completionRate}%</span>
            </div>

            <Progress value={completionRate} className="h-2" />

            {completionRate < 100 && (
                <p className="text-xs text-muted-foreground">{t('completeProfileHint')}</p>
            )}
        </div>
    );
};
