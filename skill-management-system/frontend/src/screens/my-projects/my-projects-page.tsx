'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { useRouter } from '@/libs/i18nNavigation';
import { PageHeader } from '@/components/ui/page-header';
import { ErrorState } from '@/components/ui/error-state';
import { useMyProjects } from './hooks/use-my-projects';
import { ProjectsContent } from './components/projects/projects-content';
import { ProjectsPagination } from './components/projects/projects-pagination';

export default function MyProjectsPage() {
    const t = useTranslations('myProjects');
    const router = useRouter();

    const {
        projects,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        onNextPage,
        onPreviousPage,
        refetch,
        isFetching
    } = useMyProjects();

    // Error State
    if (isError) {
        return (
            <ErrorState
                title={t('error')}
                description={t('errorDescription')}
                onRetry={refetch}
                isRetrying={isFetching}
                goBack={() => router.push('/dashboard/home')}
            />
        )
    }

    return (
        <div className="min-h-screen bg-muted/30">
            <div className="pointer-events-none absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />

            <div className="relative mx-auto max-w-[1440px] space-y-8 px-6 py-8 lg:px-16 lg:py-12">
                <PageHeader
                    title={t('title')}
                    description={
                        <div className="flex items-center gap-2">
                            <span>{t('description')}</span>
                            {totalElements > 0 && (
                                <span className="rounded-full bg-muted px-2 py-1 text-xs text-muted-foreground">
                    {t('totalProjects', { count: totalElements })}
                </span>
                            )}
                        </div>
                    }
                />

                <Card className="border-border bg-white shadow-edag">
                    <CardContent className="p-6 space-y-6">
                        <ProjectsContent projects={projects} isLoading={isLoading} />

                        {totalPages > 1 && (
                            <ProjectsPagination
                                page={page}
                                totalPages={totalPages}
                                totalElements={totalElements}
                                onPageChange={setPage}
                                onNextPage={onNextPage}
                                onPreviousPage={onPreviousPage}
                                isLoading={isLoading}
                            />
                        )}
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}