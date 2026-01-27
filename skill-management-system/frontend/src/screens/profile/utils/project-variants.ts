import { Loader2, CheckCircle2, Clock } from 'lucide-react';
import type { ProfileProject } from '@/store/api/profile-api';
import type { LucideIcon } from 'lucide-react';

type ProjectStatus = ProfileProject['status'];

interface ProjectStatusConfig {
    color: string;
    icon: LucideIcon;
}

const PROJECT_STATUS_CONFIG: Record<ProjectStatus, ProjectStatusConfig> = {
    ACTIVE: {
        color: 'bg-success/10 text-success border-success/20 hover:bg-success/10 gap-1.5',
        icon: Loader2,
    },
    COMPLETED: {
        color: 'bg-muted text-muted-foreground border-border hover:bg-muted/10 gap-1.5',
        icon: CheckCircle2,
    },
    PLANNED: {
        color: 'bg-warning/10 text-warning border-warning/20 hover:bg-warning/10 gap-1.5',
        icon: Clock,
    },
};

export const getProjectStatusConfig = (status: ProjectStatus): ProjectStatusConfig => {
    return PROJECT_STATUS_CONFIG[status];
};