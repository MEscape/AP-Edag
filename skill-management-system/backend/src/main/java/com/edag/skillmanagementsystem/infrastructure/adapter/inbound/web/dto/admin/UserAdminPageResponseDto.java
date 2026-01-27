package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import com.edag.skillmanagementsystem.domain.model.user.User; // adjust if needed
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * DTO representing a paginated admin user list.
 *
 * <p>This record wraps admin-visible user data together with pagination metadata for API responses.
 */
@Builder
@Schema(
    name = "UserAdminPageResponseDto",
    description = "Paginated list of users for administrative management")
public record UserAdminPageResponseDto(
    @Schema(description = "List of users on this page") List<UserAdminDto> users,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  /**
   * Maps a {@link Page<User>} into a paginated admin user response DTO.
   *
   * @param page the paginated user result
   * @return a mapped {@link UserAdminPageResponseDto}
   */
  public static UserAdminPageResponseDto from(Page<User> page) {
    return UserAdminPageResponseDto.builder()
        .users(page.getContent().stream().map(UserAdminDto::from).toList())
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
