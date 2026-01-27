package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.user.EmployeeNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.user.InvalidProfileDataException;
import com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.user.UserAlreadyExistsException;
import com.edag.skillmanagementsystem.domain.exception.user.UserNotFoundException;
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
 * Exception handler for user and profile related domain exceptions.
 *
 * <p>Handles exceptions related to user management, profile operations, and employee lookups.
 * Provides localized error messages following RFC 7807.
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class UserExceptionHandler {

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
   * Handles user not found exceptions.
   *
   * @param ex the {@link UserNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

    log.debug("User not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles employee not found exceptions.
   *
   * @param ex the {@link EmployeeNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(EmployeeNotFoundException.class)
  public ProblemDetail handleEmployeeNotFoundException(
      EmployeeNotFoundException ex, WebRequest request) {

    log.debug("Employee not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles profile not found exceptions.
   *
   * @param ex the {@link ProfileNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(ProfileNotFoundException.class)
  public ProblemDetail handleProfileNotFoundException(
      ProfileNotFoundException ex, WebRequest request) {

    log.debug("Profile not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles user already exists exceptions.
   *
   * @param ex the {@link UserAlreadyExistsException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the conflict error
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExistsException(
      UserAlreadyExistsException ex, WebRequest request) {

    log.debug("User already exists: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setTitle(getMessage("error.conflict", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles invalid profile data exceptions.
   *
   * @param ex the {@link InvalidProfileDataException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the validation error
   */
  @ExceptionHandler(InvalidProfileDataException.class)
  public ProblemDetail handleInvalidProfileDataException(
      InvalidProfileDataException ex, WebRequest request) {

    log.debug("Invalid profile data: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage("error.badrequest", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }
}
