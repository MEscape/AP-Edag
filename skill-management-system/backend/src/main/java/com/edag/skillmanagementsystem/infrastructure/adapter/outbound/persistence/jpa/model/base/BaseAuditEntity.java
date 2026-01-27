package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract base class providing auditing support for JPA entities.
 *
 * <p>This superclass supplies automatically managed timestamps that track when an entity was
 * created and last updated. Spring Data JPA's {@link AuditingEntityListener} populates these fields
 * during persistence lifecycle events.
 *
 * <h3>Characteristics</h3>
 *
 * <ul>
 *   <li>Marked as {@code @MappedSuperclass}, so fields are inherited by child entities.
 *   <li>{@code @CreatedDate} is populated once and never changed.
 *   <li>{@code @LastModifiedDate} is updated on each modifying operation.
 * </ul>
 *
 * <p>Useful for all entities requiring consistent audit metadata.
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity {

  /** Timestamp when the entity was created. Automatically assigned once. */
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  /** Timestamp of the most recent update to this entity. */
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
