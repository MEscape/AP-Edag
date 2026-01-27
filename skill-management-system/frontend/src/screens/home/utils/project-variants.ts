import type { Project } from '@/store/api/dashboard-api';

export const getProjectStatusColor = (status: Project['status']) => {
    const colors = {
        ACTIVE: 'bg-success/10 text-success hover:bg-success/20 border-success/20',
        COMPLETED: 'bg-muted text-muted-foreground hover:bg-muted/80 border-border',
        PLANNED: 'bg-primary/10 text-primary hover:bg-primary/20 border-primary/20',
    };
    return colors[status] || colors.COMPLETED;
};
