import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Code2 } from 'lucide-react';
import { EmptyState } from '@/components/ui/empty-state';
import type { Project } from '@/store/api/project-api';
import {ProjectDetailTechnologiesSkeleton} from "@/screens/project-details/components/ui/loading-skeletons";

interface ProjectDetailTechnologiesProps {
    project?: Project;
    isLoading?: boolean;
}

export const ProjectDetailTechnologies = ({ project, isLoading }: ProjectDetailTechnologiesProps) => {
    const t = useTranslations('projectDetail');

    if (isLoading || !project) {
        return <ProjectDetailTechnologiesSkeleton />;
    }

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                        <Code2 className="size-5" />
                        {t('technologiesUsed')}
                    </CardTitle>
                    <Badge variant="outline" className="text-sm font-semibold">
                        {project.technologies.length}
                    </Badge>
                </div>
            </CardHeader>
            <CardContent>
                {project.technologies.length === 0 ? (
                    <EmptyState icon={Code2} message={t('noTechnologies')} />
                ) : (
                    <div className="flex flex-wrap gap-2">
                        {project.technologies.map((tech) => (
                            <Badge
                                key={tech}
                                variant="outline"
                                className="border-border bg-muted/30 px-3 py-1.5 text-sm font-medium text-foreground"
                            >
                                {tech}
                            </Badge>
                        ))}
                    </div>
                )}
            </CardContent>
        </Card>
    );
};