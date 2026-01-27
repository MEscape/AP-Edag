'use client';

import { useTranslations } from 'next-intl';
import { Calendar, ChevronRight, Briefcase } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { useRouter } from '@/libs/i18nNavigation';
import { getProjectStatusColor } from '../../utils/project-variants';
import { EmptyState } from '@/components/ui/empty-state';
import type { Project } from '@/store/api/dashboard-api';

interface ProjectListProps {
    projects?: Project[];
}

export const ProjectList = ({ projects }: ProjectListProps) => {
    const t = useTranslations('dashboard.recentProjects');
    const router = useRouter();
    const { formatDate } = useFormatLocalizedDate();

    const displayProjects = projects?.slice(0, 6) || [];

    return (
        <Card className="flex flex-col border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg">
            <CardHeader>
                <CardTitle className="text-xl font-bold text-foreground">{t('title')}</CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('description')}
                </CardDescription>
            </CardHeader>
            <CardContent className="flex flex-1 flex-col">
                {displayProjects.length === 0 ? (
                    <EmptyState icon={Briefcase} message={t('noProjects')} />
                ) : (
                    <>
                        <div className="space-y-4">
                            {displayProjects.map((project) => (
                                <div
                                    key={project.id}
                                    className="group flex h-[60px] items-center justify-between gap-4 border-b border-border pb-4 transition-all last:border-0 hover:border-primary/20"
                                >
                                    <div className="flex min-w-0 flex-1 flex-col justify-center gap-1">
                                        <p className="truncate font-semibold leading-tight text-foreground">
                                            {project.name}
                                        </p>
                                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                            <Calendar className="size-4 shrink-0" strokeWidth={2} />
                                            <span className="truncate">
                        {formatDate(project.startDate, {
                            day: '2-digit',
                            month: 'short',
                            year: 'numeric',
                        })}
                                                {project.endDate &&
                                                    ` - ${formatDate(project.endDate, { day: '2-digit', month: 'short', year: 'numeric' })}`}
                      </span>
                                        </div>
                                    </div>
                                    <Badge
                                        variant="outline"
                                        className={`shrink-0 border font-semibold ${getProjectStatusColor(project.status)}`}
                                    >
                                        {t(`status.${project.status.toLowerCase()}` as any)}
                                    </Badge>
                                </div>
                            ))}
                        </div>
                        <Button
                            variant="outline"
                            className="mt-auto w-full gap-2 border-border font-semibold transition-all hover:border-primary/20 hover:shadow-md"
                            onClick={() => router.push('/dashboard/project/me')}
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