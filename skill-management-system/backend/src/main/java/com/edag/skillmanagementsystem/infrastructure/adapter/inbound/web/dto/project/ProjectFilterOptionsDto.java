package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.project.ProjectFilterOptions;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

/**
 * DTO representing available filter options for project search.
 *
 * <p>This DTO provides all selectable values for dropdowns such as project statuses and
 * technologies/skills used in projects. Employee filtering is handled via search term to avoid
 * loading thousands of employee IDs. It is used by the frontend to render project search filter UI
 * elements.
 *
 * @since 1.0.0
 */
@Schema(
    name = "ProjectFilterOptionsDto",
    description =
        "Represents all available filter options for project search, including "
            + "technologies/skills used across projects.")
public record ProjectFilterOptionsDto(
    @Schema(description = "List of available technologies/skills associated with projects")
        List<TechnologyFilterOptionDto> skills) {

  /** DTO for a technology/skill option. */
  @Builder
  @Schema(description = "Technology/skill filter option with ID and name")
  public record TechnologyFilterOptionDto(
      @Schema(description = "Skill ID") String id,
      @Schema(description = "Skill name") String name) {

    public static TechnologyFilterOptionDto from(Skill skill) {
      return new TechnologyFilterOptionDto(skill.id().toString(), skill.name());
    }
  }

  /**
   * Creates a {@link ProjectFilterOptionsDto} from a domain {@link ProjectFilterOptions}.
   *
   * @param options domain filter options
   * @return DTO instance with sorted lists
   */
  public static ProjectFilterOptionsDto from(ProjectFilterOptions options) {

    List<TechnologyFilterOptionDto> skillDtos =
        options.skills().stream()
            .map(TechnologyFilterOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();

    return new ProjectFilterOptionsDto(skillDtos);
  }
}
