/**
 * Global exception handling for the web adapter layer.
 *
 * <p>Provides centralized error handling for REST controllers using Spring Boot's {@link
 * org.springframework.web.bind.annotation.RestControllerAdvice} mechanism and the {@link
 * org.springframework.http.ProblemDetail} API.
 *
 * <h3>Responsibilities:</h3>
 *
 * <ul>
 *   <li>Catch and transform application and system exceptions into standardized error responses
 *   <li>Provide consistent {@code application/problem+json} responses according to RFC 7807
 *   <li>Log exceptions and attach appropriate HTTP status codes
 * </ul>
 *
 * <h3>Key Components:</h3>
 *
 * <ul>
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception
 *       .GlobalExceptionHandler} – Handles all common exception types across the application
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception
 *       .UserExceptionHandler} – Handles user-related exceptions specifically
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception
 *       .EmployeeExceptionHandler} – Handles employee-related exceptions specifically
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception
 *       .ProfileExceptionHandler} – Handles profile-related exceptions specifically
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.exception;
