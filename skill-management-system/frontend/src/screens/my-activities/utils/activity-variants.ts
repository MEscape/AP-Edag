import type { Activity } from '@/store/api/dashboard-api';
import {
    Award,
    Briefcase,
    User,
    Shield,
    ShieldCheck,
    ShieldAlert,
    PlusCircle,
    MinusCircle,
    RefreshCcw,
    XCircle,
    Clock,
} from 'lucide-react';

export const getActivityIcon = (type: Activity['activityType']) => {
    const icons = {
        // Skill-related
        SKILL_ADDED: Award,
        SKILL_UPDATED: Award,
        SKILL_REMOVED: XCircle,

        // Project-related
        PROJECT_CREATED: Briefcase,
        PROJECT_STARTED: Briefcase,
        PROJECT_COMPLETED: Briefcase,
        PROJECT_PLANNED: Briefcase,
        PROJECT_UPDATED: RefreshCcw,
        PROJECT_DELETED: XCircle,

        // Project members
        PROJECT_MEMBER_ADDED: PlusCircle,
        PROJECT_MEMBER_REMOVED: MinusCircle,
        PROJECT_MEMBER_ROLE_CHANGED: RefreshCcw,

        // Project skills/tech
        PROJECT_SKILL_ADDED: Award,
        PROJECT_SKILL_REMOVED: XCircle,

        // Role request events
        ROLE_REQUEST_CREATED: Shield,
        ROLE_REQUEST_APPROVED: ShieldCheck,
        ROLE_REQUEST_REJECTED: ShieldAlert,
        ROLE_REQUEST_CANCELLED: ShieldAlert,
        ROLE_REQUEST_REVIEWED: ShieldCheck,
        ROLE_REQUEST_UPDATED: RefreshCcw,

        // Profile-related
        PROFILE_CREATED: User,
        PROFILE_UPDATED: RefreshCcw,

        // User account-related
        USER_ACCOUNT_CREATED: User,
        USER_EMAIL_CHANGED: RefreshCcw,
        USER_NAME_CHANGED: RefreshCcw,
        USER_ACCOUNT_DELETED: XCircle,
    };

    return icons[type] || Clock;
};

export const getActivityTranslationKey = (type: Activity['activityType']): string => {
    return type.toLowerCase();
};