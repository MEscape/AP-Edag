package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.user.User;
import com.edag.skillmanagementsystem.domain.port.inbound.UserAdminService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin.UserAdminPageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for administrative user management operations.
 *
 * <p>Provides endpoints for administrators to view and manage users in the system. All endpoints
 * require the ADMIN role.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - User Management", description = "APIs for administrative user management")
public class UserAdminController {

  private final UserAdminService userAdminService;

  /**
   * Retrieves a paginated list of all users in the system.
   *
   * @param page page number (0-indexed)
   * @param size page size
   * @param sortBy field to sort by
   * @param sortDirection sort direction (asc or desc)
   * @return paginated user response
   */
  @Operation(
      summary = "Get all users (Admin)",
      description = "Retrieves a paginated list of all users in the system. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "200",
      description = "Users retrieved successfully",
      content = @Content(schema = @Schema(implementation = UserAdminPageResponseDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<UserAdminPageResponseDto> getAllUsers(
      @Parameter(description = "Page number (0-indexed)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sort field", example = "createdAt")
          @RequestParam(defaultValue = "createdAt")
          String sortBy,
      @Parameter(description = "Sort direction", example = "desc")
          @RequestParam(defaultValue = "desc")
          String sortDirection) {

    log.debug(
        "REST request to get all users - page: {}, size: {}, sortBy: {}, direction: {}",
        page,
        size,
        sortBy,
        sortDirection);

    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<User> userPage = userAdminService.getAllUsers(pageable);

    return ResponseEntity.ok(UserAdminPageResponseDto.from(userPage));
  }

  /**
   * Searches for users by username or email with pagination.
   *
   * @param searchTerm search keyword for username or email
   * @param page page number (0-indexed)
   * @param size page size
   * @param sortBy field to sort by
   * @param sortDirection sort direction (asc or desc)
   * @return paginated user response
   */
  @Operation(
      summary = "Search users (Admin)",
      description =
          "Searches for users by username or email with case-insensitive partial matching. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "200",
      description = "Search completed successfully",
      content = @Content(schema = @Schema(implementation = UserAdminPageResponseDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/search")
  public ResponseEntity<UserAdminPageResponseDto> searchUsers(
      @Parameter(description = "Search term for username or email", example = "john") @RequestParam
          String searchTerm,
      @Parameter(description = "Page number (0-indexed)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sort field", example = "username")
          @RequestParam(defaultValue = "username")
          String sortBy,
      @Parameter(description = "Sort direction", example = "asc")
          @RequestParam(defaultValue = "asc")
          String sortDirection) {

    log.debug(
        "REST request to search users - term: '{}', page: {}, size: {}, sortBy: {}, direction: {}",
        searchTerm,
        page,
        size,
        sortBy,
        sortDirection);

    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<User> userPage = userAdminService.searchUsers(searchTerm, pageable);

    return ResponseEntity.ok(UserAdminPageResponseDto.from(userPage));
  }
}
