'use client';

import { useProfile } from '../../hooks/use-profile';
import { useSkillManagement } from '../../hooks/use-skill-management';
import { SkillsContent } from '../skills/skills-content';
import { AddEditSkillDialog } from '../skills/add-edit-skill-dialog';
import { SkillsSkeleton } from '../ui/loading-skeletons';

interface SkillsSectionProps {
    userId: string;
}

export const SkillsSection = ({ userId }: SkillsSectionProps) => {
    const { profile, isOwnProfile, isLoading } = useProfile(userId);

    const {
        isSkillDialogOpen,
        dialogMode,
        editingSkill,
        openAddDialog,
        openEditDialog,
        closeDialog,
        skillCategories,
        skillSuggestions,
        skillsByCategory,
        selectedCategoryId,
        setSelectedCategoryId,
        handleSaveSkill,
        handleDeleteSkill,
    } = useSkillManagement(userId, profile?.skills);

    if (isLoading) {
        return <SkillsSkeleton isOwnProfile={isOwnProfile} />;
    }

    return (
        <>
            <SkillsContent
                skills={profile?.skills}
                skillsByCategory={skillsByCategory}
                isOwnProfile={isOwnProfile}
                onAddSkill={openAddDialog}
                onEditSkill={openEditDialog}
                onDeleteSkill={handleDeleteSkill}
            />

            {isOwnProfile && (
                <AddEditSkillDialog
                    open={isSkillDialogOpen}
                    onOpenChange={closeDialog}
                    mode={dialogMode}
                    currentSkill={editingSkill}
                    categories={skillCategories}
                    suggestions={skillSuggestions}
                    selectedCategoryId={selectedCategoryId}
                    onSelectCategory={setSelectedCategoryId}
                    onSave={handleSaveSkill}
                />
            )}
        </>
    );
};
