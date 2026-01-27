package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.analytics;

import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import com.edag.skillmanagementsystem.domain.port.outbound.ActivityRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics.ActivityJpaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data source implementation for accessing activity data via JPA.
 *
 * <p>This class interacts with the {@link ActivityJpaRepository} to load recent user activities
 * sorted by timestamp. It maps persistence entities to domain models before returning them.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityDataSource implements ActivityRepository {

  private final ActivityJpaRepository activityRepository;

  /**
   * Retrieves a page of the most recent activities for the given user.
   *
   * @param userId the identifier of the user whose activities should be fetched
   * @param pageable pagination information including page size and sorting
   * @return a page containing the user's recent activities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Activity> getRecentActivities(UUID userId, Pageable pageable) {
    log.debug("Getting recent activities for user: {} with pageable: {}", userId, pageable);

    return activityRepository
        .findByUserIdOrderByTimestampDesc(userId, pageable)
        .map(ActivityMapper::entityToDomain);
  }
}
