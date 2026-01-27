import { useTranslations } from 'next-intl';
import { RefreshCw, Filter, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { PageHeader } from '@/components/ui/page-header';

interface DiscoverHeaderProps {
    onRefresh: () => void;
    isRefreshing: boolean;
    hasActiveFilters: boolean;
    onClearFilters: () => void;
    onToggleFilters: () => void;
    showFilters: boolean;
    totalResults: number;
}

export const EmployeeHeader = ({
                                   onRefresh,
                                   isRefreshing,
                                   hasActiveFilters,
                                   onClearFilters,
                                   onToggleFilters,
                                   showFilters,
                                   totalResults,
                               }: DiscoverHeaderProps) => {
    const t = useTranslations('discoverEmployees.header');

    return (
        <PageHeader
            title={t('title')}
            description={
                <>
                    {t('description')}{' '}
                    {totalResults > 0 && (
                        <span className="font-semibold text-foreground">
              {t('resultsCount', { count: totalResults })}
            </span>
                    )}
                </>
            }
            actions={
                <>
                    {hasActiveFilters && (
                        <Button
                            variant="outline"
                            size="default"
                            onClick={onClearFilters}
                            className="gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md"
                        >
                            <X className="size-4" />
                            {t('clearFilters')}
                        </Button>
                    )}
                    <Button
                        variant="outline"
                        size="default"
                        onClick={onToggleFilters}
                        className={`gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md ${
                            showFilters ? 'border-primary/20 bg-primary/5' : ''
                        }`}
                    >
                        <Filter className="size-4" />
                        {t('filters')}
                    </Button>
                    <Button
                        variant="outline"
                        size="default"
                        onClick={onRefresh}
                        disabled={isRefreshing}
                        className="gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md"
                    >
                        <RefreshCw className={`size-4 ${isRefreshing ? 'animate-spin' : ''}`} />
                        {t('refresh')}
                    </Button>
                </>
            }
        />
    );
};