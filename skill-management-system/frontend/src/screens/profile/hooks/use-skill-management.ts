import { useState, useCallback, useMemo } from 'react';
import {
    useAddSkillMutation,
    useUpdateSkillMutation,
    useDeleteSkillMutation,
    type AddSkillRequest,
    type UpdateSkillRequest,
    type ProfileSkill,
} from '@/store/api/profile-api';
import {
    useGetSkillCategoriesQuery,
    useGetAllSkillsQuery,
    useGetSkillsByCategoryQuery,
} from '@/store/api/option-api';
import { toast } from 'sonner';
import { useTranslations } from 'next-intl';
import { useDialog } from '@/hooks/use-dialog';

export const useSkillManagement = (userId: string = 'me', profileSkills?: readonly ProfileSkill[]) => {
    const t = useTranslations('profile');

    // Dialog State mit useDialog Hook
    const { isOpen, open, close } = useDialog();
    const [dialogMode, setDialogMode] = useState<'add' | 'edit'>('add');
    const [editingSkill, setEditingSkill] = useState<ProfileSkill | undefined>();
    const [selectedCategoryId, setSelectedCategoryId] = useState<string>('');

    // API Queries
    const { data: categoriesData } = useGetSkillCategoriesQuery();
    const { data: allSkillsData } = useGetAllSkillsQuery(undefined, {
        skip: !!selectedCategoryId,
    });
    const { data: categorySkillsData } = useGetSkillsByCategoryQuery(selectedCategoryId, {
        skip: !selectedCategoryId,
    });

    // Mutations
    const [addSkill, { isLoading: isAdding }] = useAddSkillMutation();
    const [updateSkill, { isLoading: isUpdating }] = useUpdateSkillMutation();
    const [deleteSkill, { isLoading: isDeleting }] = useDeleteSkillMutation();

    // Computed values
    const skillCategories = useMemo(() => categoriesData?.categories || [], [categoriesData]);
    const skillSuggestions = useMemo(
        () => (selectedCategoryId ? categorySkillsData?.skills || [] : allSkillsData?.skills || []),
        [selectedCategoryId, categorySkillsData, allSkillsData]
    );

    const skillsByCategory = useMemo(() => {
        if (!profileSkills) return {};

        return profileSkills.reduce((acc, skill) => {
            if (!acc[skill.category]) {
                acc[skill.category] = [];
            }
            acc[skill.category]!.push(skill);
            return acc;
        }, {} as Record<string, ProfileSkill[]>);
    }, [profileSkills]);

    // Dialog Handlers
    const openAddDialog = useCallback(() => {
        setDialogMode('add');
        setEditingSkill(undefined);
        open();
    }, [open]);

    const openEditDialog = useCallback((skill: ProfileSkill) => {
        setDialogMode('edit');
        setEditingSkill(skill);
        open();
    }, [open]);

    const closeDialog = useCallback(() => {
        close();
        setEditingSkill(undefined);
    }, [close]);

    // Mutation Handlers
    const handleSaveSkill = useCallback(
        async (skill: AddSkillRequest, skillId?: string) => {
            try {
                if (!skillId) {
                    await addSkill({ userId, skill }).unwrap();
                    toast.success(t('skillDialog.addSuccess'));
                } else {
                    const updateData: UpdateSkillRequest = {
                        score: skill.score,
                        yearsOfExperience: skill.yearsOfExperience,
                        lastUsed: skill.lastUsed,
                    };
                    await updateSkill({ userId, skillId, data: updateData }).unwrap();
                    toast.success(t('skillDialog.updateSuccess'));
                }
                closeDialog();
                return true;
            } catch (error) {
                toast.error(t('skillDialog.saveError'));
                return false;
            }
        },
        [userId, addSkill, updateSkill, closeDialog, t]
    );

    const handleDeleteSkill = useCallback(
        async (skillId: string) => {
            try {
                await deleteSkill({ userId, skillId }).unwrap();
                toast.success(t('skills.deleteSuccess'));
                return true;
            } catch (error) {
                toast.error(t('skills.deleteError'));
                return false;
            }
        },
        [userId, deleteSkill, t]
    );

    return {
        // Dialog State
        isSkillDialogOpen: isOpen,
        dialogMode,
        editingSkill,
        openAddDialog,
        openEditDialog,
        closeDialog,

        // Skills Data
        skillCategories,
        skillSuggestions,
        skillsByCategory,
        selectedCategoryId,
        setSelectedCategoryId,

        // Actions
        handleSaveSkill,
        handleDeleteSkill,

        // Loading States
        isAdding,
        isUpdating,
        isDeleting,
    };
};