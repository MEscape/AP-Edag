package com.edag.skillmanagementsystem.infrastructure.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Keycloak Admin Client.
 *
 * <p>Binds properties prefixed with {@code skill-management.keycloak-admin} from application
 * configuration files. Manages Keycloak admin connection settings and credentials for
 * administrative operations such as user and role management.
 *
 * <p>These values should be kept confidential and loaded securely from environment variables or
 * externalized configuration sources.
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "skill-management.keycloak-admin")
@Validated
@Component
@Data
public class KeycloakAdminProperties {

  /** Keycloak server URL (e.g., http://localhost:8080 or https://auth.example.com). */
  @NotBlank(message = "Keycloak server URL must not be blank")
  private String serverUrl;

  /** Realm name for authentication (the realm where the admin user exists). */
  @NotBlank(message = "Keycloak realm must not be blank")
  private String realm;

  /** Target realm for user management operations (where users and roles are managed). */
  @NotBlank(message = "Keycloak target realm must not be blank")
  private String targetRealm;

  /** Admin username for Keycloak authentication. */
  @NotBlank(message = "Keycloak admin username must not be blank")
  private String username;

  /** Admin password for Keycloak authentication. */
  @NotBlank(message = "Keycloak admin password must not be blank")
  private String password;

  /** Client ID for admin access (e.g., admin-cli). */
  @NotBlank(message = "Keycloak client ID must not be blank")
  private String clientId;
}
