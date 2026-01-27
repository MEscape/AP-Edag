package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link SkillEntity} persistence operations.
 *
 * <p>Provides CRUD operations and derived queries for managing master skill data.
 */
@Repository
public interface SkillJpaRepository extends JpaRepository<SkillEntity, UUID> {

  /**
   * Finds all active skills.
   *
   * @return list of active skills
   */
  List<SkillEntity> findByActiveTrue();

  /**
   * Finds all active skills in a specific category.
   *
   * @param categoryId the category ID
   * @return list of active skills in the category
   */
  List<SkillEntity> findByCategoryIdAndActiveTrue(UUID categoryId);

  /**
   * Finds a specific skill by its ID and category ID.
   *
   * @param categoryId the category ID
   * @param skillId the skill ID
   * @return the skill if found
   */
  Optional<SkillEntity> findByCategoryIdAndId(UUID categoryId, UUID skillId);
}
