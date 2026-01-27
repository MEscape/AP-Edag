package com.edag.skillmanagementsystem.domain.exception.skill;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a profile-related skill cannot be found.
 *
 * <p>This exception is used when attempting to retrieve, update, or delete a skill assigned to a
 * user's profile, but no matching skill exists. It supports both lookup by skill ID and by skill
 * name.
 *
 * <p>The exception message uses message keys so that clients can provide localized error responses.
 *
 * @since 1.0.0
 */
@SuppressWarnings("java:S110")
public class ProfileSkillNotFoundException extends ResourceNotFoundException {

  /**
   * Creates an exception indicating that no skill with the given ID exists in the user's profile.
   *
   * @param skillId the ID of the missing skill
   */
  public ProfileSkillNotFoundException(UUID skillId, UUID userId) {
    super("error.profile.skill.not.found.id", skillId, userId);
  }
}
