'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Shield } from 'lucide-react';
import { useRoleRequestsReview } from '../../hooks/use-role-requests-review';
import { RoleRequestsTabs } from '../role-requests/role-requests-tab';
import { RoleRequestReviewDialog } from '../role-requests/role-request-review-dialog';

export const RoleRequestsSection = () => {
    const t = useTranslations('admin.roleRequests');

    const reviewHook = useRoleRequestsReview();

    return (
        <>
            <Card className="border-border bg-white shadow-edag">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                        <Shield className="size-5 text-primary" />
                        {t('title')}
                    </CardTitle>
                    <CardDescription className="text-base text-muted-foreground">
                        {t('description')}
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <RoleRequestsTabs
                        onReviewClick={reviewHook.openReviewDialog}
                        isReviewing={reviewHook.isReviewing}
                    />
                </CardContent>
            </Card>

            <RoleRequestReviewDialog
                isOpen={!!reviewHook.selectedRequest}
                selectedRequest={reviewHook.selectedRequest}
                reviewAction={reviewHook.reviewAction}
                adminComment={reviewHook.adminComment}
                isReviewing={reviewHook.isReviewing}
                onClose={reviewHook.closeReviewDialog}
                onCommentChange={reviewHook.setAdminComment}
                onSubmit={reviewHook.submitReview}
            />
        </>
    );
};