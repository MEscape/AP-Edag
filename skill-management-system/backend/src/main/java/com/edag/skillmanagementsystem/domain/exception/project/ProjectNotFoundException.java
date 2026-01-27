package com.edag.skillmanagementsystem.domain.exception.project;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.io.Serial;
import java.util.UUID;

/**
 * Exception thrown when a requested project is not found.
 *
 * <p>This exception should be thrown when attempting to retrieve or operate on a project that does
 * not exist in the system.
 *
 * @since 1.0.0
 */
@SuppressWarnings("java:S110")
public class ProjectNotFoundException extends ResourceNotFoundException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new project not found exception with the project ID.
   *
   * @param projectId the ID of the project that was not found
   */
  public ProjectNotFoundException(UUID projectId) {
    super("project.notFound", projectId);
  }
}
