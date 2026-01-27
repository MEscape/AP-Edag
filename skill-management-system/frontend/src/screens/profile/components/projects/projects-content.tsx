'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Briefcase } from 'lucide-react';
import { ProjectCard } from './project-card';
import type { ProfileProject } from '@/store/api/profile-api';

interface ProjectsContentProps {
    projects?: readonly ProfileProject[];
    isOwnProfile: boolean;
    userId?: string;
}

export const ProjectsContent = ({ projects, isOwnProfile, userId }: ProjectsContentProps) => {
    const t = useTranslations('profile');

    const hasProjects = projects && projects.length > 0;

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <CardTitle className="text-xl font-bold tracking-tight">
                    {t('projects.title')}
                </CardTitle>
            </CardHeader>
            <CardContent>
                {hasProjects && userId ? (
                    <div className="space-y-4">
                        {projects.map((project) => (
                            <ProjectCard key={project.id} project={project} userId={userId} />
                        ))}
                    </div>
                ) : (
                    <EmptyProjectsState isOwnProfile={isOwnProfile} />
                )}
            </CardContent>
        </Card>
    );
};

function EmptyProjectsState({ isOwnProfile }: { isOwnProfile: boolean }) {
    const t = useTranslations('profile');

    return (
        <div className="flex flex-col items-center justify-center gap-4 py-12 text-center">
            <div className="flex size-16 items-center justify-center rounded-lg bg-muted">
                <Briefcase className="size-8 text-muted-foreground" />
            </div>
            <div className="space-y-2">
                <h3 className="text-lg font-semibold text-foreground">{t('projects.noProjects')}</h3>
                <p className="text-sm text-muted-foreground">
                    {isOwnProfile
                        ? t('projects.noProjectsDescriptionOwn')
                        : t('projects.noProjectsDescription')}
                </p>
            </div>
        </div>
    );
}