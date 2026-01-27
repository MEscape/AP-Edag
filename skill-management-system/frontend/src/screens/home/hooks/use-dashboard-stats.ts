import {useGetUserStatisticsQuery} from "@/store/api/dashboard-api";

export const useDashboardStats = (userId: string = 'me') => {
    const { data: stats, isLoading, isError, isFetching, refetch } = useGetUserStatisticsQuery(userId);

    return {
        stats,
        isLoading,
        isError,
        isFetching,
        refetch,
    };
};