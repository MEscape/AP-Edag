import { useTranslations } from 'next-intl';
import { EmployeeCard } from './employee-card';
import { EmployeeGridSkeleton } from '../ui/loading-skeletons';
import { EmptyState } from '@/components/ui/empty-state';
import { Users } from 'lucide-react';
import type { Employee } from '@/store/api/employee-api';

interface EmployeeGridProps {
    employees: Employee[];
    isAnySelectionMode: boolean;
    toggleEmployee: (employee: Employee) => void;
    isEmployeeSelected: (employeeId: string) => boolean;
    isLoading: boolean;
}

export const EmployeeGrid = ({
                                 employees,
                                 isLoading,
                                    isAnySelectionMode,
                                    toggleEmployee,
                                    isEmployeeSelected
}: EmployeeGridProps) => {
    const t = useTranslations('discoverEmployees.employeeGrid');

    if (isLoading) {
        return <EmployeeGridSkeleton />;
    }

    if (!employees || employees.length === 0) {
        return (
            <EmptyState
                icon={Users}
                message={t('noResults')}
            />
        );
    }

    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {employees.map((employee) => (
                <EmployeeCard
                    key={employee.id}
                    employee={employee}
                    isAnySelectionMode={isAnySelectionMode}
                    toggleEmployee={toggleEmployee}
                    isEmployeeSelected={isEmployeeSelected}
                />
            ))}
        </div>
    );
};