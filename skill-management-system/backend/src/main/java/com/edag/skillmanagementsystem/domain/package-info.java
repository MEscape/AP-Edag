/**
 * Domain layer – core domain model, exceptions and port interfaces.
 *
 * <p>The domain layer contains the business model, domain exceptions and the definition of inbound
 * and outbound ports (interfaces) used by the application layer. Domain entities, value objects and
 * domain services should encapsulate business rules and invariants and be independent of frameworks
 * and persistence concerns.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code model} – Domain entities and value objects
 *   <li>{@code exception} – Domain-specific exceptions and error types
 *   <li>{@code port} – Port interfaces (inbound/outbound) for application use cases
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.domain;
