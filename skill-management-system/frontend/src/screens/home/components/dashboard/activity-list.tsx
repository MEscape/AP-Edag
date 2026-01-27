'use client';

import { useTranslations } from 'next-intl';
import { ChevronRight, Clock } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { useRouter } from '@/libs/i18nNavigation';
import { getActivityIcon } from '../../utils/activity-variants';
import { EmptyState } from '@/components/ui/empty-state';
import type { Activity } from '@/store/api/dashboard-api';

interface ActivityListProps {
    activities?: Activity[];
}

export const ActivityList = ({ activities }: ActivityListProps) => {
    const t = useTranslations('dashboard.recentActivities');
    const { formatTimestamp } = useFormatLocalizedDate();
    const router = useRouter();

    const displayActivities = activities?.slice(0, 6) || [];

    return (
        <Card className="flex flex-col border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg">
            <CardHeader>
                <CardTitle className="text-xl font-bold text-foreground">{t('title')}</CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('description')}
                </CardDescription>
            </CardHeader>
            <CardContent className="flex flex-1 flex-col">
                {displayActivities.length === 0 ? (
                    <EmptyState icon={Clock} message={t('noActivities')} />
                ) : (
                    <>
                        <div className="space-y-4">
                            {displayActivities.map((activity) => {
                                const Icon = getActivityIcon(activity.activityType);
                                const activityTypeKey = activity.activityType.toLowerCase();

                                return (
                                    <div key={activity.id} className="group flex h-[60px] gap-4 transition-all">
                                        <div className="flex size-12 shrink-0 items-center justify-center rounded-lg bg-secondary/10 transition-all group-hover:bg-secondary/20">
                                            <Icon className="size-6 text-secondary" strokeWidth={2} />
                                        </div>
                                        <div className="flex min-w-0 flex-1 flex-col justify-center gap-1">
                                            <p className="truncate font-semibold leading-tight text-foreground">
                                                {t(`types.${activityTypeKey}.title` as any)}
                                            </p>
                                            <p className="truncate text-sm leading-relaxed text-muted-foreground">
                                                {t(`types.${activityTypeKey}.description` as any)} •{' '}
                                                {formatTimestamp(activity.timestamp)}
                                            </p>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                        <Button
                            variant="outline"
                            onClick={() => router.push('/dashboard/activity/me')}
                            className="mt-auto w-full gap-2 border-border font-semibold transition-all hover:border-primary/20 hover:shadow-md"
                        >
                            {t('viewAll')}
                            <ChevronRight className="size-4" />
                        </Button>
                    </>
                )}
            </CardContent>
        </Card>
    );
};