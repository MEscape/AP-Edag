package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for role request information.
 *
 * @since 1.0.0
 */
@Schema(description = "Role request information")
public record RoleRequestResponseDto(
    @Schema(description = "Request ID", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
    @Schema(
            description = "User ID who made the request",
            example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
    @Schema(description = "Username of requester", example = "john.doe") String username,
    @Schema(description = "Email of requester", example = "john.doe@example.com") String email,
    @Schema(description = "Requested role", example = "MANAGER") RoleType requestedRole,
    @Schema(description = "Request status", example = "PENDING") RequestStatus status,
    @Schema(description = "User's reason for requesting the role") String reason,
    @Schema(description = "Admin who reviewed the request") UUID reviewedBy,
    @Schema(description = "Username of reviewer", example = "admin.user") String reviewerUsername,
    @Schema(description = "Timestamp when reviewed") Instant reviewedAt,
    @Schema(description = "Admin's comment on the decision") String adminComment,
    @Schema(description = "Timestamp when request was created") Instant createdAt,
    @Schema(description = "Timestamp when request was last updated") Instant updatedAt) {

  /**
   * Creates a response DTO from a domain model.
   *
   * @param roleRequest the domain model
   * @return the response DTO
   */
  public static RoleRequestResponseDto from(RoleRequest roleRequest) {
    return new RoleRequestResponseDto(
        roleRequest.id(),
        roleRequest.userId(),
        roleRequest.username(),
        roleRequest.email(),
        roleRequest.requestedRole(),
        roleRequest.status(),
        roleRequest.reason(),
        roleRequest.reviewedBy(),
        roleRequest.reviewerUsername(),
        roleRequest.reviewedAt(),
        roleRequest.adminComment(),
        roleRequest.createdAt(),
        roleRequest.updatedAt());
  }
}
