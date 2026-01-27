package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import com.edag.skillmanagementsystem.domain.port.inbound.ProfileService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.profile.UpdateProfileRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.profile.UserProfileResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user profile management operations.
 *
 * <p>Provides endpoints for retrieving and updating user profiles. Supports "me" as userId for
 * current authenticated user.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Profile Management", description = "APIs for managing user profiles")
public class ProfileController {

  private final ProfileService profileService;
  private final AuthorizationUtils authorizationUtils;

  @Operation(
      summary = "Get user profile",
      description =
          "Retrieves complete user profile including skills, projects, and statistics. "
              + "Use 'me' as userId to get the current authenticated user's profile.")
  @ApiResponse(
      responseCode = "200",
      description = "Profile retrieved successfully",
      content = @Content(schema = @Schema(implementation = UserProfileResponseDto.class)))
  @ApiResponse(responseCode = "400", description = "Invalid user ID format", content = @Content)
  @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileResponseDto> getProfile(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      Principal principal) {

    log.debug("REST request to get profile for user: {}", userId);

    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);
    UserProfile profile = profileService.getProfile(resolvedUserId);

    return ResponseEntity.ok(UserProfileResponseDto.from(profile));
  }

  @Operation(
      summary = "Update user profile",
      description =
          "Updates basic profile information. All fields are optional. "
              + "Users can only update their own profile.")
  @ApiResponse(
      responseCode = "200",
      description = "Profile updated successfully",
      content = @Content(schema = @Schema(implementation = UserProfileResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Invalid request data or reference data values",
      content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User not authorized to modify this profile",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
  @PutMapping("/{userId}")
  public ResponseEntity<UserProfileResponseDto> updateProfile(
      @Parameter(description = "User ID or 'me' for current user", example = "me")
          @PathVariable("userId")
          String userId,
      @Valid @RequestBody UpdateProfileRequestDto request,
      Principal principal) {

    log.debug("REST request to update profile for user: {}", userId);

    // Validate user has permission to update this profile
    authorizationUtils.validateUserAccess(userId, principal);

    // Resolve userId and perform update
    UUID resolvedUserId = authorizationUtils.resolveUserId(userId, principal);
    UserProfile profile =
        profileService.updateProfile(
            resolvedUserId,
            request.positionId(),
            request.locationId(),
            request.availability(),
            request.yearsOfExperience(),
            request.bio());

    return ResponseEntity.ok(UserProfileResponseDto.from(profile));
  }
}
