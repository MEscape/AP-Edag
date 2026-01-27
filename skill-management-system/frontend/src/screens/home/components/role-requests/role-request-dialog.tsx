'use client';

import { useEffect } from 'react';
import { useTranslations } from 'next-intl';
import { Shield, AlertCircle, Loader2 } from 'lucide-react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { useRoleRequestForm } from '../../hooks/use-role-request-form';
import type { RoleType } from '@/store/api/role-request-api';

interface RoleRequestDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSubmit: (params: {
    requestedRole: RoleType;
    reason: string;
  }) => Promise<{ success: boolean; error?: string }>;
  isLoading: boolean;
  error: string | null;
  onClearError: () => void;
}

const ROLE_OPTIONS: RoleType[] = ['MANAGER', 'ADMIN'];

export const RoleRequestDialog = ({
                                    open,
                                    onOpenChange,
                                    onSubmit,
                                    isLoading,
                                    error,
                                    onClearError,
                                  }: RoleRequestDialogProps) => {
  const t = useTranslations('dashboard');

  const {
    selectedRole,
    setSelectedRole,
    reason,
    setReason,
    localError,
    setLocalError,
    handleSubmit: handleFormSubmit,
    resetForm,
  } = useRoleRequestForm({
    onSubmit,
    onSuccess: () => onOpenChange(false),
  });

  useEffect(() => {
    if (!open) {
      setLocalError(null);
      onClearError();
    }
  }, [open, onClearError, setLocalError]);

  const handleCancel = () => {
    resetForm();
    onClearError();
    onOpenChange(false);
  };

  const displayError = localError || (error ? t(error as any) : null);

  return (
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="max-w-xl">
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2 text-xl">
              <Shield className="size-5 text-primary" />
              {t('roleRequest.title')}
            </DialogTitle>
            <DialogDescription className="text-base">
              {t('roleRequest.description')}
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-5 py-4">
            <div className="space-y-3">
              <Label className="text-sm font-semibold">{t('roleRequest.selectRole')}</Label>
              <div className="grid grid-cols-2 gap-3">
                {ROLE_OPTIONS.map((role) => (
                    <button
                        key={role}
                        type="button"
                        onClick={() => setSelectedRole(role)}
                        className={`rounded-lg border-2 p-4 text-left transition-all hover:border-primary/50 ${
                            selectedRole === role
                                ? 'border-primary bg-primary/5 shadow-sm'
                                : 'border-border bg-white'
                        }`}
                    >
                      <div className="font-semibold">
                        {t(`roleRequest.roles.${role.toLowerCase()}` as any)}
                      </div>
                      <div className="mt-1 text-sm text-muted-foreground">
                        {t(`roleRequest.roleDescriptions.${role.toLowerCase()}` as any)}
                      </div>
                    </button>
                ))}
              </div>
            </div>

            <div className="space-y-3">
              <Label htmlFor="reason" className="text-sm font-semibold">
                {t('roleRequest.reason')}
              </Label>
              <Textarea
                  id="reason"
                  placeholder={t('roleRequest.reasonPlaceholder')}
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  className="min-h-[120px] resize-none"
                  maxLength={1000}
              />
              <div className="flex items-center justify-between text-xs text-muted-foreground">
                <span>{t('roleRequest.reasonHint')}</span>
                <span>{reason.length}/1000</span>
              </div>
            </div>

            {displayError && (
                <div className="flex items-start gap-2 rounded-lg border border-destructive/20 bg-destructive/5 p-3">
                  <AlertCircle className="mt-0.5 size-4 shrink-0 text-destructive" />
                  <p className="text-sm text-destructive">{displayError}</p>
                </div>
            )}
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={handleCancel} disabled={isLoading}>
              {t('roleRequest.cancel')}
            </Button>
            <Button
                onClick={() => handleFormSubmit(t as any)}
                disabled={isLoading || !reason || reason.trim().length < 10}
            >
              {isLoading && <Loader2 className="mr-2 size-4 animate-spin" />}
              {t('roleRequest.submit')}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
  );
};
