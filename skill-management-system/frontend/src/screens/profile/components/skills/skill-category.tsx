'use client';

import { SkillCard } from './skill-card';
import type { ProfileSkill } from '@/store/api/profile-api';

interface SkillCategoryProps {
    category: string;
    skills: readonly ProfileSkill[];
    isExpanded: boolean;
    onToggle: () => void;
    isOwnProfile: boolean;
    onEditSkill: (skill: ProfileSkill) => void;
    onDeleteSkill: (skillId: string) => void;
}

export const SkillCategory = ({
                                  category,
                                  skills,
                                  isExpanded,
                                  onToggle,
                                  isOwnProfile,
                                  onEditSkill,
                                  onDeleteSkill,
                              }: SkillCategoryProps) => {
    return (
        <div className="space-y-4">
            <button
                onClick={onToggle}
                className="flex w-full items-center justify-between text-left transition-colors hover:text-primary"
            >
                <h3 className="text-base font-semibold text-foreground">
                    {category}
                    <span className="ml-2 text-sm font-normal text-muted-foreground">
            ({skills.length})
          </span>
                </h3>
                <span className="text-muted-foreground">{isExpanded ? '−' : '+'}</span>
            </button>

            {isExpanded && (
                <div className="grid gap-4 sm:grid-cols-2">
                    {skills.map((skill) => (
                        <SkillCard
                            key={skill.id}
                            skill={skill}
                            isOwnProfile={isOwnProfile}
                            onEdit={() => onEditSkill(skill)}
                            onDelete={() => onDeleteSkill(skill.id)}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};