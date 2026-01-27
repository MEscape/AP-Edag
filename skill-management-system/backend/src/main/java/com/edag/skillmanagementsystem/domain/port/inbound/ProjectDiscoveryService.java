package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectFilterOptions;
import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Defines the contract for project discovery and search operations.
 *
 * <p>This service provides capabilities for searching, filtering, and retrieving projects created
 * by managers or assigned to specific users. It supports pagination and sorting to handle large
 * datasets efficiently and offers methods for retrieving project metadata used in UI filters.
 *
 * @since 1.0.0
 */
public interface ProjectDiscoveryService {

  /**
   * Searches for projects based on the provided criteria.
   *
   * <p>This method supports complex filtering, pagination, and sorting to help managers discover
   * projects they have created. Filters may include project name, client, date ranges, status, and
   * other searchable metadata.
   *
   * @param criteria the search criteria including filters for name, status, dates, client, etc.
   * @param pageable the pagination and sorting information
   * @return a paginated list of {@link Project} objects matching the criteria
   */
  Page<Project> searchManagerProjects(ProjectSearchCriteria criteria, Pageable pageable);

  /**
   * Retrieves a single project by its unique identifier.
   *
   * <p>Only the manager who created the project should be allowed to access this project.
   *
   * @param projectId the unique identifier of the project
   * @param managerId the unique identifier of the manager requesting the project
   * @return the {@link Project} if found and owned by the manager
   */
  Project getProjectById(UUID projectId, UUID managerId);

  /**
   * Retrieves all available filter options for project search.
   *
   * <p>This method returns lists of unique values such as clients, statuses, and other project
   * attributes that can be used to populate dropdowns in the UI when filtering project lists.
   *
   * @param managerId the ID of the manager whose project metadata should be aggregated
   * @return a {@link ProjectFilterOptions} object containing all available filter values
   */
  ProjectFilterOptions getFilterOptions(UUID managerId);

  /**
   * Retrieves recent projects for a given user, ordered by start date.
   *
   * <p>This method is used for the "Get user projects" endpoint, typically to fetch timeline-style
   * project history for employees.
   *
   * @param userId the unique identifier of the employee whose recent projects should be retrieved
   * @param pageable the pagination information
   * @return a paginated list of the user's recent {@link Project} entries
   */
  Page<Project> getRecentProjects(UUID userId, Pageable pageable);
}
