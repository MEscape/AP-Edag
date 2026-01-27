package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Service interface for managing user skills in profiles.
 *
 * <p>Provides methods to add, update, and delete skills associated with user profiles, including
 * validation of skill existence, activity status, and duplication checks.
 *
 * @since 1.0.0
 */
public interface SkillManagementService {

  /**
   * Adds a new skill to a user's profile.
   *
   * <p>Validates that the skill exists, is active, and is not already in the user's profile.
   *
   * @param userId the user's unique identifier
   * @param skillId the skill's unique identifier
   * @param categoryId the category's unique identifier
   * @param proficiencyScore the proficiency score (1-5)
   * @param yearsOfExperience years of experience with the skill
   * @param lastUsed date when the skill was last used
   * @return the created skill relationship
   * @throws com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException if the
   *     user's profile does not exist
   * @throws com.edag.skillmanagementsystem.domain.exception.skill.SkillNotFoundException if the
   *     skill does not exist in reference data
   * @throws com.edag.skillmanagementsystem.domain.exception.skill.InactiveSkillException if the
   *     skill is inactive
   * @throws com.edag.skillmanagementsystem.domain.exception.skill.DuplicateSkillException if the
   *     skill already exists in the user's profile
   */
  ProfileSkill addSkill(
      UUID userId,
      UUID skillId,
      UUID categoryId,
      int proficiencyScore,
      double yearsOfExperience,
      LocalDate lastUsed);

  /**
   * Updates an existing skill in a user's profile.
   *
   * <p>All update parameters are optional. Only non-null values will be updated.
   *
   * @param userId the user's unique identifier
   * @param skillId the skill relationship unique identifier
   * @param proficiencyScore the new proficiency score (optional)
   * @param yearsOfExperience the new years of experience (optional)
   * @param lastUsed the new last used date (optional)
   * @return the updated skill relationship
   * @throws com.edag.skillmanagementsystem.domain.exception.skill.ProfileSkillNotFoundException if
   *     the skill relationship does not exist for the user
   */
  ProfileSkill updateSkill(
      UUID userId,
      UUID skillId,
      Integer proficiencyScore,
      Double yearsOfExperience,
      LocalDate lastUsed);

  /**
   * Deletes a skill from a user's profile.
   *
   * @param userId the user's unique identifier
   * @param skillId the skill relationship unique identifier
   * @throws com.edag.skillmanagementsystem.domain.exception.skill.ProfileSkillNotFoundException if
   *     the skill relationship does not exist for the user
   */
  void deleteSkill(UUID userId, UUID skillId);
}
