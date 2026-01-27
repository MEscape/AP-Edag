package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.analytics.Activity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository port for accessing activity and audit-trail data.
 *
 * <p>This interface abstracts the retrieval of user-related activities (e.g., interactions,
 * updates, or changes in the system) for use in dashboards, timelines, or audit logs.
 */
public interface ActivityRepository {

  /**
   * Retrieves a paginated list of recent activities performed by or related to a user.
   *
   * <p>Activities are typically sorted by timestamp in descending order. Implementations may
   * include filtering logic or joins with additional metadata entities depending on persistence
   * design.
   *
   * @param userId the identifier of the user whose activity history should be fetched
   * @param pageable pagination and sorting parameters
   * @return a page containing recent user activities
   */
  Page<Activity> getRecentActivities(UUID userId, Pageable pageable);
}
