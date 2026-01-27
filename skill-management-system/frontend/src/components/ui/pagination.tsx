import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onNextPage: () => void;
  onPreviousPage: () => void;
  isLoading?: boolean;
}

export const Pagination = ({
  currentPage,
  totalPages,
  onPageChange,
  onNextPage,
  onPreviousPage,
  isLoading,
}: PaginationProps) => {
  const t = useTranslations('common.pagination');

  if (totalPages <= 1) {
    return null;
  }

  const getPageNumbers = (): (number | string)[] => {
    const pages: (number | string)[] = [];
    const visiblePages = 5;
    const lastPage = totalPages - 1;

    if (totalPages <= visiblePages) {
      return Array.from({ length: totalPages }, (_, i) => i);
    }

    const addEllipsisIfNeeded = (arr: (number | string)[], prev: number, next: number) => {
      if (next - prev > 1) arr.push('...');
    };

    pages.push(0);

    const start = Math.max(1, currentPage - 1);
    const end = Math.min(lastPage - 1, currentPage + 1);

    addEllipsisIfNeeded(pages, 0, start);

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    addEllipsisIfNeeded(pages, end, lastPage);

    pages.push(lastPage);
    return pages;
  };

  const pageNumbers = getPageNumbers();

  return (
    <div className="flex items-center justify-center gap-2">
      <Button
        variant="outline"
        size="default"
        onClick={onPreviousPage}
        disabled={currentPage === 0 || isLoading}
        className="gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md disabled:opacity-50"
      >
        <ChevronLeft className="size-4" />
        {t('previous')}
      </Button>

      <div className="flex gap-2">
        {pageNumbers.map((page, index) => {
          if (page === '...') {
            return (
              <div key={`ellipsis-${index}`} className="flex items-center px-2">
                <span className="text-muted-foreground">...</span>
              </div>
            );
          }

          const pageNumber = page as number;
          const isActive = pageNumber === currentPage;

          return (
            <Button
              key={pageNumber}
              variant={isActive ? 'default' : 'outline'}
              size="default"
              onClick={() => onPageChange(pageNumber)}
              disabled={isLoading}
              className={`min-w-[44px] font-semibold transition-all ${
                isActive
                  ? 'bg-primary text-primary-foreground hover:bg-primary/90'
                  : 'border-border bg-white hover:border-primary/20 hover:shadow-md'
              }`}
            >
              {pageNumber + 1}
            </Button>
          );
        })}
      </div>

      <Button
        variant="outline"
        size="default"
        onClick={onNextPage}
        disabled={currentPage === totalPages - 1 || isLoading}
        className="gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md disabled:opacity-50"
      >
        {t('next')}
        <ChevronRight className="size-4" />
      </Button>
    </div>
  );
};
