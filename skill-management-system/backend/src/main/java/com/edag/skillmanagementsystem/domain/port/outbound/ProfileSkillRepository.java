package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for profile skill operations.
 *
 * <p>This repository handles persistence operations related to skills associated with user
 * profiles, including adding, updating, deleting, and querying skills.
 *
 * @since 1.0.0
 */
public interface ProfileSkillRepository {

  /**
   * Adds a skill to a user's profile.
   *
   * <p>The skill must exist in reference data before it can be added. If the user already has this
   * skill, returns empty Optional.
   *
   * @param userId the user ID
   * @param skillId the skill ID from reference data
   * @param proficiencyScore the proficiency score (0-100)
   * @param yearsOfExperience years of experience with the skill
   * @param lastUsed the date the skill was last used
   * @return an Optional containing the created profile skill if successful, empty if user not
   *     found, skill not found, or skill already exists
   */
  Optional<ProfileSkill> addSkill(
      UUID userId,
      UUID skillId,
      int proficiencyScore,
      double yearsOfExperience,
      LocalDate lastUsed);

  /**
   * Updates a skill in a user's profile.
   *
   * <p>All update parameters are optional. Pass null to skip updating a field.
   *
   * @param userId the user ID
   * @param skillId the skill ID
   * @param proficiencyScore the new proficiency score (optional)
   * @param yearsOfExperience the new years of experience (optional)
   * @param lastUsed the new last used date (optional)
   * @return an Optional containing the updated profile skill if successful, empty if skill not
   *     found for this user
   */
  Optional<ProfileSkill> updateSkill(
      UUID userId,
      UUID skillId,
      Integer proficiencyScore,
      Double yearsOfExperience,
      LocalDate lastUsed);

  /**
   * Deletes a skill from a user's profile.
   *
   * @param userId the user ID
   * @param skillId the skill ID
   * @return true if the skill was deleted, false if skill not found for this user
   */
  boolean deleteSkill(UUID userId, UUID skillId);
}
