package com.edag.skillmanagementsystem.domain.model.project;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a fully resolved project member enriched with human-readable details.
 *
 * <p>This record is used on the read/query side and contains both the identifiers and the
 * corresponding display names. It is typically constructed by aggregating project member entities
 * and their related employee and position data.
 *
 * <p>Unlike {@link ProjectMemberId}, this model is intended for API responses, UI rendering, and
 * reporting.
 *
 * @param employeeId the unique identifier of the employee
 * @param employeeName the resolved full name of the employee
 * @param positionId the unique identifier of the employee’s position
 * @param positionName the human-readable name of the position
 * @since 1.0.0
 */
@Builder
public record ProjectMemberName(
    UUID employeeId, String employeeName, UUID positionId, String positionName) {}
