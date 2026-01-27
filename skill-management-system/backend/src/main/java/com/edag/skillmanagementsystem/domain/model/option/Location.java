package com.edag.skillmanagementsystem.domain.model.option;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a work location in the master location catalog.
 *
 * <p>This domain entity represents a physical or virtual work location where employees can be
 * based. Locations are managed centrally by administrators.
 *
 * @param id the unique identifier of the location
 * @param name the name of the location (e.g., "Munich", "Berlin", "Remote")
 * @param active whether this location is currently active and available
 */
@Builder
public record Location(UUID id, String name, boolean active) {}
