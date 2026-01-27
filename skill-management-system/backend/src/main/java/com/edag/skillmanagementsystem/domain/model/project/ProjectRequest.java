package com.edag.skillmanagementsystem.domain.model.project;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a request to create a new project.
 *
 * <p>This domain model is used specifically for project creation. All required fields must be
 * provided.
 *
 * @param name the project name (required)
 * @param description the project description (optional)
 * @param status the project status (required)
 * @param startDate the project start date (required)
 * @param endDate the project end date (optional)
 * @param client the client name (optional)
 * @param technologies list of technology/skill IDs used in the project (optional)
 * @param members list of project members with employee IDs and position IDs (required, min 1)
 * @since 1.0.0
 */
@Builder
public record ProjectRequest(
    String name,
    String description,
    ProjectStatus status,
    LocalDate startDate,
    LocalDate endDate,
    String client,
    List<UUID> technologies,
    List<ProjectMemberId> members) {}
