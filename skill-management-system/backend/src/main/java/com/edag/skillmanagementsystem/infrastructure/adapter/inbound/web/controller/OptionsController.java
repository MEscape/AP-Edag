package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Position;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import com.edag.skillmanagementsystem.domain.port.inbound.OptionsService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin.CreateLocationRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin.CreatePositionRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin.CreateSkillCategoryRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin.CreateSkillRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option.LocationOptionsResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option.PositionOptionsResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option.SkillCategoriesOptionsResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option.SkillOptionsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving available reference data options.
 *
 * <p>Provides endpoints for fetching lists of available skills, categories, positions, and
 * locations that can be used for dropdowns and selection in the UI. All endpoints are publicly
 * accessible to authenticated users.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/options")
@RequiredArgsConstructor
@Slf4j
@Tag(
    name = "Reference Data Options",
    description = "APIs for retrieving available options for skills, positions, and locations")
public class OptionsController {

  private final OptionsService optionsService;

  /**
   * Retrieves all available skill categories.
   *
   * @return response containing list of skill categories
   */
  @Operation(
      summary = "Get skill categories",
      description = "Retrieves all available skill categories for filtering and selection.")
  @ApiResponse(
      responseCode = "200",
      description = "Categories retrieved successfully",
      content =
          @Content(schema = @Schema(implementation = SkillCategoriesOptionsResponseDto.class)))
  @GetMapping("/skill-categories")
  public ResponseEntity<SkillCategoriesOptionsResponseDto> getSkillCategories() {
    log.debug("REST request to get skill categories");

    return ResponseEntity.ok(
        SkillCategoriesOptionsResponseDto.from(optionsService.getAvailableSkillCategories()));
  }

  /**
   * Retrieves all available skills across all categories.
   *
   * @return response containing list of all skills
   */
  @Operation(
      summary = "Get all available skills",
      description = "Retrieves all available skills across all categories.")
  @ApiResponse(
      responseCode = "200",
      description = "Skills retrieved successfully",
      content = @Content(schema = @Schema(implementation = SkillOptionsResponseDto.class)))
  @GetMapping("/skills")
  public ResponseEntity<SkillOptionsResponseDto> getAllSkills() {
    log.debug("REST request to get all available skills");

    return ResponseEntity.ok(SkillOptionsResponseDto.from(optionsService.getAllAvailableSkills()));
  }

  /**
   * Retrieves all available skills filtered by a specific category.
   *
   * @param categoryId the skill category name
   * @return response containing list of skills in the specified category
   */
  @Operation(
      summary = "Get available skills by category",
      description = "Retrieves all available skills filtered by a specific category.")
  @ApiResponse(
      responseCode = "200",
      description = "Skills retrieved successfully",
      content = @Content(schema = @Schema(implementation = SkillOptionsResponseDto.class)))
  @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
  @GetMapping("/skills/category/{categoryId}")
  public ResponseEntity<SkillOptionsResponseDto> getSkillsByCategory(
      @Parameter(description = "Skill category name", example = "Frontend")
          @PathVariable("categoryId")
          String categoryId) {

    log.debug("REST request to get available skills for category: {}", categoryId);

    return ResponseEntity.ok(
        SkillOptionsResponseDto.from(
            optionsService.getAvailableSkillsByCategory(UUID.fromString(categoryId))));
  }

  /**
   * Retrieves all available positions/roles from existing employee data.
   *
   * @return response containing list of available positions
   */
  @Operation(
      summary = "Get available positions",
      description = "Retrieves all available positions/roles from existing employee data.")
  @ApiResponse(
      responseCode = "200",
      description = "Positions retrieved successfully",
      content = @Content(schema = @Schema(implementation = PositionOptionsResponseDto.class)))
  @GetMapping("/positions")
  public ResponseEntity<PositionOptionsResponseDto> getPositions() {
    log.debug("REST request to get available positions");

    return ResponseEntity.ok(
        PositionOptionsResponseDto.from(optionsService.getAvailablePositions()));
  }

  /**
   * Retrieves all available work locations.
   *
   * @return response containing list of available locations
   */
  @Operation(
      summary = "Get available locations",
      description = "Retrieves all available work locations.")
  @ApiResponse(
      responseCode = "200",
      description = "Locations retrieved successfully",
      content = @Content(schema = @Schema(implementation = LocationOptionsResponseDto.class)))
  @GetMapping("/locations")
  public ResponseEntity<LocationOptionsResponseDto> getLocations() {
    log.debug("REST request to get available locations");

    return ResponseEntity.ok(
        LocationOptionsResponseDto.from(optionsService.getAvailableLocations()));
  }

  // ---------------------------------------------------------------------------
  // ADMIN OPERATIONS
  // ---------------------------------------------------------------------------

  /**
   * Creates a new skill category (Admin only).
   *
   * @param request the category creation request
   * @return the created category
   */
  @Operation(
      summary = "Create skill category (Admin)",
      description = "Creates a new skill category. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "201",
      description = "Category created successfully",
      content =
          @Content(
              schema =
                  @Schema(
                      implementation =
                          SkillCategoriesOptionsResponseDto.SkillCategoryOptionDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/skill-categories")
  public ResponseEntity<SkillCategoriesOptionsResponseDto.SkillCategoryOptionDto>
      createSkillCategory(@Valid @RequestBody CreateSkillCategoryRequestDto request) {

    log.info("REST request to create skill category: {}", request.name());

    SkillCategory category = optionsService.createSkillCategory(request.name());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SkillCategoriesOptionsResponseDto.SkillCategoryOptionDto.from(category));
  }

  /**
   * Creates a new skill within a category (Admin only).
   *
   * @param request the skill creation request
   * @return the created skill
   */
  @Operation(
      summary = "Create skill (Admin)",
      description = "Creates a new skill within a category. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "201",
      description = "Skill created successfully",
      content =
          @Content(schema = @Schema(implementation = SkillOptionsResponseDto.SkillOptionDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @ApiResponse(responseCode = "404", description = "Category not found")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/skills")
  public ResponseEntity<SkillOptionsResponseDto.SkillOptionDto> createSkill(
      @Valid @RequestBody CreateSkillRequestDto request) {

    log.info(
        "REST request to create skill: {} in category: {}", request.name(), request.categoryId());

    Skill skill = optionsService.createSkill(request.name(), request.categoryId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SkillOptionsResponseDto.SkillOptionDto.from(skill));
  }

  /**
   * Creates a new position (Admin only).
   *
   * @param request the position creation request
   * @return the created position
   */
  @Operation(
      summary = "Create position (Admin)",
      description = "Creates a new position/role. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "201",
      description = "Position created successfully",
      content =
          @Content(
              schema =
                  @Schema(implementation = PositionOptionsResponseDto.PositionOptionDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/positions")
  public ResponseEntity<PositionOptionsResponseDto.PositionOptionDto> createPosition(
      @Valid @RequestBody CreatePositionRequestDto request) {

    log.info("REST request to create position: {}", request.name());

    Position position = optionsService.createPosition(request.name());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(PositionOptionsResponseDto.PositionOptionDto.from(position));
  }

  /**
   * Creates a new location (Admin only).
   *
   * @param request the location creation request
   * @return the created location
   */
  @Operation(
      summary = "Create location (Admin)",
      description = "Creates a new work location. Requires ADMIN role.")
  @ApiResponse(
      responseCode = "201",
      description = "Location created successfully",
      content =
          @Content(
              schema =
                  @Schema(implementation = LocationOptionsResponseDto.LocationOptionDto.class)))
  @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/locations")
  public ResponseEntity<LocationOptionsResponseDto.LocationOptionDto> createLocation(
      @Valid @RequestBody CreateLocationRequestDto request) {

    log.info("REST request to create location: {}", request.name());

    Location location = optionsService.createLocation(request.name());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(LocationOptionsResponseDto.LocationOptionDto.from(location));
  }
}
