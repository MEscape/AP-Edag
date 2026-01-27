'use client';

import { useSession, signOut } from 'next-auth/react';
import React, { useEffect } from 'react';
import {logger} from "@/libs/logger";
import { useSessionSync } from '@/hooks/use-session-sync';

type SessionCheckerProps = {
    readonly children: React.ReactNode;
};

/**
 * Hook to automatically sign out users when token refresh fails
 * Add this to your root layout or a wrapper component
 */
export function useSessionChecker() {
    const { data: session, status } = useSession();

    useEffect(() => {
        if (status === 'authenticated' && session?.error === 'RefreshAccessTokenError') {
            logger.warn('Session expired or token refresh failed. Signing out...');

            // Sign out and redirect to sign in page
            signOut({
                callbackUrl: '/',
                redirect: true
            });
        }
    }, [session, status]);

    return { session, status };
}

export function SessionChecker({ children }: SessionCheckerProps) {
    useSessionChecker();
    useSessionSync();
    return <>{children}</>;
}
