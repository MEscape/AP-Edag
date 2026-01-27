package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.shared.PageMetadataResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.domain.Page;

/**
 * Data transfer object for a paginated list of recent user activities.
 *
 * <p>Includes both the activity entries and pagination metadata.
 *
 * @since 1.0.0
 */
@Builder
@Schema(description = "Paginated list of recent user activities")
public record RecentActivitiesResponseDto(
    @Schema(description = "List of activities on the current page") List<ActivityDto> activities,
    @Schema(description = "Pagination metadata") PageMetadataResponseDto metadata) {

  /**
   * Maps a Page of domain {@link Activity} objects into a response DTO.
   *
   * @param page the activity page
   * @return mapped DTO
   */
  public static RecentActivitiesResponseDto from(Page<Activity> page) {
    return RecentActivitiesResponseDto.builder()
        .activities(page.getContent().stream().map(ActivityDto::from).toList())
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

  /** DTO representing a single activity entry. */
  @Builder
  @Schema(description = "Single activity entry")
  public record ActivityDto(
      @Schema(description = "Activity identifier") UUID id,
      @Schema(description = "User identifier associated with this activity") UUID userId,
      @Schema(
              description = "Type of activity",
              allowableValues = {
                      // Skill-related activities
                      "SKILL_ADDED",
                      "SKILL_UPDATED",
                      "SKILL_REMOVED",

                      // Project-related activities
                      "PROJECT_CREATED",
                      "PROJECT_STARTED",
                      "PROJECT_COMPLETED",
                      "PROJECT_PLANNED",
                      "PROJECT_UPDATED",
                      "PROJECT_DELETED",

                      // Project member activities
                      "PROJECT_MEMBER_ADDED",
                      "PROJECT_MEMBER_REMOVED",
                      "PROJECT_MEMBER_ROLE_CHANGED",

                      // Project skill/technology activities
                      "PROJECT_SKILL_ADDED",
                      "PROJECT_SKILL_REMOVED",

                      // Role request activities
                      "ROLE_REQUEST_CREATED",
                      "ROLE_REQUEST_APPROVED",
                      "ROLE_REQUEST_REJECTED",
                      "ROLE_REQUEST_CANCELLED",
                      "ROLE_REQUEST_REVIEWED",
                      "ROLE_REQUEST_UPDATED",

                      // Profile-related activities
                      "PROFILE_CREATED",
                      "PROFILE_UPDATED",

                      // User account activities
                      "USER_ACCOUNT_CREATED",
                      "USER_EMAIL_CHANGED",
                      "USER_NAME_CHANGED",
                      "USER_ACCOUNT_DELETED"
              })
      String activityType,
      @Schema(description = "Identifier of the related entity, if any") UUID relatedEntityId,
      @Schema(description = "Type of the related entity (e.g., SKILL, PROJECT)")
          String relatedEntityType,
      @Schema(description = "Timestamp when the activity occurred") Instant timestamp) {

    /** Maps a domain {@link Activity} object into a DTO. */
    public static ActivityDto from(Activity activity) {
      return ActivityDto.builder()
          .id(activity.id())
          .userId(activity.userId())
          .activityType(activity.activityType().name())
          .relatedEntityId(activity.relatedEntityId())
          .relatedEntityType(activity.relatedEntityType())
          .timestamp(activity.timestamp())
          .build();
    }
  }
}
