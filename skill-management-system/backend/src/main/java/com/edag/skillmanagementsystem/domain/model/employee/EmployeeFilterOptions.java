package com.edag.skillmanagementsystem.domain.model.employee;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import java.util.List;

/**
 * Represents available filter options for employee search.
 *
 * <p>This domain value object contains all unique values that can be used as filter options in the
 * employee search interface, including full skill, location, and category domain models with IDs
 * and names.
 *
 * @param skills list of all active skills available in the system
 * @param locations list of all active employee locations
 * @param skillCategories list of all active skill categories
 */
public record EmployeeFilterOptions(
    List<Skill> skills, List<Location> locations, List<SkillCategory> skillCategories) {}
