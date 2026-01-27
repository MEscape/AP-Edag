import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Checkbox } from '@/components/ui/checkbox';
import { Separator } from '@/components/ui/separator';
import { MapPin, Clock, TrendingUp, Code, Layers } from 'lucide-react';
import { FilterPanelSkeleton } from '../ui/loading-skeletons';
import type { EmployeeFilterOptions, EmployeeSearchFilters } from '@/store/api/employee-api';

interface FilterPanelProps {
    filterOptions?: EmployeeFilterOptions;
    currentFilters: EmployeeSearchFilters;
    onFilterChange: (filters: Partial<EmployeeSearchFilters>) => void;
    isLoading: boolean;
}

export const FilterPanel = ({
                                filterOptions,
                                currentFilters,
                                onFilterChange,
                                isLoading,
                            }: FilterPanelProps) => {
    const t = useTranslations('discoverEmployees.filters');

    const toggleArrayFilter = (filterKey: keyof EmployeeSearchFilters, value: string) => {
        const currentArray = (currentFilters[filterKey] as string[]) || [];
        const newArray = currentArray.includes(value)
            ? currentArray.filter((item) => item !== value)
            : [...currentArray, value];

        onFilterChange({ [filterKey]: newArray.length > 0 ? newArray : undefined });
    };

    const isFilterSelected = (filterKey: keyof EmployeeSearchFilters, value: string) => {
        const currentArray = (currentFilters[filterKey] as string[]) || [];
        return currentArray.includes(value);
    };

    if (isLoading) {
        return <FilterPanelSkeleton />;
    }

    return (
        <Card className="border-border bg-white shadow-sm">
            <CardHeader>
                <CardTitle className="text-lg font-bold">{t('title')}</CardTitle>
            </CardHeader>

            <CardContent className="space-y-6">
                <div className="space-y-3">
                    <Label className="flex items-center gap-2 text-sm font-semibold">
                        <Clock className="size-4 text-muted-foreground" />
                        {t('availability.title')}
                    </Label>
                    <div className="space-y-2.5">
                        {['available', 'partially_available', 'unavailable'].map((status) => (
                            <div key={status} className="flex items-center space-x-2">
                                <Checkbox
                                    id={`availability-${status}`}
                                    checked={isFilterSelected('availability', status)}
                                    onCheckedChange={() => toggleArrayFilter('availability', status)}
                                    className="border-border data-[state=checked]:border-primary data-[state=checked]:bg-primary"
                                />
                                <label
                                    htmlFor={`availability-${status}`}
                                    className="cursor-pointer text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                >
                                    {t(`availability.${status}` as any)}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>

                <Separator />

                {filterOptions?.skillCategories && filterOptions.skillCategories.length > 0 && (
                    <>
                        <div className="space-y-3">
                            <Label className="flex items-center gap-2 text-sm font-semibold">
                                <Layers className="size-4 text-muted-foreground" />
                                {t('skillCategories')}
                            </Label>
                            <div className="max-h-64 space-y-2.5 overflow-y-auto">
                                {filterOptions.skillCategories.map((category) => (
                                    <div key={category.id} className="flex items-center space-x-2">
                                        <Checkbox
                                            id={`category-${category.id}`}
                                            checked={isFilterSelected('skillCategoryIds', category.id)}
                                            onCheckedChange={() => toggleArrayFilter('skillCategoryIds', category.id)}
                                            className="border-border data-[state=checked]:border-primary data-[state=checked]:bg-primary"
                                        />
                                        <label
                                            htmlFor={`category-${category.id}`}
                                            className="cursor-pointer text-sm font-medium leading-none"
                                        >
                                            {category.name}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <Separator />
                    </>
                )}

                {filterOptions?.skills && filterOptions.skills.length > 0 && (
                    <>
                        <div className="space-y-3">
                            <Label className="flex items-center gap-2 text-sm font-semibold">
                                <Code className="size-4 text-muted-foreground" />
                                {t('skills')}
                            </Label>
                            <div className="max-h-64 space-y-2.5 overflow-y-auto">
                                {filterOptions.skills.map((skill) => (
                                    <div key={skill.id} className="flex items-center space-x-2">
                                        <Checkbox
                                            id={`skill-${skill.id}`}
                                            checked={isFilterSelected('skillIds', skill.id)}
                                            onCheckedChange={() => toggleArrayFilter('skillIds', skill.id)}
                                            className="border-border data-[state=checked]:border-primary data-[state=checked]:bg-primary"
                                        />
                                        <label
                                            htmlFor={`skill-${skill.id}`}
                                            className="cursor-pointer text-sm font-medium leading-none"
                                        >
                                            {skill.name}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <Separator />
                    </>
                )}

                {filterOptions?.locations && filterOptions.locations.length > 0 && (
                    <>
                        <div className="space-y-3">
                            <Label className="flex items-center gap-2 text-sm font-semibold">
                                <MapPin className="size-4 text-muted-foreground" />
                                {t('locations')}
                            </Label>
                            <div className="max-h-64 space-y-2.5 overflow-y-auto">
                                {filterOptions.locations.map((location) => (
                                    <div key={location.id} className="flex items-center space-x-2">
                                        <Checkbox
                                            id={`location-${location.id}`}
                                            checked={isFilterSelected('locationIds', location.id)}
                                            onCheckedChange={() => toggleArrayFilter('locationIds', location.id)}
                                            className="border-border data-[state=checked]:border-primary data-[state=checked]:bg-primary"
                                        />
                                        <label
                                            htmlFor={`location-${location.id}`}
                                            className="cursor-pointer text-sm font-medium leading-none"
                                        >
                                            {location.name}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <Separator />
                    </>
                )}

                <div className="space-y-3">
                    <Label className="flex items-center gap-2 text-sm font-semibold">
                        <TrendingUp className="size-4 text-muted-foreground" />
                        {t('experience.title')}
                    </Label>
                    <Select
                        value={currentFilters.minExperience?.toString() || '0'}
                        onValueChange={(value) => {
                            const years = Number.parseInt(value);
                            onFilterChange({ minExperience: years === 0 ? undefined : years });
                        }}
                    >
                        <SelectTrigger className="w-full border-border bg-white">
                            <SelectValue placeholder={t('experience.select')} />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="0">{t('experience.any')}</SelectItem>
                            <SelectItem value="2">{t('experience.years', { years: 2 })}</SelectItem>
                            <SelectItem value="5">{t('experience.years', { years: 5 })}</SelectItem>
                            <SelectItem value="10">{t('experience.years', { years: 10 })}</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </CardContent>
        </Card>
    );
}