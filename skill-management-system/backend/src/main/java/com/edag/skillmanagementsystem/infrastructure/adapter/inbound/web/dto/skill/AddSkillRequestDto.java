package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for adding a new skill to a user profile.
 *
 * <p>Contains all necessary information to create a new skill association for a user.
 */
@Schema(description = "Request to add a new skill to user profile")
public record AddSkillRequestDto(
    @Schema(
            description = "ID of the skill category",
            example = "550e8400-e29b-41d4-a716-446655440010")
        @NotNull(message = "{skill.category.id.required}")
        UUID categoryId,
    @Schema(description = "ID of the skill", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull(message = "{skill.id.required}")
        UUID skillId,
    @Schema(
            description = "Proficiency score (1-100)",
            example = "80",
            minimum = "1",
            maximum = "100")
        @Min(value = 1, message = "{skill.score.range}")
        @Max(value = 100, message = "{skill.score.range}")
        int score,
    @Schema(description = "Years of experience with this skill", example = "3")
        @DecimalMin(value = "0.0", message = "{skill.years.of.experience.negative}")
        @DecimalMax(value = "100.0", message = "{skill.years.of.experience.exceed}")
        double yearsOfExperience,
    @Schema(description = "Date when the skill was last used", example = "2024-11-01")
        LocalDate lastUsed) {}
