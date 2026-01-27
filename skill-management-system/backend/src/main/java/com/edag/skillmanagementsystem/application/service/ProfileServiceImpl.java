package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.user.InvalidProfileDataException;
import com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException;
import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import com.edag.skillmanagementsystem.domain.port.inbound.ProfileService;
import com.edag.skillmanagementsystem.domain.port.outbound.ProfileRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ProfileService interface.
 *
 * <p>This service orchestrates profile management operations, handling profile retrieval and
 * updates with comprehensive error handling and validation.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;

  @Override
  public UserProfile getProfile(UUID userId) {
    log.debug("Retrieving profile for user: {}", userId);

    return profileRepository
        .findProfileByUserId(userId)
        .orElseThrow(
            () -> {
              log.warn("Profile not found for user: {}", userId);
              return new ProfileNotFoundException(userId);
            });
  }

  @Override
  public UserProfile updateProfile(
      UUID userId,
      UUID positionId,
      UUID locationId,
      String availability,
      Double yearsOfExperience,
      String bio) {

    log.debug(
        "Updating profile for user: {} (positionId: {}, locationId: {}, availability: {}, experience: {}, bio: {})",
        userId,
        positionId,
        locationId,
        availability,
        yearsOfExperience,
        bio != null ? "provided" : "null");

    // Validate profile exists before attempting update
    if (!profileRepository.existsByUserId(userId)) {
      log.warn("Profile not found for user: {}", userId);
      throw new ProfileNotFoundException(userId);
    }

    // Attempt to update profile
    return profileRepository
        .updateProfileInfo(userId, positionId, locationId, availability, yearsOfExperience, bio)
        .orElseGet(
            () -> {
              // Update failed due to invalid reference data
              log.error("Profile update failed for user: {} - invalid reference data", userId);

              throw new InvalidProfileDataException(userId);
            });
  }
}
