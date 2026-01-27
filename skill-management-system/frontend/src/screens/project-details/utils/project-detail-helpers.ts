import type { Project } from '@/store/api/project-api';
import {useTranslations} from "next-intl";

/**
 * Get the Badge variant styling for project status
 */
export const getProjectStatusVariant = (status: Project['status']): string => {
    switch (status) {
        case 'ACTIVE':
            return 'bg-success/10 text-success border-success/20';
        case 'COMPLETED':
            return 'bg-muted text-muted-foreground border-border';
        case 'PLANNED':
            return 'bg-warning/10 text-warning border-warning/20';
        default:
            return 'bg-muted text-muted-foreground border-border';
    }
};

/**
 * Calculate project duration and format it
 */
export const calculateProjectDuration = (
    startDate: string,
    endDate: string | null,
    t: ReturnType<typeof useTranslations<'projectDetail'>>,
): string => {
    const start = new Date(startDate);
    const end = endDate ? new Date(endDate) : new Date();
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    const months = Math.floor(diffDays / 30);
    const days = diffDays % 30;

    if (months === 0) {
        return t('durationDays', { days });
    }
    if (days === 0) {
        return t('durationMonths', { months });
    }
    return t('durationFull', { months, days });
};
