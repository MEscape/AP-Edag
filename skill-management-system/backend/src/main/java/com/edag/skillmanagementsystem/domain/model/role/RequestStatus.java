package com.edag.skillmanagementsystem.domain.model.role;

/**
 * Enumeration of role request statuses.
 *
 * <p>Represents the lifecycle status of a role change request.
 *
 * @since 1.0.0
 */
public enum RequestStatus {
  /** Request is pending admin review. */
  PENDING,

  /** Request has been approved by an admin. */
  APPROVED,

  /** Request has been rejected by an admin. */
  REJECTED
}
