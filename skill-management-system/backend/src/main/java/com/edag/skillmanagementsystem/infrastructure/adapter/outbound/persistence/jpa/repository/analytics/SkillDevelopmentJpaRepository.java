package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.analytics;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.SkillDevelopmentEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for managing {@link SkillDevelopmentEntity} persistence.
 *
 * <p>This repository provides:
 *
 * <ul>
 *   <li>Standard CRUD operations
 *   <li>User-specific lookups for skill development timelines
 *   <li>Ordered retrieval of time-series data for analytics dashboards
 * </ul>
 *
 * <p>Skill development data represents monthly snapshots of a user's skill progression, used for
 * trend charts and analytic evaluations across the system.
 */
@Repository
public interface SkillDevelopmentJpaRepository extends JpaRepository<SkillDevelopmentEntity, UUID> {

  /**
   * Retrieves all skill development entries for a user occurring after a given month, ordered
   * chronologically in ascending order.
   *
   * <p>This method is primarily used for:
   *
   * <ul>
   *   <li>Rendering time-based skill progression charts
   *   <li>Computing analytics over a defined lookback window
   * </ul>
   *
   * @param userId the user whose skill development data should be retrieved
   * @param cutoffDate the minimum month to include (exclusive)
   * @return a chronologically ordered list of {@link SkillDevelopmentEntity} records
   */
  List<SkillDevelopmentEntity> findByUserIdAndMonthAfterOrderByMonthAsc(
      UUID userId, LocalDate cutoffDate);
}
