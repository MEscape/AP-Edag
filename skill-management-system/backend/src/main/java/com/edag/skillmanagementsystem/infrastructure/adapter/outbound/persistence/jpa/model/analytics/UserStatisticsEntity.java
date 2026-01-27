package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
 * JPA entity representing aggregated statistical data for a user.
 *
 * <p>This entity stores a snapshot of computed metrics such as total skills, projects, evaluations,
 * recommendations, and average skill scores. These values support dashboard overviews and analytics
 * queries across the system.
 *
 * <h3>Characteristics</h3>
 *
 * <ul>
 *   <li>Shares its primary key with {@link UserEntity} via {@code @MapsId}.
 *   <li>Updated whenever relevant domain events trigger a recalculation.
 *   <li>Tracks the last calculation timestamp for data freshness.
 * </ul>
 */
@Entity
@Table(
    name = "user_statistics",
    indexes = {@Index(name = "idx_user_stats_last_calculated", columnList = "last_calculated")})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserStatisticsEntity {

  /**
   * Identifier of the user to whom these statistics belong.
   *
   * <p>Stored directly for efficient querying without joining to {@link UserEntity}.
   */
  @Id
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  /**
   * The user this statistics snapshot belongs to.
   *
   * <p>One-to-one relationship with the user.
   *
   * <p>Note: For most queries, use {@link #userId} directly to avoid unnecessary joins.
   */
  @OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_user_stats_user"))
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /** Total number of skills the user currently possesses. */
  @Column(name = "total_skills", nullable = false)
  @Builder.Default
  private int totalSkills = 0;

  /** Total number of projects associated with the user. */
  @Column(name = "total_projects", nullable = false)
  @Builder.Default
  private int totalProjects = 0;

  /** Total number of skill recommendations received by the user. */
  @Column(name = "total_recommendations", nullable = false)
  @Builder.Default
  private int totalRecommendations = 0;

  /** Number of currently active projects assigned to the user. */
  @Column(name = "active_projects", nullable = false)
  @Builder.Default
  private int activeProjects = 0;

  /** Average proficiency score across all user skills. Null if user has no skills yet. */
  @Column(name = "average_skill_score", precision = 5, scale = 2)
  private BigDecimal averageSkillScore = BigDecimal.ZERO;

  /**
   * Timestamp of the last statistics recalculation.
   *
   * <p>Automatically updated before insert or update operations.
   */
  @Column(name = "last_calculated", nullable = false)
  @Builder.Default
  private Instant lastCalculated = Instant.now();

  /** Ensures that {@code lastCalculated} is always refreshed on updates. */
  @PrePersist
  @PreUpdate
  protected void updateCalculationTime() {
    lastCalculated = Instant.now();
  }
}
