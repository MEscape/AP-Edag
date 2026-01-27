package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option;

import com.edag.skillmanagementsystem.domain.model.option.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** Response DTO containing available skill options. */
@Schema(description = "List of available skill options")
public record SkillOptionsResponseDto(
    @Schema(description = "List of skills with ID, name, and category")
        List<SkillOptionDto> skills) {

  /** DTO representing a single skill option. */
  @Schema(description = "Skill option with ID, name, and category")
  public record SkillOptionDto(
      @Schema(description = "Skill ID", example = "550e8400-e29b-41d4-a716-446655440001") String id,
      @Schema(description = "Skill name", example = "React") String name,
      @Schema(description = "Category ID", example = "550e8400-e29b-41d4-a716-446655440010")
          String categoryId,
      @Schema(description = "Category name", example = "Frontend") String categoryName) {

    public static SkillOptionDto from(Skill skill) {
      return new SkillOptionDto(
          skill.id().toString(), skill.name(), skill.categoryId().toString(), skill.categoryName());
    }
  }

  /**
   * Creates a SkillOptionsResponseDto from a list of domain Skill objects.
   *
   * @param skills the list of skill domain models
   * @return the response DTO
   */
  public static SkillOptionsResponseDto from(List<Skill> skills) {
    List<SkillOptionDto> skillDtos =
        skills.stream()
            .map(SkillOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    return new SkillOptionsResponseDto(skillDtos);
  }
}
