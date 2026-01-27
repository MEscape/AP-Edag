import {UserProfile} from "@/store/api/profile-api";

export const AVAILABILITY_STYLES: Record<UserProfile["availability"], string> = {
    available:
        'bg-success/10 text-success border-success/20 hover:bg-success/10 gap-1.5 px-3 py-1',
    partially_available:
        'bg-warning/10 text-warning border-warning/20 hover:bg-warning/10 gap-1.5 px-3 py-1',
    unavailable:
        'bg-muted text-muted-foreground border-border hover:bg-muted/10 gap-1.5 px-3 py-1',
};