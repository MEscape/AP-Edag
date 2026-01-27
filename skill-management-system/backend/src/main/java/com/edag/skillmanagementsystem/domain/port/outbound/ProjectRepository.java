package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Outbound port for project persistence operations.
 *
 * <p>This repository defines the persistence contract for creating, updating, searching, and
 * retrieving projects. All JPA or database-related logic is implemented in the corresponding data
 * source adapter.
 *
 * <p>Supports:
 *
 * <ul>
 *   <li>Creating a new project with validation
 *   <li>Updating an existing project with partial update semantics
 *   <li>Deleting a project
 *   <li>Finding projects by ID
 *   <li>Searching projects using complex filter criteria
 *   <li>Retrieving recent projects for a user
 * </ul>
 *
 * @since 1.0.0
 */
public interface ProjectRepository {

  /**
   * Retrieves a paginated list of recent projects for a given user, ordered by start date.
   *
   * @param userId the ID of the user whose projects should be retrieved
   * @param pageable pagination and sorting options
   * @return a paginated list of {@link Project} objects
   */
  Page<Project> getRecentProjects(UUID userId, Pageable pageable);

  /**
   * Persistently creates a new project owned by the provided manager.
   *
   * <p>Returns {@link Optional#empty()} if validation fails (e.g., manager does not exist,
   * referenced employees/skills not found, etc.).
   *
   * @param projectRequest the project creation data
   * @param managerId the ID of the manager creating the project
   * @return an {@link Optional} containing the created project, or empty if validation fails
   */
  Optional<Project> save(ProjectRequest projectRequest, UUID managerId);

  /**
   * Updates an existing project using the provided update object.
   *
   * <p>Only non-null fields from {@link ProjectRequest} are applied. Returns empty if the project
   * does not exist or validation fails.
   *
   * @param projectId the ID of the project to update
   * @param update the update payload
   * @return an {@link Optional} with the updated project, or empty if not found
   */
  Optional<Project> update(UUID projectId, ProjectRequest update);

  /**
   * Retrieves a project by its unique identifier.
   *
   * @param projectId the ID of the project
   * @return an {@link Optional} containing the project if found
   */
  Optional<Project> findById(UUID projectId);

  /**
   * Deletes a project by its unique identifier.
   *
   * @param projectId the ID of the project to delete
   */
  void deleteById(UUID projectId);

  /**
   * Searches for projects using the provided criteria and pagination settings.
   *
   * @param criteria the search criteria including filters such as status, employees, and skills
   * @param pageable pagination and sorting information
   * @return a paginated list of projects matching the criteria
   */
  Page<Project> searchProjects(ProjectSearchCriteria criteria, Pageable pageable);
}
