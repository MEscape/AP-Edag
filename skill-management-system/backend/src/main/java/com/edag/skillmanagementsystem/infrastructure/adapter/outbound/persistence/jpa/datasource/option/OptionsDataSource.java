package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.option;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import com.edag.skillmanagementsystem.domain.port.outbound.OptionsRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillCategoryEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.LocationJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.PositionJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.SkillCategoryJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.SkillJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA-based implementation of the OptionsRepository.
 *
 * <p>This datasource handles all read and write operations for reference data such as skills,
 * categories, positions, and locations. Results are cached to improve performance as reference data
 * changes infrequently.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OptionsDataSource implements OptionsRepository {

  private final SkillCategoryJpaRepository categoryRepository;
  private final SkillJpaRepository skillRepository;
  private final PositionJpaRepository positionRepository;
  private final LocationJpaRepository locationRepository;

  // ---------------------------------------------------------------------------
  // CATEGORY
  // ---------------------------------------------------------------------------

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "skillCategories", unless = "#result.isEmpty()")
  public List<SkillCategory> findAllActiveSkillCategories() {
    log.debug("Finding all active skill categories");

    return categoryRepository.findByActiveTrue().stream()
        .map(OptionsMapper::categoryEntityToDomain)
        .toList();
  }

  @Override
  @Transactional
  public SkillCategory createCategory(String name) {
    log.debug("Creating new category: {}", name);

    SkillCategoryEntity category = SkillCategoryEntity.builder().name(name).active(true).build();

    SkillCategoryEntity saved = categoryRepository.save(category);
    return OptionsMapper.categoryEntityToDomain(saved);
  }

  // ---------------------------------------------------------------------------
  // SKILL
  // ---------------------------------------------------------------------------

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "skills", unless = "#result.isEmpty()")
  public List<Skill> findAllActiveSkills() {
    log.debug("Finding all active skills");

    return skillRepository.findByActiveTrue().stream()
        .map(OptionsMapper::skillEntityToDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "skillsByCategory", key = "#categoryId", unless = "#result.isEmpty()")
  public List<Skill> findActiveSkillsByCategoryId(UUID categoryId) {
    log.debug("Finding active skills for categoryId: {}", categoryId);

    return skillRepository.findByCategoryIdAndActiveTrue(categoryId).stream()
        .map(OptionsMapper::skillEntityToDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "skillByCategoryAndId", key = "#categoryId + '-' + #skillId")
  public Optional<Skill> findSkillByCategoryIdAndSkillId(UUID categoryId, UUID skillId) {
    log.debug("Finding skill by categoryId: {} and skillId: {}", categoryId, skillId);

    return skillRepository
        .findByCategoryIdAndId(categoryId, skillId)
        .map(OptionsMapper::skillEntityToDomain);
  }

  @Override
  @Transactional
  public Skill createSkill(String name, UUID categoryId) {
    log.debug("Creating skill '{}' in category {}", name, categoryId);

    SkillCategoryEntity category =
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

    SkillEntity skill = SkillEntity.builder().name(name).category(category).active(true).build();

    SkillEntity saved = skillRepository.save(skill);
    return OptionsMapper.skillEntityToDomain(saved);
  }

  // ---------------------------------------------------------------------------
  // LOCATION
  // ---------------------------------------------------------------------------

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "locations", unless = "#result.isEmpty()")
  public List<Location> findAllActiveLocations() {
    log.debug("Finding all active locations");

    return locationRepository.findByActiveTrue().stream()
        .map(OptionsMapper::locationEntityToDomain)
        .toList();
  }

  @Override
  @Transactional
  public Location createLocation(String name) {
    log.debug("Creating location '{}'", name);

    LocationEntity location = LocationEntity.builder().name(name).active(true).build();

    LocationEntity saved = locationRepository.save(location);
    return OptionsMapper.locationEntityToDomain(saved);
  }

  // ---------------------------------------------------------------------------
  // POSITIONS
  // ---------------------------------------------------------------------------

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "positions", unless = "#result.isEmpty()")
  public List<Position> findAllActivePositions() {
    log.debug("Finding all active positions");

    return positionRepository.findByActiveTrue().stream()
        .map(OptionsMapper::positionEntityToDomain)
        .toList();
  }

  @Override
  @Transactional
  public Position createPosition(String name) {
    log.debug("Creating position: {}", name);

    PositionEntity position = PositionEntity.builder().name(name).active(true).build();

    PositionEntity saved = positionRepository.save(position);
    return OptionsMapper.positionEntityToDomain(saved);
  }
}
