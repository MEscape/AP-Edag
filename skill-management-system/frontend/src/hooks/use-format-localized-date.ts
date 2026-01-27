'use client';

import { useLocale, useTranslations } from 'next-intl';

/**
 * Provides locale-aware date and timestamp formatting utilities.
 */
export const useFormatLocalizedDate = () => {
    const locale = useLocale() || 'de-DE';
    const t = useTranslations('common.time');

    /**
     * Format a standard date based on locale.
     * Example: "Nov 2025" or "Nov. 2025"
     */
    const formatDate = (
        dateString: string,
        options?: Intl.DateTimeFormatOptions
    ) => {
        const resolvedOptions = options ?? { month: 'short', year: 'numeric' };

        const date = new Date(dateString);
        if (Number.isNaN(date.getTime())) return dateString;
        return date.toLocaleDateString(locale, resolvedOptions);
    };

    /**
     * Human-friendly "time ago" style date (localized).
     */
    const formatTimestamp = (timestamp: string) => {
        const date = new Date(timestamp);
        if (Number.isNaN(date.getTime())) return timestamp;

        const now = new Date();
        const diffInHours = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60));

        if (diffInHours < 1) return t('minutesAgo');
        if (diffInHours < 24)
            return t('hoursAgo', { count: diffInHours });
        if (diffInHours < 48) return t('yesterday');

        // fallback formatted date
        return date.toLocaleDateString(locale, {
            day: '2-digit',
            month: 'short',
        });
    };

    return { formatDate, formatTimestamp };
};
