package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.port.inbound.UserSyncService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.webhook.KeycloakWebhookEventDto;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling inbound webhook events from Keycloak.
 *
 * <p>This controller receives and processes user-related events such as creation, updates, and
 * deletions. It delegates synchronization logic to the {@link UserSyncService}, which ensures that
 * user data in the Skill Management System remains consistent with Keycloak.
 *
 * <p><b>Note:</b> This endpoint is intended for internal system communication and is hidden from
 * Swagger / OpenAPI documentation.
 *
 * @since 1.0.0
 */
@Hidden
@RestController
@RequestMapping("/v1/webhooks/keycloak")
@RequiredArgsConstructor
@Slf4j
public class KeycloakWebhookController {

  /** Application service responsible for user synchronization logic. */
  private final UserSyncService userSyncService;

  /**
   * Handles incoming webhook events from Keycloak.
   *
   * @param event the incoming Keycloak event payload
   * @return an HTTP response indicating the outcome of the operation
   */
  @PostMapping
  public ResponseEntity<Void> handleWebhook(@Valid @RequestBody KeycloakWebhookEventDto event) {
    log.info(
        "Received Keycloak webhook event: {} for user: {}", event.eventType(), event.username());

    switch (event.eventType()) {
      case "CREATE" -> handleCreate(event);
      case "UPDATE" -> handleUpdate(event);
      case "DELETE" -> handleDelete(event);
      default -> {
        log.warn("Unknown event type: {}", event.eventType());
        return ResponseEntity.badRequest().build();
      }
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Handles the "CREATE" event received from Keycloak.
   *
   * @param event the webhook event containing user details
   */
  private void handleCreate(KeycloakWebhookEventDto event) {
    userSyncService.createUser(
        new UserSyncService.CreateUserCommand(
            event.keycloakId(),
            event.username(),
            event.email(),
            event.firstName(),
            event.lastName()));
  }

  /**
   * Handles the "UPDATE" event received from Keycloak.
   *
   * @param event the webhook event containing updated user details
   */
  private void handleUpdate(KeycloakWebhookEventDto event) {
    userSyncService.updateUser(
        new UserSyncService.UpdateUserCommand(
            event.keycloakId(),
            event.username(),
            event.email(),
            event.firstName(),
            event.lastName()));
  }

  /**
   * Handles the "DELETE" event received from Keycloak.
   *
   * @param event the webhook event containing the user’s Keycloak ID
   */
  private void handleDelete(KeycloakWebhookEventDto event) {
    userSyncService.deleteUser(new UserSyncService.DeleteUserCommand(event.keycloakId()));
  }
}
