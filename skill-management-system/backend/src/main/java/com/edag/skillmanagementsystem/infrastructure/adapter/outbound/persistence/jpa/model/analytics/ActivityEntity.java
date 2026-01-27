package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.ActivityType;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
import org.hibernate.annotations.Immutable;

/**
 * JPA entity representing a user activity event within the system.
 *
 * <p>This entity stores immutable audit-like records describing user interactions, including
 * activity type, timestamp, and optionally a related domain entity. Activity entries are typically
 * used for analytics, monitoring, and user history views.
 *
 * <p>The table is indexed for efficient filtering by user, activity type, and related entities.
 *
 * <h3>Characteristics</h3>
 *
 * <ul>
 *   <li>Immutable – activity records cannot be updated once persisted.
 *   <li>Timestamped – automatically assigned at creation time.
 *   <li>Contextual – can reference another entity via {@code entityType} and {@code entityId}.
 * </ul>
 */
@Entity
@Table(
    name = "activities",
    indexes = {
      @Index(name = "idx_activities_user_timestamp", columnList = "user_id, timestamp DESC")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Immutable
public class ActivityEntity {

  /** Unique identifier of the activity record. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /**
   * Identifier of the user who triggered this activity.
   *
   * <p>Stored directly for efficient querying without joining to {@link UserEntity}.
   */
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  /**
   * User who triggered this activity.
   *
   * <p>Loaded lazily due to its potential size and relationships.
   */
  @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_activity_user"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /** Classification of the activity (e.g., CREATED_SKILL, UPDATED_PROFILE). */
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 64)
  private ActivityType type;

  /**
   * Timestamp when the activity occurred.
   *
   * <p>Automatically assigned during persistence.
   */
  @Column(name = "timestamp", nullable = false, updatable = false)
  @Builder.Default
  private Instant timestamp = Instant.now();

  /**
   * Identifier of the related entity (optional).
   *
   * <p>Used when the activity refers to a specific object such as a skill or project.
   */
  @Column(name = "entity_id")
  private UUID entityId;

  /**
   * Type of the related entity (optional).
   *
   * <p>Stored as a simple string for flexibility across domain objects.
   */
  @Column(name = "entity_type", length = 50)
  private String entityType;

  /** Ensures a timestamp is set before persisting the entity. */
  @PrePersist
  protected void onCreate() {
    if (timestamp == null) {
      timestamp = Instant.now();
    }
  }
}
