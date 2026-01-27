package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.ActivityEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting {@link ActivityEntity} persistence objects into {@link Activity}
 * domain models.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActivityMapper {

  /**
   * Maps an {@link ActivityEntity} to its corresponding {@link Activity} domain model.
   *
   * @param entity the activity entity to convert; may be {@code null}
   * @return the mapped domain activity, or {@code null} if the input is null
   */
  public static Activity entityToDomain(ActivityEntity entity) {
    if (entity == null) {
      return null;
    }

    return Activity.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .activityType(entity.getType())
        .timestamp(entity.getTimestamp())
        .relatedEntityId(entity.getEntityId())
        .relatedEntityType(entity.getEntityType())
        .build();
  }
}
