package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillCategoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link SkillCategoryEntity} persistence operations.
 *
 * <p>Provides CRUD operations and derived queries for managing master skill category data.
 */
@Repository
public interface SkillCategoryJpaRepository extends JpaRepository<SkillCategoryEntity, UUID> {

  /**
   * Finds all active skill categories.
   *
   * @return list of active skill categories
   */
  List<SkillCategoryEntity> findByActiveTrue();
}
