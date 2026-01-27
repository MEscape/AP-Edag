import { useState } from 'react';
import { useTranslations } from 'next-intl';
import { useApproveRoleRequestMutation, useRejectRoleRequestMutation } from '@/store/api/admin-api';
import { toast } from 'sonner';
import type { RoleRequest } from '@/store/api/admin-api';

type ReviewAction = 'APPROVED' | 'REJECTED';

export const useRoleRequestsReview = () => {
    const t = useTranslations('admin.roleRequests');
    const [selectedRequest, setSelectedRequest] = useState<RoleRequest | null>(null);
    const [reviewAction, setReviewAction] = useState<ReviewAction | null>(null);
    const [adminComment, setAdminComment] = useState('');

    const [approveRequest, { isLoading: isApproving }] = useApproveRoleRequestMutation();
    const [rejectRequest, { isLoading: isRejecting }] = useRejectRoleRequestMutation();

    const isReviewing = isApproving || isRejecting;

    const openReviewDialog = (request: RoleRequest, action: ReviewAction) => {
        setSelectedRequest(request);
        setReviewAction(action);
        setAdminComment('');
    };

    const closeReviewDialog = () => {
        setSelectedRequest(null);
        setReviewAction(null);
        setAdminComment('');
    };

    const submitReview = async () => {
        if (!selectedRequest || !reviewAction) return;

        try {
            const body = adminComment.trim() ? { adminComment: adminComment.trim() } : undefined;

            if (reviewAction === 'APPROVED') {
                await approveRequest({
                    requestId: selectedRequest.id,
                    body,
                }).unwrap();
            } else {
                await rejectRequest({
                    requestId: selectedRequest.id,
                    body,
                }).unwrap();
            }

            toast.success(
                reviewAction === 'APPROVED'
                    ? t('approveSuccess', { username: selectedRequest.username })
                    : t('rejectSuccess', { username: selectedRequest.username })
            );

            closeReviewDialog();
        } catch (error) {
            toast.error(t('reviewError'));
        }
    };

    return {
        selectedRequest,
        reviewAction,
        adminComment,
        isReviewing,
        openReviewDialog,
        closeReviewDialog,
        setAdminComment,
        submitReview,
    };
};