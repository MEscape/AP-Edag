import { useState, useCallback } from 'react';
import { useTranslations } from 'next-intl';
import { toast } from 'sonner';
import {
    useCreateProjectMutation,
    useUpdateProjectMutation,
    useDeleteProjectMutation,
    type Project,
    type CreateProjectRequest,
    type UpdateProjectRequest,
} from '@/store/api/project-api';
import { useDialog } from '@/hooks/use-dialog';

type DialogMode = 'create' | 'edit';

export const useProjectDialog = () => {
    const t = useTranslations('discoverProjects');

    // Use the shared dialog hook fully
    const { isOpen, open, close, setIsOpen } = useDialog(false);

    const [mode, setMode] = useState<DialogMode>('create');
    const [editingProject, setEditingProject] = useState<Project | null>(null);

    const [createMutation, { isLoading: isCreating }] = useCreateProjectMutation();
    const [updateMutation, { isLoading: isUpdating }] = useUpdateProjectMutation();
    const [deleteMutation, { isLoading: isDeleting }] = useDeleteProjectMutation();

    const isSaving = isCreating || isUpdating;

    // ---- OPEN DIALOG ----
    const openCreateDialog = useCallback(() => {
        setMode('create');
        setEditingProject(null);
        open();
    }, [open]);

    const openEditDialog = useCallback(
        (project: Project) => {
            setMode('edit');
            setEditingProject(project);
            open();
        },
        [open]
    );

    // ---- CLOSE DIALOG ----
    const closeDialog = useCallback(() => {
        setEditingProject(null);
        close();
    }, [close]);

    // ---- HANDLE CREATE ----
    const handleCreate = async (data: CreateProjectRequest) => {
        try {
            await createMutation(data).unwrap();
            toast.success(t('createSuccess', { name: data.name }));
            closeDialog();
            return true;
        } catch {
            toast.error(t('createError'));
            return false;
        }
    };

    // ---- HANDLE UPDATE ----
    const handleUpdate = async (projectId: string, data: UpdateProjectRequest) => {
        try {
            await updateMutation({ projectId, data }).unwrap();
            toast.success(t('updateSuccess'));
            closeDialog();
            return true;
        } catch {
            toast.error(t('updateError'));
            return false;
        }
    };

    // ---- HANDLE DELETE ----
    const handleDelete = async (projectId: string) => {
        if (!confirm(t('deleteConfirmation'))) return false;

        try {
            await deleteMutation(projectId).unwrap();
            toast.success(t('deleteSuccess'));
            return true;
        } catch {
            toast.error(t('deleteError'));
            return false;
        }
    };

    return {
        isOpen,
        open: openCreateDialog,
        close: closeDialog,
        setIsOpen,

        mode,
        editingProject,
        isSaving,
        isDeleting,

        openCreateDialog,
        openEditDialog,
        closeDialog,

        handleCreate,
        handleUpdate,
        handleDelete,
    };
};
