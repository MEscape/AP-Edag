package com.edag.skillmanagementsystem.domain.model.analytics;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents an activity or event in the skill management system.
 *
 * <p>Activities track significant user actions such as skill additions, evaluations, project
 * assignments, and profile updates. This serves as an audit trail and provides recent activity
 * feeds.
 *
 * @param id the unique identifier of the activity
 * @param userId the unique identifier of the user who owns this activity
 * @param activityType the type/category of the activity
 * @param timestamp the exact time when the activity occurred
 * @param relatedEntityId the ID of the related entity (e.g., skill ID, project ID)
 * @param relatedEntityType the type of related entity (e.g., "SKILL", "PROJECT")
 */
@Builder
public record Activity(
    UUID id,
    UUID userId,
    ActivityType activityType,
    Instant timestamp,
    UUID relatedEntityId,
    String relatedEntityType) {}
