package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;

/**
 * Exception thrown when an invalid request parameter or input is provided.
 *
 * <p>This exception should be thrown when request validation fails beyond standard Bean Validation,
 * such as business logic validation or parameter constraints.
 */
public class InvalidRequestException extends DomainException {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Constructs a new invalid request exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  public InvalidRequestException(String messageKey) {
    super(messageKey);
  }

  /**
   * Constructs a new invalid request exception with message key and arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  public InvalidRequestException(String messageKey, Object... messageArgs) {
    super(messageKey, messageArgs);
  }
}
