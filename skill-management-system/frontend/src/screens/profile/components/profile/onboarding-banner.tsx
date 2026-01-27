'use client';

import { useTranslations } from 'next-intl';
import { AlertCircle } from 'lucide-react';

export const OnboardingBanner = () => {
    const t = useTranslations('profile');

    return (
        <div className="flex items-start gap-4 rounded-lg border border-warning bg-warning/10 p-4">
            <div className="flex size-10 shrink-0 items-center justify-center rounded-lg bg-warning/20">
                <AlertCircle className="size-5 text-warning" />
            </div>

            <div className="flex-1 space-y-1">
                <h3 className="text-sm font-semibold">{t('onboarding.title')}</h3>
                <p className="text-sm text-muted-foreground">{t('onboarding.description')}</p>
            </div>
        </div>
    );
};
