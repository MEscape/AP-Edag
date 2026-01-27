package com.edag.skillmanagementsystem.domain.model.option;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a skill category in the master category catalog.
 *
 * <p>This domain entity represents a category used to group related skills together. Categories are
 * managed centrally by administrators.
 *
 * @param id the unique identifier of the category
 * @param name the name of the category (e.g., "Backend", "Frontend", "DevOps")
 * @param active whether this category is currently active and available
 */
@Builder
public record SkillCategory(UUID id, String name, boolean active) {}
