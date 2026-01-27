package com.edag.skillmanagementsystem.domain.exception.role;

import com.edag.skillmanagementsystem.domain.exception.shared.DomainException;
import java.io.Serial;

/**
 * Exception thrown when Keycloak role management operations fail.
 *
 * @since 1.0.0
 */
public class KeycloakRoleManagementException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new Keycloak role management exception.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public KeycloakRoleManagementException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }

  /**
   * Constructs a new Keycloak role management exception with cause.
   *
   * @param messageKey the i18n message key
   * @param cause the underlying cause
   * @param messageArgs the arguments for the message template
   */
  public KeycloakRoleManagementException(
      String messageKey, Throwable cause, Object... messageArgs) {
    super(messageKey, cause, messageArgs);
  }
}
