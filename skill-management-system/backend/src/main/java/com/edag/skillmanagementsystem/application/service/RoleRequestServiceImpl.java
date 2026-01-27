package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException;
import com.edag.skillmanagementsystem.domain.exception.role.RoleRequestNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException;
import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import com.edag.skillmanagementsystem.domain.port.inbound.RoleRequestService;
import com.edag.skillmanagementsystem.domain.port.outbound.KeycloakAdminClient;
import com.edag.skillmanagementsystem.domain.port.outbound.RoleRequestRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the RoleRequestService interface.
 *
 * <p>Orchestrates role request management operations including creation, approval, rejection, and
 * integration with Keycloak for role assignment.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleRequestServiceImpl implements RoleRequestService {

  private final RoleRequestRepository roleRequestRepository;
  private final KeycloakAdminClient keycloakAdminClient;

  @Override
  public RoleRequest createRoleRequest(UUID userId, RoleType requestedRole, String reason) {
    log.debug("Creating role request for user: {} requesting role: {}", userId, requestedRole);

    // Check if user already has a pending request
    if (roleRequestRepository.existsByUserIdAndStatus(userId, RequestStatus.PENDING)) {
      log.warn("User {} already has a pending role request", userId);
      throw new InvalidRoleRequestException("error.role.request.already.pending", userId);
    }

    RoleRequest roleRequest =
        RoleRequest.builder()
            .userId(userId)
            .requestedRole(requestedRole)
            .status(RequestStatus.PENDING)
            .reason(reason)
            .build();

    RoleRequest saved = roleRequestRepository.save(roleRequest);
    log.info(
        "Role request created with ID: {} for user: {} requesting role: {}",
        saved.id(),
        userId,
        requestedRole);

    return saved;
  }

  @Override
  @Transactional(readOnly = true)
  public List<RoleRequest> getMyRequests(UUID userId) {
    log.debug("Retrieving role requests for user: {}", userId);
    return roleRequestRepository.findByUserId(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RoleRequest> getRequestsByStatus(RequestStatus status, Pageable pageable) {
    log.debug("Retrieving role requests with status: {} and pagination: {}", status, pageable);

    if (status == null) {
      return roleRequestRepository.findAll(pageable);
    }

    return roleRequestRepository.findByStatus(status, pageable);
  }

  @Override
  public RoleRequest approveRequest(UUID requestId, UUID adminId, String adminComment) {
    log.debug("Approving role request: {} by admin: {}", requestId, adminId);

    RoleRequest request = getRequestById(requestId);

    // Validate request is pending
    if (request.status() != RequestStatus.PENDING) {
      log.warn("Cannot approve role request {} - status is {}", requestId, request.status());
      throw new InvalidRoleRequestException("error.role.request.not.pending", requestId);
    }

    try {
      // Assign role in Keycloak
      keycloakAdminClient.assignRoleToUser(request.userId(), request.requestedRole());
      log.info(
          "Successfully assigned role {} to user {} in Keycloak",
          request.requestedRole(),
          request.userId());
    } catch (Exception e) {
      log.error(
          "Failed to assign role {} to user {} in Keycloak",
          request.requestedRole(),
          request.userId(),
          e);
      throw e; // Re-throw to trigger transaction rollback
    }

    // Update request status
    request = request.withApproved(adminId, adminComment);

    RoleRequest updated = roleRequestRepository.save(request);
    log.info(
        "Role request {} approved by admin {}. Role {} assigned to user {}",
        requestId,
        adminId,
        request.requestedRole(),
        request.userId());

    return updated;
  }

  @Override
  public RoleRequest rejectRequest(UUID requestId, UUID adminId, String adminComment) {
    log.debug("Rejecting role request: {} by admin: {}", requestId, adminId);

    RoleRequest request = getRequestById(requestId);

    // Validate request is pending
    if (request.status() != RequestStatus.PENDING) {
      log.warn("Cannot reject role request {} - status is {}", requestId, request.status());
      throw new InvalidRoleRequestException("error.role.request.not.pending", requestId);
    }

    // Update request status
    request = request.withRejected(adminId, adminComment);

    RoleRequest updated = roleRequestRepository.save(request);
    log.info("Role request {} rejected by admin {}", requestId, adminId);

    return updated;
  }

  @Override
  public void cancelRequest(UUID requestId, UUID userId) {
    log.debug("Canceling role request: {} by user: {}", requestId, userId);

    RoleRequest request = getRequestById(requestId);

    // Validate user owns the request
    if (!request.userId().equals(userId)) {
      log.warn(
          "User {} attempted to cancel request {} owned by {}",
          userId,
          requestId,
          request.userId());
      throw new AccessDeniedException("error.forbidden", requestId.toString());
    }

    // Validate request is pending
    if (request.status() != RequestStatus.PENDING) {
      log.warn("Cannot cancel role request {} - status is {}", requestId, request.status());
      throw new InvalidRoleRequestException("error.role.request.not.pending", requestId);
    }

    roleRequestRepository.deleteById(requestId);
    log.info("Role request {} canceled by user {}", requestId, userId);
  }

  /**
   * Retrieves a role request by its ID, throwing an exception if not found. *
   *
   * @param requestId the ID of the role request
   * @return the RoleRequest
   */
  private RoleRequest getRequestById(UUID requestId) {
    log.debug("Retrieving role request: {}", requestId);
    return roleRequestRepository
        .findById(requestId)
        .orElseThrow(
            () -> {
              log.warn("Role request not found: {}", requestId);
              return new RoleRequestNotFoundException(requestId);
            });
  }
}
