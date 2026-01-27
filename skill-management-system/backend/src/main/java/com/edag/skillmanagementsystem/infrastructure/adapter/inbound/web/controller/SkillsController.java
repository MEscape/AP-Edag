package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.domain.port.inbound.SkillManagementService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill.AddSkillRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill.ProfileSkillResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.skill.UpdateSkillRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.security.AuthorizationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing user skills in profiles.
 *
 * <p>Provides endpoints for adding, updating, and deleting skills in user profiles. All operations
 * require the authenticated user to own the profile being modified.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Skills Management", description = "APIs for managing user profile skills")
public class SkillsController {

  private final SkillManagementService skillManagementService;
  private final AuthorizationUtils authorizationUtils;

  @Operation(
      summary = "Add skill to profile",
      description =
          "Adds a new skill to the user's profile with proficiency information. "
              + "The skill must exist in the reference data and be active. "
              + "Duplicate skills are not allowed.")
  @ApiResponse(
      responseCode = "201",
      description = "Skill added successfully",
      content = @Content(schema = @Schema(implementation = ProfileSkillResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Invalid request data, skill not found, skill inactive, or duplicate skill",
      content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to modify this profile",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
  @PostMapping("/{userId}/skills")
  public ResponseEntity<ProfileSkillResponseDto> addSkill(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      @Valid @RequestBody AddSkillRequestDto request,
      Principal principal) {

    log.debug("REST request to add skillId '{}' to user: {}", request.skillId(), userId);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    ProfileSkill skill =
        skillManagementService.addSkill(
            resolvedUserId,
            request.skillId(),
            request.categoryId(),
            request.score(),
            request.yearsOfExperience(),
            request.lastUsed());

    return ResponseEntity.status(HttpStatus.CREATED).body(ProfileSkillResponseDto.from(skill));
  }

  @Operation(
      summary = "Update skill in profile",
      description =
          "Updates an existing skill's proficiency information. "
              + "All fields are optional - only provided fields will be updated.")
  @ApiResponse(
      responseCode = "200",
      description = "Skill updated successfully",
      content = @Content(schema = @Schema(implementation = ProfileSkillResponseDto.class)))
  @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to modify this profile",
      content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "Profile or skill relationship not found",
      content = @Content)
  @PutMapping("/{userId}/skills/{skillId}")
  public ResponseEntity<ProfileSkillResponseDto> updateSkill(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      @Parameter(
              description = "Skill relationship unique identifier",
              example = "123e4567-e89b-12d3-a456-426614174000")
          @PathVariable("skillId")
          String skillId,
      @Valid @RequestBody UpdateSkillRequestDto request,
      Principal principal) {

    log.debug("REST request to update skill {} for user: {}", skillId, userId);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    ProfileSkill skill =
        skillManagementService.updateSkill(
            resolvedUserId,
            UUID.fromString(skillId),
            request.score(),
            request.yearsOfExperience(),
            request.lastUsed());

    return ResponseEntity.ok(ProfileSkillResponseDto.from(skill));
  }

  @Operation(
      summary = "Delete skill from profile",
      description = "Removes a skill from the user's profile permanently.")
  @ApiResponse(responseCode = "204", description = "Skill deleted successfully")
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to modify this profile",
      content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "Profile or skill relationship not found",
      content = @Content)
  @DeleteMapping("/{userId}/skills/{skillId}")
  public ResponseEntity<Void> deleteSkill(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      @Parameter(
              description = "Skill relationship unique identifier",
              example = "123e4567-e89b-12d3-a456-426614174000")
          @PathVariable("skillId")
          String skillId,
      Principal principal) {

    log.debug("REST request to delete skill {} for user: {}", skillId, userId);

    // Validate access and resolve userId
    authorizationUtils.validateUserAccess(userId, principal);
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);

    skillManagementService.deleteSkill(resolvedUserId, UUID.fromString(skillId));

    return ResponseEntity.noContent().build();
  }
}
