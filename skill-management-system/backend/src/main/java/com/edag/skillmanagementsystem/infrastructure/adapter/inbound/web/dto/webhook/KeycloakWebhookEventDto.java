package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.webhook;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Represents a webhook event payload received from Keycloak.
 *
 * <p>This DTO is used to transfer user-related event data (e.g., creation, update, or deletion)
 * from Keycloak to the Skill Management System. Each event contains essential user attributes
 * required for synchronization.
 *
 * @param eventType the type of the Keycloak event ({@code CREATE}, {@code UPDATE}, {@code DELETE})
 * @param keycloakId the unique identifier of the user in Keycloak
 * @param username the username of the user in Keycloak
 * @param email the email address of the user in Keycloak
 * @param firstName the first name of the user
 * @param lastName the last name of the user
 * @since 1.0.0
 */
public record KeycloakWebhookEventDto(

    /**
     * The event type triggered in Keycloak.
     *
     * <p>Possible values include {@code CREATE}, {@code UPDATE}, and {@code DELETE}. This field is
     * mandatory.
     */
    @NotBlank(message = "Event type must not be blank") String eventType,

    /**
     * The unique identifier of the user in Keycloak.
     *
     * <p>This field must not be {@code null} and corresponds to the subject ID in Keycloak.
     */
    @NotNull(message = "Keycloak ID must not be null") UUID keycloakId,

    /**
     * The username of the user in Keycloak.
     *
     * <p>Must be between 3 and 128 characters and cannot be blank.
     */
    @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 128, message = "Username must be between 3 and 128 characters")
        String username,

    /**
     * The user's email address registered in Keycloak.
     *
     * <p>Must be a valid email format and cannot be blank.
     */
    @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a valid email address")
        String email,

    /**
     * The user's first name in Keycloak.
     *
     * <p>Optional but must not exceed 128 characters.
     */
    @Size(max = 128, message = "First name must not exceed 128 characters") String firstName,

    /**
     * The user's last name in Keycloak.
     *
     * <p>Optional but must not exceed 128 characters.
     */
    @Size(max = 128, message = "Last name must not exceed 128 characters") String lastName) {}
