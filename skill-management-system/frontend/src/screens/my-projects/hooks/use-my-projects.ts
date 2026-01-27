import { useSession } from 'next-auth/react';
import { useGetUserProjectsQuery } from '@/store/api/project-api';
import { usePagination } from '@/hooks/use-pagination';

const PAGE_SIZE = 12;

export const useMyProjects = () => {
    const { data: session } = useSession();
    const { page, setPage, onNextPage, onPreviousPage } = usePagination();

    const { data, isLoading, isError, refetch, isFetching } = useGetUserProjectsQuery(
        {
            userId: session?.user?.id || '',
            page,
            size: PAGE_SIZE,
        },
        { skip: !session?.user?.id }
    );

    const projects = data?.projects ?? [];
    const metadata = data?.metadata;
    const totalPages = metadata?.totalPages ?? 0;
    const totalElements = metadata?.totalElements ?? 0;

    return {
        projects,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        onNextPage,
        onPreviousPage,
        refetch,
        isFetching
    };
};