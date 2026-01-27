import { useGetSkillDevelopmentTrendsQuery } from '@/store/api/dashboard-api';

export const useDashboardOverview = (userId: string = 'me') => {
    const {
        data: skillData,
        isLoading,
        isError,
        isFetching,
        refetch
    } = useGetSkillDevelopmentTrendsQuery(userId);

    return {
        skillDevelopment: skillData?.dataPoints,
        isLoading,
        isError,
        isFetching,
        refetch,
    };
};