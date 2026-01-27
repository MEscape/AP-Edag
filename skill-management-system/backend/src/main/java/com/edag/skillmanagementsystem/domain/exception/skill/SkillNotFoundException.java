package com.edag.skillmanagementsystem.domain.exception.skill;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a requested skill is not found.
 *
 * <p>This exception is raised when attempting to access or manipulate a skill that does not exist
 * in the system or in a user's skill set.
 */
@SuppressWarnings("java:S110")
public class SkillNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new SkillNotFoundException with the specified skill ID.
   *
   * @param skillId the ID of the skill that was not found
   */
  public SkillNotFoundException(UUID skillId) {
    super("error.skill.not.found.id", skillId);
  }
}
