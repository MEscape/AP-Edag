package com.edag.skillmanagementsystem.domain.model.project;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a project in the system.
 *
 * <p>This domain entity contains detailed information about a project including timeline, status,
 * technologies used, and team members with their roles.
 *
 * @param id the unique identifier of the project
 * @param name the name of the project
 * @param description the project description
 * @param startDate the project start date
 * @param endDate the project end date (null if ongoing)
 * @param status the current status of the project
 * @param client the client name
 * @param teamSize the number of team members
 * @param technologies list of technologies used in the project
 * @param createdByUserId the ID of the user (manager) who created this project
 * @param members list of project members with their roles
 * @since 1.0.0
 */
@Builder
public record Project(
    UUID id,
    String name,
    String description,
    ProjectStatus status,
    LocalDate startDate,
    LocalDate endDate,
    String client,
    Integer teamSize,
    Set<String> technologies,
    UUID createdByUserId,
    Set<ProjectMemberName> members) {}
