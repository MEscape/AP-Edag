/**
 * Domain exceptions and domain-specific error types.
 *
 * <p>Defines checked and unchecked exceptions that represent domain error conditions (resource not
 * found, invalid request, duplicates, etc.). These exceptions are intended to be thrown by domain
 * services and entities and are handled by application or adapter layers.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code analytics} – Exceptions related to analytics and statistics features
 *   <li>{@code shared} – Common domain exceptions used across the bounded contexts
 *   <li>{@code skill} – Skill-specific domain exceptions
 *   <li>{@code user} – User and profile related exceptions
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.domain.exception;
