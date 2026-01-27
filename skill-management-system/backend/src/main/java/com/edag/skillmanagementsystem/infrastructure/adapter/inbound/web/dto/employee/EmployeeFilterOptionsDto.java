package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee;

import com.edag.skillmanagementsystem.domain.model.employee.EmployeeFilterOptions;
import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * DTO representing available filter options for employee search.
 *
 * <p>This record provides lists of unique values with IDs and names that can be used in filter
 * dropdowns in the frontend.
 */
@Schema(
    name = "EmployeeFilterOptionsDto",
    description =
        "Represents all available filter options for employee search, including skills, "
            + "locations, and skill categories with their IDs and names.")
public record EmployeeFilterOptionsDto(
    @Schema(description = "List of all unique skills available for filtering")
        List<SkillFilterOptionDto> skills,
    @Schema(description = "List of all unique employee locations available for filtering")
        List<LocationFilterOptionDto> locations,
    @Schema(description = "List of all unique skill categories available for filtering")
        List<SkillCategoryFilterOptionDto> skillCategories) {

  /** DTO representing a skill filter option with ID, name, and category. */
  @Schema(description = "Skill filter option with ID, name, and category information")
  public record SkillFilterOptionDto(
      @Schema(description = "Skill ID", example = "550e8400-e29b-41d4-a716-446655440001") String id,
      @Schema(description = "Skill name", example = "Java") String name,
      @Schema(description = "Category ID", example = "550e8400-e29b-41d4-a716-446655440010")
          String categoryId,
      @Schema(description = "Category name", example = "Backend") String categoryName) {

    public static SkillFilterOptionDto from(Skill skill) {
      return new SkillFilterOptionDto(
          skill.id().toString(), skill.name(), skill.categoryId().toString(), skill.categoryName());
    }
  }

  /** DTO representing a location filter option with ID and name. */
  @Schema(description = "Location filter option with ID and name")
  public record LocationFilterOptionDto(
      @Schema(description = "Location ID", example = "550e8400-e29b-41d4-a716-446655440020")
          String id,
      @Schema(description = "Location name", example = "Munich") String name) {

    public static LocationFilterOptionDto from(Location location) {
      return new LocationFilterOptionDto(location.id().toString(), location.name());
    }
  }

  /** DTO representing a skill category filter option with ID and name. */
  @Schema(description = "Skill category filter option with ID and name")
  public record SkillCategoryFilterOptionDto(
      @Schema(description = "Category ID", example = "550e8400-e29b-41d4-a716-446655440030")
          String id,
      @Schema(description = "Category name", example = "Backend") String name) {

    public static SkillCategoryFilterOptionDto from(SkillCategory category) {
      return new SkillCategoryFilterOptionDto(category.id().toString(), category.name());
    }
  }

  /**
   * Creates an {@link EmployeeFilterOptionsDto} from a domain {@link EmployeeFilterOptions}.
   *
   * @param options the domain filter options
   * @return a new DTO instance
   */
  public static EmployeeFilterOptionsDto from(final EmployeeFilterOptions options) {
    List<SkillFilterOptionDto> skillDtos =
        options.skills().stream()
            .map(SkillFilterOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();

    List<LocationFilterOptionDto> locationDtos =
        options.locations().stream()
            .map(LocationFilterOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();

    List<SkillCategoryFilterOptionDto> categoryDtos =
        options.skillCategories().stream()
            .map(SkillCategoryFilterOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();

    return new EmployeeFilterOptionsDto(skillDtos, locationDtos, categoryDtos);
  }
}
