import { useState } from 'react';
import { useDashboardStats } from './use-dashboard-stats';
import { useDashboardOverview } from './use-dashboard-overview';
import { useRoleRequests } from './use-role-requests';
import { useRecentActivities } from './use-recent-activities';

export const useDashboardRefresh = () => {
    const [isRefreshing, setIsRefreshing] = useState(false);

    const stats = useDashboardStats();
    const overview = useDashboardOverview();
    const roleRequests = useRoleRequests();
    const activities = useRecentActivities();

    const refreshAll = async () => {
        setIsRefreshing(true);

        await Promise.all([
            stats.refetch(),
            overview.refetch(),
            roleRequests.refetch(),
            activities.refetch(),
        ]);

        setIsRefreshing(false);
    };

    return {
        isRefreshing,
        refreshAll,
    };
};