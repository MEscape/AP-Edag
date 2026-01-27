package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.option;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillCategoryEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Maps between JPA entities and domain models for reference data options.
 *
 * <p>This mapper handles the transformation of skills, categories, and locations from persistence
 * entities to domain objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionsMapper {

  /**
   * Converts a skill category entity to domain model.
   *
   * @param entity the skill category entity
   * @return the skill category domain model, or null if entity is null
   */
  public static SkillCategory categoryEntityToDomain(SkillCategoryEntity entity) {
    if (entity == null) {
      return null;
    }

    return SkillCategory.builder()
        .id(entity.getId())
        .name(entity.getName())
        .active(entity.isActive())
        .build();
  }

  /**
   * Converts a skill entity to domain model.
   *
   * @param entity the skill entity
   * @return the skill domain model, or null if entity is null
   */
  public static Skill skillEntityToDomain(SkillEntity entity) {
    if (entity == null) {
      return null;
    }

    return Skill.builder()
        .id(entity.getId())
        .name(entity.getName())
        .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
        .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
        .active(entity.isActive())
        .build();
  }

  /**
   * Converts a location entity to domain model.
   *
   * @param entity the location entity
   * @return the location domain model, or null if entity is null
   */
  public static Location locationEntityToDomain(LocationEntity entity) {
    if (entity == null) {
      return null;
    }

    return Location.builder()
        .id(entity.getId())
        .name(entity.getName())
        .active(entity.isActive())
        .build();
  }

  /**
   * Converts a position entity to domain model.
   *
   * @param entity the position entity
   * @return the position domain model, or null if entity is null
   */
  public static Position positionEntityToDomain(PositionEntity entity) {
    if (entity == null) {
      return null;
    }

    return Position.builder()
        .id(entity.getId())
        .name(entity.getName())
        .active(entity.isActive())
        .build();
  }
}
