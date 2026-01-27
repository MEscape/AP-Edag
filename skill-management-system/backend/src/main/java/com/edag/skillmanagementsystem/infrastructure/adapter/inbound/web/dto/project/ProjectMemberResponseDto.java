package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Response DTO representing a fully resolved project member.
 *
 * <p>This DTO is used for query/read operations and provides human-readable employee and position
 * names alongside their identifiers.
 *
 * @param employeeId the employee's ID
 * @param employeeName the employee's display name
 * @param positionId the position's ID
 * @param positionName the position's display name
 * @since 1.0.0
 */
@Builder
@Schema(description = "Resolved project member information (IDs + names)")
public record ProjectMemberResponseDto(
    @Schema(description = "Employee ID") String employeeId,
    @Schema(description = "Employee full name") String employeeName,
    @Schema(description = "Position ID") String positionId,
    @Schema(description = "Position name") String positionName) {

  /** Maps a {@link ProjectMemberName} domain model into a response DTO. */
  public static ProjectMemberResponseDto from(ProjectMemberName member) {
    return ProjectMemberResponseDto.builder()
        .employeeId(member.employeeId().toString())
        .employeeName(member.employeeName())
        .positionId(member.positionId().toString())
        .positionName(member.positionName())
        .build();
  }
}
