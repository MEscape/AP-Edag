package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.port.inbound.AnalyticsService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics.RecentActivitiesResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics.SkillDevelopmentTrendsResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics.UserStatisticsResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for analytics and activity tracking operations.
 *
 * <p>Provides endpoints for retrieving user statistics, skill development trends, and recent
 * activity feeds for dashboard and reporting purposes. All operations require the authenticated
 * user to access their own data.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
@Slf4j
@Tag(
    name = "Analytics & Activities",
    description = "APIs for user statistics, skill trends, and activity tracking")
public class AnalyticsController {

  private final AnalyticsService analyticsService;
  private final AuthorizationUtils authorizationUtils;

  private static final int MAX_PAGE_SIZE = 100;

  /**
   * Retrieves aggregated statistics for a specific user.
   *
   * <p>Includes values such as total skills, activity metrics, and other analytical indicators
   * relevant for profile insights.
   *
   * @param userId the ID of the user whose statistics should be returned (or "me" for current user)
   * @param principal the authenticated user principal
   * @return a response containing the user's statistics
   */
  @Operation(
      summary = "Get user statistics",
      description =
          "Retrieves aggregated statistics for a specific user including "
              + "total skills, skill level distribution, and certifications. "
              + "Users can only access their own statistics.")
  @ApiResponse(
      responseCode = "200",
      description = "Statistics retrieved successfully",
      content = @Content(schema = @Schema(implementation = UserStatisticsResponseDto.class)))
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to access these statistics",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "User statistics not found", content = @Content)
  @GetMapping("/users/{userId}/statistics")
  public ResponseEntity<UserStatisticsResponseDto> getUserStatistics(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      Principal principal) {

    log.debug("REST request to get statistics for user: {}", userId);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    return ResponseEntity.ok(
        UserStatisticsResponseDto.from(analyticsService.getUserStatistics(resolvedUserId)));
  }

  /**
   * Retrieves a user's skill development trend data.
   *
   * <p>Returns historical changes in skill progression, typically used for charts or trend analysis
   * components.
   *
   * @param userId the ID of the user (or "me" for current user)
   * @param principal the authenticated user principal
   * @return time-series data of the user's skill development
   */
  @Operation(
      summary = "Get skill development trends",
      description =
          "Retrieves time series data showing how a user's skills have "
              + "evolved over time, typically covering the past 6 months. "
              + "Users can only access their own trends.")
  @ApiResponse(
      responseCode = "200",
      description = "Trends retrieved successfully",
      content =
          @Content(schema = @Schema(implementation = SkillDevelopmentTrendsResponseDto.class)))
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to access these trends",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  @GetMapping("/users/{userId}/skill-trends")
  public ResponseEntity<SkillDevelopmentTrendsResponseDto> getSkillDevelopmentTrends(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      Principal principal) {

    log.debug("REST request to get skill development trends for user: {}", userId);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    return ResponseEntity.ok(
        SkillDevelopmentTrendsResponseDto.from(
            analyticsService.getSkillDevelopmentTrends(resolvedUserId)));
  }

  /**
   * Retrieves a paginated list of recent activities for a user.
   *
   * <p>Includes actions such as skill updates, project involvement, profile changes, and other
   * tracked events.
   *
   * <p>Pagination limits the result size and ensures efficient retrieval.
   *
   * @param userId the ID of the user (or "me" for current user)
   * @param page the page number (0-indexed)
   * @param size the number of items per page (maximum 100)
   * @param principal the authenticated user principal
   * @return a paginated activity response
   */
  @Operation(
      summary = "Get recent activities",
      description =
          "Retrieves a paginated list of recent activities for a user, "
              + "including skill additions, updates, and certifications. "
              + "Users can only access their own activities.")
  @ApiResponse(
      responseCode = "200",
      description = "Activities retrieved successfully",
      content = @Content(schema = @Schema(implementation = RecentActivitiesResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Invalid pagination parameters",
      content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to access these activities",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  @GetMapping("/users/{userId}/activities")
  public ResponseEntity<RecentActivitiesResponseDto> getRecentActivities(
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

    log.debug(
        "REST request to get recent activities for user: {} (page: {}, size: {})",
        userId,
        page,
        size);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    // Validate and limit page size
    int validatedSize = Math.min(size, MAX_PAGE_SIZE);
    if (validatedSize != size) {
      log.warn(
          "Requested page size {} exceeds maximum {}, using maximum instead", size, MAX_PAGE_SIZE);
    }

    Pageable pageable = PageRequest.of(page, validatedSize, Sort.by("timestamp").descending());

    return ResponseEntity.ok(
        RecentActivitiesResponseDto.from(
            analyticsService.getRecentActivities(resolvedUserId, pageable)));
  }
}
