package com.edag.skillmanagementsystem.domain.model.analytics;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents analytical insight into a user's skill development over a specific time period.
 *
 * <p>This record is used to track monthly progression, including how many skills were added,
 * updated, or removed, as well as overall proficiency trends. It enables charts, dashboards, and
 * reports that visualize learning and skill evolution.
 *
 * @param id the unique identifier of this data point
 * @param userId the unique identifier of the user
 * @param month the month this data point represents (typically the first day of the month)
 * @param totalSkills the total number of skills the user had during this period
 * @param skillsAdded number of new skills acquired in this month
 * @param skillsUpdated number of skills whose proficiency changed in this month
 * @param skillsRemoved number of skills removed during this month
 * @param topCategory the skill category with the most activity or highest impact in this period
 */
@Builder
public record SkillDevelopmentDataPoint(
    UUID id,
    UUID userId,
    LocalDate month,
    int totalSkills,
    int skillsAdded,
    int skillsUpdated,
    int skillsRemoved,
    String topCategory) {}
