import { useState } from 'react';
import type { RoleType } from '@/store/api/role-request-api';

interface UseRoleRequestFormParams {
  onSubmit: (params: {
    requestedRole: RoleType;
    reason: string
  }) => Promise<{ success: boolean; error?: string }>;
  onSuccess?: () => void;
}

export const useRoleRequestForm = ({
                                     onSubmit,
                                     onSuccess,
                                   }: UseRoleRequestFormParams) => {
  const [selectedRole, setSelectedRole] = useState<RoleType>('MANAGER');
  const [reason, setReason] = useState('');
  const [localError, setLocalError] = useState<string | null>(null);

  const resetForm = () => {
    setSelectedRole('MANAGER');
    setReason('');
    setLocalError(null);
  };

  const handleSubmit = async (t: (key: string) => string) => {
    setLocalError(null);

    // Validation
    if (!reason || reason.trim().length < 10) {
      setLocalError(t('roleRequest.reasonTooShort'));
      return;
    }

    if (reason.length > 1000) {
      setLocalError(t('roleRequest.reasonTooLong'));
      return;
    }

    const result = await onSubmit({
      requestedRole: selectedRole,
      reason: reason.trim(),
    });

    if (result.success) {
      resetForm();
      onSuccess?.();
    } else if (result.error) {
      setLocalError(t(result.error));
    }
  };

  return {
    selectedRole,
    setSelectedRole,
    reason,
    setReason,
    localError,
    setLocalError,
    handleSubmit,
    resetForm,
  };
};
