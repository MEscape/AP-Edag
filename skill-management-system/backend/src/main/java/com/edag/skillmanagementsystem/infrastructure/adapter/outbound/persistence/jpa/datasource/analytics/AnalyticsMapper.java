package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.SkillDevelopmentEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.UserStatisticsEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting analytics-related persistence entities into corresponding domain
 * models.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnalyticsMapper {

  /**
   * Maps a {@link UserStatisticsEntity} to a {@link UserStatistics} domain model.
   *
   * @param entity the statistics entity to convert; may be {@code null}
   * @return the mapped domain model, or {@code null} if the entity is null
   */
  public static UserStatistics statsEntityToDomain(UserStatisticsEntity entity) {
    if (entity == null) {
      return null;
    }

    return UserStatistics.builder()
        .userId(entity.getUserId())
        .totalSkills(entity.getTotalSkills())
        .totalProjects(entity.getTotalProjects())
        .totalRecommendations(entity.getTotalRecommendations())
        .activeProjects(entity.getActiveProjects())
        .averageSkillScore(entity.getAverageSkillScore().doubleValue())
        .build();
  }

  /**
   * Maps a {@link SkillDevelopmentEntity} to a {@link SkillDevelopmentDataPoint} domain model.
   *
   * @param entity the skill development entity to convert; may be {@code null}
   * @return the mapped data point, or {@code null} if the entity is null
   */
  public static SkillDevelopmentDataPoint skillDevEntityToDomain(SkillDevelopmentEntity entity) {
    if (entity == null) {
      return null;
    }

    return SkillDevelopmentDataPoint.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .month(entity.getMonth())
        .totalSkills(entity.getTotalSkills())
        .skillsAdded(entity.getSkillsAdded())
        .skillsUpdated(entity.getSkillsUpdated())
        .skillsRemoved(entity.getSkillsRemoved())
        .topCategory(entity.getTopCategory())
        .build();
  }
}
