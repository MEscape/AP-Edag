package com.edag.skillmanagementsystem.domain.exception.user;

import com.edag.skillmanagementsystem.domain.exception.shared.InvalidRequestException;
import java.util.UUID;

/**
 * Exception thrown when user profile data is invalid.
 *
 * <p>This exception is typically raised when updating a profile with invalid values—such as
 * malformed fields, inconsistent references, or business rule violations.
 *
 * <p>The error uses the i18n message key {@code error.profile.invalid.data} and includes the
 * provided identifier as a message argument for localization or detailed error reporting.
 */
@SuppressWarnings("java:S110")
public class InvalidProfileDataException extends InvalidRequestException {

  /**
   * Constructs a new {@code InvalidProfileDataException} with user ID and field details.
   *
   * @param userId the user ID associated with the invalid profile data
   */
  public InvalidProfileDataException(UUID userId) {
    super("error.profile.invalid.data", userId);
  }
}
