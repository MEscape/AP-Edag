package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

/**
 * Request DTO representing a project member assignment for create or update operations.
 *
 * <p>This DTO contains only identifier values and is mapped directly to the {@link ProjectMemberId}
 * write model. All referenced IDs must exist in the system.
 *
 * @param employeeId the unique employee identifier
 * @param positionId the unique position identifier
 * @since 1.0.0
 */
@Builder
@Schema(description = "Project member assignment (IDs only)")
public record ProjectMemberRequestDto(
    @NotNull(message = "{project.member.employeeId.required}")
        @Schema(description = "Employee ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID employeeId,
    @NotNull(message = "{project.member.positionId.required}")
        @Schema(description = "Position ID", example = "987e6543-e21b-45cd-b678-123456789abc")
        UUID positionId) {

  /**
   * Converts this DTO into a domain write model {@link ProjectMemberId}.
   *
   * @return mapped ProjectMemberId instance
   */
  public ProjectMemberId toDomain() {
    return ProjectMemberId.builder().employeeId(employeeId).positionId(positionId).build();
  }
}
