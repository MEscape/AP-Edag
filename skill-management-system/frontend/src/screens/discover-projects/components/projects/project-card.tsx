import { useTranslations } from 'next-intl';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import {
    Calendar,
    Users,
    Building2,
    Edit,
    Trash2,
    MoreVertical,
    ChevronRight,
} from 'lucide-react';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import type { Project } from '@/store/api/project-api';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { useSession } from 'next-auth/react';
import { getProjectStatusVariant } from '../../utils/project-variants';

interface ProjectCardProps {
    project: Project;
    onEdit: (project: Project) => void;
    onDelete: (projectId: string) => void;
    onViewDetails: (projectId: string) => void;
}

export const ProjectCard = ({
                                project,
                                onEdit,
                                onDelete,
                                onViewDetails,
                            }: ProjectCardProps) => {
    const t = useTranslations('discoverProjects.card');
    const { formatDate } = useFormatLocalizedDate();
    const { data: session } = useSession();

    const isOwner = session?.user?.id === project.createdByUserId;

    return (
        <Card className="group border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg">
            <CardContent className="p-6">
                <div className="space-y-5">
                    <div className="space-y-3">
                        <div className="flex items-start justify-between gap-3">
                            <div className="min-w-0 flex-1">
                                <h3 className="text-lg font-bold text-foreground break-words mb-1.5">
                                    {project.name}
                                </h3>
                                {project.description && (
                                    <p className="text-sm text-muted-foreground break-words line-clamp-3 leading-relaxed">
                                        {project.description}
                                    </p>
                                )}
                            </div>

                            {isOwner && (
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="size-8 shrink-0 p-0"
                                        >
                                            <MoreVertical className="size-4" />
                                        </Button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end">
                                        <DropdownMenuItem onClick={() => onEdit(project)}>
                                            <Edit className="mr-2 size-4" />
                                            {t('edit')}
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            onClick={() => onDelete(project.id)}
                                            className="text-destructive focus:text-destructive focus:bg-destructive/10"
                                        >
                                            <Trash2 className="mr-2 size-4" />
                                            {t('delete')}
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            )}
                        </div>

                        <div className="flex items-center gap-2">
                            <Badge
                                variant="outline"
                                className={`border font-semibold whitespace-nowrap ${getProjectStatusVariant(project.status)}`}
                            >
                                {t(`status.${project.status.toLowerCase()}` as any)}
                            </Badge>

                            {isOwner && (
                                <Badge
                                    variant="outline"
                                    className="border-primary/20 bg-primary/5 text-xs whitespace-nowrap"
                                >
                                    {t('owner')}
                                </Badge>
                            )}
                        </div>
                    </div>

                    <div className="space-y-2.5 border-t border-border pt-4">
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Calendar className="size-4 shrink-0" />
                            <span className="truncate">
                {formatDate(project.startDate, { day: '2-digit', month: 'short', year: 'numeric' })}
                                {project.endDate &&
                                    ` - ${formatDate(project.endDate, { day: '2-digit', month: 'short', year: 'numeric' })}`}
              </span>
                        </div>

                        {project.client && (
                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                <Building2 className="size-4 shrink-0" />
                                <span className="truncate">{project.client}</span>
                            </div>
                        )}

                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Users className="size-4 shrink-0" />
                            <span>{t('teamSize', { count: project.teamSize })}</span>
                        </div>
                    </div>

                    {project.technologies.length > 0 && (
                        <div className="space-y-3 border-t border-border pt-4">
                            <div className="flex flex-wrap gap-2">
                                {project.technologies.slice(0, 6).map((tech) => (
                                    <Badge
                                        key={tech}
                                        variant="outline"
                                        className="border-border bg-muted/30 text-xs font-medium text-foreground"
                                    >
                                        {tech}
                                    </Badge>
                                ))}
                                {project.technologies.length > 6 && (
                                    <Badge
                                        variant="outline"
                                        className="border-border bg-muted/30 text-xs font-medium text-muted-foreground"
                                    >
                                        +{project.technologies.length - 6}
                                    </Badge>
                                )}
                            </div>
                        </div>
                    )}

                    <div className="border-t border-border pt-4">
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => onViewDetails(project.id)}
                            className="w-full gap-2 font-semibold"
                        >
                            {t('viewDetails')}
                            <ChevronRight className="size-4" />
                        </Button>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};