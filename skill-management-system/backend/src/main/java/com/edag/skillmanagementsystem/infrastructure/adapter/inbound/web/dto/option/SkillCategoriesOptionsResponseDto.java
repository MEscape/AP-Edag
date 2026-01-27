package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option;

import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** Response DTO containing available skill categories. */
@Schema(description = "List of available skill categories")
public record SkillCategoriesOptionsResponseDto(
    @Schema(description = "List of skill categories with ID and name")
        List<SkillCategoryOptionDto> categories) {

  /** DTO representing a single skill category option. */
  @Schema(description = "Skill category option with ID and name")
  public record SkillCategoryOptionDto(
      @Schema(description = "Category ID", example = "550e8400-e29b-41d4-a716-446655440010")
          String id,
      @Schema(description = "Category name", example = "Frontend") String name) {

    public static SkillCategoryOptionDto from(SkillCategory category) {
      return new SkillCategoryOptionDto(category.id().toString(), category.name());
    }
  }

  /**
   * Creates a SkillCategoriesOptionsResponseDto from a list of domain SkillCategory objects.
   *
   * @param categories the list of skill category domain models
   * @return the response DTO
   */
  public static SkillCategoriesOptionsResponseDto from(List<SkillCategory> categories) {
    List<SkillCategoryOptionDto> categoryDtos =
        categories.stream()
            .map(SkillCategoryOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    return new SkillCategoriesOptionsResponseDto(categoryDtos);
  }
}
