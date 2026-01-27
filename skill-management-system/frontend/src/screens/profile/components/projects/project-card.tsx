'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Briefcase, Calendar, Building2 } from 'lucide-react';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { useRouter } from '@/libs/i18nNavigation';
import { getProjectStatusConfig } from '../../utils/project-variants';
import type { ProfileProject } from '@/store/api/profile-api';

interface ProjectCardProps {
    project: ProfileProject;
    userId: string;
}

export const ProjectCard = ({ project, userId }: ProjectCardProps) => {
    const t = useTranslations('profile');
    const { formatDate } = useFormatLocalizedDate();
    const router = useRouter();

    const statusConfig = getProjectStatusConfig(project.status);
    const StatusIcon = statusConfig.icon;

    const userMember = project.members.find((member) => member.employeeId === userId);
    const userPosition = userMember?.positionName;

    return (
        <Card
            className="cursor-pointer transition-all hover:border-primary/30 hover:shadow-md"
            onClick={() => router.push(`/dashboard/project/${project.id}`)}
        >
            <CardContent className="p-4">
                <div className="space-y-3">
                    <div className="flex flex-col gap-2 sm:flex-row sm:items-start sm:justify-between">
                        <div className="space-y-1">
                            <h4 className="font-semibold text-foreground">{project.name}</h4>
                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                <Briefcase className="size-4" />
                                <span>{userPosition}</span>
                            </div>
                        </div>
                        <Badge className={statusConfig.color}>
                            <StatusIcon className="size-3" />
                            {t(`projects.status.${project.status.toLowerCase()}` as any)}
                        </Badge>
                    </div>

                    <p className="text-sm leading-relaxed text-muted-foreground">{project.description}</p>

                    <div className="flex items-center gap-2 text-xs text-muted-foreground">
                        <Calendar className="size-3.5" />
                        <span>
              {formatDate(project.startDate, {
                  day: '2-digit',
                  month: 'short',
                  year: 'numeric',
              })}
                            {project.endDate && (
                                <>
                                    {' '}
                                    -{' '}
                                    {formatDate(project.endDate, {
                                        day: '2-digit',
                                        month: 'short',
                                        year: 'numeric',
                                    })}
                                </>
                            )}
                            {!project.endDate && project.status === 'ACTIVE' && (
                                <> - {t('projects.ongoing')}</>
                            )}
            </span>
                    </div>

                    {project.client && (
                        <div className="flex items-center gap-2 text-xs text-muted-foreground">
                            <Building2 className="size-3.5" />
                            <span>{project.client}</span>
                        </div>
                    )}

                    {project.technologies.length > 0 && (
                        <div className="flex flex-wrap gap-1.5">
                            {project.technologies.map((tech) => (
                                <Badge key={tech} variant="secondary" className="text-xs">
                                    {tech}
                                </Badge>
                            ))}
                        </div>
                    )}
                </div>
            </CardContent>
        </Card>
    );
};