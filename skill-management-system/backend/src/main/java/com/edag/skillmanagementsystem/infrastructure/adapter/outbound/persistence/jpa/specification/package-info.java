/**
 * JPA {@link org.springframework.data.jpa.domain.Specification} builders for dynamic queries.
 *
 * <p>This package contains utility classes that build Spring Data JPA {@code Specification}
 * instances to express dynamic filtering and query predicates for entity types (for example {@code
 * EmployeeSpecification}). Specifications are consumed by repository implementations or datasource
 * adapters to apply complex search criteria at the database level.
 *
 * <p>Keep specification builders focused on criteria construction and avoid embedding business
 * logic or I/O operations. Reuse and combine small specifications where possible to improve
 * testability.
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.specification;
