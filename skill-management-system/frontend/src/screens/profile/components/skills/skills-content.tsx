'use client';

import { useState, useMemo } from 'react';
import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Plus, Award } from 'lucide-react';
import { SkillCategory } from './skill-category';
import type { ProfileSkill } from '@/store/api/profile-api';

interface SkillsContentProps {
    skills?: readonly ProfileSkill[];
    skillsByCategory: Record<string, readonly ProfileSkill[]>;
    isOwnProfile: boolean;
    onAddSkill: () => void;
    onEditSkill: (skill: ProfileSkill) => void;
    onDeleteSkill: (skillId: string) => Promise<boolean>;
}

export const SkillsContent = ({
                                  skills,
                                  skillsByCategory,
                                  isOwnProfile,
                                  onAddSkill,
                                  onEditSkill,
                                  onDeleteSkill,
                              }: SkillsContentProps) => {
    const t = useTranslations('profile');

    const initialExpandedCategories = useMemo(
        () => new Set(Object.keys(skillsByCategory)),
        [skillsByCategory]
    );

    const [expandedCategories, setExpandedCategories] = useState<Set<string>>(
        initialExpandedCategories
    );

    const toggleCategory = (category: string) => {
        setExpandedCategories((prev) => {
            const newSet = new Set(prev);
            if (newSet.has(category)) {
                newSet.delete(category);
            } else {
                newSet.add(category);
            }
            return newSet;
        });
    };

    const handleDeleteSkill = async (skillId: string) => {
        if (globalThis.window.confirm(t('skills.deleteConfirm'))) {
            await onDeleteSkill(skillId);
        }
    };

    const hasSkills = skills && skills.length > 0;
    const categories = Object.entries(skillsByCategory);

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <div className="flex items-center justify-between">
                    <CardTitle className="text-xl font-bold tracking-tight">
                        {t('skills.title')}
                    </CardTitle>
                    {isOwnProfile && (
                        <Button onClick={onAddSkill} size="sm" className="gap-2">
                            <Plus className="size-4" />
                            {t('skills.addSkill')}
                        </Button>
                    )}
                </div>
            </CardHeader>
            <CardContent>
                {hasSkills ? (
                    <div className="space-y-6">
                        {categories.map(([category, categorySkills]) => (
                            <SkillCategory
                                key={category}
                                category={category}
                                skills={categorySkills}
                                isExpanded={expandedCategories.has(category)}
                                onToggle={() => toggleCategory(category)}
                                isOwnProfile={isOwnProfile}
                                onEditSkill={onEditSkill}
                                onDeleteSkill={handleDeleteSkill}
                            />
                        ))}
                    </div>
                ) : (
                    <EmptySkillsState isOwnProfile={isOwnProfile} onAddSkill={onAddSkill} />
                )}
            </CardContent>
        </Card>
    );
};

function EmptySkillsState({
                              isOwnProfile,
                              onAddSkill,
                          }: {
    isOwnProfile: boolean;
    onAddSkill: () => void;
}) {
    const t = useTranslations('profile');

    return (
        <div className="flex flex-col items-center justify-center gap-4 py-12 text-center">
            <div className="flex size-16 items-center justify-center rounded-lg bg-muted">
                <Award className="size-8 text-muted-foreground" />
            </div>
            <div className="space-y-2">
                <h3 className="text-lg font-semibold text-foreground">{t('skills.noSkills')}</h3>
                <p className="text-sm text-muted-foreground">
                    {isOwnProfile
                        ? t('skills.noSkillsDescriptionOwn')
                        : t('skills.noSkillsDescription')}
                </p>
            </div>
            {isOwnProfile && (
                <Button onClick={onAddSkill} className="mt-4 gap-2">
                    <Plus className="size-4" />
                    {t('skills.addFirstSkill')}
                </Button>
            )}
        </div>
    );
}