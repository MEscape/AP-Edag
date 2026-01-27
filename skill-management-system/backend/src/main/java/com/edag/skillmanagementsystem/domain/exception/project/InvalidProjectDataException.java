package com.edag.skillmanagementsystem.domain.exception.project;

import com.edag.skillmanagementsystem.domain.exception.shared.InvalidRequestException;
import java.util.UUID;

/**
 * Exception thrown when project data validation fails.
 *
 * <p>This exception is raised when project creation or update data contains invalid or inconsistent
 * information, such as invalid date ranges, missing required fields, or conflicting values.
 *
 * <p>Examples include:
 *
 * <ul>
 *   <li>Start date is after end date
 *   <li>Empty member lists when members are required
 *   <li>Invalid status transitions
 *   <li>Missing required project fields
 * </ul>
 */
@SuppressWarnings("java:S110")
public class InvalidProjectDataException extends InvalidRequestException {

  /**
   * Constructs a new {@code InvalidProjectDataException} with project ID and field details.
   *
   * @param projectId the ID of the project with invalid data
   */
  public InvalidProjectDataException(UUID projectId) {
    super("error.project.invalid.data", projectId);
  }

  /**
   * Constructs a new {@code InvalidProjectDataException} with project name and field details.
   *
   * @param projectName the name of the project with invalid data
   */
  public InvalidProjectDataException(String projectName) {
    super("error.project.invalid.data.name", projectName);
  }
}
