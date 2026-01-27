package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.role.InvalidRoleRequestException;
import com.edag.skillmanagementsystem.domain.exception.role.KeycloakRoleManagementException;
import com.edag.skillmanagementsystem.domain.exception.role.RoleRequestNotFoundException;
import java.time.Instant;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for role-request related domain exceptions.
 *
 * <p>Handles exceptions related to role management, Keycloak role operations, and invalid role
 * requests. Provides localized error messages following RFC 7807.
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RoleExceptionHandler {

  private final MessageSource messageSource;

  /**
   * Enriches the given {@link ProblemDetail} with standard metadata.
   *
   * @param problem the problem detail object to enrich
   * @param ex the original exception
   */
  private void enrichProblem(ProblemDetail problem, Exception ex) {
    problem.setProperty("timestamp", Instant.now());
    problem.setProperty("exception", ex.getClass().getSimpleName());
  }

  /**
   * Retrieves a localized message from the message source.
   *
   * @param key the message key
   * @param locale the locale
   * @param args optional message arguments
   * @return the localized message
   */
  private String getMessage(String key, Locale locale, Object... args) {
    return messageSource.getMessage(key, args, key, locale);
  }

  /**
   * Handles {@link RoleRequestNotFoundException} which indicates that a role request could not be
   * located based on the provided identifier.
   *
   * @param ex the thrown exception
   * @param request the active web request
   * @return a populated {@link ProblemDetail} describing the error
   */
  @ExceptionHandler(RoleRequestNotFoundException.class)
  public ProblemDetail handleRoleRequestNotFound(
      RoleRequestNotFoundException ex, WebRequest request) {

    log.debug("Role request not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));

    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles {@link InvalidRoleRequestException}, typically thrown when a user attempts an invalid
   * or logically inconsistent role operation such as approving an already-approved request or
   * rejecting a request in an invalid state.
   *
   * @param ex the thrown exception
   * @param request the active web request
   * @return a {@link ProblemDetail} describing the validation failure
   */
  @ExceptionHandler(InvalidRoleRequestException.class)
  public ProblemDetail handleInvalidRoleRequest(
      InvalidRoleRequestException ex, WebRequest request) {

    log.debug("Invalid role request: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problem.setTitle(getMessage("error.badrequest", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));

    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles {@link KeycloakRoleManagementException}, typically thrown when communication with
   * Keycloak fails or role assignment/removal encounters an unexpected internal error.
   *
   * @param ex the thrown exception
   * @param request the active web request
   * @return a {@link ProblemDetail} representing the internal failure
   */
  @ExceptionHandler(KeycloakRoleManagementException.class)
  public ProblemDetail handleKeycloakRoleManagement(
      KeycloakRoleManagementException ex, WebRequest request) {

    log.error("Keycloak role management failure: {}", ex.getMessage(), ex);

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    problem.setTitle(getMessage("error.internal", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));

    enrichProblem(problem, ex);
    return problem;
  }
}
