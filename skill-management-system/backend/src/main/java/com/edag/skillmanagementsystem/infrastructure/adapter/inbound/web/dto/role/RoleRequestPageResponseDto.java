package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role;

import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * DTO representing a paginated role request response.
 *
 * <p>This record wraps role request results together with pagination metadata for API responses.
 *
 * @since 1.0.0
 */
@Builder
@Schema(name = "RoleRequestPageResponseDto", description = "Paginated list of role requests")
public record RoleRequestPageResponseDto(
    @Schema(description = "List of role requests on this page")
        List<RoleRequestResponseDto> requests,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  /**
   * Maps a {@link Page<RoleRequest>} into a paginated response DTO.
   *
   * @param page the paginated role request result
   * @return mapped DTO
   */
  public static RoleRequestPageResponseDto from(Page<RoleRequest> page) {
    return RoleRequestPageResponseDto.builder()
        .requests(page.getContent().stream().map(RoleRequestResponseDto::from).toList())
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
