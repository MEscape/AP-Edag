package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;

/**
 * Exception thrown when a user attempts to access a resource they don't have permission to access.
 *
 * <p>This exception should be thrown when a user tries to access data that belongs to another user
 * or when they lack the necessary permissions.
 */
public class AccessDeniedException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new access denied exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  public AccessDeniedException(String messageKey) {
    super(messageKey);
  }

  /**
   * Constructs a new access denied exception with the specified message key and arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public AccessDeniedException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }
}
