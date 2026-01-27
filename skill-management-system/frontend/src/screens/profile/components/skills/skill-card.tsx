'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { Edit, Trash2, Calendar } from 'lucide-react';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import { getSkillLevelLabel } from '../../utils/skill-variants';
import type { ProfileSkill } from '@/store/api/profile-api';

interface SkillCardProps {
    skill: ProfileSkill;
    isOwnProfile: boolean;
    onEdit: () => void;
    onDelete: () => void;
}

export const SkillCard = ({ skill, isOwnProfile, onEdit, onDelete }: SkillCardProps) => {
    const t = useTranslations('profile');
    const { formatDate } = useFormatLocalizedDate();

    const scoreLabel = getSkillLevelLabel(skill.proficiencyScore, t);

    return (
        <Card className="group relative transition-all hover:border-primary/30 hover:shadow-md">
            <CardHeader className="pb-3">
                <div className="flex items-start justify-between gap-2">
                    <div className="flex-1 space-y-1">
                        <CardTitle className="text-base font-semibold">{skill.skillName}</CardTitle>
                        <Badge variant="secondary" className="text-xs">
                            {skill.yearsOfExperience}{' '}
                            {skill.yearsOfExperience === 1 ? t('skills.year') : t('skills.years')}
                        </Badge>
                    </div>
                    {isOwnProfile && (
                        <div className="flex gap-1 opacity-0 transition-opacity group-hover:opacity-100">
                            <Button variant="ghost" size="icon" className="size-8" onClick={onEdit}>
                                <Edit className="size-3.5" />
                            </Button>
                            <Button
                                variant="ghost"
                                size="icon"
                                className="size-8 hover:bg-destructive/10"
                                onClick={onDelete}
                            >
                                <Trash2 className="size-3.5 text-destructive" />
                            </Button>
                        </div>
                    )}
                </div>
            </CardHeader>

            <CardContent className="space-y-3 pt-0">
                <div className="space-y-1.5">
                    <div className="flex items-center justify-between text-xs">
                        <span className="font-medium text-muted-foreground">{scoreLabel}</span>
                        <span className="font-semibold text-foreground">{skill.proficiencyScore}/100</span>
                    </div>
                    <Progress value={skill.proficiencyScore} className="h-2" />
                </div>

                {skill.lastUsed && (
                    <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                        <Calendar className="size-3" />
                        <span>
              {t('skills.lastUsed')}{' '}
                            {formatDate(skill.lastUsed, {
                                day: '2-digit',
                                month: 'short',
                                year: 'numeric',
                            })}
            </span>
                    </div>
                )}
            </CardContent>
        </Card>
    );
};