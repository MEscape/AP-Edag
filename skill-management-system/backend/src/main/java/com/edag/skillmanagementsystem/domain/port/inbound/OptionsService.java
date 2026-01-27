package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for retrieving available reference data options.
 *
 * <p>Provides methods to fetch lists of available skills, categories, positions, and locations that
 * can be used for dropdowns and selection in the UI. Returns full domain models to allow the
 * presentation layer flexibility in data transformation.
 *
 * @since 1.0.0
 */
public interface OptionsService {

  /**
   * Retrieves all available skill categories.
   *
   * @return list of skill category domain models
   */
  List<SkillCategory> getAvailableSkillCategories();

  /**
   * Retrieves all available skills across all categories.
   *
   * @return list of all skill domain models
   */
  List<Skill> getAllAvailableSkills();

  /**
   * Retrieves all available skills filtered by category.
   *
   * @param categoryId the category ID to filter by
   * @return list of skill domain models in the specified category
   */
  List<Skill> getAvailableSkillsByCategory(UUID categoryId);

  /**
   * Retrieves all available positions/roles.
   *
   * @return list of position domain models
   */
  List<Position> getAvailablePositions();

  /**
   * Retrieves all available work locations.
   *
   * @return list of location domain models
   */
  List<Location> getAvailableLocations();

  // ---------------------------------------------------------------------------
  // ADMIN OPERATIONS
  // ---------------------------------------------------------------------------

  /**
   * Creates a new skill category (Admin only).
   *
   * @param name the name of the category
   * @return the created skill category
   */
  SkillCategory createSkillCategory(String name);

  /**
   * Creates a new skill under a specific category (Admin only).
   *
   * @param name the name of the skill
   * @param categoryId the category ID
   * @return the created skill
   */
  Skill createSkill(String name, UUID categoryId);

  /**
   * Creates a new position (Admin only).
   *
   * @param name the name of the position
   * @return the created position
   */
  Position createPosition(String name);

  /**
   * Creates a new location (Admin only).
   *
   * @param name the name of the location
   * @return the created location
   */
  Location createLocation(String name);
}
