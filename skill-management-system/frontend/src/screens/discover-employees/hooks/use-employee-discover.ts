import { useState, useCallback } from 'react';
import {
    useSearchEmployeesQuery,
    useGetFilterOptionsQuery,
    type EmployeeSearchFilters
} from '@/store/api/employee-api';
import { usePagination } from '@/hooks/use-pagination';

export const useEmployeeDiscover = () => {
    const [filters, setFilters] = useState<EmployeeSearchFilters>({});
    const [sortBy, setSortBy] = useState<'name' | 'experience' | 'projects' | 'skills'>('name');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

    const { page, setPage, onNextPage, onPreviousPage, resetPage } = usePagination();
    const pageSize = 12;

    // API Queries
    const {
        data: employeeData,
        isLoading,
        isFetching,
        isError,
        error,
        refetch,
    } = useSearchEmployeesQuery({
        page,
        size: pageSize,
        filters,
        sortBy,
        sortOrder,
    });

    const {
        data: filterOptions,
        isLoading: isLoadingFilters,
    } = useGetFilterOptionsQuery();

    // Filter Actions
    const updateFilters = useCallback((newFilters: Partial<EmployeeSearchFilters>) => {
        setFilters((prev) => ({ ...prev, ...newFilters }));
        resetPage();
    }, [resetPage]);

    const clearFilters = useCallback(() => {
        setFilters({});
        resetPage();
    }, [resetPage]);

    const setSearchTerm = useCallback((searchTerm: string) => {
        updateFilters({ searchTerm: searchTerm.trim() || undefined });
    }, [updateFilters]);

    // Sorting
    const changeSorting = useCallback((newSortBy: typeof sortBy, newSortOrder?: typeof sortOrder) => {
        setSortBy(newSortBy);
        setSortOrder(newSortOrder || (sortOrder === 'asc' ? 'desc' : 'asc'));
    }, [sortOrder]);

    // Computed values
    const hasActiveFilters = Object.values(filters).some((value) =>
        Array.isArray(value) ? value.length > 0 : value !== undefined
    );

    return {
        // Data
        employees: employeeData?.employees ?? [],
        totalElements: employeeData?.metadata.totalElements ?? 0,
        totalPages: employeeData?.metadata.totalPages ?? 0,
        page,
        pageSize,
        filterOptions,

        // Current State
        filters,
        sortBy,
        sortOrder,

        // Loading States
        isLoading: isLoading || isLoadingFilters,
        isFetching,
        isError,
        error,

        // Actions
        updateFilters,
        clearFilters,
        setSearchTerm,
        changeSorting,
        setPage,
        onNextPage,
        onPreviousPage,
        refetch,

        // Computed
        hasActiveFilters,
    };
};