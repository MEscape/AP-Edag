package com.edag.skillmanagementsystem.domain.model.option;

import java.util.UUID;
import lombok.Builder;

/**
 * Represents a skill in the master skill catalog.
 *
 * <p>This domain entity represents a skill that can be assigned to employees. Skills are managed
 * centrally by administrators and can be enabled or disabled system-wide.
 *
 * @param id the unique identifier of the skill
 * @param name the name of the skill (e.g., "Java", "Spring Boot")
 * @param categoryId the ID of the skill category this skill belongs to
 * @param categoryName the name of the skill category (for display purposes)
 * @param active whether this skill is currently active and available for assignment
 */
@Builder
public record Skill(UUID id, String name, UUID categoryId, String categoryName, boolean active) {}
