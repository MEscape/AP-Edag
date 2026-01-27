import { useState, useEffect } from 'react';
import { useDebounce } from '@/hooks/use-debounce';
import { usePagination } from '@/hooks/use-pagination';
import {
    useSearchProjectsQuery,
    type ProjectSearchFilters,
} from '@/store/api/project-api';

export const useProjectDiscover = () => {
    const [search, setSearch] = useState('');
    const [filters, setFilters] = useState<ProjectSearchFilters>({});
    const debouncedSearch = useDebounce(search, 300);
    const { page, setPage, onNextPage, onPreviousPage, resetPage } = usePagination();

    // Reset page when search or filters change
    useEffect(() => {
        resetPage();
    }, [debouncedSearch, filters, resetPage]);

    const combinedFilters = {
        ...filters,
        searchTerm: debouncedSearch || undefined,
    };

    const { data, isLoading, isFetching, isError, refetch } = useSearchProjectsQuery({
        filters: combinedFilters,
        page,
        size: 20,
        sortBy: 'startDate',
        sortOrder: 'desc',
    });

    const projects = data?.projects ?? [];
    const metadata = data?.metadata;
    const totalPages = metadata?.totalPages ?? 0;
    const totalElements = metadata?.totalElements ?? 0;

    const hasActiveFilters =
        Boolean(search) ||
        (filters.statusList ?? []).length > 0 ||
        (filters.employeeIds ?? []).length > 0 ||
        (filters.skillIds ?? []).length > 0;

    const updateFilters = (newFilters: Partial<ProjectSearchFilters>) => {
        setFilters((prev) => ({ ...prev, ...newFilters }));
    };

    const clearFilters = () => {
        setSearch('');
        setFilters({});
    };

    return {
        projects,
        totalPages,
        totalElements,
        page,
        isLoading,
        isFetching,
        isError,
        filters,
        hasActiveFilters,
        setPage,
        setSearch,
        onNextPage,
        onPreviousPage,
        updateFilters,
        clearFilters,
        refetch,
    };
};