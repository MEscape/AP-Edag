package com.edag.skillmanagementsystem.domain.model.role;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

/**
 * Domain model representing a role change request.
 *
 * <p>Users can request role changes (USER, MANAGER, ADMIN) which must be approved or rejected by
 * administrators. The request tracks the lifecycle of the request including timestamps, reviewer
 * information, and comments.
 *
 * @since 1.0.0
 */
@Builder
public record RoleRequest(

    /** Unique identifier for the role request. */
    UUID id,

    /** User ID who requested the role change. */
    UUID userId,

    /** Username of the requester for display purposes. */
    String username,

    /** Email of the requester for display purposes. */
    String email,

    /** The role being requested. */
    RoleType requestedRole,

    /** Current status of the request. */
    RequestStatus status,

    /** User-provided reason for requesting the role. */
    String reason,

    /** Admin who reviewed (approved/rejected) the request. */
    UUID reviewedBy,

    /** Username of the reviewer for display purposes. */
    String reviewerUsername,

    /** Timestamp when the request was reviewed. */
    Instant reviewedAt,

    /** Admin's comment on their decision. */
    String adminComment,

    /** Timestamp when the request was created. */
    Instant createdAt,

    /** Timestamp when the request was last updated. */
    Instant updatedAt) {

  /**
   * Returns a new {@code RoleRequest} representing an approved request.
   *
   * @param adminId the ID of the admin approving the request
   * @param adminComment the review comment provided by the admin
   * @return a new {@code RoleRequest} instance with updated approval fields
   */
  public RoleRequest withApproved(UUID adminId, String adminComment) {
    return new RoleRequest(
        id,
        userId,
        username,
        email,
        requestedRole,
        RequestStatus.APPROVED, // updated
        reason,
        adminId, // updated
        reviewerUsername,
        Instant.now(), // updated
        adminComment, // updated
        createdAt,
        Instant.now() // updated last modified timestamp
        );
  }

  /**
   * Returns a new {@code RoleRequest} representing a rejected request.
   *
   * @param adminId the ID of the admin rejecting the request
   * @param adminComment the review comment provided by the admin
   * @return a new {@code RoleRequest} instance with updated rejection fields
   */
  public RoleRequest withRejected(UUID adminId, String adminComment) {
    return new RoleRequest(
        id,
        userId,
        username,
        email,
        requestedRole,
        RequestStatus.REJECTED, // updated
        reason,
        adminId, // updated
        reviewerUsername,
        Instant.now(), // updated
        adminComment, // updated
        createdAt,
        Instant.now() // updated last modified timestamp
        );
  }
}
