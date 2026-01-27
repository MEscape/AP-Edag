package com.edag.skillmanagementsystem.domain.exception.skill;

import com.edag.skillmanagementsystem.domain.exception.shared.InvalidRequestException;

/**
 * Exception thrown when attempting to add or update a skill that is marked as inactive within the
 * system’s reference catalog.
 *
 * <p>Inactive skills cannot be newly assigned to users, although historical records referencing
 * them remain valid.
 *
 * <p>Uses message keys to support localized API error responses.
 *
 * @since 1.0.0
 */
@SuppressWarnings("java:S110")
public class InactiveSkillException extends InvalidRequestException {

  /**
   * Creates an exception indicating that the skill is inactive and cannot be used in profile
   * operations.
   *
   * @param skillName the name of the inactive skill
   */
  public InactiveSkillException(String skillName) {
    super("error.profile.skill.inactive", skillName);
  }
}
