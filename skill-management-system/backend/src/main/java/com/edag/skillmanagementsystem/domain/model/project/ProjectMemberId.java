package com.edag.skillmanagementsystem.domain.model.project;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a project member reference using identifier values.
 *
 * <p>This record is used on the write side (create and update operations) where members are
 * identified by their unique IDs. It contains only the references to existing employees and
 * positions, without resolved names or details.
 *
 * <p>This model is typically mapped from inbound request DTOs and later validated or enriched
 * during persistence.
 *
 * @param employeeId the unique identifier of the employee participating in the project
 * @param positionId the unique identifier of the employee’s position within the project
 * @since 1.0.0
 */
@Builder
public record ProjectMemberId(UUID employeeId, UUID positionId) {}
