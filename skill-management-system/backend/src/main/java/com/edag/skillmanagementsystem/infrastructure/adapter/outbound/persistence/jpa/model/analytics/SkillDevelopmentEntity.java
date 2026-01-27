package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JPA entity representing aggregated monthly skill development metrics for a user.
 *
 * <p>This entity captures month-by-month trends such as total skills, added or updated skills,
 * proficiency evolution, and the top skill category. It is used for analytics dashboards and
 * long-term growth tracking.
 *
 * <h3>Constraints & Indexing</h3>
 *
 * <ul>
 *   <li>A unique record exists per user and month.
 *   <li>Indexed by {@code user_id} and {@code month} for efficient range queries such as retrieving
 *       recent development history.
 * </ul>
 */
@Entity
@Table(
    name = "skill_development",
    indexes = {@Index(name = "idx_skill_development_user_month", columnList = "user_id, month")},
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_skill_development_user_month",
          columnNames = {"user_id", "month"})
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SkillDevelopmentEntity {

  /** Unique identifier of this skill development record. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /**
   * Identifier of the user to whom this skill development snapshot belongs.
   *
   * <p>Stored directly for efficient querying without joining to {@link UserEntity}.
   */
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  /**
   * User to whom this skill development snapshot belongs.
   *
   * <p>Each user has at most one record per month.
   *
   * <p>Note: For most queries, use {@link #userId} directly to avoid unnecessary joins.
   */
  @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_skill_development_user"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /**
   * The calendar month this record represents.
   *
   * <p>Stored as the first day of the month for consistency.
   */
  @Column(name = "month", nullable = false)
  private LocalDate month;

  /** Total number of skills the user had in the given month. */
  @Column(name = "total_skills", nullable = false)
  @Builder.Default
  private int totalSkills = 0;

  /** Number of new skills added during the month. */
  @Column(name = "skills_added", nullable = false)
  @Builder.Default
  private int skillsAdded = 0;

  /** Number of skills that were updated (e.g., proficiency changed) during the month. */
  @Column(name = "skills_updated", nullable = false)
  @Builder.Default
  private int skillsUpdated = 0;

  /** Number of skills removed from the user's profile during the month. */
  @Column(name = "skills_removed", nullable = false)
  @Builder.Default
  private int skillsRemoved = 0;

  /** Most prominent skill category for the month (optional). */
  @Column(name = "top_category")
  private String topCategory;
}
