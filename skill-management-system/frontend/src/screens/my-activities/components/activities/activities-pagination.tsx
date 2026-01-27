import { useTranslations } from 'next-intl';
import { Pagination } from '@/components/ui/pagination';

interface ActivitiesPaginationProps {
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
    onNextPage: () => void;
    onPreviousPage: () => void;
    isLoading?: boolean;
}

export const ActivitiesPagination = ({
                                         page,
                                         totalPages,
                                         totalElements,
                                         onPageChange,
                                         onNextPage,
                                         onPreviousPage,
                                         isLoading = false,
                                     }: ActivitiesPaginationProps) => {
    const t = useTranslations('myActivities');

    return (
        <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
                {t('showing', {
                    from: page * 15 + 1,
                    to: Math.min((page + 1) * 15, totalElements),
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