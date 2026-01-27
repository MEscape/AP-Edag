package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import java.util.UUID;

/**
 * Service interface for user profile management operations.
 *
 * <p>Provides methods to retrieve and update user profile information including basic details,
 * skills, projects, position, location, and availability status.
 *
 * @since 1.0.0
 */
public interface ProfileService {

  /**
   * Retrieves a user's complete profile.
   *
   * <p>Supports using "me" as the userId to retrieve the currently authenticated user's profile.
   * The userId will be resolved to the actual user identifier through the security context.
   *
   * @param userId the user identifier or "me" for the current user
   * @return the user's complete profile
   * @throws com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException if the
   *     profile does not exist
   */
  UserProfile getProfile(UUID userId);

  /**
   * Updates a user's profile information.
   *
   * <p>All parameters are optional. Only non-null values will be updated. Validates that position,
   * location, and availability values exist in reference data.
   *
   * @param userId the user identifier or "me" for the current user
   * @param positionId the new positionId (optional)
   * @param locationId the new work locationId (optional)
   * @param availability the new availability status (optional)
   * @param yearsOfExperience the years of professional experience (optional)
   * @param bio the new biography text (optional)
   * @return the updated user profile
   * @throws com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException if the
   *     profile does not exist
   * @throws com.edag.skillmanagementsystem.domain.exception.user.InvalidProfileDataException if any
   *     reference data value is invalid
   */
  UserProfile updateProfile(
      UUID userId,
      UUID positionId,
      UUID locationId,
      String availability,
      Double yearsOfExperience,
      String bio);
}
