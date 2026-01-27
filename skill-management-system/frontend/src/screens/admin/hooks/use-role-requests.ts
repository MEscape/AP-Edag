import { useGetRoleRequestsByStatusQuery } from '@/store/api/admin-api';
import { usePagination } from "@/hooks/use-pagination";

type RequestStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export const useRoleRequests = (status: RequestStatus) => {
    const { page, setPage, onNextPage, onPreviousPage, resetPage } = usePagination();

    const { data, isLoading, isError } = useGetRoleRequestsByStatusQuery({
        status,
        page,
        size: 10,
        sortBy: 'createdAt',
        sortOrder: 'desc',
    });

    const requests = data?.requests ?? [];
    const metadata = data?.metadata;
    const totalPages = metadata?.totalPages ?? 0;
    const totalElements = metadata?.totalElements ?? 0;

    return {
        requests,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        onNextPage,
        onPreviousPage,
        resetPage,
    };
};