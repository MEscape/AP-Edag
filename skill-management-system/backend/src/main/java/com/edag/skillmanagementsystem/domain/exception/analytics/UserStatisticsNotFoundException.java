package com.edag.skillmanagementsystem.domain.exception.analytics;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when dashboard data is not available or cannot be calculated.
 *
 * <p>This exception is raised when dashboard statistics or related data cannot be retrieved or
 * computed for a user, typically due to missing or incomplete information.
 */
@SuppressWarnings("java:S110")
public class UserStatisticsNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new exception with a formatted message including the user ID.
   *
   * @param userId the UUID of the user whose statistics were not found
   */
  public UserStatisticsNotFoundException(UUID userId) {
    super("error.user.statistics.not.found.id", userId);
  }
}
