package com.edag.skillmanagementsystem.domain.exception.role;

import com.edag.skillmanagementsystem.domain.exception.shared.InvalidRequestException;
import java.util.UUID;

/**
 * Exception thrown when a role request operation is invalid.
 *
 * @since 1.0.0
 */
@SuppressWarnings("java:S110")
public class InvalidRoleRequestException extends InvalidRequestException {

  /**
   * Constructs a new {@code InvalidRoleRequestException} using a specific message key.
   *
   * @param messageKey the i18n message key
   * @param id the related role request ID or user ID
   */
  public InvalidRoleRequestException(String messageKey, UUID id) {
    super(messageKey, id);
  }
}
