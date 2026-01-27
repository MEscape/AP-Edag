package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectFilterOptions;
import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import com.edag.skillmanagementsystem.domain.port.inbound.ProjectDiscoveryService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectFilterOptionsDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectSearchFiltersDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectSearchResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.RecentProjectsResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.security.AuthorizationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for project management operations.
 *
 * <p>Provides endpoints for retrieving user projects with details about their role, timeline,
 * status, and technologies used. All operations require the authenticated user to access their own
 * data.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Projects", description = "APIs for managing and retrieving user project information")
public class ProjectDiscoverController {

  private final ProjectDiscoveryService projectDiscoveryService;
  private final AuthorizationUtils authorizationUtils;

  /**
   * Retrieves a paginated list of projects for a specific user.
   *
   * <p>Returns projects ordered by start date (most recent first) with details including role,
   * technologies, client information, and project status.
   *
   * @param userId the ID of the user (or "me" for current user)
   * @param page the page number (0-indexed)
   * @param size the number of items per page (maximum 100)
   * @param principal the authenticated user principal
   * @return a paginated response containing the user's projects
   */
  @Operation(
      summary = "Get user projects",
      description =
          "Retrieves a paginated list of projects for a user, ordered by "
              + "start date (most recent first). Includes project details, role, "
              + "status, and technologies used. Users can only access their own projects.")
  @ApiResponse(
      responseCode = "200",
      description = "Projects retrieved successfully",
      content = @Content(schema = @Schema(implementation = RecentProjectsResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Invalid pagination parameters",
      content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to access these projects",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  @GetMapping("/users/{userId}")
  public ResponseEntity<RecentProjectsResponseDto> getUserProjects(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      @Parameter(description = "Page number (0-indexed)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page (max 100)", example = "20")
          @RequestParam(defaultValue = "20")
          int size,
      Principal principal) {

    log.debug("REST request to get projects for user: {} (page: {}, size: {})", userId, page, size);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());

    return ResponseEntity.ok(
        RecentProjectsResponseDto.from(
            projectDiscoveryService.getRecentProjects(resolvedUserId, pageable)));
  }

  /**
   * Searches for projects based on provided filters, pagination, and sorting parameters.
   *
   * <p>This endpoint supports filtering based on project attributes such as name, status, client,
   * and start/end dates. Results are paginated and can be sorted by various fields.
   *
   * @param filtersDto the set of filter criteria (automatically bound from query parameters)
   * @param page the page number (zero-based, default: 0)
   * @param size the page size (default: 20, max: 100)
   * @param sortBy the field to sort by (default: startDate)
   * @param sortOrder the sort order: asc or desc (default: desc)
   * @return paginated list of matching projects with metadata
   */
  @Operation(
      summary = "Search manager's projects",
      description =
          "Search for projects created by the authenticated manager with support for filtering, "
              + "pagination, and sorting. Allows filtering by name, client, status, and date ranges.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved project search results",
      content = @Content(schema = @Schema(implementation = ProjectSearchResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Bad request - invalid parameters",
      content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User does not have MANAGER role",
      content = @Content)
  @GetMapping("/search")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ProjectSearchResponseDto> searchProjects(
      @ParameterObject @ModelAttribute ProjectSearchFiltersDto filtersDto,
      @Parameter(description = "Page number (zero-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20")
          int size,
      @Parameter(
              description = "Sort by field (name, startDate, status, client)",
              example = "startDate")
          @RequestParam(defaultValue = "startDate")
          String sortBy,
      @Parameter(description = "Sort order (asc or desc)", example = "desc")
          @RequestParam(defaultValue = "desc")
          String sortOrder) {

    log.debug(
        "REST request to search projects - filters: {}, page: {}, size: {}, sortBy: {}, sortOrder: {}",
        filtersDto,
        page,
        size,
        sortBy,
        sortOrder);

    // Build pageable with sorting
    Sort sort =
        sortOrder.equalsIgnoreCase("desc")
            ? Sort.by(mapSortField(sortBy)).descending()
            : Sort.by(mapSortField(sortBy)).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    // Execute search
    ProjectSearchCriteria criteria = filtersDto.toDomain();
    Page<Project> results = projectDiscoveryService.searchManagerProjects(criteria, pageable);

    log.debug(
        "Project search completed - found {} of {} total projects",
        results.getNumberOfElements(),
        results.getTotalElements());

    return ResponseEntity.ok(ProjectSearchResponseDto.from(results));
  }

  /**
   * Retrieves a single project by its unique identifier.
   *
   * <p>This endpoint returns detailed information about a specific project. Only the manager who
   * created the project has access to the project data.
   *
   * @param projectId the project's unique identifier
   * @param principal the authenticated user principal
   * @return the project details if found and owned by the requesting manager
   */
  @Operation(
      summary = "Get project by ID",
      description =
          "Retrieves detailed information about a specific project by its ID. "
              + "Only the manager who created the project may access it.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved project",
      content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
  @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User is not authorized to access this project",
      content = @Content)
  @GetMapping("/{projectId}")
  public ResponseEntity<ProjectResponseDto> getProjectById(
      @Parameter(description = "Project ID", example = "123e4567-e89b-12d3-a456-426614174000")
          @PathVariable("projectId")
          UUID projectId,
      Principal principal) {

    log.debug("REST request to get project by id: {}", projectId);

    // Resolve manager ID (the requesting user)
    UUID managerId = authorizationUtils.resolveUserId("me", principal);

    // Fetch project (must belong to this manager)
    Project project = projectDiscoveryService.getProjectById(projectId, managerId);

    log.debug("Retrieved project: {} ({})", project.name(), project.id());

    return ResponseEntity.ok(ProjectResponseDto.from(project));
  }

  /**
   * Retrieves all available filter options for project search.
   *
   * <p>This endpoint returns lists of unique values such as project clients, project statuses, and
   * other attributes that can be used to populate filter dropdowns in the user interface.
   *
   * <p>Only users with the MANAGER role may access this endpoint.
   *
   * @return filter options containing unique values for project search
   */
  @Operation(
      summary = "Get project filter options",
      description =
          "Retrieves all available filter options including unique clients, project statuses, "
              + "and other attributes for use in project search filters.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved project filter options",
      content = @Content(schema = @Schema(implementation = ProjectFilterOptionsDto.class)))
  @ApiResponse(
      responseCode = "403",
      description = "User does not have MANAGER role",
      content = @Content)
  @GetMapping("/filter-options")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ProjectFilterOptionsDto> getFilterOptions(Principal principal) {

    log.debug("REST request to get project filter options");

    UUID managerId = authorizationUtils.resolveUserId("me", principal);

    ProjectFilterOptions options = projectDiscoveryService.getFilterOptions(managerId);

    log.debug("Retrieved project filter options - {} skills", options.skills().size());

    return ResponseEntity.ok(ProjectFilterOptionsDto.from(options));
  }

  private String mapSortField(String sortBy) {
    return switch (sortBy.toLowerCase()) {
      case "name" -> "name";
      case "startdate" -> "startDate";
      case "status" -> "status";
      case "client" -> "client";
      default -> "startDate";
    };
  }
}
