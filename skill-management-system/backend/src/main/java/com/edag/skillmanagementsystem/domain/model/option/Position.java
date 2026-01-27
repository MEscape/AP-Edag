package com.edag.skillmanagementsystem.domain.model.option;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a job position or role in the organization.
 *
 * <p>This domain entity is part of the master data catalog and defines standardized roles such as
 * "Software Engineer", "Team Lead", or "Project Manager".
 *
 * @param id the unique identifier of the position
 * @param name the human-readable name of the position
 * @param active whether this position is currently active and selectable
 */
@Builder
public record Position(UUID id, String name, boolean active) {}
