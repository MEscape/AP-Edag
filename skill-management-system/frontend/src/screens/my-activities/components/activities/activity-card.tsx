import { useTranslations } from 'next-intl';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { getActivityIcon, getActivityTranslationKey } from '../../utils/activity-variants';
import type { Activity } from '@/store/api/dashboard-api';

interface ActivityCardProps {
    activity: Activity;
}

export const ActivityCard = ({ activity }: ActivityCardProps) => {
    const t = useTranslations('myActivities');
    const { formatTimestamp } = useFormatLocalizedDate();

    const Icon = getActivityIcon(activity.activityType);
    const typeKey = getActivityTranslationKey(activity.activityType);

    return (
        <div className="group flex gap-4 rounded-lg border border-border bg-muted/30 p-4 transition-all hover:border-primary/20 hover:shadow-md">
            <div className="flex size-12 shrink-0 items-center justify-center rounded-lg bg-secondary/10 transition-all group-hover:bg-secondary/20">
                <Icon className="size-6 text-secondary" strokeWidth={2} />
            </div>
            <div className="min-w-0 flex-1 space-y-1">
                <p className="font-semibold leading-tight text-foreground">
                    {t(`types.${typeKey}.title` as any)}
                </p>
                <p className="text-sm leading-relaxed text-muted-foreground">
                    {t(`types.${typeKey}.description` as any)}
                </p>
                <p className="text-xs font-medium text-muted-foreground">
                    {formatTimestamp(activity.timestamp)}
                </p>
            </div>
        </div>
    );
};