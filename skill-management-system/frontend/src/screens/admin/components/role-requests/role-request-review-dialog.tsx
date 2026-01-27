import { useTranslations } from 'next-intl';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import {Check, Loader2Icon, X} from 'lucide-react';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestReviewDialogProps {
    isOpen: boolean;
    selectedRequest: RoleRequest | null;
    reviewAction: 'APPROVED' | 'REJECTED' | null;
    adminComment: string;
    isReviewing: boolean;
    onClose: () => void;
    onCommentChange: (comment: string) => void;
    onSubmit: () => void;
}

export const RoleRequestReviewDialog = ({
                                            isOpen,
                                            selectedRequest,
                                            reviewAction,
                                            adminComment,
                                            isReviewing,
                                            onClose,
                                            onCommentChange,
                                            onSubmit,
                                        }: RoleRequestReviewDialogProps) => {
    const t = useTranslations('admin.roleRequests');

    return (
        <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        {reviewAction === 'APPROVED' ? t('approveTitle') : t('rejectTitle')}
                    </DialogTitle>
                    <DialogDescription>
                        {reviewAction === 'APPROVED'
                            ? t('approveDescription', { username: selectedRequest?.username! })
                            : t('rejectDescription', { username: selectedRequest?.username! })}
                    </DialogDescription>
                </DialogHeader>

                <div className="space-y-4 py-4">
                    <div className="space-y-2">
                        <Label htmlFor="adminComment">{t('adminCommentLabel')}</Label>
                        <Textarea
                            id="adminComment"
                            placeholder={t('adminCommentPlaceholder')}
                            value={adminComment}
                            onChange={(e) => onCommentChange(e.target.value)}
                            rows={4}
                        />
                        <p className="text-xs text-muted-foreground">{t('adminCommentHint')}</p>
                    </div>
                </div>

                <DialogFooter>
                    <Button variant="outline" onClick={onClose} disabled={isReviewing}>
                        {t('cancel')}
                    </Button>
                    <Button
                        variant={reviewAction === 'APPROVED' ? 'default' : 'destructive'}
                        onClick={onSubmit}
                        disabled={isReviewing}
                    >
                        {isReviewing ? (
                            <Loader2Icon
                                aria-hidden="true"
                                className="mr-2 size-3 animate-spin"
                            />
                        ) : reviewAction === 'APPROVED' ? (
                            <Check className="mr-2 size-4" />
                        ) : (
                            <X className="mr-2 size-4" />
                        )}
                        {reviewAction === 'APPROVED' ? t('confirmApprove') : t('confirmReject')}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};