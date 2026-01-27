package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import java.util.List;
import java.util.UUID;

/**
 * Repository port for retrieving analytics and statistics related to user skills, performance, and
 * development metrics.
 *
 * <p>This interface focuses exclusively on analytics computations and aggregated results.
 * Project-related and activity-related data is handled via their dedicated repository ports.
 */
public interface AnalyticsRepository {

  /**
   * Retrieves aggregated dashboard statistics for the specified user.
   *
   * <p>Typical metrics may include completed projects, skill proficiency levels, activity counts,
   * recently improved skills, or other KPIs shown on the user's dashboard.
   *
   * @param userId the identifier of the user whose statistics should be retrieved
   * @return an aggregated data model containing the user's high-level dashboard statistics
   */
  UserStatistics getUserStatistics(UUID userId);

  /**
   * Retrieves time-based skill development metrics for the given user.
   *
   * <p>Implementations may compute and return learning progress, proficiency trends, or growth
   * indicators for each tracked skill.
   *
   * @param userId the identifier of the user
   * @return a list of data points representing the user's skill development over time
   */
  List<SkillDevelopmentDataPoint> getSkillDevelopmentData(UUID userId);
}
