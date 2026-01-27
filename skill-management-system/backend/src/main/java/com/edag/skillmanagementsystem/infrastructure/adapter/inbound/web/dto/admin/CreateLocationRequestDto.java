package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new location.
 *
 * <p>This request is used by administrators to add new work locations to the system.
 *
 * @param name the name of the location (e.g., "Munich", "Remote", "Berlin Office")
 * @since 1.0.0
 */
@Schema(description = "Request to create a new location")
public record CreateLocationRequestDto(
    @Schema(description = "Name of the location", example = "Frankfurt")
        @NotBlank(message = "{admin.location.name.required}")
        @Size(min = 2, max = 128, message = "{admin.location.name.size}")
        String name) {}
