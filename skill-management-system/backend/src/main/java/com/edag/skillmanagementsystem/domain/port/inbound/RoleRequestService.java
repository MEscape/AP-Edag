package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for role request management operations.
 *
 * <p>Provides methods for users to request role changes and for administrators to approve or reject
 * those requests. Role changes are applied to Keycloak upon approval.
 *
 * @since 1.0.0
 */
public interface RoleRequestService {

  /**
   * Creates a new role request for a user.
   *
   * <p>Users can request USER, MANAGER, or ADMIN roles. Only one pending request per user is
   * allowed.
   *
   * @param userId the user requesting the role
   * @param requestedRole the role being requested
   * @param reason the justification for the role request
   * @return the created role request
   * @throws com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException if
   *     user already has a pending request
   */
  RoleRequest createRoleRequest(UUID userId, RoleType requestedRole, String reason);

  /**
   * Retrieves all role requests for the current user.
   *
   * @param userId the user ID
   * @return list of role requests for the user
   */
  List<RoleRequest> getMyRequests(UUID userId);

  /**
   * Retrieves role requests filtered by status with pagination and sorting.
   *
   * @param status the request status to filter by (optional, if null returns all)
   * @param pageable pagination and sorting parameters
   * @return paginated list of role requests
   */
  Page<RoleRequest> getRequestsByStatus(RequestStatus status, Pageable pageable);

  /**
   * Approves a role request and assigns the role in Keycloak (admin only).
   *
   * @param requestId the request ID
   * @param adminId the admin approving the request
   * @param adminComment optional admin comment
   * @return the updated role request
   * @throws com.edag.skillmanagementsystem.domain.exception.role.RoleRequestNotFoundException if
   *     request not found
   * @throws com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException if
   *     request is not in pending status
   * @throws com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException if
   *     Keycloak role assignment fails
   */
  RoleRequest approveRequest(UUID requestId, UUID adminId, String adminComment);

  /**
   * Rejects a role request (admin only).
   *
   * @param requestId the request ID
   * @param adminId the admin rejecting the request
   * @param adminComment the reason for rejection
   * @return the updated role request
   * @throws com.edag.skillmanagementsystem.domain.exception.role.RoleRequestNotFoundException if
   *     request not found
   * @throws com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException if
   *     request is not in pending status
   */
  RoleRequest rejectRequest(UUID requestId, UUID adminId, String adminComment);

  /**
   * Cancels a pending role request (requester only).
   *
   * @param requestId the request ID
   * @param userId the user canceling the request
   * @throws com.edag.skillmanagementsystem.domain.exception.role.RoleRequestNotFoundException if
   *     request not found
   * @throws com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException if user is
   *     not the requester
   * @throws com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException if
   *     request is not in pending status
   */
  void cancelRequest(UUID requestId, UUID userId);
}
