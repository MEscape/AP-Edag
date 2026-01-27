package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.ActivityEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for managing {@link ActivityEntity} persistence.
 *
 * <p>This repository provides:
 *
 * <ul>
 *   <li>Standard CRUD operations
 *   <li>Pagination support for activity feeds
 *   <li>Query methods optimized for retrieving user-specific activity history
 * </ul>
 *
 * <p>Activity records represent interactions or system events performed by users. Queries are
 * optimized for analytics dashboards and activity streams where ordering and efficient pagination
 * are important.
 */
@Repository
public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, UUID> {

  /**
   * Retrieves a paginated list of activities for a given user, ordered by timestamp in descending
   * order (newest activities first).
   *
   * <p>The {@code pageable} argument controls page size, page number, and sorting. No additional
   * relationships are fetched eagerly; standard lazy-loading rules apply.
   *
   * @param userId the ID of the user whose activities should be retrieved
   * @param pageable pagination configuration
   * @return a {@link Page} containing the user's recent activities
   */
  Page<ActivityEntity> findByUserIdOrderByTimestampDesc(UUID userId, Pageable pageable);
}
