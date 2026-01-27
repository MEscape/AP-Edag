import { useTranslations } from 'next-intl';
import { Pagination } from '@/components/ui/pagination';

interface EmployeePaginationProps {
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
    onNextPage: () => void;
    onPreviousPage: () => void;
    isLoading?: boolean;
}

export const EmployeePagination = ({
                                       page,
                                       totalPages,
                                       totalElements,
                                       onPageChange,
                                       onNextPage,
                                       onPreviousPage,
                                       isLoading = false,
                                   }: EmployeePaginationProps) => {
    const t = useTranslations('discoverEmployees');

    return (
        <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
                {t('showing', {
                    from: page * 20 + 1,
                    to: Math.min((page + 1) * 20, totalElements),
                    total: totalElements,
                })}
            </p>
            <Pagination
                currentPage={page}
                totalPages={totalPages}
                onPageChange={onPageChange}
                onNextPage={onNextPage}
                onPreviousPage={onPreviousPage}
                isLoading={isLoading}
            />
        </div>
    );
};