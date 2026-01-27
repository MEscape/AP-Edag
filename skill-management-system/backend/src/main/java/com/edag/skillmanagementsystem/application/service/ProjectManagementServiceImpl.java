package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.project.InvalidProjectDataException;
import com.edag.skillmanagementsystem.domain.exception.project.ProjectNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException;
import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import com.edag.skillmanagementsystem.domain.port.inbound.ProjectManagementService;
import com.edag.skillmanagementsystem.domain.port.outbound.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the project management service.
 *
 * <p>This service handles project creation, updates, and deletion operations. All operations are
 * restricted to the manager who owns the project.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectManagementServiceImpl implements ProjectManagementService {

  private final ProjectRepository projectRepository;

  @Override
  public Project createProject(final ProjectRequest project, final UUID managerId) {
    log.debug("Creating new project: {} by manager: {}", project.name(), managerId);

    // Save project
    Project createdProject =
        projectRepository
            .save(project, managerId)
            .orElseThrow(
                () -> {
                  log.error(
                      "Failed to create project: {} - likely invalid reference data (manager, employees, positions, or skills)",
                      project.name());
                  return new InvalidProjectDataException(project.name());
                });

    log.info(
        "Successfully created project with ID: {} by manager: {}", createdProject.id(), managerId);

    return createdProject;
  }

  @Override
  public Project updateProject(
      final UUID projectId, final ProjectRequest updatedProject, final UUID managerId) {
    log.debug("Updating project: {} by manager: {}", projectId, managerId);
    // Verify project exists and manager owns it
    verifyProjectOwnership(projectId, managerId);

    // Perform update
    Project updated =
        projectRepository
            .update(projectId, updatedProject)
            .orElseThrow(
                () -> {
                  log.error(
                      "Failed to update project: {} - likely invalid reference data", projectId);
                  return new InvalidProjectDataException(projectId);
                });

    log.info("Successfully updated project: {} by manager: {}", projectId, managerId);

    return updated;
  }

  @Override
  public void deleteProject(final UUID projectId, final UUID managerId) {
    log.debug("Deleting project: {} by manager: {}", projectId, managerId);

    // Verify project exists and manager owns it
    verifyProjectOwnership(projectId, managerId);

    // Delete project
    projectRepository.deleteById(projectId);

    log.info("Successfully deleted project: {} by manager: {}", projectId, managerId);
  }

  /**
   * Verifies that a project exists and is owned by the specified manager.
   *
   * @param projectId the project ID to verify
   * @param managerId the manager ID to verify ownership
   * @return the project if found and owned by manager
   * @throws ProjectNotFoundException if project doesn't exist
   * @throws AccessDeniedException if manager doesn't own the project
   */
  private Project verifyProjectOwnership(final UUID projectId, final UUID managerId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

    if (!project.createdByUserId().equals(managerId)) {
      log.warn(
          "Access denied: Manager {} attempted to modify project {} owned by {}",
          managerId,
          projectId,
          project.createdByUserId());
      throw new AccessDeniedException("error.project.access.denied", projectId);
    }

    return project;
  }
}
