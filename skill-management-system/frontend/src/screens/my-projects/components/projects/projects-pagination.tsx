import { useTranslations } from 'next-intl';
import { Pagination } from '@/components/ui/pagination';

interface ProjectsPaginationProps {
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
    onNextPage: () => void;
    onPreviousPage: () => void;
    isLoading?: boolean;
}

export const ProjectsPagination = ({
                                       page,
                                       totalPages,
                                       totalElements,
                                       onPageChange,
                                       onNextPage,
                                       onPreviousPage,
                                       isLoading = false,
                                   }: ProjectsPaginationProps) => {
    const t = useTranslations('myProjects');

    const pageSize = 12;
    const from = page * pageSize + 1;
    const to = Math.min((page + 1) * pageSize, totalElements);

    return (
        <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
                {t('showing', { from, to, total: totalElements })}
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