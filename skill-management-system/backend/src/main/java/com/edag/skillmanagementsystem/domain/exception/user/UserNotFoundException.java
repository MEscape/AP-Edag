package com.edag.skillmanagementsystem.domain.exception.user;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a user cannot be found.
 *
 * <p>This exception is raised when attempting to retrieve or operate on a user entity that does not
 * exist in the system.
 *
 * <p>The exception uses an i18n message key and the user identifier as a message argument.
 */
@SuppressWarnings("java:S110")
public class UserNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new {@code UserNotFoundException} for the specified user ID.
   *
   * @param userId the unique identifier of the user that could not be found
   */
  public UserNotFoundException(UUID userId) {
    super("error.user.not.found.id", userId);
  }
}
