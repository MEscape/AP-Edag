'use client';

import { useTranslations } from 'next-intl';
import { useDashboardStats } from '../../hooks/use-dashboard-stats';
import { DashboardCards } from '../dashboard/dashboard-cards';
import { DashboardCardsSkeleton } from '../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';

export const StatsSection = () => {
  const t = useTranslations('dashboard');
  const { stats, isLoading, isError, refetch, isFetching } = useDashboardStats();

  if (isLoading) {
    return <DashboardCardsSkeleton />;
  }

  if (isError) {
    return (
      <ErrorState
        title={t('error')}
        description={t('errorDescription')}
        onRetry={refetch}
        isRetrying={isFetching}
      />
    );
  }

  return <DashboardCards stats={stats} />;
};
