package com.edag.skillmanagementsystem.domain.model.skill;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a skill in a user's profile with proficiency information.
 *
 * <p>This domain entity contains information about a user's proficiency in a specific skill,
 * including the skill details, category, proficiency score, years of experience, and last usage
 * date.
 *
 * @param id the unique identifier of the user-skill relationship (EmployeeSkillEntity ID)
 * @param skillName the name of the skill
 * @param category the category of the skill
 * @param proficiencyScore the proficiency score (0-100)
 * @param yearsOfExperience the years of experience with this skill
 * @param lastUsed the date when the skill was last used (optional)
 */
@Builder
public record ProfileSkill(
    UUID id,
    String skillName,
    String category,
    int proficiencyScore,
    double yearsOfExperience,
    LocalDate lastUsed) {}
