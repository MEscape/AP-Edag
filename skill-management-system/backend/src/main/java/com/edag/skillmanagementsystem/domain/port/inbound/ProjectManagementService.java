package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import java.util.UUID;

/**
 * Defines the contract for managing project assignments.
 *
 * <p>This service provides operations for creating, updating, and deleting projects. All actions
 * are restricted to the manager who owns the project.
 *
 * @since 1.0.0
 */
public interface ProjectManagementService {

  /**
   * Creates a new project assignment owned by the given manager.
   *
   * @param project the project to create
   * @param managerId the ID of the manager performing the operation
   * @return the created {@link Project}
   */
  Project createProject(ProjectRequest project, UUID managerId);

  /**
   * Updates an existing project.
   *
   * <p>Only the manager who created the project may update it.
   *
   * @param projectId the ID of the project to update
   * @param updatedProject the updated project data
   * @param managerId the ID of the manager performing the operation
   * @return the updated {@link Project}
   */
  Project updateProject(UUID projectId, ProjectRequest updatedProject, UUID managerId);

  /**
   * Deletes a project owned by the given manager.
   *
   * @param projectId the ID of the project to delete
   * @param managerId the ID of the manager performing the deletion
   */
  void deleteProject(UUID projectId, UUID managerId);
}
