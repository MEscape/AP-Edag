package com.edag.skillmanagementsystem.domain.model.employee;

import com.edag.skillmanagementsystem.domain.model.user.AvailabilityStatus;
import java.util.List;
import java.util.UUID;

/**
 * Represents search criteria for filtering employees.
 *
 * <p>This domain value object encapsulates all filter parameters that can be applied when searching
 * for employees, including search terms, skills, locations, and availability constraints.
 *
 * @param searchTerm optional text search term for matching against employee names, emails, or
 *     positions
 * @param skillIds optional list of skill ids to filter by
 * @param skillCategoryIds optional list of skill category ids to filter by
 * @param locationIds optional list of work location ids to filter by
 * @param availability optional list of availability statuses to filter by
 * @param minExperience optional minimum years of experience required
 */
public record EmployeeSearchCriteria(
    String searchTerm,
    List<UUID> skillIds,
    List<UUID> skillCategoryIds,
    List<UUID> locationIds,
    List<AvailabilityStatus> availability,
    Integer minExperience) {}
