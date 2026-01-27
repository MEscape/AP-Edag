package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.exception.analytics.UserStatisticsNotFoundException;
import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for analytics and activity tracking operations.
 *
 * <p>Provides methods to retrieve user statistics, skill development trends, and recent user
 * activities for dashboard and reporting purposes.
 *
 * @since 1.0.0
 */
public interface AnalyticsService {

  /**
   * Retrieves aggregated statistics for a specific user.
   *
   * <p>Statistics typically include metrics such as total skills, skill levels, certifications, and
   * other relevant KPIs.
   *
   * @param userId the unique identifier of the user
   * @return the user's statistics
   * @throws UserStatisticsNotFoundException if no statistics exist for the user
   */
  UserStatistics getUserStatistics(UUID userId);

  /**
   * Retrieves skill development trend data for a specific user.
   *
   * <p>Returns a time series of data points showing how the user's skills have evolved over time,
   * typically over the past 6 months.
   *
   * @param userId the unique identifier of the user
   * @return list of skill development data points ordered chronologically
   */
  List<SkillDevelopmentDataPoint> getSkillDevelopmentTrends(UUID userId);

  /**
   * Retrieves a paginated list of recent activities for a specific user.
   *
   * <p>Activities include events such as skill additions, level changes, certification completions,
   * and other tracked user actions.
   *
   * @param userId the unique identifier of the user
   * @param pageable pagination and sorting parameters
   * @return a page containing recent activities
   */
  Page<Activity> getRecentActivities(UUID userId, Pageable pageable);
}
