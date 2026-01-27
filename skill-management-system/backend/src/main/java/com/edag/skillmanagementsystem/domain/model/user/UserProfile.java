package com.edag.skillmanagementsystem.domain.model.user;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a complete user profile including personal details, skills, projects, and aggregated
 * statistics.
 *
 * <p>This domain entity aggregates identity information, employment details, skills, and project
 * involvement to provide a comprehensive view of a user's professional profile.
 *
 * @param userId the unique identifier of the user
 * @param firstName the user's first name
 * @param lastName the user's last name
 * @param email the user's email address
 * @param position the user's job position or role
 * @param positionId the unique identifier of the user's position
 * @param location the user's work location
 * @param locationId the unique identifier of the user's location
 * @param availability the user's current availability status
 * @param joinDate the date the user joined the organization
 * @param yearsOfExperience the user's total years of professional experience
 * @param bio the user's biography or personal description
 * @param skills a list of the user's skills with proficiency details
 * @param projects a list of the user's projects
 * @param totalProjects total number of projects the user has worked on
 * @param activeProjects number of currently active projects involving the user
 * @param totalSkills total number of skills the user possesses
 * @param averageSkillScore the average score across all user skills
 * @param completionRate profile completion percentage (0–100)
 * @param lastUpdated timestamp of the last update to this profile
 */
@Builder
public record UserProfile(
    UUID userId,
    String firstName,
    String lastName,
    String email,
    String position,
    UUID positionId,
    String location,
    UUID locationId,
    AvailabilityStatus availability,
    Instant joinDate,
    double yearsOfExperience,
    String bio,
    List<ProfileSkill> skills,
    List<Project> projects,
    int totalProjects,
    int activeProjects,
    int totalSkills,
    double averageSkillScore,
    double completionRate,
    Instant lastUpdated) {}
