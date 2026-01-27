import {useEffect, useState} from 'react';
import { useGetAllUsersQuery, useSearchUsersQuery } from '@/store/api/admin-api';
import { useDebounce } from '@/hooks/use-debounce';
import {usePagination} from "@/hooks/use-pagination";

export const useUsersTable = () => {
    const [search, setSearch] = useState('');
    const debouncedSearch = useDebounce(search, 300);
    const { page, setPage, onNextPage, onPreviousPage, resetPage } = usePagination();

    // Reset page when search changes
    useEffect(() => {
        resetPage();
    }, [debouncedSearch, resetPage]);

    // Use different queries based on whether there's a search term
    const shouldSearch = debouncedSearch.trim().length > 0;

    const getAllUsersResult = useGetAllUsersQuery(
        {
            page,
            size: 10,
            sortBy: 'createdAt',
            sortDirection: 'desc',
        },
        { skip: shouldSearch }
    );

    const searchUsersResult = useSearchUsersQuery(
        {
            searchTerm: debouncedSearch,
            page,
            size: 10,
            sortBy: 'username',
            sortDirection: 'asc',
        },
        { skip: !shouldSearch }
    );

    // Use the appropriate result based on search state
    const { data, isLoading, isError } = shouldSearch ? searchUsersResult : getAllUsersResult;

    const users = data?.users ?? [];
    const metadata = data?.metadata;
    const totalPages = metadata?.totalPages ?? 0;
    const totalElements = metadata?.totalElements ?? 0;

    return {
        users,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        setSearch,
        onNextPage,
        onPreviousPage,
    };
};