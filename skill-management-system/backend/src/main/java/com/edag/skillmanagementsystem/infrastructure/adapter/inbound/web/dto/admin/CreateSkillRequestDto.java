package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * DTO for creating a new skill within a category.
 *
 * <p>This request is used by administrators to add new skills to the system within a specific
 * category.
 *
 * @param name the name of the skill (e.g., "Spring Boot", "Kubernetes")
 * @param categoryId the UUID of the category this skill belongs to
 * @since 1.0.0
 */
@Schema(description = "Request to create a new skill")
public record CreateSkillRequestDto(
    @Schema(description = "Name of the skill", example = "Kubernetes")
        @NotBlank(message = "{admin.skill.name.required}")
        @Size(min = 2, max = 128, message = "{admin.skill.name.size}")
        String name,
    @Schema(
            description = "ID of the skill category",
            example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "{admin.skill.category.required}")
        UUID categoryId) {}
