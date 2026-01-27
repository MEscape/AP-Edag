package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.keycloak;

import com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException;
import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import com.edag.skillmanagementsystem.domain.port.outbound.KeycloakAdminClient;
import com.edag.skillmanagementsystem.infrastructure.properties.KeycloakAdminProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Component;

/**
 * Keycloak Admin Client implementation for role management.
 *
 * <p>Provides integration with Keycloak Admin API to assign and remove user roles. Uses service
 * account credentials for authentication.
 *
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClientImpl implements KeycloakAdminClient {

  private final KeycloakAdminProperties properties;
  private Keycloak keycloak;

  /** Initializes the Keycloak admin client after bean construction. */
  @PostConstruct
  public void init() {
    try {
      log.info("Initializing Keycloak Admin Client for realm: {}", properties.getTargetRealm());

      keycloak =
          KeycloakBuilder.builder()
              .serverUrl(properties.getServerUrl())
              .realm(properties.getRealm())
              .clientId(properties.getClientId())
              .username(properties.getUsername())
              .password(properties.getPassword())
              .build();

      // Test connection
      keycloak.serverInfo().getInfo();
      log.info("Keycloak Admin Client initialized successfully");
    } catch (Exception e) {
      log.error("Failed to initialize Keycloak Admin Client", e);
      throw new KeycloakRoleManagementException("error.keycloak.init.failed", e, e.getMessage());
    }
  }

  /** Closes the Keycloak admin client before bean destruction. */
  @PreDestroy
  public void cleanup() {
    if (keycloak != null) {
      log.info("Closing Keycloak Admin Client");
      keycloak.close();
    }
  }

  @Override
  public void assignRoleToUser(UUID userId, RoleType role) {
    log.debug("Assigning role {} to user {}", role, userId);

    try {
      RealmResource realmResource = keycloak.realm(properties.getTargetRealm());
      UsersResource usersResource = realmResource.users();
      UserResource userResource = usersResource.get(userId.toString());

      // Get role representation
      RoleRepresentation roleRepresentation =
          realmResource.roles().get(role.name().toLowerCase()).toRepresentation();

      // Assign role to user
      userResource.roles().realmLevel().add(List.of(roleRepresentation));

      log.info("Successfully assigned role {} to user {}", role, userId);
    } catch (Exception e) {
      log.error("Failed to assign role {} to user {}", role, userId, e);
      throw new KeycloakRoleManagementException(
          "error.keycloak.role.assign.failed", e, role.name(), userId);
    }
  }

  @Override
  public void removeRoleFromUser(UUID userId, RoleType role) {
    log.debug("Removing role {} from user {}", role, userId);

    try {
      RealmResource realmResource = keycloak.realm(properties.getTargetRealm());
      UsersResource usersResource = realmResource.users();
      UserResource userResource = usersResource.get(userId.toString());

      // Get role representation
      RoleRepresentation roleRepresentation =
          realmResource.roles().get(role.name().toLowerCase()).toRepresentation();

      // Remove role from user
      userResource.roles().realmLevel().remove(List.of(roleRepresentation));

      log.info("Successfully removed role {} from user {}", role, userId);
    } catch (Exception e) {
      log.error("Failed to remove role {} from user {}", role, userId, e);
      throw new KeycloakRoleManagementException(
          "error.keycloak.role.remove.failed", e, role.name(), userId);
    }
  }

  @Override
  public List<RoleType> getUserRoles(UUID userId) {
    log.debug("Retrieving roles for user {}", userId);

    try {
      RealmResource realmResource = keycloak.realm(properties.getTargetRealm());
      UsersResource usersResource = realmResource.users();
      UserResource userResource = usersResource.get(userId.toString());

      List<RoleRepresentation> roles = userResource.roles().realmLevel().listEffective();

      List<RoleType> roleTypes = new ArrayList<>();
      for (RoleRepresentation role : roles) {
        try {
          roleTypes.add(RoleType.valueOf(role.getName().toUpperCase()));
        } catch (IllegalArgumentException e) {
          // Ignore roles that don't match our enum
          log.debug("Ignoring unknown role: {}", role.getName());
        }
      }

      log.debug("User {} has roles: {}", userId, roleTypes);
      return roleTypes;
    } catch (Exception e) {
      log.error("Failed to retrieve roles for user {}", userId, e);
      throw new KeycloakRoleManagementException("error.keycloak.role.get.failed", e, userId);
    }
  }
}
