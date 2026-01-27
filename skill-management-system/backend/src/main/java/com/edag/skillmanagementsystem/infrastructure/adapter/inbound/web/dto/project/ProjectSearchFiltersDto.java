package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import com.edag.skillmanagementsystem.domain.model.project.ProjectStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

/**
 * DTO for project search filter parameters.
 *
 * <p>This record captures filter criteria from API requests and can be converted to a domain {@link
 * ProjectSearchCriteria}. Supports filtering by project name/description, status, assigned
 * employees, and associated technologies/skills.
 *
 * @since 1.0.0
 */
@Schema(
    name = "ProjectSearchFiltersDto",
    description =
        "Filter criteria for project search requests, including search term, project status, "
            + "employee assignments, and related technologies/skills.")
public record ProjectSearchFiltersDto(
    @Parameter(description = "Text search term for filtering by name, description, or client")
        @Schema(example = "Customer Portal", nullable = true)
        String searchTerm,
    @Parameter(description = "List of project statuses to filter by")
        @Schema(
            example = "[\"ACTIVE\", \"COMPLETED\"]",
            allowableValues = {"ACTIVE", "COMPLETED", "PLANNED"},
            nullable = true)
        List<String> statusList,
    @Parameter(description = "List of employee IDs assigned to projects")
        @Schema(example = "[\"123e4567-e89b-12d3-a456-426614174000\"]", nullable = true)
        List<UUID> employeeIds,
    @Parameter(description = "List of skill/technology IDs associated with projects")
        @Schema(example = "[\"223e4567-e89b-12d3-a456-426614174000\"]", nullable = true)
        List<UUID> skillIds) {

  /**
   * Converts this DTO to a domain {@link ProjectSearchCriteria}.
   *
   * @return the domain search criteria
   */
  public ProjectSearchCriteria toDomain() {
    List<ProjectStatus> mappedStatuses =
        statusList != null ? statusList.stream().map(this::mapStringToStatus).toList() : null;

    return new ProjectSearchCriteria(searchTerm, mappedStatuses, employeeIds, skillIds);
  }

  /** Maps a string to a {@link ProjectStatus} enum, with validation. */
  private ProjectStatus mapStringToStatus(final String status) {
    return switch (status.toUpperCase()) {
      case "ACTIVE" -> ProjectStatus.ACTIVE;
      case "COMPLETED" -> ProjectStatus.COMPLETED;
      case "PLANNED" -> ProjectStatus.PLANNED;
      default -> throw new IllegalArgumentException("Invalid project status: " + status);
    };
  }
}
