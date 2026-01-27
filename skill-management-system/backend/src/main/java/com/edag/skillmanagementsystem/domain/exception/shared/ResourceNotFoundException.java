package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;

/**
 * Exception thrown when a requested resource is not found.
 *
 * <p>This exception should be thrown when attempting to retrieve or operate on an entity that does
 * not exist in the system.
 */
public class ResourceNotFoundException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new resource not found exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  public ResourceNotFoundException(String messageKey) {
    super(messageKey);
  }

  /**
   * Constructs a new resource not found exception with the specified message key and arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public ResourceNotFoundException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }
}
