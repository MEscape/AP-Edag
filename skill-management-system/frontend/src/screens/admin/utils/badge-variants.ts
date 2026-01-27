export const getRoleBadgeVariant = (role: string): 'destructive' | 'default' | 'secondary' => {
    switch (role.toLowerCase()) {
        case 'admin':
            return 'destructive';
        case 'managers':
            return 'default';
        default:
            return 'secondary';
    }
};

export const getStatusBadgeVariant = (status: string): 'default' | 'destructive' | 'secondary' => {
    switch (status.toUpperCase()) {
        case 'APPROVED':
            return 'default';
        case 'REJECTED':
            return 'destructive';
        case 'PENDING':
            return 'secondary';
        default:
            return 'secondary';
    }
};