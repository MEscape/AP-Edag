package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.UserStatisticsEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for managing {@link UserStatisticsEntity} persistence.
 *
 * <p>This repository provides:
 *
 * <ul>
 *   <li>Standard CRUD operations
 *   <li>User-specific lookup for analytical dashboard statistics
 * </ul>
 *
 * <p>User statistics represent precomputed aggregates used in the dashboard, such as total skills,
 * projects, evaluations, and other performance metrics. These values are typically recalculated on
 * a scheduled basis and cached for fast read access.
 */
@Repository
public interface UserStatisticsJpaRepository extends JpaRepository<UserStatisticsEntity, UUID> {

  /**
   * Retrieves the statistics record associated with the given user ID.
   *
   * <p>Used primarily by the analytics module to fetch dashboard metrics for a single user.
   *
   * @param userId the ID of the user whose statistics should be retrieved
   * @return an {@link Optional} containing the statistics entity if found
   */
  Optional<UserStatisticsEntity> findByUserId(UUID userId);
}
