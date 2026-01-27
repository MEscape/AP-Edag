'use client';

import { useTranslations } from 'next-intl';
import { useRouter } from '@/libs/i18nNavigation';
import { ProjectDetailHeader } from './components/project/project-detail-header';
import { ProjectDetailInfo } from './components/project/project-detail-info';
import { ProjectDetailMembers } from './components/project/project-detail-members';
import { ProjectDetailTechnologies } from './components/project/project-detail-technologies';
import { useProjectDetail } from './hooks/use-project-detail';
import {ErrorState} from "@/components/ui/error-state";

interface ProjectDetailPageProps {
    readonly projectId: string;
}

export default function ProjectDetailPage({ projectId }: ProjectDetailPageProps) {
    const t = useTranslations('projectDetail');
    const router = useRouter();

    const { project, isLoading, isError, refetch, isFetching } = useProjectDetail(projectId);

    // Error State
    if (isError || (!isLoading && !project)) {
        return (
            <ErrorState
                title={t('error')}
                description={t('errorDescription')}
                isRetrying={isFetching}
                onRetry={refetch}
                goBack={() => router.push('/dashboard/discover/projects')}
            />
        );
    }

    return (
        <div className="min-h-screen bg-muted/30">
            <div className="mx-auto max-w-[1440px] px-6 py-8 lg:px-16 lg:py-12">
                <div className="space-y-6">
                    <ProjectDetailHeader project={project} isLoading={isLoading} />

                    <div className="grid gap-6 lg:grid-cols-3">
                        <div className="space-y-6 lg:col-span-2">
                            <ProjectDetailInfo project={project} isLoading={isLoading} />
                            <ProjectDetailMembers project={project} isLoading={isLoading} />
                        </div>

                        <div className="space-y-6">
                            <ProjectDetailTechnologies project={project} isLoading={isLoading} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}