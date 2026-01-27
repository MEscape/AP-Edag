package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;

import com.edag.skillmanagementsystem.domain.exception.skill.DuplicateSkillException;
import com.edag.skillmanagementsystem.domain.exception.skill.InactiveSkillException;
import com.edag.skillmanagementsystem.domain.exception.skill.ProfileSkillNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.skill.SkillNotFoundException;
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
 * Exception handler for skill related domain exceptions.
 *
 * <p>Handles exceptions related to skill management, profile skills, and skill validation. Provides
 * localized error messages following RFC 7807.
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class SkillExceptionHandler {

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
   * Handles skill not found exceptions.
   *
   * @param ex the {@link SkillNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(SkillNotFoundException.class)
  public ProblemDetail handleSkillNotFoundException(SkillNotFoundException ex, WebRequest request) {

    log.debug("Skill not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles profile skill not found exceptions.
   *
   * @param ex the {@link ProfileSkillNotFoundException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the not found error
   */
  @ExceptionHandler(ProfileSkillNotFoundException.class)
  public ProblemDetail handleProfileSkillNotFoundException(
      ProfileSkillNotFoundException ex, WebRequest request) {

    log.debug("Profile skill not found: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(getMessage("error.notfound", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles duplicate skill exceptions.
   *
   * @param ex the {@link DuplicateSkillException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the conflict error
   */
  @ExceptionHandler(DuplicateSkillException.class)
  public ProblemDetail handleDuplicateSkillException(
      DuplicateSkillException ex, WebRequest request) {

    log.debug("Duplicate skill: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setTitle(getMessage("error.conflict", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }

  /**
   * Handles inactive skill exceptions.
   *
   * @param ex the {@link InactiveSkillException} instance
   * @param request the current web request
   * @return a {@link ProblemDetail} describing the validation error
   */
  @ExceptionHandler(InactiveSkillException.class)
  public ProblemDetail handleInactiveSkillException(InactiveSkillException ex, WebRequest request) {

    log.debug("Inactive skill: {}", ex.getMessage());

    Locale locale = request.getLocale();
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(getMessage("error.badrequest", locale));
    problem.setDetail(getMessage(ex.getMessageKey(), locale, ex.getMessageArgs()));
    enrichProblem(problem, ex);
    return problem;
  }
}
