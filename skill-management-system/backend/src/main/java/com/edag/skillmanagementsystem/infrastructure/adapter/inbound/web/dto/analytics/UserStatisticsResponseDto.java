package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

/**
 * Data transfer object representing aggregated dashboard statistics for a user.
 *
 * @since 1.0.0
 */
@Builder
@Schema(description = "Aggregated statistics for a user")
public record UserStatisticsResponseDto(
    @Schema(
            description = "User unique identifier",
            example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,
    @Schema(description = "Total number of skills", example = "25") int totalSkills,
    @Schema(description = "Total number of projects", example = "12") int totalProjects,
    @Schema(description = "Total number of received recommendations", example = "6")
        int totalRecommendations,
    @Schema(description = "Number of currently active projects", example = "3") int activeProjects,
    @Schema(description = "Average skill score across all skills", example = "3.4")
        Double averageSkillScore) {

  /**
   * Creates a DTO instance from a {@link UserStatistics} domain model.
   *
   * @param statistics the domain object to convert
   * @return the corresponding response DTO
   */
  public static UserStatisticsResponseDto from(UserStatistics statistics) {
    return UserStatisticsResponseDto.builder()
        .userId(statistics.userId())
        .totalSkills(statistics.totalSkills())
        .totalProjects(statistics.totalProjects())
        .totalRecommendations(statistics.totalRecommendations())
        .activeProjects(statistics.activeProjects())
        .averageSkillScore(statistics.averageSkillScore())
        .build();
  }
}
