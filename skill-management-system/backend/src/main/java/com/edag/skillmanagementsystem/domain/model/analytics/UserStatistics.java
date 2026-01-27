package com.edag.skillmanagementsystem.domain.model.analytics;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents aggregated analytics and dashboard metrics for a specific user.
 *
 * <p>This record encapsulates high-level statistical values related to the user's skills, project
 * involvement, and evaluation activity. It is used primarily for analytics dashboards and summary
 * views.
 *
 * @param userId the unique identifier of the user
 * @param totalSkills the total number of skills the user possesses
 * @param totalProjects the total number of projects associated with the user
 * @param totalRecommendations the number of skill or project recommendations generated for the user
 * @param activeProjects the number of projects the user is currently involved in
 * @param averageSkillScore the user's average proficiency score across all skills
 */
@Builder
public record UserStatistics(
    UUID userId,
    int totalSkills,
    int totalProjects,
    int totalRecommendations,
    int activeProjects,
    double averageSkillScore) {}
