import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar, Building2, Clock } from 'lucide-react';
import type { Project } from '@/store/api/project-api';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { calculateProjectDuration } from '../../utils/project-detail-helpers';
import {ProjectDetailInfoSkeleton} from "@/screens/project-details/components/ui/loading-skeletons";

interface ProjectDetailInfoProps {
    project?: Project;
    isLoading?: boolean;
}

export const ProjectDetailInfo = ({ project, isLoading }: ProjectDetailInfoProps) => {
    const t = useTranslations('projectDetail');
    const { formatDate } = useFormatLocalizedDate();

    if (isLoading || !project) {
        return <ProjectDetailInfoSkeleton />;
    }

    const duration = calculateProjectDuration(project.startDate, project.endDate, t);

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <CardTitle className="text-xl font-bold text-foreground">
                    {t('projectInformation')}
                </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
                {project.description && (
                    <div className="space-y-2">
                        <h3 className="text-sm font-semibold text-foreground">{t('description')}</h3>
                        <p className="leading-relaxed text-muted-foreground">{project.description}</p>
                    </div>
                )}

                <div className="space-y-4 rounded-lg border border-border bg-muted/30 p-4">
                    <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                        <Calendar className="size-4" />
                        {t('timeline')}
                    </div>

                    <div className="grid gap-4 sm:grid-cols-2">
                        <div className="space-y-1">
                            <p className="text-xs font-medium text-muted-foreground">{t('startDate')}</p>
                            <p className="text-sm font-semibold text-foreground">
                                {formatDate(project.startDate, {
                                    day: '2-digit',
                                    month: 'long',
                                    year: 'numeric',
                                })}
                            </p>
                        </div>

                        <div className="space-y-1">
                            <p className="text-xs font-medium text-muted-foreground">{t('endDate')}</p>
                            <p className="text-sm font-semibold text-foreground">
                                {project.endDate
                                    ? formatDate(project.endDate, {
                                        day: '2-digit',
                                        month: 'long',
                                        year: 'numeric',
                                    })
                                    : t('ongoing')}
                            </p>
                        </div>
                    </div>

                    <div className="flex items-center gap-2 rounded-lg border border-border bg-white p-3">
                        <Clock className="size-4 text-primary" />
                        <div>
                            <p className="text-xs font-medium text-muted-foreground">{t('projectDuration')}</p>
                            <p className="text-sm font-semibold text-foreground">{duration}</p>
                        </div>
                    </div>
                </div>

                {project.client && (
                    <div className="space-y-2">
                        <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                            <Building2 className="size-4" />
                            {t('clientInformation')}
                        </div>
                        <p className="text-sm text-muted-foreground">{project.client}</p>
                    </div>
                )}
            </CardContent>
        </Card>
    );
};