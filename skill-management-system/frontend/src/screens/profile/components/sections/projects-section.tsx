'use client';

import { useProfile } from '../../hooks/use-profile';
import { ProjectsContent } from '../projects/projects-content';
import { ProjectsSkeleton } from '../ui/loading-skeletons';

interface ProjectsSectionProps {
    userId: string;
}

export const ProjectsSection = ({ userId }: ProjectsSectionProps) => {
    const { profile, isOwnProfile, isLoading } = useProfile(userId);

    if (isLoading) {
        return <ProjectsSkeleton />;
    }

    return (
        <ProjectsContent
            projects={profile?.projects}
            isOwnProfile={isOwnProfile}
            userId={profile?.id}
        />
    );
};