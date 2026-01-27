package com.edag.skillmanagementsystem.domain.exception.user;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a profile is not found.
 *
 * <p>This exception is raised when attempting to access or update a user profile that does not
 * exist in the system.
 */
@SuppressWarnings("java:S110")
public class ProfileNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new ProfileNotFoundException with the specified user ID.
   *
   * @param userId the ID of the user whose profile was not found
   */
  public ProfileNotFoundException(UUID userId) {
    super("error.profile.not.found", userId);
  }
}
