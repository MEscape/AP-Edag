'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { useRouter } from '@/libs/i18nNavigation';
import { PageHeader } from '@/components/ui/page-header';
import { useMyActivities } from './hooks/use-my-activities';
import { ActivitiesContent } from './components/activities/activities-content';
import { ActivitiesPagination } from './components/activities/activities-pagination';
import {ErrorState} from "@/components/ui/error-state";

export default function MyActivitiesPage() {
    const t = useTranslations('myActivities');
    const router = useRouter();

    const {
        activities,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        onNextPage,
        onPreviousPage,
        refetch,
        isFetching,
    } = useMyActivities();

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
                    {t('totalActivities', { count: totalElements })}
                </span>
                            )}
                        </div>
                    }
                />

                <Card className="border-border bg-white shadow-edag">
                    <CardContent className="p-6 space-y-6">
                        <ActivitiesContent activities={activities} isLoading={isLoading} />

                        {totalPages > 1 && (
                            <ActivitiesPagination
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