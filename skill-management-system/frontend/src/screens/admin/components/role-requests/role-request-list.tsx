import { RoleRequestsSkeleton } from '../ui/loading-skeletons';
import { EmptyState } from '@/components/ui/empty-state';
import { RoleRequestCard } from './role-request-card';
import { RoleRequestsPagination } from './role-requests-pagination';
import type { RoleRequest } from '@/store/api/role-request-api';
import type { LucideIcon } from 'lucide-react';

interface RoleRequestListProps {
    requests: RoleRequest[];
    isLoading: boolean;
    emptyIcon: LucideIcon;
    emptyMessage: string;
    showActions?: boolean;
    onReviewClick?: (request: RoleRequest, action: 'APPROVED' | 'REJECTED') => void;
    isReviewing?: boolean;
    // Pagination
    page: number;
    totalPages: number;
    totalElements: number;
    onPageChange: (page: number) => void;
}

export const RoleRequestList = ({
                                    requests,
                                    isLoading,
                                    emptyIcon,
                                    emptyMessage,
                                    showActions = false,
                                    onReviewClick,
                                    isReviewing = false,
                                    page,
                                    totalPages,
                                    totalElements,
                                    onPageChange,
                                }: RoleRequestListProps) => {
    if (isLoading) {
        return <RoleRequestsSkeleton />;
    }

    if (!requests || requests.length === 0) {
        return <EmptyState icon={emptyIcon} message={emptyMessage} />;
    }

    return (
        <div className="space-y-4">
            <div className="space-y-4">
                {requests.map((request) => (
                    <RoleRequestCard
                        key={request.id}
                        request={request}
                        showActions={showActions}
                        onReviewClick={onReviewClick}
                        isReviewing={isReviewing}
                    />
                ))}
            </div>
            {totalPages > 1 && (
                <RoleRequestsPagination
                    page={page}
                    totalPages={totalPages}
                    totalElements={totalElements}
                    onPageChange={onPageChange}
                    onNextPage={() => onPageChange(page + 1)}
                    onPreviousPage={() => onPageChange(page - 1)}
                    isLoading={isLoading}
                />
            )}
        </div>
    );
};