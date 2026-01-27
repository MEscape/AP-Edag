package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role;

import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a role request.
 *
 * @since 1.0.0
 */
@Schema(description = "Request to create a role change request")
public record CreateRoleRequestDto(
    @Schema(
            description = "Role being requested",
            example = "MANAGER",
            allowableValues = {"USER", "MANAGER", "ADMIN"})
        @NotNull(message = "{role.request.role.required}")
        RoleType requestedRole,
    @Schema(
            description = "Justification for the role request",
            example = "I need manager access to oversee team projects and manage resources.")
        @Size(min = 10, max = 1000, message = "{role.request.reason.size}")
        String reason) {}
