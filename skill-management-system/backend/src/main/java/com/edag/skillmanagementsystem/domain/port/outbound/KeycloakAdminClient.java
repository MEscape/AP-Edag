package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.role.RoleType;
import java.util.List;
import java.util.UUID;

/**
 * Port interface for Keycloak role management operations.
 *
 * <p>Provides methods to assign and remove user roles in Keycloak as an administrator.
 *
 * @since 1.0.0
 */
public interface KeycloakAdminClient {

  /**
   * Assigns a role to a user in Keycloak.
   *
   * @param userId the Keycloak user ID (UUID)
   * @param role the role to assign
   * @throws com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException if
   *     the operation fails
   */
  void assignRoleToUser(UUID userId, RoleType role);

  /**
   * Removes a role from a user in Keycloak.
   *
   * @param userId the Keycloak user ID (UUID)
   * @param role the role to remove
   * @throws com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException if
   *     the operation fails
   */
  void removeRoleFromUser(UUID userId, RoleType role);

  /**
   * Retrieves all roles assigned to a user in Keycloak.
   *
   * @param userId the Keycloak user ID (UUID)
   * @return list of assigned roles
   * @throws com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException if
   *     the operation fails
   */
  List<RoleType> getUserRoles(UUID userId);
}
