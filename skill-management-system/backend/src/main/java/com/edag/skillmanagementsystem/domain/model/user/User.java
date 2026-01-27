package com.edag.skillmanagementsystem.domain.model.user;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a user in the skill management system.
 *
 * <p>Users are authenticated via Keycloak. This domain entity stores the local user profile
 * information synchronized from Keycloak, including identifiers, username, and basic profile data.
 *
 * @param id the Keycloak user identifier (subject claim from JWT)
 * @param username the username used for login and identification
 * @param email the user's email address
 * @param firstName the user's first name
 * @param lastName the user's last name
 * @param createdAt the timestamp when the user record was created
 * @param updatedAt the timestamp when the user record was last updated
 */
@Builder
public record User(
    UUID id,
    String username,
    String email,
    String firstName,
    String lastName,
    Instant createdAt,
    Instant updatedAt) {}
