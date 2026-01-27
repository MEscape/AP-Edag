package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import com.edag.skillmanagementsystem.domain.port.inbound.OptionsService;
import com.edag.skillmanagementsystem.domain.port.outbound.OptionsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the OptionsService interface.
 *
 * <p>This service orchestrates the retrieval of reference data options such as skill categories,
 * skills, positions, and locations. It delegates persistence operations to the OptionsRepository
 * and returns domain models that can be transformed by the presentation layer.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OptionsServiceImpl implements OptionsService {

  private final OptionsRepository optionsRepository;

  @Override
  public List<SkillCategory> getAvailableSkillCategories() {
    log.debug("Retrieving available skill categories");

    List<SkillCategory> categories = optionsRepository.findAllActiveSkillCategories();

    log.debug("Found {} active skill categories", categories.size());

    return categories;
  }

  @Override
  public List<Skill> getAllAvailableSkills() {
    log.debug("Retrieving all available skills");

    List<Skill> skills = optionsRepository.findAllActiveSkills();

    log.debug("Found {} active skills", skills.size());

    return skills;
  }

  @Override
  public List<Skill> getAvailableSkillsByCategory(UUID categoryId) {
    log.debug("Retrieving available skills for categoryId: {}", categoryId);

    List<Skill> skills = optionsRepository.findActiveSkillsByCategoryId(categoryId);

    log.debug("Found {} active skills for categoryId '{}'", skills.size(), categoryId);

    return skills;
  }

  @Override
  public List<Position> getAvailablePositions() {
    log.debug("Retrieving available positions");

    List<Position> positions = optionsRepository.findAllActivePositions();

    log.debug("Found {} active positions", positions.size());

    return positions;
  }

  @Override
  public List<Location> getAvailableLocations() {
    log.debug("Retrieving available locations");

    List<Location> locations = optionsRepository.findAllActiveLocations();

    log.debug("Found {} active locations", locations.size());

    return locations;
  }

  // ---------------------------------------------------------------------------
  // ADMIN OPERATIONS
  // ---------------------------------------------------------------------------

  @Override
  public SkillCategory createSkillCategory(String name) {
    log.info("Creating new skill category: {}", name);

    SkillCategory category = optionsRepository.createCategory(name);

    log.info("Created skill category with ID: {}", category.id());

    return category;
  }

  @Override
  public Skill createSkill(String name, UUID categoryId) {
    log.info("Creating new skill '{}' in category {}", name, categoryId);

    Skill skill = optionsRepository.createSkill(name, categoryId);

    log.info("Created skill with ID: {}", skill.id());

    return skill;
  }

  @Override
  public Position createPosition(String name) {
    log.info("Creating new position: {}", name);

    Position position = optionsRepository.createPosition(name);

    log.info("Created position with ID: {}", position.id());

    return position;
  }

  @Override
  public Location createLocation(String name) {
    log.info("Creating new location: {}", name);

    Location location = optionsRepository.createLocation(name);

    log.info("Created location with ID: {}", location.id());

    return location;
  }
}
