import { useTranslations } from 'next-intl';
import { Pagination } from '@/components/ui/pagination';

interface RoleRequestsPaginationProps {
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
    onNextPage: () => void;
    onPreviousPage: () => void;
    isLoading?: boolean;
}

export const RoleRequestsPagination = ({
                                           page,
                                           totalPages,
                                           totalElements,
                                           onPageChange,
                                           onNextPage,
                                           onPreviousPage,
                                           isLoading = false,
                                       }: RoleRequestsPaginationProps) => {
    const t = useTranslations('admin.roleRequests');

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