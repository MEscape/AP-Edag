import { useTranslations } from 'next-intl';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Clock, CheckCircle, XCircle } from 'lucide-react';
import { RoleRequestList } from './role-request-list';
import { useRoleRequests } from '../../hooks/use-role-requests';
import type { RoleRequest } from '@/store/api/role-request-api';

interface RoleRequestsTabsProps {
    onReviewClick: (request: RoleRequest, action: 'APPROVED' | 'REJECTED') => void;
    isReviewing: boolean;
}

export const RoleRequestsTabs = ({
                                     onReviewClick,
                                     isReviewing,
                                 }: RoleRequestsTabsProps) => {
    const t = useTranslations('admin.roleRequests');

    const pending = useRoleRequests('PENDING');
    const approved = useRoleRequests('APPROVED');
    const rejected = useRoleRequests('REJECTED');

    return (
        <Tabs defaultValue="pending" className="space-y-6">
            <TabsList className="grid w-full grid-cols-3">
                <TabsTrigger value="pending" className="gap-2">
                    <Clock className="size-4" />
                    {t('tabs.pending')} ({pending.totalElements})
                </TabsTrigger>
                <TabsTrigger value="approved" className="gap-2">
                    <CheckCircle className="size-4" />
                    {t('tabs.approved')} ({approved.totalElements})
                </TabsTrigger>
                <TabsTrigger value="rejected" className="gap-2">
                    <XCircle className="size-4" />
                    {t('tabs.rejected')} ({rejected.totalElements})
                </TabsTrigger>
            </TabsList>

            <TabsContent value="pending" className="space-y-4">
                <RoleRequestList
                    requests={pending.requests}
                    isLoading={pending.isLoading}
                    emptyIcon={Clock}
                    emptyMessage={t('noPending')}
                    showActions
                    onReviewClick={onReviewClick}
                    isReviewing={isReviewing}
                    page={pending.page}
                    totalPages={pending.totalPages}
                    totalElements={pending.totalElements}
                    onPageChange={pending.setPage}
                />
            </TabsContent>

            <TabsContent value="approved" className="space-y-4">
                <RoleRequestList
                    requests={approved.requests}
                    isLoading={approved.isLoading}
                    emptyIcon={CheckCircle}
                    emptyMessage={t('noApproved')}
                    page={approved.page}
                    totalPages={approved.totalPages}
                    totalElements={approved.totalElements}
                    onPageChange={approved.setPage}
                />
            </TabsContent>

            <TabsContent value="rejected" className="space-y-4">
                <RoleRequestList
                    requests={rejected.requests}
                    isLoading={rejected.isLoading}
                    emptyIcon={XCircle}
                    emptyMessage={t('noRejected')}
                    page={rejected.page}
                    totalPages={rejected.totalPages}
                    totalElements={rejected.totalElements}
                    onPageChange={rejected.setPage}
                />
            </TabsContent>
        </Tabs>
    );
};