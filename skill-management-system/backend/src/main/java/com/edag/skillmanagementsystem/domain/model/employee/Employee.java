package com.edag.skillmanagementsystem.domain.model.employee;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.domain.model.user.AvailabilityStatus;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents an employee profile in the skill management system.
 *
 * <p>This domain entity contains employee-specific information extending the base user data with
 * work-related attributes such as position, location, availability, skills, and project history.
 *
 * @param userId the unique identifier linking to the user account
 * @param firstName the employee's first name
 * @param lastName the employee's last name
 * @param email the employee's email address
 * @param position the employee's job position or role
 * @param location the employee's work location
 * @param availability the employee's current availability status
 * @param yearsOfExperience the total years of professional experience
 * @param skills list of top employee skills (max 3 for search views)
 * @param totalProjects the total number of projects the employee has worked on
 * @param totalSkills the total number of skills possessed by the employee
 */
@Builder
public record Employee(
    UUID userId,
    String firstName,
    String lastName,
    String email,
    String position,
    String location,
    AvailabilityStatus availability,
    double yearsOfExperience,
    List<ProfileSkill> skills,
    int totalProjects,
    int totalSkills) {}
