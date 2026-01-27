import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Users, Briefcase, ExternalLink } from 'lucide-react';
import { EmptyState } from '@/components/ui/empty-state';
import type { Project } from '@/store/api/project-api';
import { useRouter } from '@/libs/i18nNavigation';
import {ProjectDetailMembersSkeleton} from "@/screens/project-details/components/ui/loading-skeletons";

interface ProjectDetailMembersProps {
    project?: Project;
    isLoading?: boolean;
}

export const ProjectDetailMembers = ({ project, isLoading }: ProjectDetailMembersProps) => {
    const t = useTranslations('projectDetail');
    const router = useRouter();

    const handleViewProfile = (employeeId: string) => {
        router.push(`/dashboard/profile/${employeeId}`);
    };

    if (isLoading || !project) {
        return <ProjectDetailMembersSkeleton />;
    }

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                        <Users className="size-5" />
                        {t('teamMembers')}
                    </CardTitle>
                    <Badge variant="outline" className="text-sm font-semibold">
                        {project.members.length}
                    </Badge>
                </div>
            </CardHeader>
            <CardContent>
                {project.members.length === 0 ? (
                    <EmptyState icon={Users} message={t('noMembers')} />
                ) : (
                    <div className="space-y-3">
                        {project.members.map((member) => (
                            <div
                                key={member.employeeId}
                                className="group flex items-center justify-between gap-4 rounded-lg border border-border bg-muted/30 p-4 transition-all hover:border-primary/20 hover:shadow-md"
                            >
                                <div className="flex min-w-0 flex-1 items-center gap-4">
                                    <div className="flex size-12 shrink-0 items-center justify-center rounded-lg bg-primary/10 font-semibold text-primary">
                                        {member.employeeName
                                            .split(' ')
                                            .map((n) => n[0])
                                            .join('')
                                            .toUpperCase()
                                            .slice(0, 2)}
                                    </div>

                                    <div className="min-w-0 flex-1 space-y-1">
                                        <p className="truncate font-semibold text-foreground">
                                            {member.employeeName}
                                        </p>
                                        <div className="flex items-center gap-2">
                                            <Briefcase className="size-3 shrink-0 text-muted-foreground" />
                                            <p className="truncate text-sm text-muted-foreground">
                                                {member.positionName}
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <Button
                                    variant="ghost"
                                    size="sm"
                                    onClick={() => handleViewProfile(member.employeeId)}
                                    className="shrink-0 gap-2 opacity-0 transition-opacity group-hover:opacity-100"
                                >
                                    {t('viewProfile')}
                                    <ExternalLink className="size-4" />
                                </Button>
                            </div>
                        ))}
                    </div>
                )}
            </CardContent>
        </Card>
    );
};