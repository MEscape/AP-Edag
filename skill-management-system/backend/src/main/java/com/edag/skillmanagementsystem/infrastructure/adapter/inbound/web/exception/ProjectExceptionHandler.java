package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.project.InvalidProjectDataException;
import com.edag.skillmanagementsystem.domain.exception.project.ProjectNotFoundException;
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
 * Exception handler for project related domain exceptions.
 *
 * <p>Handles exceptions related to project operations, including project not found errors, invalid
 * project data, and other project-specific validation failures. Provides localized error messages
 * following RFC 7807.
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ProjectExceptionHandler {

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
   * Handles project not found exceptions.
   *
   * @param ex the {@link ProjectNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(ProjectNotFoundException.class)
  public ProblemDetail handleProjectNotFoundException(
      ProjectNotFoundException ex, WebRequest request) {

    log.debug("Project not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles invalid project data exceptions.
   *
   * @param ex the {@link InvalidProjectDataException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the bad request error
   */
  @ExceptionHandler(InvalidProjectDataException.class)
  public ProblemDetail handleInvalidProjectDataException(
      InvalidProjectDataException ex, WebRequest request) {

    log.debug("Invalid project data: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage("error.badrequest", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }
}
