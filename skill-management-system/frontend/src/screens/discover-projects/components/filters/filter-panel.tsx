import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Separator } from '@/components/ui/separator';
import { X, Search, FolderKanban, Users, Code } from 'lucide-react';
import type { ProjectSearchFilters, TechnologyFilterOption } from '@/store/api/project-api';
import { useState, useMemo } from 'react';
import {FilterPanelSkeleton} from "@/screens/discover-projects/components/ui/loading-skeletons";

interface FilterPanelProps {
    filters: ProjectSearchFilters;
    onFiltersChange: (filters: Partial<ProjectSearchFilters>) => void;
    skillOptions?: TechnologyFilterOption[];
    onEmployeeSelect: () => void;
    selectedEmployees?: Array<{ id: string; name: string }>;
    onRemoveEmployee: (id: string) => void;
    isLoading: boolean;
}

export const FilterPanel = ({
                                filters,
                                onFiltersChange,
                                skillOptions = [],
                                onEmployeeSelect,
                                selectedEmployees = [],
                                onRemoveEmployee,
                                isLoading
                            }: FilterPanelProps) => {
    const t = useTranslations('discoverProjects.filters');
    const [skillSearch, setSkillSearch] = useState('');

    const projectStatuses = ['ACTIVE', 'COMPLETED', 'PLANNED'];

    // Filter skills based on search
    const filteredSkills = useMemo(() => {
        if (!skillSearch.trim()) return skillOptions;
        return skillOptions.filter((skill) =>
            skill.name.toLowerCase().includes(skillSearch.toLowerCase())
        );
    }, [skillOptions, skillSearch]);

    const handleStatusToggle = (status: string) => {
        const currentStatuses = filters.statusList || [];
        const newStatuses = currentStatuses.includes(status)
            ? currentStatuses.filter((s) => s !== status)
            : [...currentStatuses, status];

        onFiltersChange({ statusList: newStatuses.length > 0 ? newStatuses : undefined });
    };

    const handleSkillToggle = (skillId: string) => {
        const currentSkills = filters.skillIds || [];
        const newSkills = currentSkills.includes(skillId)
            ? currentSkills.filter((id) => id !== skillId)
            : [...currentSkills, skillId];

        onFiltersChange({ skillIds: newSkills.length > 0 ? newSkills : undefined });
    };

    if (isLoading) {
        return <FilterPanelSkeleton />
    }

    return (
        <Card className="border-border bg-white shadow-sm">
            <CardHeader className="space-y-1">
                <div className="flex items-center justify-between">
                    <CardTitle className="text-lg font-bold">
                        {t('title')}
                    </CardTitle>
                </div>
            </CardHeader>

            <CardContent className="space-y-6">
                <div className="space-y-3">
                    <Label className="flex items-center gap-2 text-sm font-semibold">
                        <FolderKanban className="size-4 text-muted-foreground" />
                        {t('status')}
                    </Label>
                    <div className="space-y-2.5">
                        {projectStatuses.map((status) => (
                            <div key={status} className="flex items-center space-x-2">
                                <Checkbox
                                    id={`status-${status}`}
                                    checked={filters.statusList?.includes(status) || false}
                                    onCheckedChange={() => handleStatusToggle(status)}
                                    className="border-border data-[state=checked]:bg-primary data-[state=checked]:border-primary"
                                />
                                <label
                                    htmlFor={`status-${status}`}
                                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer"
                                >
                                    {t(`statusOptions.${status.toLowerCase()}` as any)}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>

                <Separator />

                <div className="space-y-3">
                    <Label className="flex items-center gap-2 text-sm font-semibold">
                        <Users className="size-4 text-muted-foreground" />
                        {t('employees')}
                    </Label>
                    <button
                        type="button"
                        onClick={onEmployeeSelect}
                        className="w-full rounded-lg border border-border bg-white px-4 py-2.5 text-left text-sm font-medium text-foreground transition-all hover:bg-muted/50"
                    >
                        {t('selectEmployees')}
                    </button>
                    {selectedEmployees.length > 0 && (
                        <div className="flex flex-wrap gap-2">
                            {selectedEmployees.map((employee) => (
                                <Badge
                                    key={employee.id}
                                    variant="outline"
                                    className="gap-1 border-primary/20 bg-primary/5 pr-1 text-foreground"
                                >
                                    {employee.name}
                                    <button
                                        type="button"
                                        onClick={() => onRemoveEmployee(employee.id)}
                                        className="ml-1 rounded-full p-0.5 hover:bg-primary/10"
                                    >
                                        <X className="size-3" />
                                    </button>
                                </Badge>
                            ))}
                        </div>
                    )}
                </div>

                <Separator />

                {skillOptions.length > 0 && (
                    <div className="space-y-3">
                        <Label className="flex items-center gap-2 text-sm font-semibold">
                            <Code className="size-4 text-muted-foreground" />
                            {t('skills')}
                        </Label>

                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
                            <Input
                                type="text"
                                value={skillSearch}
                                onChange={(e) => setSkillSearch(e.target.value)}
                                placeholder={t('skillsPlaceholder')}
                                className="pl-9 border-border bg-white"
                            />
                        </div>

                        <div className="max-h-64 space-y-2.5 overflow-y-auto">
                            {filteredSkills.length > 0 ? (
                                filteredSkills.map((skill) => (
                                    <div key={skill.id} className="flex items-center space-x-2">
                                        <Checkbox
                                            id={`skill-${skill.id}`}
                                            checked={filters.skillIds?.includes(skill.id) || false}
                                            onCheckedChange={() => handleSkillToggle(skill.id)}
                                            className="border-border data-[state=checked]:bg-primary data-[state=checked]:border-primary"
                                        />
                                        <label
                                            htmlFor={`skill-${skill.id}`}
                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer"
                                        >
                                            {skill.name}
                                        </label>
                                    </div>
                                ))
                            ) : (
                                <p className="text-center text-sm text-muted-foreground py-4">
                                    {t('noSkills')}
                                </p>
                            )}
                        </div>

                        {filters.skillIds && filters.skillIds.length > 0 && (
                            <div className="text-xs text-muted-foreground">
                                {filters.skillIds.length} {t('skillsSelected', { count: filters.skillIds.length })}
                            </div>
                        )}
                    </div>
                )}
            </CardContent>
        </Card>
    );
};