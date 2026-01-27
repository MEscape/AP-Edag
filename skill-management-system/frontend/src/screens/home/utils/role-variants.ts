import type { RoleRequest } from '@/store/api/role-request-api';
import { Clock, CheckCircle, XCircle } from 'lucide-react';

export const getRoleRequestStatusBadgeVariant = (
    status: RoleRequest['status']
): 'default' | 'destructive' | 'secondary' => {
    switch (status) {
        case 'APPROVED':
            return 'default';
        case 'REJECTED':
            return 'destructive';
        case 'PENDING':
        default:
            return 'secondary';
    }
};

export const getRoleRequestRoleBadgeVariant = (
    role: RoleRequest['requestedRole']
): 'default' | 'destructive' | 'secondary' => {
    switch (role) {
        case 'ADMIN':
            return 'destructive';
        case 'MANAGER':
            return 'default';
        default:
            return 'secondary';
    }
};

export const getRoleRequestStatusIcon = (status: RoleRequest['status']) => {
    const icons = {
        PENDING: Clock,
        APPROVED: CheckCircle,
        REJECTED: XCircle,
    };
    return icons[status];
};
