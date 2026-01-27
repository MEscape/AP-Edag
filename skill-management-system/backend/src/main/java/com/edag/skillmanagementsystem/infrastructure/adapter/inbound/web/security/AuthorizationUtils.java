package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.security;

import com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling authentication and authorization operations.
 *
 * <p>Provides methods for resolving user identifiers, validating access permissions, and managing
 * the "me" alias for the currently authenticated user.
 *
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationUtils {

  private static final String CURRENT_USER_ALIAS = "me";

  /**
   * Resolves a user identifier to a UUID, supporting the "me" alias.
   *
   * @param userId the user identifier string (UUID or "me")
   * @param principal the authenticated user principal
   * @return the resolved user UUID
   */
  public UUID resolveUserId(String userId, Principal principal) {
    if (CURRENT_USER_ALIAS.equalsIgnoreCase(userId)) {
      log.debug("Resolving 'me' to current authenticated user");
      return UUID.fromString(principal.getName());
    }

    try {
      return UUID.fromString(userId);
    } catch (IllegalArgumentException e) {
      log.warn("Invalid UUID format: {}", userId);
      throw new IllegalArgumentException("Invalid user identifier format: " + userId, e);
    }
  }

  /**
   * Retrieves the UUID of the currently authenticated user.
   *
   * @param principal the authenticated principal
   * @return the user UUID from the Keycloak token
   */
  public UUID getCurrentUserId(Principal principal) {
    return UUID.fromString(principal.getName());
  }

  /** Retrieves the username (preferred_username) of the current user. */
  public String getCurrentUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) return null;

    Object details = auth.getPrincipal();
    if (details instanceof OAuth2AuthenticatedPrincipal principal) {
      return (String) principal.getAttributes().get("preferred_username");
    }

    return auth.getName();
  }

  /** Validates that users can only access their own resources. */
  public void validateUserAccess(String targetUserId, Principal principal) {
    if (CURRENT_USER_ALIAS.equalsIgnoreCase(targetUserId)) return;

    UUID authenticated = getCurrentUserId(principal);
    UUID target;

    try {
      target = UUID.fromString(targetUserId);
    } catch (Exception e) {
      throw new AccessDeniedException("error.forbidden", targetUserId);
    }

    if (!authenticated.equals(target)) {
      throw new AccessDeniedException("error.forbidden", targetUserId);
    }
  }

  /** Checks if a given identifier refers to the current user. */
  public boolean isCurrentUser(String userId, Principal principal) {
    if (CURRENT_USER_ALIAS.equalsIgnoreCase(userId)) return true;

    try {
      return getCurrentUserId(principal).equals(UUID.fromString(userId));
    } catch (Exception e) {
      return false;
    }
  }
}
