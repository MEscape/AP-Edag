package com.edag.keycloak.webhook;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;


public class WebhookEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final Logger LOG = Logger.getLogger(WebhookEventListenerProviderFactory.class.getName());

    private String webhookUrl;
    private String username;
    private String password;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new WebhookEventListenerProvider(session, webhookUrl, username, password);
    }

    @Override
    public void init(Config.Scope config) {
        webhookUrl = config.get("webhookUrl");
        username = config.get("username");
        password = config.get("password");

        if (webhookUrl == null || webhookUrl.isEmpty()) {
            throw new IllegalArgumentException("webhookUrl must be configured");
        }

        if (LOG.isLoggable(Level.INFO)) {
            LOG.info(String.format(
                    "Webhook event listener initialized: url=%s, authConfigured=%s",
                    webhookUrl,
                    username != null && password != null
            ));
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // No post-initialization actions are required for this implementation.
    }

    @Override
    public void close() {
        // No resources to close; nothing to clean up.
    }

    @Override
    public String getId() {
        return "webhook-event-listener";
    }
}
