import {
  useGetMyRoleRequestsQuery,
  useCreateRoleRequestMutation,
  useCancelRoleRequestMutation,
  type RoleType,
} from '@/store/api/role-request-api';
import { toast } from 'sonner';
import { useTranslations } from 'next-intl';

export const useRoleRequests = () => {
  const t = useTranslations('dashboard.roleRequest');

  const {
    data: requests,
    isLoading,
    isError,
    refetch
  } = useGetMyRoleRequestsQuery();

  const [createMutation, { isLoading: isCreating }] = useCreateRoleRequestMutation();
  const [cancelMutation, { isLoading: isCancelling }] = useCancelRoleRequestMutation();

  const pendingRequests = requests?.filter((r) => r.status === 'PENDING') || [];
  const historyRequests = requests?.filter((r) => r.status !== 'PENDING') || [];

  const createRoleRequest = async (params: { requestedRole: RoleType; reason: string }) => {
    try {
      await createMutation(params).unwrap();
      toast.success(t('createSuccess'));
      return { success: true as const };
    } catch (err: any) {
      const errorKey = err.status === 400 ? 'alreadyHasPendingRequest' : 'genericError';
      toast.error(t(errorKey));
      return { success: false as const, error: errorKey };
    }
  };

  const cancelRoleRequest = async (requestId: string) => {
    try {
      await cancelMutation(requestId).unwrap();
      toast.success(t('cancelSuccess'));
      return { success: true as const };
    } catch {
      toast.error(t('cancelError'));
      return { success: false as const };
    }
  };

  return {
    requests: requests || [],
    pendingRequests,
    historyRequests,
    isLoading,
    isError,
    isCreating,
    isCancelling,
    createRoleRequest,
    cancelRoleRequest,
    refetch,
  };
};