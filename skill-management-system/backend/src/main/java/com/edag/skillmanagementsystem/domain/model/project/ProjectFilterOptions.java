package com.edag.skillmanagementsystem.domain.model.project;

import com.edag.skillmanagementsystem.domain.model.option.Skill;
import java.util.List;

/**
 * Represents available filter options for project search.
 *
 * <p>This domain value object contains all unique values that can be used as filter options in the
 * project search interface, including technologies/skills used across projects. Employee filtering
 * is handled via search term to avoid loading thousands of employee IDs.
 *
 * @param skills list of all skills/technologies associated with projects
 */
public record ProjectFilterOptions(List<Skill> skills) {}
