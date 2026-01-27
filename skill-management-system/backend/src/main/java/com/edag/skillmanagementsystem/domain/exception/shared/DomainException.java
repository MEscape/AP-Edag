package com.edag.skillmanagementsystem.domain.exception.shared;

import java.io.Serial;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Base exception for all domain-level exceptions in the skill management system.
 *
 * <p>This exception serves as the parent class for all custom business logic exceptions. It
 * supports internationalized error messages through message keys and arguments.
 *
 * <p>Extending exceptions should supply meaningful {@code messageKey} values that map to localized
 * error messages in the resource bundles.
 *
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class DomainException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  /** The internationalization key used for localized error messages. */
  private final String messageKey;

  /** Optional arguments used for formatting localized message templates. */
  private final transient Object[] messageArgs;

  /**
   * Constructs a new domain exception with the specified message key.
   *
   * @param messageKey the i18n message key
   */
  protected DomainException(String messageKey) {
    super(messageKey);
    this.messageKey = messageKey;
    this.messageArgs = new Object[0];
  }

  /**
   * Constructs a new domain exception with the specified message key and arguments.
   *
   * @param messageKey the i18n message key
   * @param messageArgs the arguments for the message template
   */
  protected DomainException(String messageKey, Object... messageArgs) {
    super(messageKey);
    this.messageKey = messageKey;
    this.messageArgs = messageArgs != null ? messageArgs.clone() : new Object[0];
  }

  /**
   * Constructs a new domain exception with the specified message key and cause.
   *
   * @param messageKey the i18n message key
   * @param cause the cause of the exception
   */
  protected DomainException(String messageKey, Throwable cause) {
    super(messageKey, cause);
    this.messageKey = messageKey;
    this.messageArgs = new Object[0];
  }
}
