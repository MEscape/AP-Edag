package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new position/role.
 *
 * <p>This request is used by administrators to add new job positions to the system.
 *
 * @param name the name of the position (e.g., "Senior Software Engineer", "Technical Lead")
 * @since 1.0.0
 */
@Schema(description = "Request to create a new position")
public record CreatePositionRequestDto(
    @Schema(description = "Name of the position", example = "Senior Software Engineer")
        @NotBlank(message = "{admin.position.name.required}")
        @Size(min = 2, max = 128, message = "{admin.position.name.size}")
        String name) {}
