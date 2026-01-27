import { useTranslations } from 'next-intl';
import { Calendar } from 'lucide-react';
import { EmptyState } from '@/components/ui/empty-state';
import { ProjectsSkeleton } from '../ui/projects-skeleton';
import { ProjectCard } from './project-card';
import type { Project } from '@/store/api/project-api';

interface ProjectsContentProps {
    projects: Project[];
    isLoading: boolean;
}

export const ProjectsContent = ({ projects, isLoading }: ProjectsContentProps) => {
    const t = useTranslations('myProjects');

    if (isLoading) {
        return <ProjectsSkeleton />;
    }

    if (projects.length === 0) {
        return (
            <EmptyState icon={Calendar} message={t('noProjects')} />
        );
    }

    return (
        <div className="space-y-3">
            {projects.map((project) => (
                <ProjectCard key={project.id} project={project} />
            ))}
        </div>
    );
};