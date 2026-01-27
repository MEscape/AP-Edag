package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.SkillDevelopmentDataPoint;
import com.edag.skillmanagementsystem.domain.model.analytics.UserStatistics;
import com.edag.skillmanagementsystem.domain.port.outbound.AnalyticsRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics.SkillDevelopmentJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics.UserStatisticsJpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data source implementation for analytics-related persistence operations.
 *
 * <p>Provides access to user statistics and skill development trend data using JPA repositories and
 * maps persistence entities to domain models.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsDataSource implements AnalyticsRepository {

  private final UserStatisticsJpaRepository statsRepository;
  private final SkillDevelopmentJpaRepository skillDevRepository;

  /** Default number of months to look back when retrieving skill development trends. */
  private static final int DEFAULT_MONTHS_LOOKBACK = 6;

  @Override
  @Transactional(readOnly = true)
  public UserStatistics getUserStatistics(UUID userId) {
    log.debug("Getting dashboard stats for user: {}", userId);

    return statsRepository
        .findByUserId(userId)
        .map(AnalyticsMapper::statsEntityToDomain)
        .orElseThrow(
            () -> new IllegalStateException("No dashboard stats found for user: " + userId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<SkillDevelopmentDataPoint> getSkillDevelopmentData(UUID userId) {
    log.debug("Getting skill development data for user: {}", userId);

    LocalDate cutoffDate = LocalDate.now().minusMonths(DEFAULT_MONTHS_LOOKBACK);

    return skillDevRepository.findByUserIdAndMonthAfterOrderByMonthAsc(userId, cutoffDate).stream()
        .map(AnalyticsMapper::skillDevEntityToDomain)
        .toList();
  }
}
