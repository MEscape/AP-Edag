import { useTranslations } from 'next-intl';
import { Clock } from 'lucide-react';
import { EmptyState } from '@/components/ui/empty-state';
import { LoadingSkeletons } from '../ui/loading-skeletons';
import { ActivityCard } from './activity-card';
import type { Activity } from '@/store/api/dashboard-api';

interface ActivitiesContentProps {
    activities: Activity[];
    isLoading: boolean;
}

export const ActivitiesContent = ({ activities, isLoading }: ActivitiesContentProps) => {
    const t = useTranslations('myActivities');

    if (isLoading) {
        return <LoadingSkeletons />;
    }

    if (activities.length === 0) {
        return <EmptyState icon={Clock} message={t('noActivities')} />;
    }

    return (
        <div className="space-y-4">
            {activities.map((activity) => (
                <ActivityCard key={activity.id} activity={activity} />
            ))}
        </div>
    );
};