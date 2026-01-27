package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.profile;

import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill.ProfileSkillResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO representing a complete user profile.
 *
 * <p>Includes personal information, skills, projects, and aggregated statistics.
 */
@Schema(description = "Complete user profile including identity, skills, projects, and statistics")
public record UserProfileResponseDto(
    @Schema(
            description = "Unique identifier of the user",
            example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
    @Schema(description = "User's first name", example = "Max") String firstName,
    @Schema(description = "User's last name", example = "Mustermann") String lastName,
    @Schema(description = "User's email address", example = "max.mustermann@edag.com") String email,
    @Schema(
            description = "User's job position ID",
            example = "550e8400-e29b-41d4-a716-446655440001")
        String positionId,
    @Schema(description = "User's job position or role", example = "Full Stack Developer")
        String position,
    @Schema(
            description = "User's work location ID",
            example = "550e8400-e29b-41d4-a716-446655440020")
        String locationId,
    @Schema(description = "User's work location", example = "Fulda") String location,
    @Schema(
            description = "Current availability status",
            example = "available",
            allowableValues = {"available", "partially_available", "unavailable"})
        String availability,
    @Schema(
            description = "Date when the user joined the organization",
            example = "2022-01-15T10:00:00Z")
        Instant joinDate,
    @Schema(description = "Total years of professional experience", example = "5")
        double yearsOfExperience,
    @Schema(description = "Short biography or personal description") String bio,
    @Schema(description = "List of the user's skills") List<ProfileSkillResponseDto> skills,
    @Schema(description = "List of the user's projects") List<ProjectResponseDto> projects,
    @Schema(description = "Total number of projects", example = "5") int totalProjects,
    @Schema(description = "Number of currently active projects", example = "2") int activeProjects,
    @Schema(description = "Total number of skills", example = "8") int totalSkills,
    @Schema(description = "Average skill proficiency score", example = "3.8")
        double averageSkillScore,
    @Schema(description = "Profile completion percentage (0-100)", example = "85.5")
        double completionRate,
    @Schema(description = "Timestamp of the last profile update", example = "2024-11-10T14:30:00Z")
        Instant lastUpdated) {

  /**
   * Maps a domain {@link UserProfile} into a response DTO.
   *
   * @param profile the domain model
   * @return the mapped DTO
   */
  public static UserProfileResponseDto from(UserProfile profile) {

    List<ProfileSkillResponseDto> skills =
        profile.skills().stream().map(ProfileSkillResponseDto::from).toList();

    List<ProjectResponseDto> projects =
        profile.projects().stream().map(ProjectResponseDto::from).toList();

    return new UserProfileResponseDto(
        profile.userId().toString(),
        profile.firstName(),
        profile.lastName(),
        profile.email(),
        toStringOrNull(profile.positionId()),
        profile.position(),
        toStringOrNull(profile.locationId()),
        profile.location(),
        profile.availability().name().toLowerCase(),
        profile.joinDate(),
        profile.yearsOfExperience(),
        profile.bio(),
        skills,
        projects,
        profile.totalProjects(),
        profile.activeProjects(),
        profile.totalSkills(),
        profile.averageSkillScore(),
        profile.completionRate(),
        profile.lastUpdated());
  }

  /**
   * Safely converts a UUID to String, returning null if the UUID is null.
   *
   * @param uuid the UUID to convert
   * @return the string representation or null
   */
  private static String toStringOrNull(UUID uuid) {
    return uuid != null ? uuid.toString() : null;
  }
}
