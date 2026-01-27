/**
 * JPA entity model classes used by the outbound persistence adapters.
 *
 * <p>This package contains the JPA entity definitions that map domain objects to database tables.
 * Entities are lightweight, persistable representations and should not contain business logic.
 * Mappers in the corresponding {@code datasource} package convert between these entities and the
 * domain model objects where necessary.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code analytics} – Entities for activity logs, skill development and statistics
 *   <li>{@code base} – Base audit entity with common persistence fields (id, createdAt, updatedAt)
 *   <li>{@code employee} – Entities representing employees, employee skills and project relations
 *   <li>{@code option} – Lookup entities such as locations, positions, skills and categories
 *   <li>{@code user} – User and identity related entities
 * </ul>
 *
 * <p>These entities are consumed by Spring Data JPA repositories and the outbound adapters to
 * persist and load domain state in a database-backed environment.
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model;
