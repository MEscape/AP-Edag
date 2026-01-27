package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * Data transfer object for a paginated list of user projects.
 *
 * <p>Includes both the project entries and pagination metadata.
 *
 * @since 1.0.0
 */
@Builder
@Schema(description = "Paginated list of recent user projects")
public record RecentProjectsResponseDto(
    @Schema(description = "List of projects on the current page") List<ProjectResponseDto> projects,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  public static RecentProjectsResponseDto from(Page<Project> page) {
    return RecentProjectsResponseDto.builder()
        .projects(page.getContent().stream().map(ProjectResponseDto::from).toList())
        .metadata(
            PageMetadataResponseDto.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build())
        .build();
  }
}
