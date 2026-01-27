package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberId;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import com.edag.skillmanagementsystem.domain.model.project.ProjectStatus;
import com.edag.skillmanagementsystem.infrastructure.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * DTO for updating an existing project.
 *
 * @since 1.0.0
 */
@ValidDateRange(start = "startDate", end = "endDate")
@Builder
@Schema(description = "Request to update an existing project")
public record UpdateProjectRequestDto(
    @Schema(description = "Project name", example = "Customer Portal Redesign") String name,
    @Schema(
            description = "Detailed project description",
            example = "Complete redesign of the customer-facing portal")
        String description,
    @Schema(description = "Project status", example = "ACTIVE")
        @Pattern(regexp = "PLANNED|ACTIVE|COMPLETED", message = "{project.status.invalid}")
        String status,
    @Schema(description = "Project start date", example = "2024-01-15") LocalDate startDate,
    @Schema(description = "Project end date (null if ongoing)", example = "2024-12-31")
        LocalDate endDate,
    @Schema(description = "Client name", example = "Acme Corporation") String client,
    @Schema(
            description = "Technology/skill IDs used in the project",
            example = "[\"123e4567-e89b-12d3-a456-426614174001\"]")
        List<UUID> technologyIds,
    @Valid @Schema(description = "List of project members (employee & position IDs)")
        List<ProjectMemberRequestDto> members) {

  /**
   * Converts this DTO to a {@link ProjectRequest} domain model.
   *
   * @return the ProjectUpdate domain model
   */
  public ProjectRequest toDomain() {
    return ProjectRequest.builder()
        .name(name)
        .description(description)
        .status(status != null ? ProjectStatus.valueOf(status) : null)
        .startDate(startDate)
        .endDate(endDate)
        .client(client)
        .technologies(technologyIds)
        .members(mapMembers())
        .build();
  }

  private List<ProjectMemberId> mapMembers() {
    return members.stream()
        .map(
            m ->
                ProjectMemberId.builder()
                    .employeeId(m.employeeId())
                    .positionId(m.positionId())
                    .build())
        .toList();
  }
}
