package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.analytics.UserStatisticsNotFoundException;
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
 * Exception handler for analytics related domain exceptions.
 *
 * <p>Handles exceptions related to user statistics, analytics data, and dashboard operations.
 * Provides localized error messages following RFC 7807.
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AnalyticsExceptionHandler {

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
   * Handles user statistics not found exceptions.
   *
   * @param ex the {@link UserStatisticsNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(UserStatisticsNotFoundException.class)
  public ProblemDetail handleUserStatisticsNotFoundException(
      UserStatisticsNotFoundException ex, WebRequest request) {

    log.debug("User statistics not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }
}
