package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;

/**
 * Exception thrown when attempting to create a resource that already exists.
 *
 * <p>This exception should be thrown when a unique constraint violation would occur or when
 * duplicate resources are detected during creation operations.
 */
public class ResourceAlreadyExistsException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new resource already exists exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  public ResourceAlreadyExistsException(String messageKey) {
    super(messageKey);
  }

  /**
   * Constructs a new resource already exists exception with the specified message key and
   * arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public ResourceAlreadyExistsException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }
}
