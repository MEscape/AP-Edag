package com.edag.keycloak.webhook;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebhookEventListenerProvider implements EventListenerProvider {

    private static final Logger LOG = Logger.getLogger(WebhookEventListenerProvider.class.getName());

    private final KeycloakSession session;
    private final String webhookUrl;
    private final String username;
    private final String password;
    private final HttpClient httpClient;

    public WebhookEventListenerProvider(KeycloakSession session, String webhookUrl, String username, String password) {
        this.session = session;
        this.webhookUrl = webhookUrl;
        this.username = username;
        this.password = password;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.REGISTER) {
            UserModel user = session.users().getUserById(session.getContext().getRealm(), event.getUserId());
            if (user != null) {
                sendWebhook("CREATE", user);
            } else if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(String.format("User not found for REGISTER event [userId=%s]", event.getUserId()));
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        if (adminEvent.getResourceType() != ResourceType.USER) {
            return;
        }

        String userId = extractUserId(adminEvent.getResourcePath());
        if (userId == null) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(String.format("Could not extract userId from resource path [path=%s]", adminEvent.getResourcePath()));
            }
            return;
        }

        UserModel user = session.users().getUserById(session.getContext().getRealm(), userId);
        if (user == null) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(String.format("User not found for AdminEvent [userId=%s]", userId));
            }
            return;
        }

        OperationType operation = adminEvent.getOperationType();
        switch (operation) {
            case CREATE:
            case UPDATE:
            case DELETE:
                sendWebhook(operation.name(), user);
                break;
            default:
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine(String.format("Ignoring admin operation type [operation=%s, userId=%s]", operation, userId));
                }
                break;
        }
    }

    private String extractUserId(String resourcePath) {
        if (resourcePath.startsWith("users/")) {
            String[] parts = resourcePath.split("/");
            if (parts.length >= 2) {
                return parts[1];
            }
        }
        return null;
    }

    private void sendWebhook(String eventType, UserModel user) {
        String usernameStr = user.getUsername() != null ? user.getUsername() : "(unknown)";
        try {
            String json = buildJsonPayload(eventType, user);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));

            // Add Basic Auth if credentials are provided
            if (username != null && password != null) {
                String auth = username + ":" + password;
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                requestBuilder.header("Authorization", "Basic " + encodedAuth);
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info(String.format("Webhook sent successfully [event=%s, user=%s, status=%d]",
                            eventType, usernameStr, statusCode));
                }
            } else {
                if (LOG.isLoggable(Level.WARNING)) {
                    LOG.warning(String.format("Webhook failed [event=%s, user=%s, status=%d, body=%s]",
                            eventType, usernameStr, statusCode, response.body()));
                }
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, String.format(
                    "I/O error sending webhook [event=%s, user=%s, url=%s]: %s",
                    eventType, usernameStr, webhookUrl, e.getMessage()), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.log(Level.SEVERE, String.format(
                    "Webhook interrupted [event=%s, user=%s, url=%s]",
                    eventType, usernameStr, webhookUrl), e);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format(
                    "Unexpected error sending webhook [event=%s, user=%s, url=%s]: %s",
                    eventType, usernameStr, webhookUrl, e.getMessage()), e);
        }
    }

    private String buildJsonPayload(String eventType, UserModel user) {
        return String.format(
                "{\"eventType\":\"%s\",\"keycloakId\":\"%s\",\"username\":\"%s\",\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\"}",
                eventType,
                user.getId(),
                escapeJson(user.getUsername()),
                escapeJson(user.getEmail()),
                escapeJson(user.getFirstName()),
                escapeJson(user.getLastName())
        );
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public void close() {
        // HttpClient doesn't need explicit cleanup
    }
}
