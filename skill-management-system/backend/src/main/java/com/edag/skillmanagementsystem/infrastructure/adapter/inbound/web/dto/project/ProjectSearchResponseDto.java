package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * DTO representing a paginated project search response.
 *
 * <p>This record mirrors the structure of EmployeeSearchResponseDto to keep API pagination
 * consistent.
 *
 * @since 1.0.0
 */
@Builder
@Schema(
    name = "ProjectSearchResponseDto",
    description = "Paginated list of projects returned for a search request")
public record ProjectSearchResponseDto(
    @Schema(description = "List of projects on this page") List<ProjectResponseDto> projects,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  /**
   * Maps a {@link Page<Project>} into a paginated response DTO.
   *
   * @param page the paginated project result
   * @return mapped DTO
   */
  public static ProjectSearchResponseDto from(Page<Project> page) {
    return ProjectSearchResponseDto.builder()
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
