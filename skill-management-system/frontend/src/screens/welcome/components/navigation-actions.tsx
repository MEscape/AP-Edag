'use client';

import { ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useRouter } from '@/libs/i18nNavigation';
import { signIn } from 'next-auth/react';
import { useLocale } from 'next-intl';

type NavigationActionsProps = {
    isAuthenticated: boolean;
    toDashboardLabel: string;
    signInLabel: string;
};

export const NavigationActions = ({
                                      isAuthenticated,
                                      toDashboardLabel,
                                      signInLabel,
                                  }: NavigationActionsProps) => {
    const router = useRouter();
    const locale = useLocale();

    const handleSignIn = () => {
        signIn('keycloak',
            { callbackUrl: '/dashboard/home' },
            { ui_locales: locale }
        );
    };

    const handleNavigateToDashboard = () => {
        router.push('/dashboard/home');
    };

    if (isAuthenticated) {
        return (
            <Button
                onClick={handleNavigateToDashboard}
                size="sm"
                className="bg-primary font-medium hover:bg-primary/90"
            >
                {toDashboardLabel}
                <ArrowRight className="ml-1.5 size-4" />
            </Button>
        );
    }

    return (
        <Button
            onClick={handleSignIn}
            variant="outline"
            size="sm"
            className="font-medium"
        >
            {signInLabel}
        </Button>
    );
};