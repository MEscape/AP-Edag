export const getProjectStatusColor = (status: string): string => {
    switch (status.toUpperCase()) {
        case 'ACTIVE':
            return 'bg-success/10 text-success border-success/20';
        case 'COMPLETED':
            return 'bg-muted text-muted-foreground border-border';
        case 'PLANNED':
            return 'bg-primary/10 text-primary border-primary/20';
        default:
            return 'bg-muted text-muted-foreground border-border';
    }
};

export const getProjectStatusTranslationKey = (status: string): string => {
    return status.toLowerCase();
};