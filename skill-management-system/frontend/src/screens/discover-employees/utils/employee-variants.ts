export const AVAILABILITY_STYLES = {
    available: 'bg-success/10 text-success border-success/20',
    partially_available: 'bg-warning/10 text-warning border-warning/20',
    unavailable: 'bg-muted text-muted-foreground border-border',
} as const;

export const getAvailabilityStyle = (availability: keyof typeof AVAILABILITY_STYLES): string => {
    return AVAILABILITY_STYLES[availability] || AVAILABILITY_STYLES.unavailable;
};
