package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.domain.port.inbound.RoleRequestService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role.CreateRoleRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role.ReviewRoleRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role.RoleRequestPageResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role.RoleRequestResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.security.AuthorizationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for role request management operations.
 *
 * <p>This controller provides endpoints for users to request role changes and for administrators to
 * approve or reject those requests. All role change operations integrate with Keycloak to apply
 * role assignments.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/role-requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Request Management", description = "APIs for managing user role change requests")
public class RoleRequestController {

  private final RoleRequestService roleRequestService;
  private final AuthorizationUtils authorizationUtils;

  /**
   * Creates a new role request.
   *
   * <p>Allows authenticated users to request a role change (USER, MANAGER, ADMIN). Only one pending
   * request per user is allowed at any given time.
   *
   * @param request the role request creation payload
   * @param principal the authenticated user principal
   * @return the created role request
   */
  @Operation(
      summary = "Create role request",
      description =
          "Creates a new role change request. Users may request USER, MANAGER, or ADMIN roles. "
              + "Only one pending request per user is allowed.")
  @ApiResponse(
      responseCode = "201",
      description = "Role request created successfully",
      content = @Content(schema = @Schema(implementation = RoleRequestResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Invalid request data or user already has a pending request",
      content = @Content)
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @PostMapping
  public ResponseEntity<RoleRequestResponseDto> createRoleRequest(
      @Valid @RequestBody CreateRoleRequestDto request, Principal principal) {

    log.debug("REST request to create role request: {}", request.requestedRole());

    UUID userId = authorizationUtils.resolveUserId("me", principal);
    RoleRequest created =
        roleRequestService.createRoleRequest(userId, request.requestedRole(), request.reason());

    log.info(
        "Role request created with ID: {} for user: {} requesting role: {}",
        created.id(),
        userId,
        request.requestedRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(RoleRequestResponseDto.from(created));
  }

  /**
   * Retrieves all role requests made by the current user.
   *
   * @param principal the authenticated user principal
   * @return list of role requests belonging to the current user
   */
  @Operation(
      summary = "Get my role requests",
      description = "Retrieves all role requests made by the currently authenticated user.")
  @ApiResponse(
      responseCode = "200",
      description = "Role requests retrieved successfully",
      content =
          @Content(
              array =
                  @ArraySchema(schema = @Schema(implementation = RoleRequestResponseDto.class))))
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @GetMapping("/me")
  public ResponseEntity<List<RoleRequestResponseDto>> getMyRequests(Principal principal) {

    log.debug("REST request to get my role requests");

    UUID userId = authorizationUtils.resolveUserId("me", principal);
    List<RoleRequest> requests = roleRequestService.getMyRequests(userId);

    return ResponseEntity.ok(requests.stream().map(RoleRequestResponseDto::from).toList());
  }

  /**
   * Retrieves role requests filtered by status with pagination and sorting.
   *
   * <p>Only users with the ADMIN role may access this endpoint.
   *
   * @param status the request status to filter by (PENDING, APPROVED, REJECTED)
   * @param page the page number (zero-based, default: 0)
   * @param size the page size (default: 20)
   * @param sortBy the field to sort by (default: createdAt)
   * @param sortOrder the sort order: asc or desc (default: desc)
   * @return paginated list of role requests filtered by status
   */
  @Operation(
      summary = "Get role requests by status",
      description =
          "Retrieves role requests filtered by status (PENDING, APPROVED, REJECTED) with pagination support. Admin only.")
  @ApiResponse(
      responseCode = "200",
      description = "Role requests retrieved successfully",
      content = @Content(schema = @Schema(implementation = RoleRequestPageResponseDto.class)))
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User does not have ADMIN role",
      content = @Content)
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoleRequestPageResponseDto> getRequestsByStatus(
      @Parameter(
              description = "Filter by status (PENDING, APPROVED, REJECTED)",
              example = "APPROVED")
          @RequestParam(required = false)
          com.edag.skillmanagementsystem.domain.model.role.RequestStatus status,
      @Parameter(description = "Page number (zero-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20")
          int size,
      @Parameter(
              description = "Sort by field (createdAt, status, requestedRole)",
              example = "createdAt")
          @RequestParam(defaultValue = "createdAt")
          String sortBy,
      @Parameter(description = "Sort order (asc or desc)", example = "desc")
          @RequestParam(defaultValue = "desc")
          String sortOrder) {

    log.debug(
        "REST request to get role requests - status: {}, page: {}, size: {}, sortBy: {}, sortOrder: {}",
        status,
        page,
        size,
        sortBy,
        sortOrder);

    // Build pageable with sorting
    Sort sort =
        sortOrder.equalsIgnoreCase("desc")
            ? Sort.by(mapSortField(sortBy)).descending()
            : Sort.by(mapSortField(sortBy)).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    // Execute search
    Page<RoleRequest> results = roleRequestService.getRequestsByStatus(status, pageable);

    log.debug(
        "Role requests retrieval completed - found {} of {} total requests",
        results.getNumberOfElements(),
        results.getTotalElements());

    return ResponseEntity.ok(RoleRequestPageResponseDto.from(results));
  }

  /**
   * Approves a pending role request.
   *
   * <p>Only users with the ADMIN role may approve requests. The approval triggers role assignment
   * in Keycloak.
   *
   * @param requestId the unique identifier of the role request
   * @param review optional admin comment for the approval
   * @param principal the authenticated admin principal
   * @return the approved role request
   */
  @Operation(
      summary = "Approve role request",
      description =
          "Approves a pending role request and assigns the requested role in Keycloak. Admin only.")
  @ApiResponse(
      responseCode = "200",
      description = "Role request approved successfully",
      content = @Content(schema = @Schema(implementation = RoleRequestResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Request is not in pending status",
      content = @Content)
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User does not have ADMIN role",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Role request not found", content = @Content)
  @ApiResponse(
      responseCode = "500",
      description = "Keycloak role assignment failed",
      content = @Content)
  @PostMapping("/{requestId}/approve")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoleRequestResponseDto> approveRequest(
      @Parameter(description = "Role request ID", example = "550e8400-e29b-41d4-a716-446655440000")
          @PathVariable
          UUID requestId,
      @Valid @RequestBody(required = false) ReviewRoleRequestDto review,
      Principal principal) {

    log.debug("REST request to approve role request: {}", requestId);

    UUID adminId = authorizationUtils.resolveUserId("me", principal);
    String adminComment = review != null ? review.adminComment() : null;

    RoleRequest approved = roleRequestService.approveRequest(requestId, adminId, adminComment);

    log.info("Role request approved: {} by admin: {}", requestId, adminId);

    return ResponseEntity.ok(RoleRequestResponseDto.from(approved));
  }

  /**
   * Rejects a pending role request.
   *
   * <p>Only users with the ADMIN role may reject requests.
   *
   * @param requestId the unique identifier of the role request
   * @param review optional admin comment for the rejection
   * @param principal the authenticated admin principal
   * @return the rejected role request
   */
  @Operation(
      summary = "Reject role request",
      description = "Rejects a pending role request with an optional admin comment. Admin only.")
  @ApiResponse(
      responseCode = "200",
      description = "Role request rejected successfully",
      content = @Content(schema = @Schema(implementation = RoleRequestResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Request is not in pending status",
      content = @Content)
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User does not have ADMIN role",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Role request not found", content = @Content)
  @PostMapping("/{requestId}/reject")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoleRequestResponseDto> rejectRequest(
      @Parameter(description = "Role request ID", example = "550e8400-e29b-41d4-a716-446655440000")
          @PathVariable
          UUID requestId,
      @Valid @RequestBody(required = false) ReviewRoleRequestDto review,
      Principal principal) {

    log.debug("REST request to reject role request: {}", requestId);

    UUID adminId = authorizationUtils.resolveUserId("me", principal);
    String adminComment = review != null ? review.adminComment() : null;

    RoleRequest rejected = roleRequestService.rejectRequest(requestId, adminId, adminComment);

    log.info("Role request rejected: {} by admin: {}", requestId, adminId);

    return ResponseEntity.ok(RoleRequestResponseDto.from(rejected));
  }

  /**
   * Cancels a pending role request.
   *
   * <p>Users may only cancel their own pending requests.
   *
   * @param requestId the unique identifier of the role request
   * @param principal the authenticated user principal
   * @return an empty response with status 204
   */
  @Operation(
      summary = "Cancel role request",
      description =
          "Cancels a pending role request. Users can only cancel their own pending requests.")
  @ApiResponse(responseCode = "204", description = "Role request canceled successfully")
  @ApiResponse(
      responseCode = "400",
      description = "Request is not in pending status",
      content = @Content)
  @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User can only cancel their own requests",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Role request not found", content = @Content)
  @DeleteMapping("/{requestId}")
  public ResponseEntity<Void> cancelRequest(
      @Parameter(description = "Role request ID", example = "550e8400-e29b-41d4-a716-446655440000")
          @PathVariable
          UUID requestId,
      Principal principal) {

    log.debug("REST request to cancel role request: {}", requestId);

    UUID userId = authorizationUtils.resolveUserId("me", principal);
    roleRequestService.cancelRequest(requestId, userId);

    log.info("Role request canceled: {} by user: {}", requestId, userId);

    return ResponseEntity.noContent().build();
  }

  /**
   * Maps sort field names from API to domain model fields.
   *
   * @param sortBy the API sort field
   * @return the mapped domain field name
   */
  private String mapSortField(String sortBy) {
    return switch (sortBy.toLowerCase()) {
      case "createdat" -> "createdAt";
      case "status" -> "status";
      case "requestedrole" -> "requestedRole";
      case "reviewedat" -> "reviewedAt";
      default -> "createdAt";
    };
  }
}
