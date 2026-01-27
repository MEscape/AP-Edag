import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Calendar, Users, Building2, Code2 } from 'lucide-react';
import type { Project } from '@/store/api/project-api';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { getProjectStatusVariant } from '../../utils/project-detail-helpers';
import {ProjectDetailHeaderSkeleton} from "@/screens/project-details/components/ui/loading-skeletons";

interface ProjectDetailHeaderProps {
    project?: Project;
    isLoading?: boolean;
}

export const ProjectDetailHeader = ({ project, isLoading }: ProjectDetailHeaderProps) => {
    const t = useTranslations('projectDetail');
    const { formatDate } = useFormatLocalizedDate();

    if (isLoading || !project) {
        return <ProjectDetailHeaderSkeleton />;
    }

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardContent className="p-8">
                <div className="space-y-6">
                    <div className="space-y-3">
                        <div className="flex flex-wrap items-start justify-between gap-4">
                            <h1 className="text-3xl font-bold tracking-tight text-foreground">
                                {project.name}
                            </h1>
                            <Badge
                                variant="outline"
                                className={`border text-sm font-semibold ${getProjectStatusVariant(project.status)}`}
                            >
                                {t(`status.${project.status.toLowerCase()}` as any)}
                            </Badge>
                        </div>

                        {project.description && (
                            <p className="text-base leading-relaxed text-muted-foreground">
                                {project.description}
                            </p>
                        )}
                    </div>

                    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
                        <div className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-4">
                            <div className="flex size-10 items-center justify-center rounded-lg bg-primary/10">
                                <Calendar className="size-5 text-primary" />
                            </div>
                            <div className="min-w-0 flex-1">
                                <p className="text-xs font-medium text-muted-foreground">{t('duration')}</p>
                                <p className="truncate text-sm font-semibold text-foreground">
                                    {formatDate(project.startDate, { day: '2-digit', month: 'short', year: 'numeric' })}
                                    {project.endDate && (
                                        <> - {formatDate(project.endDate, { day: '2-digit', month: 'short', year: 'numeric' })}</>
                                    )}
                                </p>
                            </div>
                        </div>

                        <div className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-4">
                            <div className="flex size-10 items-center justify-center rounded-lg bg-primary/10">
                                <Users className="size-5 text-primary" />
                            </div>
                            <div className="min-w-0 flex-1">
                                <p className="text-xs font-medium text-muted-foreground">{t('teamSize')}</p>
                                <p className="text-sm font-semibold text-foreground">
                                    {t('teamMembers', { count: project.teamSize })}
                                </p>
                            </div>
                        </div>

                        {project.client && (
                            <div className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-4">
                                <div className="flex size-10 items-center justify-center rounded-lg bg-primary/10">
                                    <Building2 className="size-5 text-primary" />
                                </div>
                                <div className="min-w-0 flex-1">
                                    <p className="text-xs font-medium text-muted-foreground">{t('client')}</p>
                                    <p className="truncate text-sm font-semibold text-foreground">{project.client}</p>
                                </div>
                            </div>
                        )}

                        <div className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-4">
                            <div className="flex size-10 items-center justify-center rounded-lg bg-primary/10">
                                <Code2 className="size-5 text-primary" />
                            </div>
                            <div className="min-w-0 flex-1">
                                <p className="text-xs font-medium text-muted-foreground">{t('technologies')}</p>
                                <p className="text-sm font-semibold text-foreground">
                                    {t('technologiesCount', { count: project.technologies.length })}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};