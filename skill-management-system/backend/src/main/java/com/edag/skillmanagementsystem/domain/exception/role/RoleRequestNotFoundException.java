package com.edag.skillmanagementsystem.domain.exception.role;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a role request is not found.
 *
 * @since 1.0.0
 */
@SuppressWarnings("java:S110")
public class RoleRequestNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new {@code RoleRequestNotFoundException} with the ID of the missing role request.
   *
   * @param requestId the ID of the missing role request
   */
  public RoleRequestNotFoundException(UUID requestId) {
    super("error.role.request.not.found", requestId);
  }
}
