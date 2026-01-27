package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;

/**
 * Exception thrown when a user is authenticated but lacks the necessary authorization to perform a
 * specific operation.
 *
 * <p>This exception should be thrown when a user's role or permissions are insufficient for the
 * requested action, such as when a regular user attempts to perform an admin-only operation.
 *
 * <p><strong>Note:</strong> This differs from {@link AccessDeniedException} which is used when a
 * user tries to access resources they don't own or have explicit access to.
 *
 * @since 1.0.0
 */
public class AuthorizationDeniedException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new authorization denied exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  public AuthorizationDeniedException(String messageKey) {
    super(messageKey);
  }

  /**
   * Constructs a new authorization denied exception with the specified message key and arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public AuthorizationDeniedException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }

  /**
   * Constructs a new authorization denied exception with the specified message key and cause.
   *
   * @param messageKey the i18n message key
   * @param cause the cause of the exception
   */
  public AuthorizationDeniedException(String messageKey, Throwable cause) {
    super(messageKey, cause);
  }
}
