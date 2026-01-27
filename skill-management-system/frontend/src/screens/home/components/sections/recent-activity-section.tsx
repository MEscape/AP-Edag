'use client';

import { useTranslations } from 'next-intl';
import { useRecentActivities } from '../../hooks/use-recent-activities';
import { ProjectList } from '../dashboard/project-list';
import { ActivityList } from '../dashboard/activity-list';
import { ProjectListSkeleton, ActivityListSkeleton } from '../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';

export const RecentActivitySection = () => {
  const t = useTranslations('dashboard');
  const {
    projects,
    activities,
    isLoadingProjects,
    isLoadingActivities,
    isError,
      isFetching,
      refetch,
  } = useRecentActivities();

  if (isError) {
    return (
      <ErrorState
        title={t('error')}
        description={t('errorDescription')}
        isRetrying={isFetching}
        onRetry={refetch}
      />
    );
  }

  return (
      <div className="grid gap-6 lg:grid-cols-2">
        {isLoadingProjects ? (
            <ProjectListSkeleton />
        ) : (
            <ProjectList projects={projects} />
        )}
        {isLoadingActivities ? (
            <ActivityListSkeleton />
        ) : (
            <ActivityList activities={activities} />
        )}
      </div>
  );
};