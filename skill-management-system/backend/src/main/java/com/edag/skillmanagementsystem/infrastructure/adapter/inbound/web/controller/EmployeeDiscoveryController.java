package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import com.edag.skillmanagementsystem.domain.port.inbound.EmployeeDiscoveryService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee.EmployeeFilterOptionsDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee.EmployeeResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee.EmployeeSearchFiltersDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee.EmployeeSearchResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for employee discovery and search endpoints.
 *
 * <p>This controller provides endpoints for searching employees, retrieving individual employee
 * details, and fetching available filter options. It supports complex filtering, pagination, and
 * sorting to facilitate employee discovery within the organization.
 *
 * <p>Most endpoints are accessible to all authenticated users. The employee details endpoint
 * supports the "me" alias to allow users to retrieve their own profile.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Discovery", description = "Employee search and discovery endpoints")
public class EmployeeDiscoveryController {

  private final EmployeeDiscoveryService employeeDiscoveryService;
  private final AuthorizationUtils authorizationUtils;

  /**
   * Searches for employees based on provided filters, pagination, and sorting parameters.
   *
   * <p>This endpoint supports multiple filter criteria including search term, skills, skill
   * categories, locations, availability status, and minimum experience. Results are paginated and
   * can be sorted by various fields.
   *
   * @param filtersDto the set of filter criteria (bound automatically from query parameters)
   * @param page the page number (zero-based, default: 0)
   * @param size the page size (default: 20)
   * @param sortBy the field to sort by (default: name)
   * @param sortOrder the sort order: asc or desc (default: asc)
   * @return a paginated response containing matching employees and metadata
   */
  @Operation(
      summary = "Search employees",
      description =
          "Search for employees with filtering, pagination, and sorting support. "
              + "Supports multiple filter criteria including skills, location, availability, and experience.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved employee search results",
      content = @Content(schema = @Schema(implementation = EmployeeSearchResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Bad request - invalid parameters",
      content = @Content)
  @GetMapping("/search")
  public ResponseEntity<EmployeeSearchResponseDto> searchEmployees(
      @ParameterObject @ModelAttribute EmployeeSearchFiltersDto filtersDto,
      @Parameter(description = "Page number (zero-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20")
          int size,
      @Parameter(
              description = "Sort by field (name, experience, projects, skills)",
              example = "name")
          @RequestParam(defaultValue = "name")
          String sortBy,
      @Parameter(description = "Sort order (asc or desc)", example = "asc")
          @RequestParam(defaultValue = "asc")
          String sortOrder) {

    log.debug(
        "REST request to search employees - filters: {}, page: {}, size: {}, sortBy: {}, sortOrder: {}",
        filtersDto,
        page,
        size,
        sortBy,
        sortOrder);

    EmployeeSearchCriteria criteria = filtersDto.toDomain();

    // Build pageable with sorting
    Sort sort =
        sortOrder.equalsIgnoreCase("desc")
            ? Sort.by(mapSortField(sortBy)).descending()
            : Sort.by(mapSortField(sortBy)).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    // Execute search
    Page<Employee> results = employeeDiscoveryService.searchEmployees(criteria, pageable);

    log.debug(
        "Employee search completed - found {} of {} total employees",
        results.getNumberOfElements(),
        results.getTotalElements());

    return ResponseEntity.ok(EmployeeSearchResponseDto.from(results));
  }

  /**
   * Retrieves a single employee by their unique identifier.
   *
   * <p>Supports the "me" alias to allow authenticated users to retrieve their own profile.
   *
   * @param userId the employee's unique identifier or "me" for current user
   * @param principal the authenticated user principal
   * @return the employee details if found
   */
  @Operation(
      summary = "Get employee by ID",
      description =
          "Retrieves detailed information about a specific employee by their ID. "
              + "Use 'me' to retrieve your own profile.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved employee",
      content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class)))
  @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
  @GetMapping("/{userId}")
  public ResponseEntity<EmployeeResponseDto> getEmployeeById(
      @Parameter(description = "Employee ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      Principal principal) {

    log.debug("REST request to get employee by id: {}", userId);

    // Resolve userId (supports "me" alias)
    UUID resolvedId = authorizationUtils.resolveUserId(userId, principal);

    // Fetch employee
    Employee employee = employeeDiscoveryService.getEmployeeById(resolvedId);

    log.debug("Retrieved employee: {} {}", employee.firstName(), employee.lastName());

    return ResponseEntity.ok(EmployeeResponseDto.from(employee));
  }

  /**
   * Retrieves all available filter options for employee search.
   *
   * <p>This endpoint returns lists of unique values for skills, locations, and skill categories
   * that can be used to populate filter dropdowns in the user interface.
   *
   * @return filter options containing unique values
   */
  @Operation(
      summary = "Get filter options",
      description =
          "Retrieves all available filter options including unique skills, locations, "
              + "and skill categories for use in search filters.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved filter options",
      content = @Content(schema = @Schema(implementation = EmployeeFilterOptionsDto.class)))
  @GetMapping("/filter-options")
  public ResponseEntity<EmployeeFilterOptionsDto> getFilterOptions() {

    log.debug("REST request to get employee filter options");

    var options = employeeDiscoveryService.getFilterOptions();

    log.debug(
        "Retrieved filter options - {} skills, {} locations, {} categories",
        options.skills().size(),
        options.locations().size(),
        options.skillCategories().size());

    return ResponseEntity.ok(EmployeeFilterOptionsDto.from(options));
  }

  /**
   * Maps the frontend sort field names to database column names.
   *
   * @param sortBy the frontend sort field
   * @return the database column name
   */
  private String mapSortField(String sortBy) {
    return switch (sortBy.toLowerCase()) {
      case "experience" -> "yearsOfExperience";
      case "projects" -> "totalProjects";
      case "skills" -> "skillCount";
      default -> "user.lastName";
    };
  }
}
