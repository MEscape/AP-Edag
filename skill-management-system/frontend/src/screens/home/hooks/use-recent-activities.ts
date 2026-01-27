import {
    useGetRecentProjectsQuery,
    useGetRecentActivitiesQuery
} from '@/store/api/dashboard-api';

export const useRecentActivities = (
    userId: string = 'me',
    projectsSize: number = 6,
    activitiesSize: number = 6
) => {
    const {
        data: projectsData,
        isLoading: isLoadingProjects,
        isError: isErrorProjects,
        refetch: refetchProjects,
        isFetching: isFetchingProjects,
    } = useGetRecentProjectsQuery({
        userId,
        page: 0,
        size: projectsSize,
    });

    const {
        data: activitiesData,
        isLoading: isLoadingActivities,
        isError: isErrorActivities,
        refetch: refetchActivities,
        isFetching: isFetchingActivities,
    } = useGetRecentActivitiesQuery({
        userId,
        page: 0,
        size: activitiesSize,
    });

    const refetch = async () => {
        refetchProjects();
        refetchActivities();
    }

    return {
        projects: projectsData?.projects,
        activities: activitiesData?.activities,
        isLoadingProjects,
        isLoadingActivities,
        isError: isErrorProjects || isErrorActivities,
        refetch,
        isFetching: isFetchingActivities || isFetchingProjects,
    };
};