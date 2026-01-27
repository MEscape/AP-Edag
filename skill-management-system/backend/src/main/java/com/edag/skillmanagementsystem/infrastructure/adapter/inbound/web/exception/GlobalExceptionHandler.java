package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.shared.AccessDeniedException;
import com.edag.skillmanagementsystem.domain.exception.shared.AuthorizationDeniedException;
import com.edag.skillmanagementsystem.domain.exception.shared.DomainException;
import com.edag.skillmanagementsystem.domain.exception.shared.InvalidRequestException;
import com.edag.skillmanagementsystem.domain.exception.shared.ResourceAlreadyExistsException;
import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler that standardizes error responses across all REST controllers.
 *
 * <p>This component uses Spring Boot's {@link ProblemDetail} class to generate consistent {@code
 * application/problem+json} responses following RFC 7807. It ensures that common validation,
 * security, and system-level exceptions are translated into structured error representations for
 * API clients with internationalized messages.
 *
 * <h3>Features:</h3>
 *
 * <ul>
 *   <li>Unified error format for all REST APIs
 *   <li>Automatic handling of validation, parsing, access, and database exceptions
 *   <li>Timestamp and exception metadata enrichment for better traceability
 *   <li>Internationalized error messages (i18n) support
 * </ul>
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  private static final String BAD_REQUEST = "error.badrequest";
  private static final String INTERNAL_SERVER_ERROR = "error.internal";
  private static final String VALIDATION_ERROR = "error.validation";

  /**
   * Enriches the given {@link ProblemDetail} with standard metadata.
   *
   * <p>Each response includes a timestamp and the exception's simple class name to help clients and
   * logs trace the origin of an error.
   *
   * @param problem the problem detail object to enrich
   * @param ex the original exception
   */
  private void enrichProblem(final ProblemDetail problem, final Exception ex) {
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
   * Handles domain-level resource not found exceptions.
   *
   * @param ex the {@link ResourceNotFoundException} raised by domain logic
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(
      final ResourceNotFoundException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles domain-level resource already exists exceptions.
   *
   * @param ex the {@link ResourceAlreadyExistsException} raised by domain logic
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the conflict error
   */
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ProblemDetail handleResourceAlreadyExistsException(
      final ResourceAlreadyExistsException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setTitle(getMessage("error.conflict", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles invalid request exceptions.
   *
   * @param ex the {@link InvalidRequestException} raised by validation logic
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the invalid request error
   */
  @ExceptionHandler(InvalidRequestException.class)
  public ProblemDetail handleInvalidRequestException(
      final InvalidRequestException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(BAD_REQUEST, locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles all other domain exceptions.
   *
   * @param ex the {@link DomainException} raised by domain logic
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the error
   */
  @ExceptionHandler(DomainException.class)
  public ProblemDetail handleDomainException(final DomainException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(BAD_REQUEST, locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles exceptions thrown when request validation fails using {@code @Valid}.
   *
   * <p>This method collects all field errors and returns a localized, user-friendly message for
   * each invalid parameter. Validation messages are resolved through the {@link MessageSource}.
   *
   * @param ex the {@link MethodArgumentNotValidException} raised by Spring validation
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the validation failure
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(
      final MethodArgumentNotValidException ex, WebRequest request) {
    Locale locale = request.getLocale();

    // Collect and localize all field-level validation messages
    StringBuilder details = new StringBuilder();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError -> {
              String localizedMessage =
                  messageSource.getMessage(
                      Objects.requireNonNullElse(fieldError.getDefaultMessage(), VALIDATION_ERROR),
                      null,
                      fieldError.getDefaultMessage(),
                      locale);
              details
                  .append(fieldError.getField())
                  .append(": ")
                  .append(localizedMessage)
                  .append("; ");
            });

    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(VALIDATION_ERROR, locale));
    problem.setDetail(
        details.isEmpty() ? getMessage(VALIDATION_ERROR, locale) : details.toString().trim());
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles binding exceptions, typically occurring during parameter binding or property
   * population.
   *
   * <p>This includes validation errors for {@code @ModelAttribute} DTOs such as pagination or
   * filters.
   *
   * @param ex the {@link BindException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} representing a binding or validation error
   */
  @ExceptionHandler(BindException.class)
  public ProblemDetail handleBindException(final BindException ex, WebRequest request) {
    Locale locale = request.getLocale();

    StringBuilder details = new StringBuilder();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError -> {
              String localizedMessage =
                  messageSource.getMessage(
                      Objects.requireNonNullElse(fieldError.getDefaultMessage(), VALIDATION_ERROR),
                      null,
                      fieldError.getDefaultMessage(),
                      locale);
              details
                  .append(fieldError.getField())
                  .append(": ")
                  .append(localizedMessage)
                  .append("; ");
            });

    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(VALIDATION_ERROR, locale));
    problem.setDetail(
        details.isEmpty() ? getMessage(VALIDATION_ERROR, locale) : details.toString().trim());
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles malformed or unreadable JSON requests.
   *
   * @param ex the {@link HttpMessageNotReadableException} thrown during deserialization
   * @param request the current web request
   * @return a {@link ProblemDetail} with details about the malformed request body
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleInvalidBody(
      final HttpMessageNotReadableException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(BAD_REQUEST, locale));
    problem.setDetail(getMessage(BAD_REQUEST, locale));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles missing required request parameters.
   *
   * @param ex the {@link MissingServletRequestParameterException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} indicating the missing parameter
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ProblemDetail handleMissingParameter(
      final MissingServletRequestParameterException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(BAD_REQUEST, locale));
    problem.setDetail(ex.getMessage());
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles type mismatch exceptions when converting request parameters.
   *
   * @param ex the {@link MethodArgumentTypeMismatchException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the type mismatch
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleTypeMismatch(
      final MethodArgumentTypeMismatchException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage(BAD_REQUEST, locale));
    problem.setDetail(getMessage("validation.invalid", locale));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles domain-level authorization denied exceptions.
   *
   * @param ex the {@link AuthorizationDeniedException} raised by domain logic
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the forbidden error
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  public ProblemDetail handleAuthorizationDeniedException(
      final AuthorizationDeniedException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problem.setTitle(getMessage("error.forbidden", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles Spring Security access denied exceptions.
   *
   * @param ex the {@link org.springframework.security.access.AccessDeniedException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} representing the access denial
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleSecurityAccessDenied(
      final AccessDeniedException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problem.setTitle(getMessage("error.forbidden", locale));
    problem.setDetail(getMessage("auth.insufficient.permissions", locale));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles exceptions thrown explicitly with {@link ResponseStatusException}.
   *
   * @param ex the {@link ResponseStatusException} instance
   * @return a {@link ProblemDetail} matching the status and reason from the exception
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatus(final ResponseStatusException ex) {
    ProblemDetail problem = ProblemDetail.forStatus(ex.getStatusCode());
    problem.setTitle(ex.getReason());
    problem.setDetail(ex.getReason());
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles database access exceptions.
   *
   * @param ex the {@link DataAccessException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} indicating a database error
   */
  @ExceptionHandler(DataAccessException.class)
  public ProblemDetail handleDataAccessException(final DataAccessException ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problem.setTitle(getMessage(INTERNAL_SERVER_ERROR, locale));
    problem.setDetail(getMessage(INTERNAL_SERVER_ERROR, locale));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Catches any unhandled exceptions as a fallback.
   *
   * @param ex the {@link Exception} instance
   * @param request the current web request
   * @return a generic {@link ProblemDetail} indicating an internal server error
   */
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(final Exception ex, WebRequest request) {
    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problem.setTitle(getMessage(INTERNAL_SERVER_ERROR, locale));
    problem.setDetail(getMessage(INTERNAL_SERVER_ERROR, locale));
    enrichProblem(problem, ex);
    return problem;
  }
}
