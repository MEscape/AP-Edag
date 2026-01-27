/**
 * JPA-backed data source implementations for outbound persistence ports.
 *
 * <p>This package provides concrete adapters that interact with the database using Spring Data JPA.
 * Each data source corresponds to a bounded context (analytics, employee, option, profile, user)
 * and exposes domain-oriented operations by delegating to JPA repositories and applying
 * entity-to-domain mappings where required.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code analytics} – Data sources for activity logs, statistics, and trend data
 *   <li>{@code employee} – Data sources for employee search, listing, and project/skill summaries
 *   <li>{@code option} – Data sources for lookup options such as locations, positions, or
 *       categories
 *   <li>{@code profile} – Data sources for detailed user profile information and modifications
 *   <li>{@code user} – Data sources for user identity, preferences, and basic metadata
 * </ul>
 *
 * <p>These adapters serve as the production implementation of outbound ports defined in the domain
 * layer.
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource;
