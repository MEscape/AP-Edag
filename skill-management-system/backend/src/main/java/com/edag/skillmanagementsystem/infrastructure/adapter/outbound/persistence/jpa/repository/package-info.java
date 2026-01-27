/**
 * Spring Data JPA repository interfaces used by outbound persistence adapters.
 *
 * <p>This package exposes repository interfaces that provide data access operations for JPA
 * entities. Repositories are intended to be thin abstractions over the persistence layer and return
 * entity types or simple projections; mapping to domain objects is handled by the datasource
 * adapters.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code analytics} – Repositories for activity logs, skill development and statistics
 *   <li>{@code employee} – Repositories for employees, projects and employee skills
 *   <li>{@code option} – Repositories for lookup data like locations, positions and skills
 *   <li>{@code user} – Repositories for user identity and profile data
 * </ul>
 *
 * <p>These interfaces are implemented automatically by Spring Data JPA at runtime and are consumed
 * by the outbound adapters to perform CRUD and query operations against the database.
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository;
