package com.edag.skillmanagementsystem.domain.model.project;

import java.util.List;
import java.util.UUID;

/**
 * Represents search criteria for filtering projects.
 *
 * <p>This domain value object encapsulates all filter parameters that can be applied when searching
 * for projects, including search terms, status filters, date ranges, and employee assignments.
 *
 * @param searchTerm optional text search term for matching against project name, description, or
 *     client
 * @param statusList optional list of project statuses to filter by
 * @param employeeIds optional list of employee IDs to filter projects assigned to specific
 *     employees
 * @param skillIds optional list of skill IDs to filter projects by technologies used
 * @since 1.0.0
 */
public record ProjectSearchCriteria(
    String searchTerm,
    List<ProjectStatus> statusList,
    List<UUID> employeeIds,
    List<UUID> skillIds) {}
