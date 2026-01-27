/**
 * Application properties and configuration binding package.
 *
 * <p>Contains configuration property classes that bind external configuration sources to Java
 * objects using Spring Boot's {@code @ConfigurationProperties}.
 *
 * <h3>Key Components:</h3>
 *
 * <ul>
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.properties.SecurityProperties} -
 *       Security and CORS configuration properties
 *   <li>{@link com.edag.skillmanagementsystem.infrastructure.properties.SpringDocProperties} -
 *       OpenAPI documentation configuration properties
 * </ul>
 *
 * <h3>Configuration Files:</h3>
 *
 * <ul>
 *   <li>{@code application.yml} - Base configuration
 *   <li>{@code application-dev.yml} - Development profile
 *   <li>{@code application-prod.yml} - Production profile
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.properties;
