package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberId;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import com.edag.skillmanagementsystem.domain.model.project.ProjectStatus;
import com.edag.skillmanagementsystem.infrastructure.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * DTO for creating a new project assignment.
 *
 * @since 1.0.0
 */
@ValidDateRange(start = "startDate", end = "endDate")
@Builder
@Schema(description = "Request to create a new project assignment")
public record CreateProjectRequestDto(
    @NotBlank(message = "{project.name.required}")
        @Size(max = 255, message = "{project.name.maxLength}")
        @Schema(description = "Project name", example = "Customer Portal Redesign")
        String name,
    @Size(max = 2000, message = "{project.description.maxLength}")
        @Schema(
            description = "Detailed project description",
            example = "Complete redesign of the customer-facing portal")
        String description,
    @NotNull(message = "{project.status.required}")
        @Schema(description = "Project status", example = "ACTIVE")
        ProjectStatus status,
    @NotNull(message = "{project.startDate.required}")
        @Schema(description = "Project start date", example = "2024-01-15")
        LocalDate startDate,
    @Schema(description = "Project end date (null if ongoing)", example = "2024-12-31")
        LocalDate endDate,
    @Schema(description = "Client name", example = "Acme Corporation") String client,
    @NotEmpty(message = "{project.technologies.required}")
        @Schema(
            description = "Technology/Skill IDs used in the project",
            example = "[\"123e4567-e89b-12d3-a456-426614174001\"]")
        List<UUID> technologies,
    @NotEmpty(message = "{project.members.required}")
        @Valid
        @Schema(description = "List of project members (employee & position IDs)")
        List<ProjectMemberRequestDto> members) {

  /** Converts this DTO into the domain {@link ProjectRequest} model. */
  public ProjectRequest toDomain() {
    return ProjectRequest.builder()
        .name(name)
        .description(description)
        .status(status)
        .startDate(startDate)
        .endDate(endDate)
        .client(client)
        .technologies(technologies)
        .members(mapMembers())
        .build();
  }

  private List<ProjectMemberId> mapMembers() {
    return members.stream().map(ProjectMemberRequestDto::toDomain).toList();
  }
}
