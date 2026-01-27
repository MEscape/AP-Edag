package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.role;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JPA entity representing a role change request in the system.
 *
 * <p>This entity captures user requests for role changes (e.g., USER to MANAGER, MANAGER to ADMIN),
 * including the requested role, current status, justification, and administrative review details.
 * It supports a workflow where users submit requests that administrators can approve or reject.
 *
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "role_requests",
    indexes = {
      @Index(name = "idx_role_requests_user_status", columnList = "user_id, status"),
      @Index(name = "idx_role_requests_status", columnList = "status"),
      @Index(name = "idx_role_requests_created_at", columnList = "created_at")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoleRequestEntity extends BaseAuditEntity {

  /** Unique identifier of the role request. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** Identifier of the user who submitted the role change request. */
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  /**
   * User account that submitted this role request.
   *
   * <p>Loaded lazily to optimize performance. Mapped as read-only to the user_id column.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_role_request_user"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /**
   * Role being requested by the user (e.g., USER, MANAGER, ADMIN).
   *
   * <p>Represents the desired role the user wishes to be assigned upon approval.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "requested_role", nullable = false, length = 50)
  private RoleType requestedRole;

  /**
   * Current status of the role request (PENDING, APPROVED, REJECTED, CANCELED).
   *
   * <p>Defaults to PENDING when a new request is created.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private RequestStatus status = RequestStatus.PENDING;

  /**
   * Justification or reason provided by the user for requesting the role change.
   *
   * <p>Optional text explaining why the user needs the requested role. Can be used by
   * administrators during the review process.
   */
  @Column(name = "reason", columnDefinition = "TEXT", length = 1000)
  private String reason;

  /**
   * Identifier of the administrator who reviewed this request.
   *
   * <p>Null if the request has not yet been reviewed. Set when an admin approves or rejects the
   * request.
   */
  @Column(name = "reviewed_by")
  private UUID reviewedBy;

  /**
   * Administrator account that reviewed this role request.
   *
   * <p>Loaded lazily and mapped as read-only to the reviewed_by column. Null for pending requests.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "reviewed_by",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_role_request_reviewer"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity reviewer;

  /**
   * Timestamp when the request was reviewed by an administrator.
   *
   * <p>Null for pending requests. Set automatically when the request is approved or rejected.
   */
  @Column(name = "reviewed_at")
  private Instant reviewedAt;

  /**
   * Optional comment or feedback provided by the administrator during review.
   *
   * <p>Can contain the reason for approval or rejection, or additional instructions for the user.
   */
  @Column(name = "admin_comment", columnDefinition = "TEXT", length = 1000)
  private String adminComment;
}
