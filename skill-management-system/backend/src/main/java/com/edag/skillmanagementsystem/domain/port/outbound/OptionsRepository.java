package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for accessing reference/master data such as skills, categories, positions, and
 * locations.
 *
 * <p>Provides both read operations (typically cached) and administrative write operations for
 * creating new reference data entries.
 */
public interface OptionsRepository {

  // ---------------------------------------------------------------------------
  // CATEGORY
  // ---------------------------------------------------------------------------

  /** Retrieves all active skill categories. */
  List<SkillCategory> findAllActiveSkillCategories();

  /** Creates a new skill category. */
  SkillCategory createCategory(String name);

  // ---------------------------------------------------------------------------
  // SKILL
  // ---------------------------------------------------------------------------

  /** Retrieves all active skills. */
  List<Skill> findAllActiveSkills();

  /** Finds a skill by its name and category name (case-insensitive). */
  List<Skill> findActiveSkillsByCategoryId(UUID categoryId);

  /** Finds a specific skill by category ID and skill ID. */
  Optional<Skill> findSkillByCategoryIdAndSkillId(UUID categoryId, UUID skillId);

  /** Creates a new skill under a specific category. */
  Skill createSkill(String name, UUID categoryId);

  // ---------------------------------------------------------------------------
  // LOCATION
  // ---------------------------------------------------------------------------

  /** Retrieves all active locations. */
  List<Location> findAllActiveLocations();

  /** Creates a new location entry. */
  Location createLocation(String name);

  // ---------------------------------------------------------------------------
  // POSITION
  // ---------------------------------------------------------------------------

  /** Retrieves all active job positions. */
  List<Position> findAllActivePositions();

  /** Creates a new job position entry. */
  Position createPosition(String name);
}
