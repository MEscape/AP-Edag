/**
 * JPA-based outbound persistence adapter.
 *
 * <p>This package contains the production implementation of outbound ports using Spring Data JPA.
 * It maps domain models to persistent entities and executes database queries via repository
 * interfaces.
 *
 * <h3>Structure</h3>
 *
 * <ul>
 *   <li>{@code model} – JPA entities for persistence
 *   <li>{@code repository} – Spring Data repositories
 *   <li>{@code datasource} – Implementations of outbound ports using the repositories
 *   <li>{@code specification} – JPA Specifications for dynamic queries
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa;
