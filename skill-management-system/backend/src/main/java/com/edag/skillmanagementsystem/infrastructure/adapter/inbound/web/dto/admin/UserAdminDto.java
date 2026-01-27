package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.admin;

import com.edag.skillmanagementsystem.domain.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing a user in the admin user management interface.
 *
 * <p>Contains basic user information for display in the admin user table.
 *
 * @param id the unique user identifier
 * @param username the username
 * @param email the user's email address
 * @param firstName the user's first name
 * @param lastName the user's last name
 * @param createdAt when the user was created
 * @param updatedAt when the user was last updated
 * @since 1.0.0
 */
@Schema(description = "User information for admin management")
public record UserAdminDto(
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
    @Schema(description = "Username", example = "john.doe") String username,
    @Schema(description = "Email address", example = "john.doe@example.com") String email,
    @Schema(description = "First name", example = "John") String firstName,
    @Schema(description = "Last name", example = "Doe") String lastName,
    @Schema(description = "Account creation timestamp") Instant createdAt,
    @Schema(description = "Last update timestamp") Instant updatedAt) {

  /**
   * Converts a User domain model to a UserAdminDto.
   *
   * @param user the user domain model
   * @return the DTO representation
   */
  public static UserAdminDto from(User user) {
    return new UserAdminDto(
        user.id(),
        user.username(),
        user.email(),
        user.firstName(),
        user.lastName(),
        user.createdAt(),
        user.updatedAt());
  }
}
