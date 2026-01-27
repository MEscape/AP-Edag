package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * Data transfer object representing a chronological series of skill development metrics.
 *
 * <p>Each entry contains month-based analytics such as skills added, updated, removed, and
 * proficiency evolution.
 *
 * @since 1.0.0
 */
@Builder
@Schema(description = "Time series data showing monthly skill development trends")
public record SkillDevelopmentTrendsResponseDto(
    @Schema(description = "Monthly skill development data points in ascending order")
        List<SkillDevelopmentDataPointDto> dataPoints) {

  /**
   * Creates a response DTO from a list of domain data points.
   *
   * @param dataPoints domain model list
   * @return DTO containing mapped data points
   */
  public static SkillDevelopmentTrendsResponseDto from(List<SkillDevelopmentDataPoint> dataPoints) {
    return SkillDevelopmentTrendsResponseDto.builder()
        .dataPoints(dataPoints.stream().map(SkillDevelopmentDataPointDto::from).toList())
        .build();
  }

  /** DTO representing a single monthly skill development data point. */
  @Builder
  @Schema(description = "Single monthly skill development data point")
  public record SkillDevelopmentDataPointDto(
      @Schema(
              description = "Unique identifier of the data point",
              example = "550e8400-e29b-41d4-a716-446655440000")
          UUID id,
      @Schema(description = "User identifier", example = "123e4567-e89b-12d3-a456-426614174000")
          UUID userId,
      @Schema(description = "Month represented by the data point", example = "2024-01-01")
          LocalDate month,
      @Schema(description = "Total skills during this month", example = "18") int totalSkills,
      @Schema(description = "Number of newly added skills", example = "2") int skillsAdded,
      @Schema(description = "Number of updated skills", example = "3") int skillsUpdated,
      @Schema(description = "Number of removed skills", example = "1") int skillsRemoved,
      @Schema(description = "Most active or impactful skill category", example = "Programming")
          String topCategory) {
    /** Maps a domain object to this DTO. */
    public static SkillDevelopmentDataPointDto from(SkillDevelopmentDataPoint dataPoint) {
      return SkillDevelopmentDataPointDto.builder()
          .id(dataPoint.id())
          .userId(dataPoint.userId())
          .month(dataPoint.month())
          .totalSkills(dataPoint.totalSkills())
          .skillsAdded(dataPoint.skillsAdded())
          .skillsUpdated(dataPoint.skillsUpdated())
          .skillsRemoved(dataPoint.skillsRemoved())
          .topCategory(dataPoint.topCategory())
          .build();
    }
  }
}
