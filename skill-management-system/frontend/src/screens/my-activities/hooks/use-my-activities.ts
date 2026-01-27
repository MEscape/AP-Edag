import { useSession } from 'next-auth/react';
import { useGetRecentActivitiesQuery } from '@/store/api/dashboard-api';
import { usePagination } from '@/hooks/use-pagination';

const PAGE_SIZE = 15;

export const useMyActivities = () => {
    const { data: session } = useSession();
    const { page, setPage, onNextPage, onPreviousPage } = usePagination();

    const { data, isLoading, isError, refetch, isFetching } = useGetRecentActivitiesQuery(
        {
            userId: session?.user?.id || '',
            page,
            size: PAGE_SIZE,
        },
        { skip: !session?.user?.id }
    );

    const activities = data?.activities ?? [];
    const metadata = data?.metadata;
    const totalPages = metadata?.totalPages ?? 0;
    const totalElements = metadata?.totalElements ?? 0;

    return {
        activities,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        onNextPage,
        onPreviousPage,
        refetch,
        isFetching,
    };
};