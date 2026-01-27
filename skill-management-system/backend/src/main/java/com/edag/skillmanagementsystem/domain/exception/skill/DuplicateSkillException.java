package com.edag.skillmanagementsystem.domain.exception.skill;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceAlreadyExistsException;
import java.util.UUID;

/**
 * Exception thrown when attempting to add a skill that already exists in a user's profile.
 *
 * <p>This prevents duplicate skill assignments and ensures that each skill appears only once per
 * user.
 *
 * <p>Uses message keys to support localization in API responses.
 *
 * @since 1.0.0
 */
public class DuplicateSkillException extends ResourceAlreadyExistsException {

  /**
   * Creates an exception indicating that the given skill already exists in the user’s profile.
   *
   * @param skillName the name of the duplicate skill
   * @param userId the ID of the user who already has the skill
   */
  public DuplicateSkillException(String skillName, UUID userId) {
    super("error.profile.skill.duplicate", skillName, userId);
  }
}
