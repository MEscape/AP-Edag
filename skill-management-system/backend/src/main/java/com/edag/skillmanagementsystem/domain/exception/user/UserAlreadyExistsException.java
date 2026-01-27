package com.edag.skillmanagementsystem.domain.exception.user;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceAlreadyExistsException;
import java.util.UUID;

/**
 * Exception thrown when attempting to create or synchronize a user that already exists.
 *
 * <p>This exception is raised when a user identified by the given ID already exists in the system,
 * typically during registration or synchronization workflows.
 *
 * <p>The exception uses an i18n message key and the user identifier as a message argument.
 */
@SuppressWarnings("java:S110")
public class UserAlreadyExistsException extends ResourceAlreadyExistsException {

  /**
   * Constructs a new {@code UserAlreadyExistsException} for the specified user ID.
   *
   * @param userId the unique identifier of the user that already exists
   */
  public UserAlreadyExistsException(UUID userId) {
    super("error.user.already.exists.id", userId);
  }
}
