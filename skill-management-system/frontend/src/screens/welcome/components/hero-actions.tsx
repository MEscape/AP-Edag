'use client';

import { ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useRouter } from '@/libs/i18nNavigation';
import { signIn } from 'next-auth/react';
import { useLocale } from 'next-intl';

type HeroActionsProps = {
    isAuthenticated: boolean;
    getStartedLabel: string;
    signInLabel: string;
    keycloakNote: string;
};

export const HeroActions = ({
                                isAuthenticated,
                                getStartedLabel,
                                signInLabel,
                                keycloakNote,
                            }: HeroActionsProps) => {
    const router = useRouter();
    const locale = useLocale();

    const handleGetStarted = () => {
        if (isAuthenticated) {
            router.push('/dashboard/home');
        } else {
            signIn('keycloak',
                { callbackUrl: '/dashboard/home' },
                { ui_locales: locale }
            );
        }
    };

    return (
        <div className="flex flex-wrap items-center gap-4">
            <Button
                onClick={handleGetStarted}
                size="lg"
                className="h-12 bg-primary px-8 text-base font-semibold hover:bg-primary/90"
            >
                {isAuthenticated ? getStartedLabel : signInLabel}
                <ArrowRight className="ml-2 size-5" />
            </Button>

            {!isAuthenticated && (
                <p className="text-sm text-muted-foreground">
                    {keycloakNote}
                </p>
            )}
        </div>
    );
};