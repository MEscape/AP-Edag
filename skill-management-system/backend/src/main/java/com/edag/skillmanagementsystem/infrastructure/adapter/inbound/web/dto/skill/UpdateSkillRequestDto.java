package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

/**
 * Request DTO for updating an existing skill in a user profile.
 *
 * <p>All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Request to update an existing skill")
public record UpdateSkillRequestDto(
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
        Double yearsOfExperience,
    @Schema(description = "Date when the skill was last used", example = "2024-11-01")
        LocalDate lastUsed) {}
