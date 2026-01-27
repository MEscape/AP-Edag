package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * DTO representing an employee skill for API responses.
 *
 * <p>This record transfers skill data from the backend to the frontend, including the skill's name,
 * category, proficiency score, and experience years.
 */
@Schema(
    name = "EmployeeSkillDto",
    description =
        "Represents a skill associated with an employee, including name, category, "
            + "proficiency score, and years of experience.")
public record EmployeeSkillResponseDto(
    @Schema(
            description = "The unique identifier of the employee-skill relationship",
            example = "d4b59a8f-ec11-4a52-8d8f-1f1e4d7a4b63")
        UUID id,
    @Schema(description = "The name of the skill", example = "Java") String name,
    @Schema(description = "The proficiency score for the skill (0–100)", example = "92") int score,
    @Schema(description = "The number of years of experience with this skill", example = "5")
        double yearsOfExperience) {

  /**
   * Creates an {@link EmployeeSkillResponseDto} from a domain {@link ProfileSkill}.
   *
   * @param skill the domain skill entity
   * @return a new DTO instance
   */
  public static EmployeeSkillResponseDto from(final ProfileSkill skill) {
    return new EmployeeSkillResponseDto(
        skill.id(), skill.skillName(), skill.proficiencyScore(), skill.yearsOfExperience());
  }
}
