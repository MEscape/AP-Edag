package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;

/** DTO representing a single project entry. */
@Builder
@Schema(description = "Single project entry")
public record ProjectResponseDto(
    @Schema(description = "Project identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
    @Schema(description = "Project name", example = "Customer Portal Redesign") String name,
    @Schema(
            description = "Detailed project description",
            example = "Complete redesign of the customer-facing portal")
        String description,
    @Schema(description = "Current project status", example = "ACTIVE") String status,
    @Schema(description = "Project start date", example = "2024-01-15") LocalDate startDate,
    @Schema(description = "Project end date (null if ongoing)", example = "2024-12-31")
        LocalDate endDate,
    @Schema(description = "Client name", example = "Acme Corporation") String client,
    @Schema(description = "Number of team members", example = "5") Integer teamSize,
    @Schema(
            description = "Technologies used in the project",
            example = "[\"Java\", \"Spring Boot\", \"React\"]")
        Set<String> technologies,
    @Schema(
            description = "User ID of the manager who created this project",
            example = "223e4567-e89b-12d3-a456-426614174001")
        UUID createdByUserId,
    @Schema(description = "List of project members with resolved names")
        Set<ProjectMemberResponseDto> members) {

  /** Maps a domain {@link Project} object into a project DTO. */
  public static ProjectResponseDto from(Project project) {
    return ProjectResponseDto.builder()
        .id(project.id())
        .name(project.name())
        .description(project.description())
        .status(project.status().name())
        .startDate(project.startDate())
        .endDate(project.endDate())
        .client(project.client())
        .teamSize(project.teamSize())
        .technologies(project.technologies())
        .createdByUserId(project.createdByUserId())
        .members(mapMembers(project.members()))
        .build();
  }

  private static Set<ProjectMemberResponseDto> mapMembers(Set<ProjectMemberName> members) {
    return members.stream().map(ProjectMemberResponseDto::from).collect(Collectors.toSet());
  }
}
