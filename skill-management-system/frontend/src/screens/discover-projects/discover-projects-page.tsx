'use client';

import { useState, useEffect } from 'react';
import { useRouter } from '@/libs/i18nNavigation';
import { useSearchParams } from 'next/navigation';
import { Searchbar } from '@/components/ui/searchbar';
import { ProjectsHeader } from './components/projects/projects-header';
import { FilterPanel } from './components/filters/filter-panel';
import { ProjectGrid } from './components/projects/project-grid';
import { ProjectsPagination } from './components/projects/projects-pagination';
import { ProjectDialog } from './components/dialog/project-dialog';
import { ErrorState } from '@/components/ui/error-state';
import { useProjectDiscover } from './hooks/use-project-discover';
import { useProjectDialog } from './hooks/use-project-dialog';
import { useProjectFilters } from './hooks/use-project-filters';
import { useProjectMemberSelection } from './hooks/use-project-member-selection';
import type { Project } from '@/store/api/project-api';
import { useFilterState } from "@/hooks/use-filter-state";
import { useTranslations } from "next-intl";

export default function ProjectsPage() {
    const t = useTranslations('discoverProjects');
    const router = useRouter();
    const searchParams = useSearchParams();
    const [selectedEmployeesForFilter, setSelectedEmployeesForFilter] =
        useState<Array<{ id: string; name: string }>>([]);

    // Data & filters
    const {
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
    } = useProjectDiscover();

    // Filter options
    const { skillOptions, positionOptions, isLoading: isLoadingFilterOptions } =
        useProjectFilters();

    // Dialog management
    const {
        isOpen,
        mode,
        editingProject,
        isSaving,
        openCreateDialog,
        openEditDialog,
        closeDialog,
        handleCreate,
        handleUpdate,
        handleDelete,
    } = useProjectDialog();

    // Member selection
    const {
        selectedMembers,
        selectMembers,
        removeMember,
        updateMemberPosition,
        clearMembers,
        setMembers,
    } = useProjectMemberSelection();

    // UI State
    const { showFilters, toggleFilters } = useFilterState();

    // Load filter employees from sessionStorage on mount
    useEffect(() => {
        const savedFilterEmployees = sessionStorage.getItem('filterEmployees');
        if (savedFilterEmployees) {
            try {
                const parsed = JSON.parse(savedFilterEmployees);
                const converted = parsed.map((e: any) => ({
                    id: e.employeeId,
                    name: e.employeeName,
                }));
                setSelectedEmployeesForFilter(converted);

                const employeeIds = parsed.map((e: any) => e.employeeId);
                if (employeeIds.length > 0) {
                    updateFilters({ employeeIds });
                }
            } catch (error) {
                console.error('Failed to restore filter employees:', error);
            }
        }
    }, [updateFilters]);

    // Handle return from filter employee selection
    useEffect(() => {
        const fromFilter = searchParams.get('fromFilter');
        if (fromFilter === 'true') {
            const savedFilterEmployees = sessionStorage.getItem('filterEmployees');
            if (savedFilterEmployees) {
                try {
                    const parsed = JSON.parse(savedFilterEmployees);
                    const converted = parsed.map((e: any) => ({
                        id: e.employeeId,
                        name: e.employeeName,
                    }));
                    setSelectedEmployeesForFilter(converted);
                    const employeeIds = parsed.map((e: any) => e.employeeId);
                    if (employeeIds.length > 0) {
                        updateFilters({ employeeIds });
                    }

                    router.replace('/dashboard/discover/projects');
                } catch (error) {
                    console.error('Failed to restore filter employees:', error);
                }
            }
        }
    }, [searchParams, router, updateFilters]);

    // Handle return from member selection - Dialog recover
    useEffect(() => {
        const fromSelection = searchParams.get('fromSelection');
        if (fromSelection === 'true') {
            const savedState = sessionStorage.getItem('projectDialogState');
            if (savedState) {
                try {
                    const parsed = JSON.parse(savedState);

                    if (parsed.members && Array.isArray(parsed.members)) {
                        setMembers(parsed.members);
                    }

                    if (parsed.mode === 'edit' && parsed.projectId) {
                        const project = projects.find((p) => p.id === parsed.projectId);
                        if (project) {
                            openEditDialog(project);
                        } else {
                            openCreateDialog();
                        }
                    } else {
                        openCreateDialog();
                    }

                    router.replace('/dashboard/discover/projects');
                } catch (error) {
                    console.error('Failed to restore dialog state:', error);
                }
            }
        }
    }, [searchParams, projects, openCreateDialog, openEditDialog, router, setMembers]);

    // Employee filter handlers
    const handleEmployeeFilterSelect = () => {
        if (selectedEmployeesForFilter.length > 0) {
            const converted = selectedEmployeesForFilter.map((e) => ({
                employeeId: e.id,
                employeeName: e.name,
            }));
            sessionStorage.setItem('filterEmployees', JSON.stringify(converted));
        }
        sessionStorage.setItem('employeeFilterMode', 'projects');
        router.push('/dashboard/discover/employees?mode=filter&returnTo=projects');
    };

    const handleRemoveEmployeeFilter = (employeeId: string) => {
        const newEmployees = selectedEmployeesForFilter.filter((e) => e.id !== employeeId);
        setSelectedEmployeesForFilter(newEmployees);

        const newEmployeeIds = (filters.employeeIds ?? []).filter((id) => id !== employeeId);
        updateFilters({ employeeIds: newEmployeeIds.length > 0 ? newEmployeeIds : undefined });

        if (newEmployees.length > 0) {
            const converted = newEmployees.map((e) => ({
                employeeId: e.id,
                employeeName: e.name,
            }));
            sessionStorage.setItem('filterEmployees', JSON.stringify(converted));
        } else {
            sessionStorage.removeItem('filterEmployees');
        }
    };

    const handleClearFilters = () => {
        clearFilters();
        setSelectedEmployeesForFilter([]);
        sessionStorage.removeItem('filterEmployees');
    };

    const handleViewDetails = (projectId: string) => {
        router.push(`/dashboard/project/${projectId}`);
    };

    // Dialog handlers
    const handleSave = async (data: any) => {
        if (mode === 'edit' && editingProject) {
            const success = await handleUpdate(editingProject.id, data);
            if (success) {
                // only clear sessionStorage on successful create
                sessionStorage.removeItem('projectDialogState');
            }
            return success;
        }
        const success = await handleCreate(data);
        if (success) {
            // only clear sessionStorage on successful create
            sessionStorage.removeItem('projectDialogState');
        }
        return success;
    };

    const handleOpenDialog = (project?: Project) => {
        if (project) {
            const members = project.members.map((m) => ({
                employeeId: m.employeeId,
                employeeName: m.employeeName,
                positionId: m.positionId,
                positionName: m.positionName,
            }));
            setMembers(members);
            openEditDialog(project);
        } else {
            clearMembers();
            openCreateDialog();
        }
    };

    const handleCloseDialog = () => {
        sessionStorage.removeItem('projectDialogState');
        closeDialog();
    };

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
                <ProjectsHeader
                    onToggleFilters={toggleFilters}
                    showFilters={showFilters}
                    onRefresh={refetch}
                    isRefreshing={isFetching}
                    hasActiveFilters={hasActiveFilters}
                    onClearFilters={handleClearFilters}
                    onCreateProject={() => handleOpenDialog()}
                    totalProjects={totalElements}
                />

                <Searchbar
                    onSearch={setSearch}
                    isLoading={isLoading}
                    placeholder={t("searchPlaceholder")}
                />

                <div className="grid gap-6 lg:grid-cols-4">
                    {showFilters && (
                        <aside className="lg:col-span-1">
                            <FilterPanel
                                filters={filters}
                                onFiltersChange={updateFilters}
                                skillOptions={skillOptions}
                                onEmployeeSelect={handleEmployeeFilterSelect}
                                selectedEmployees={selectedEmployeesForFilter}
                                onRemoveEmployee={handleRemoveEmployeeFilter}
                                isLoading={isLoadingFilterOptions}
                            />
                        </aside>
                    )}

                    <div className={showFilters ? 'lg:col-span-3' : 'lg:col-span-4'}>
                        <div className="space-y-6">
                            <ProjectGrid
                                projects={projects}
                                isLoading={isLoading}
                                onEdit={handleOpenDialog}
                                onDelete={handleDelete}
                                onViewDetails={handleViewDetails}
                            />

                            {totalPages > 1 && !isLoading && (
                                <ProjectsPagination
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

            <ProjectDialog
                isOpen={isOpen}
                mode={mode}
                currentProject={editingProject}
                skillOptions={skillOptions}
                positionOptions={positionOptions}
                selectedMembers={selectedMembers}
                isSaving={isSaving}
                onClose={handleCloseDialog}
                onSave={handleSave}
                onSelectMembers={(onBeforeNavigate) => selectMembers(onBeforeNavigate)}
                onRemoveMember={removeMember}
                onUpdateMemberPosition={(empId, posId) => {
                    const position = positionOptions.find((p) => p.id === posId);
                    if (position) {
                        updateMemberPosition(empId, posId, position.name);
                    }
                }}
            />
        </div>
    );
}