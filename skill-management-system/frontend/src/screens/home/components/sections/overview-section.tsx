'use client';

import { useTranslations } from 'next-intl';
import { useDashboardOverview } from '../../hooks/use-dashboard-overview';
import { useRoleRequests } from '../../hooks/use-role-requests';
import { SkillChart } from '../dashboard/skill-chart';
import { QuickActions } from '../dashboard/quick-actions';
import { SkillChartSkeleton } from '../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';

export const OverviewSection = () => {
  const t = useTranslations('dashboard');
  const { skillDevelopment, isLoading, isError, refetch, isFetching } = useDashboardOverview();
  const { createRoleRequest, isCreating } = useRoleRequests();

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

  return (
      <div className="grid gap-6 lg:grid-cols-3">
        {isLoading ? (
            <SkillChartSkeleton />
        ) : (
            <SkillChart data={skillDevelopment} />
        )}
        <QuickActions
            onCreateRoleRequest={createRoleRequest}
            isCreating={isCreating}
        />
      </div>
  );
};
