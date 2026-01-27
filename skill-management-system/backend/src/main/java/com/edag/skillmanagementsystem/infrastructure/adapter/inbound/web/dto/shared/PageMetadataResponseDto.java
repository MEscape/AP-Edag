package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO representing pagination metadata for paginated API responses.
 *
 * <p>Provides information about the current page, total elements, and navigation flags such as
 * whether the page is the first or last within the result set.
 *
 * @param pageNumber the index of the current page (0-based)
 * @param pageSize the number of elements contained in each page
 * @param totalElements the total number of elements across all pages
 * @param totalPages the total number of available pages
 * @param first whether this page is the first page
 * @param last whether this page is the last page
 */
@Builder
@Schema(description = "Pagination metadata")
public record PageMetadataResponseDto(
    @Schema(description = "Page number") int pageNumber,
    @Schema(description = "Page size") int pageSize,
    @Schema(description = "Total number of elements") long totalElements,
    @Schema(description = "Total pages") int totalPages,
    @Schema(description = "Whether this is the first page") boolean first,
    @Schema(description = "Whether this is the last page") boolean last) {}
