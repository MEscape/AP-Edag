package com.edag.skillmanagementsystem.domain.model.role;

/**
 * Enumeration of available system roles.
 *
 * <p>These roles correspond to Keycloak realm roles and determine user permissions in the system.
 *
 * @since 1.0.0
 */
public enum RoleType {
  /** Standard user role with basic access. */
  USER,

  /** Manager role with project and team management capabilities. */
  MANAGER,

  /** Administrator role with full system access. */
  ADMIN
}
