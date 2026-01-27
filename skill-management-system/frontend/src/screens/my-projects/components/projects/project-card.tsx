import { useTranslations } from 'next-intl';
import { Badge } from '@/components/ui/badge';
import { Calendar } from 'lucide-react';
import { useRouter } from '@/libs/i18nNavigation';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { getProjectStatusColor, getProjectStatusTranslationKey } from '../../utils/project-variants';
import type { Project } from '@/store/api/project-api';

interface ProjectCardProps {
    project: Project;
}

export const ProjectCard = ({ project }: ProjectCardProps) => {
    const t = useTranslations('myProjects');
    const router = useRouter();
    const { formatDate } = useFormatLocalizedDate();

    const statusKey = getProjectStatusTranslationKey(project.status);

    return (
        <div
            onClick={() => router.push(`${project.id}`)}
            className="group flex cursor-pointer items-center justify-between gap-4 rounded-lg border border-border bg-muted/30 p-4 transition-all hover:border-primary/20 hover:shadow-md"
        >
            <div className="min-w-0 flex-1 space-y-2">
                <h3 className="truncate font-semibold text-foreground group-hover:text-primary">
                    {project.name}
                </h3>
                {project.description && (
                    <p className="line-clamp-1 text-sm text-muted-foreground">
                        {project.description}
                    </p>
                )}
                <div className="flex items-center gap-4 text-sm text-muted-foreground">
                    <div className="flex items-center gap-2">
                        <Calendar className="size-4 shrink-0" strokeWidth={2} />
                        <span className="truncate">
              {formatDate(project.startDate, { day: '2-digit', month: 'short', year: 'numeric' })}
                            {project.endDate && ` - ${formatDate(project.endDate, { day: '2-digit', month: 'short', year: 'numeric' })}`}
            </span>
                    </div>
                    {project.client && (
                        <span className="truncate">• {project.client}</span>
                    )}
                </div>
            </div>
            <Badge
                variant="outline"
                className={`shrink-0 border font-semibold ${getProjectStatusColor(project.status)}`}
            >
                {t(`status.${statusKey}` as any)}
            </Badge>
        </div>
    );
};