import { useGetProjectByIdQuery } from '@/store/api/project-api';

export const useProjectDetail = (projectId: string) => {
    const { data: project, isLoading, isError, refetch, isFetching } = useGetProjectByIdQuery(projectId, {
        skip: !projectId,
    });

    return {
        project,
        isLoading,
        isError,
        refetch,
        isFetching,
    };
};