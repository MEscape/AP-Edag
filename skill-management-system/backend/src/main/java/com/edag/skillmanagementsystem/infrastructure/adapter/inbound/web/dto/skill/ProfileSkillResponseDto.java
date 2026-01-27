package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * Response DTO representing a user's skill within their profile.
 *
 * <p>Contains the skill name, category, proficiency level, experience, and last usage date.
 */
@Schema(description = "Represents a skill possessed by a user with proficiency details")
public record ProfileSkillResponseDto(
    @Schema(
            description = "Unique identifier of the user-skill relationship",
            example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
    @Schema(description = "Name of the skill", example = "React") String skillName,
    @Schema(description = "Category of the skill", example = "Frontend") String category,
    @Schema(
            description = "Proficiency score (0–100)",
            example = "80",
            minimum = "0",
            maximum = "100")
        int proficiencyScore,
    @Schema(description = "Years of experience with this skill", example = "3")
        double yearsOfExperience,
    @Schema(description = "Date when the skill was last used", example = "2024-11-01")
        LocalDate lastUsed) {

  /**
   * Maps a domain {@link ProfileSkill} into a response DTO.
   *
   * @param skill the domain profile skill
   * @return the mapped DTO
   */
  public static ProfileSkillResponseDto from(ProfileSkill skill) {
    return new ProfileSkillResponseDto(
        skill.id().toString(),
        skill.skillName(),
        skill.category(),
        skill.proficiencyScore(),
        skill.yearsOfExperience(),
        skill.lastUsed());
  }
}
