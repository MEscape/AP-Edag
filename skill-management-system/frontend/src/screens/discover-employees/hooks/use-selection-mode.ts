import { useState, useEffect, useCallback } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { logger } from '@/libs/logger';

interface SelectedEmployee {
    employeeId: string;
    employeeName: string;
    positionId: string;
    positionName: string;
}

export const useSelectionMode = () => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const [selectedEmployees, setSelectedEmployees] = useState<SelectedEmployee[]>([]);

    // URL-Parameter auslesen
    const mode = searchParams.get('mode');
    const returnTo = searchParams.get('returnTo');
    const isSelectionMode = mode === 'select';
    const isFilterMode = mode === 'filter';
    const isAnySelectionMode = isSelectionMode || isFilterMode;

    // Load initial state from sessionStorage
    useEffect(() => {
        if (typeof window === 'undefined') return;

        try {
            if (isSelectionMode) {
                const savedState = sessionStorage.getItem('projectDialogState');
                console.log('Loading selection mode state:', savedState);
                if (savedState) {
                    const parsed = JSON.parse(savedState);
                    if (parsed.members && Array.isArray(parsed.members)) {
                        console.log('Setting selected employees from members:', parsed.members);
                        setSelectedEmployees(parsed.members);
                    }
                }
            } else if (isFilterMode) {
                const savedState = sessionStorage.getItem('filterEmployees');
                console.log('Loading filter mode state:', savedState);
                if (savedState) {
                    setSelectedEmployees(JSON.parse(savedState));
                }
            }
        } catch (error) {
            logger.error('Failed to load selection state', { error });
        }
    }, [isSelectionMode, isFilterMode]);

    // Toggle employee selection
    const toggleEmployee = useCallback((employee: {
        id: string;
        firstName: string;
        lastName: string;
        positionId: string;
        position: string;
    }) => {
        setSelectedEmployees((prev) => {
            const isSelected = prev.some((e) => e.employeeId === employee.id);

            if (isSelected) {
                const newSelected = prev.filter((e) => e.employeeId !== employee.id);
                console.log('Removed employee, new selection:', newSelected);
                return newSelected;
            }

            const newEmployee = {
                employeeId: employee.id,
                employeeName: `${employee.firstName} ${employee.lastName}`,
                positionId: employee.positionId,
                positionName: employee.position,
            };

            const newSelected = [...prev, newEmployee];
            console.log('Added employee, new selection:', newSelected);
            return newSelected;
        });
    }, []);

    // Check if employee is selected
    const isEmployeeSelected = useCallback((employeeId: string) => {
        return selectedEmployees.some((e) => e.employeeId === employeeId);
    }, [selectedEmployees]);

    // Confirm selection
    const confirmSelection = useCallback(() => {
        if (typeof window === 'undefined') return;

        console.log('Confirming selection with employees:', selectedEmployees);

        try {
            if (isFilterMode) {
                sessionStorage.setItem('filterEmployees', JSON.stringify(selectedEmployees));
            } else if (isSelectionMode) {
                const savedState = sessionStorage.getItem('projectDialogState');
                if (savedState) {
                    const parsed = JSON.parse(savedState);
                    parsed.members = selectedEmployees;
                    console.log('Updating projectDialogState with members:', parsed);
                    sessionStorage.setItem('projectDialogState', JSON.stringify(parsed));
                } else {
                    // if no existing state, create new
                    console.log('No existing state, creating new with members:', selectedEmployees);
                    sessionStorage.setItem('projectDialogState', JSON.stringify({
                        members: selectedEmployees,
                    }));
                }
            }

            if (returnTo === 'projects') {
                const param = isFilterMode ? 'fromFilter' : 'fromSelection';
                console.log('Navigating back to projects with param:', param);
                router.push(`/dashboard/discover/projects?${param}=true`);
            } else {
                router.back();
            }
        } catch (error) {
            logger.error('Failed to save selection', { error });
        }
    }, [isFilterMode, isSelectionMode, selectedEmployees, returnTo, router]);

    // Cancel selection
    const cancelSelection = useCallback(() => {
        if (returnTo === 'projects') {
            router.push('/dashboard/discover/projects');
        } else {
            router.back();
        }
    }, [returnTo, router]);

    return {
        isSelectionMode,
        isFilterMode,
        isAnySelectionMode,
        selectedEmployees,
        selectedCount: selectedEmployees.length,
        confirmSelection,
        cancelSelection,
        toggleEmployee,
        isEmployeeSelected,
    };
};