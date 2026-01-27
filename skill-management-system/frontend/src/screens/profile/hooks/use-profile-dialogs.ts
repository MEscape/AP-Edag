import { useCallback } from 'react';
import {
    useUpdateProfileMutation,
    type UpdateProfileRequest,
} from '@/store/api/profile-api';
import { toast } from 'sonner';
import { useTranslations } from 'next-intl';
import { useDialog } from '@/hooks/use-dialog';

export const useProfileDialogs = (userId: string = 'me') => {
    const t = useTranslations('profile');
    const { isOpen, open, close } = useDialog();
    const [updateProfile, { isLoading: isUpdating }] = useUpdateProfileMutation();

    const handleUpdateProfile = useCallback(
        async (data: UpdateProfileRequest) => {
            try {
                await updateProfile({ userId, data }).unwrap();
                toast.success(t('editDialog.updateSuccess'));
                close();
                return true;
            } catch (error) {
                toast.error(t('editDialog.updateError'));
                return false;
            }
        },
        [userId, updateProfile, close, t]
    );

    return {
        isEditDialogOpen: isOpen,
        openEditDialog: open,
        closeEditDialog: close,
        handleUpdateProfile,
        isUpdating,
    };
};