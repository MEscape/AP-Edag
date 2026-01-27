'use client';

import { useTranslations } from 'next-intl';
import { ErrorState } from '@/components/ui/error-state';
import { Searchbar } from '@/components/ui/searchbar';
import { useEmployeeDiscover } from './hooks/use-employee-discover';
import { useSelectionMode } from './hooks/use-selection-mode';
import { EmployeeHeader } from './components/header/employee-header';
import { SelectionBanner } from './components/header/selection-banner';
import { FilterPanel } from './components/filters/filter-panel';
import { EmployeeGrid } from './components/employees/employee-grid';
import {useFilterState} from "@/hooks/use-filter-state";
import {EmployeePagination} from "@/screens/discover-employees/components/employees/employee-pagination";

export default function EmployeeDiscoverPage() {
    const t = useTranslations('discoverEmployees');

    // Data & Filter State
    const {
        employees,
        totalElements,
        totalPages,
        page,
        filterOptions,
        filters,
        isLoading,
        isFetching,
        isError,
        updateFilters,
        clearFilters,
        setSearchTerm,
        onNextPage,
        onPreviousPage,
        setPage,
        refetch,
        hasActiveFilters,
    } = useEmployeeDiscover();

    // Selection Mode State
    const {
        isAnySelectionMode,
        isFilterMode,
        selectedCount,
        confirmSelection,
        cancelSelection,
        toggleEmployee,
        isEmployeeSelected
    } = useSelectionMode();

    // UI State
    const { showFilters, toggleFilters } = useFilterState();

    // Error State
    if (isError) {
        return (
            <ErrorState
                title={t('error')}
                description={t('errorDescription')}
                onRetry={refetch}
                isRetrying={isFetching}
            />
        );
    }

    return (
        <div className="min-h-screen bg-muted/30">
            <div className="pointer-events-none absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />

            <div className="relative mx-auto max-w-[1440px] space-y-8 px-6 py-8 lg:px-16 lg:py-12">
                <div className="space-y-8">
                    <EmployeeHeader
                        onRefresh={refetch}
                        isRefreshing={isFetching}
                        hasActiveFilters={hasActiveFilters}
                        onClearFilters={clearFilters}
                        onToggleFilters={toggleFilters}
                        showFilters={showFilters}
                        totalResults={totalElements}
                    />

                    {isAnySelectionMode && (
                        <SelectionBanner
                            isFilterMode={isFilterMode}
                            selectedCount={selectedCount}
                            onCancel={cancelSelection}
                            onConfirm={confirmSelection}
                        />
                    )}

                    <Searchbar
                        onSearch={setSearchTerm}
                        isLoading={isLoading}
                        placeholder={t('searchPlaceholder')}
                    />

                    <div className="grid gap-6 lg:grid-cols-4">
                        {showFilters && (
                            <aside className="lg:col-span-1">
                                <FilterPanel
                                    filterOptions={filterOptions}
                                    currentFilters={filters}
                                    onFilterChange={updateFilters}
                                    isLoading={isLoading}
                                />
                            </aside>
                        )}

                        <div className={showFilters ? 'lg:col-span-3' : 'lg:col-span-4'}>
                            <div className="space-y-6">
                                <EmployeeGrid
                                    employees={employees}
                                    isLoading={isLoading}
                                    isAnySelectionMode={isAnySelectionMode}
                                    toggleEmployee={toggleEmployee}
                                    isEmployeeSelected={isEmployeeSelected}
                                />

                                {totalPages > 1 && !isLoading && (
                                    <EmployeePagination
                                        page={page}
                                        totalPages={totalPages}
                                        totalElements={totalElements}
                                        onPageChange={setPage}
                                        onNextPage={onNextPage}
                                        onPreviousPage={onPreviousPage}
                                        isLoading={isFetching}
                                    />
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}