import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import { RefreshCw, Plus, X, Filter } from 'lucide-react';
import { PageHeader } from '@/components/ui/page-header';

interface ProjectsHeaderProps {
    onRefresh: () => void;
    isRefreshing: boolean;
    hasActiveFilters: boolean;
    onClearFilters: () => void;
    onCreateProject: () => void;
    onToggleFilters: () => void;
    showFilters: boolean;
    totalProjects: number;
}

export const ProjectsHeader = ({
                                   onRefresh,
                                   isRefreshing,
                                   hasActiveFilters,
                                   onClearFilters,
                                   onCreateProject,
                                   totalProjects,
                                   onToggleFilters,
                                   showFilters,
                               }: ProjectsHeaderProps) => {
    const t = useTranslations('discoverProjects.header');

    return (
        <PageHeader
            title={t('title')}
            description={
                <>
                    {t('description')}{' '}
                    {totalProjects > 0 && (
                        <span className="font-semibold text-foreground">
              {t('resultsCount', { count: totalProjects })}
            </span>
                    )}
                </>
            }
            titleSize="lg"
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
                    <Button
                        size="default"
                        onClick={onCreateProject}
                        className="gap-2 font-semibold shadow-lg transition-all hover:shadow-xl"
                    >
                        <Plus className="size-4" />
                        {t('createProject')}
                    </Button>
                </>
            }
        />
    );
};