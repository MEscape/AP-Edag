package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.analytics.UserStatisticsNotFoundException;
import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import com.edag.skillmanagementsystem.domain.port.inbound.AnalyticsService;
import com.edag.skillmanagementsystem.domain.port.outbound.ActivityRepository;
import com.edag.skillmanagementsystem.domain.port.outbound.AnalyticsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AnalyticsService interface.
 *
 * <p>This service orchestrates analytics and activity tracking operations, delegating persistence
 * operations to the appropriate repositories and handling business logic and error handling.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

  private final AnalyticsRepository analyticsRepository;
  private final ActivityRepository activityRepository;

  @Override
  public UserStatistics getUserStatistics(UUID userId) {
    log.debug("Retrieving statistics for user: {}", userId);

    try {
      UserStatistics statistics = analyticsRepository.getUserStatistics(userId);
      log.debug("Successfully retrieved statistics for user: {}", userId);
      return statistics;
    } catch (IllegalStateException e) {
      log.warn("No statistics found for user: {}", userId);
      throw new UserStatisticsNotFoundException(userId);
    }
  }

  @Override
  public List<SkillDevelopmentDataPoint> getSkillDevelopmentTrends(UUID userId) {
    log.debug("Retrieving skill development trends for user: {}", userId);

    List<SkillDevelopmentDataPoint> dataPoints =
        analyticsRepository.getSkillDevelopmentData(userId);

    log.debug("Found {} skill development data points for user: {}", dataPoints.size(), userId);

    return dataPoints;
  }

  @Override
  public Page<Activity> getRecentActivities(UUID userId, Pageable pageable) {
    log.debug("Retrieving recent activities for user: {} with pageable: {}", userId, pageable);

    Page<Activity> activities = activityRepository.getRecentActivities(userId, pageable);

    log.debug(
        "Found {} activities for user: {} (page {} of {})",
        activities.getNumberOfElements(),
        userId,
        activities.getNumber() + 1,
        activities.getTotalPages());

    return activities;
  }
}
