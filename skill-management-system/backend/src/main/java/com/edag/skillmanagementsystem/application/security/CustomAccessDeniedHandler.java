package com.edag.skillmanagementsystem.application.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Custom {@link AccessDeniedHandler} that converts Spring Security access denial events into a
 * domain-specific {@link
 * com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException}.
 *
 * <p>This ensures that authorization errors triggered by method-level security annotations (e.g.
 * {@code @PreAuthorize}) or URL-based security rules are handled consistently by the application's
 * global exception handling mechanism, producing standardized RFC 7807 {@code
 * application/problem+json} responses.
 *
 * <h3>Why this is needed:</h3>
 *
 * <ul>
 *   <li>Spring Security normally throws its own {@link AccessDeniedException} when a user lacks
 *       sufficient permissions.
 *   <li>These exceptions bypass the global exception handler unless translated into domain
 *       exceptions.
 *   <li>This handler intercepts those events and rethrows a domain exception so the {@code
 *       GlobalExceptionHandler} can format a proper API error response.
 * </ul>
 *
 * @since 1.0.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * Handles Spring Security access-denied events by translating them into a domain-level {@code
   * AccessDeniedException}.
   *
   * @param request the HTTP request during which the access denial occurred
   * @param response the HTTP response object
   * @param accessDeniedException the original Spring Security exception
   */
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) {

    throw new AccessDeniedException("auth.insufficient.permissions");
  }
}
