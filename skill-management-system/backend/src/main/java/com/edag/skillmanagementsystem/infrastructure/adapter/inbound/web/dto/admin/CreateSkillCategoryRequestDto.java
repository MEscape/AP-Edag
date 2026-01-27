package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new skill category.
 *
 * <p>This request is used by administrators to add new skill categories to the system.
 *
 * @param name the name of the skill category (e.g., "Backend", "Cloud Computing")
 * @since 1.0.0
 */
@Schema(description = "Request to create a new skill category")
public record CreateSkillCategoryRequestDto(
    @Schema(description = "Name of the skill category", example = "Cloud Computing")
        @NotBlank(message = "{admin.category.name.required}")
        @Size(min = 2, max = 128, message = "{admin.category.name.size}")
        String name) {}
