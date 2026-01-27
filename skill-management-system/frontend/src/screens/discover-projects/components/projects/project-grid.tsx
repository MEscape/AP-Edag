import { useTranslations } from 'next-intl';
import { ProjectCard } from './project-card';
import { ProjectsSkeleton } from '../ui/loading-skeletons';
import { EmptyState } from '@/components/ui/empty-state';
import { Briefcase } from 'lucide-react';
import type { Project } from '@/store/api/project-api';

interface ProjectGridProps {
    projects: Project[];
    isLoading: boolean;
    onEdit: (project: Project) => void;
    onDelete: (projectId: string) => void;
    onViewDetails: (projectId: string) => void;
}

export const ProjectGrid = ({
                                projects,
                                isLoading,
                                onEdit,
                                onDelete,
                                onViewDetails,
                            }: ProjectGridProps) => {
    const t = useTranslations('discoverProjects');

    if (isLoading) {
        return <ProjectsSkeleton />;
    }

    if (!projects || projects.length === 0) {
        return <EmptyState icon={Briefcase} message={t('noProjects')} />;
    }

    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {projects.map((project) => (
                <ProjectCard
                    key={project.id}
                    project={project}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onViewDetails={onViewDetails}
                />
            ))}
        </div>
    );
};