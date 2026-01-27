import type { Project } from '@/store/api/project-api';

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
