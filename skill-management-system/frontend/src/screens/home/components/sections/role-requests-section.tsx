'use client';

import { useRoleRequests } from '../../hooks/use-role-requests';
import { RoleRequestList } from '../role-requests/role-request-list';

export const RoleRequestsSection = () => {
  const {
    requests,
    pendingRequests,
    historyRequests,
    isLoading,
    isError,
    isCancelling,
    cancelRoleRequest,
  } = useRoleRequests();

  // don't render the section if there are no requests
  if (!isLoading &&
      !isError &&
      requests.length === 0 &&
      pendingRequests.length === 0 &&
      historyRequests.length === 0) {
    return null;
  }

  return (
      <RoleRequestList
          requests={requests}
          pendingRequests={pendingRequests}
          historyRequests={historyRequests}
          isLoading={isLoading}
          isError={isError}
          isCancelling={isCancelling}
          onCancel={cancelRoleRequest}
      />
  );
};
