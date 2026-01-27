import { useTranslations } from 'next-intl';
import { Pagination } from '@/components/ui/pagination';

interface UsersTablePaginationProps {
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
    onNextPage: () => void;
    onPreviousPage: () => void;
    isLoading?: boolean;
}

export const UsersTablePagination = ({
                                         page,
                                         totalPages,
                                         totalElements,
                                         onPageChange,
                                         onNextPage,
                                         onPreviousPage,
                                         isLoading = false,
                                     }: UsersTablePaginationProps) => {
    const t = useTranslations('admin.users');

    return (
        <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
                {t('showing', {
                    from: page * 10 + 1,
                    to: Math.min((page + 1) * 10, totalElements),
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