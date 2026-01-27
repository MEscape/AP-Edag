package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for user profile operations.
 *
 * <p>This repository handles persistence operations related to user profile information such as
 * position, location, availability, and bio. Skills management is handled by ProfileSkillRepository
 * for better separation of concerns.
 *
 * @since 1.0.0
 */
public interface ProfileRepository {

  /**
   * Finds a user profile by user ID.
   *
   * @param userId the user ID
   * @return an Optional containing the user profile if found, empty otherwise
   */
  Optional<UserProfile> findProfileByUserId(UUID userId);

  /**
   * Updates profile information for a user.
   *
   * <p>All parameters are optional. Pass null to skip updating a field. Reference data (position,
   * location) must exist before updating.
   *
   * @param userId the user ID
   * @param positionId the unique position ID (optional)
   * @param locationId the unique location ID (optional)
   * @param availability the availability status (optional)
   * @param yearsOfExperience the years of professional experience (optional)
   * @param bio the bio text (optional)
   * @return an Optional containing the updated profile if successful, empty if user not found or if
   *     reference data doesn't exist
   */
  Optional<UserProfile> updateProfileInfo(
      UUID userId,
      UUID positionId,
      UUID locationId,
      String availability,
      Double yearsOfExperience,
      String bio);

  /**
   * Checks whether a profile exists for the given user ID.
   *
   * @param userId the user ID
   * @return true if profile exists, false otherwise
   */
  boolean existsByUserId(UUID userId);
}
