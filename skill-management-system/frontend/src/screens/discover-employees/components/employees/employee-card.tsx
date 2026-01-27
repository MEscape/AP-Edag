'use client';

import { useTranslations } from 'next-intl';
import { useRouter } from '@/libs/i18nNavigation';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { Mail, MapPin, Award, ChevronRight, Check } from 'lucide-react';
import type { Employee } from '@/store/api/employee-api';
import {getAvailabilityStyle} from "@/screens/discover-employees/utils/employee-variants";

interface EmployeeCardProps {
    employee: Employee;
    isAnySelectionMode: boolean;
    toggleEmployee: (employee: Employee) => void;
    isEmployeeSelected: (employeeId: string) => boolean;
}

export const EmployeeCard = ({
                                 employee,
                                 toggleEmployee,
                                 isEmployeeSelected,
                                 isAnySelectionMode }: EmployeeCardProps) => {
    const t = useTranslations('discoverEmployees.employeeCard');
    const router = useRouter();

    const isSelected = isEmployeeSelected(employee.id);
    const availabilityStyle = getAvailabilityStyle(employee.availability);
    const initials = `${employee.firstName[0]}${employee.lastName[0]}`;
    const remainingSkillsCount = Math.max(0, employee.skillCount - 3);

    const handleClick = () => {
        if (isAnySelectionMode) {
            toggleEmployee(employee);
        } else {
            router.push(`/dashboard/profile/${employee.id}`);
        }
    };

    return (
        <Card
            className={`group border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg ${
                isAnySelectionMode && isSelected ? 'border-primary/50 ring-2 ring-primary/20' : ''
            }`}
        >
            <CardContent className="p-6">
                <div className="space-y-5">
                    <div className="flex items-start gap-4">
                        <div
                            className={`flex size-14 shrink-0 items-center justify-center rounded-lg text-lg font-bold ${
                                isAnySelectionMode && isSelected
                                    ? 'bg-primary text-primary-foreground'
                                    : 'bg-primary/10 text-primary'
                            }`}
                        >
                            {isAnySelectionMode && isSelected ? <Check className="size-6" /> : initials}
                        </div>
                        <div className="flex-1 space-y-1">
                            <h3 className="text-lg font-bold leading-tight text-foreground">
                                {employee.firstName} {employee.lastName}
                            </h3>
                            <p className="text-sm font-medium text-muted-foreground">
                                {employee.position}
                            </p>
                            <Badge variant="outline" className={`border font-semibold ${availabilityStyle}`}>
                                {t(`availability.${employee.availability}`)}
                            </Badge>
                        </div>
                    </div>

                    <Separator />

                    <div className="space-y-2">
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Mail className="size-4 shrink-0" />
                            <span className="truncate">{employee.email}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <MapPin className="size-4 shrink-0" />
                            <span className="truncate">{employee.location}</span>
                        </div>
                    </div>

                    <Separator />

                    <div className="flex gap-4">
                        <div className="flex-1 space-y-1">
                            <p className="text-2xl font-bold text-foreground">{employee.skillCount}</p>
                            <p className="text-xs text-muted-foreground">{t('stats.skills')}</p>
                        </div>
                        <div className="flex-1 space-y-1">
                            <p className="text-2xl font-bold text-foreground">{employee.totalProjects}</p>
                            <p className="text-xs text-muted-foreground">{t('stats.projects')}</p>
                        </div>
                        <div className="flex-1 space-y-1">
                            <p className="text-2xl font-bold text-foreground">{employee.yearsOfExperience}</p>
                            <p className="text-xs text-muted-foreground">{t('stats.experience')}</p>
                        </div>
                    </div>

                    {employee.skills.length > 0 && (
                        <>
                            <Separator />
                            <div className="space-y-3">
                                <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                                    <Award className="size-4" />
                                    {t('topSkills')}
                                </div>
                                <div className="flex flex-wrap gap-2">
                                    {employee.skills.map((skill) => (
                                        <Badge
                                            key={skill.id}
                                            variant="outline"
                                            className="border-border bg-muted font-semibold text-muted-foreground"
                                        >
                                            {skill.name} · S{skill.score}
                                        </Badge>
                                    ))}
                                    {remainingSkillsCount > 0 && (
                                        <Badge variant="outline" className="border-border bg-muted text-muted-foreground">
                                            +{remainingSkillsCount} {t('moreSkills')}
                                        </Badge>
                                    )}
                                </div>
                            </div>
                        </>
                    )}

                    <Button
                        variant={isAnySelectionMode && isSelected ? 'default' : 'outline'}
                        className={`w-full gap-2 font-semibold transition-all ${
                            isAnySelectionMode && isSelected
                                ? 'bg-primary text-primary-foreground hover:bg-primary/90'
                                : 'border-border hover:border-primary/20 hover:shadow-md'
                        }`}
                        onClick={handleClick}
                    >
                        {isAnySelectionMode ? (
                            isSelected ? (
                                <>
                                    <Check className="size-4" />
                                    {t('selected')}
                                </>
                            ) : (
                                t('select')
                            )
                        ) : (
                            <>
                                {t('viewProfile')}
                                <ChevronRight className="size-4" />
                            </>
                        )}
                    </Button>
                </div>
            </CardContent>
        </Card>
    );
};