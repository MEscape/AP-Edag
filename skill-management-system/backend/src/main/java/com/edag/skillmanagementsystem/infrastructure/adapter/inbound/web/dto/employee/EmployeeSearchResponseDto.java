package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * DTO representing a paginated employee search response.
 *
 * <p>This record wraps employee search results together with pagination metadata for API responses.
 */
@Builder
@Schema(
    name = "EmployeeSearchResponseDto",
    description = "Paginated list of employees returned for a search request")
public record EmployeeSearchResponseDto(
    @Schema(description = "List of employees on this page") List<EmployeeResponseDto> employees,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  /**
   * Maps a {@link Page<Employee>} into a paginated response DTO.
   *
   * @param page the paginated employee result
   * @return mapped DTO
   */
  public static EmployeeSearchResponseDto from(Page<Employee> page) {
    return EmployeeSearchResponseDto.builder()
        .employees(page.getContent().stream().map(EmployeeResponseDto::from).toList())
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
