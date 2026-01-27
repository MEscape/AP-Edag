/**
 * Data Transfer Objects (DTOs) used by the web controllers.
 *
 * <p>DTO classes define the shape of request and response payloads exchanged over the network. Keep
 * DTOs simple, serializable structures without business logic. Mapping between domain objects and
 * DTOs should be handled by controllers or dedicated mappers.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code analytics} – DTOs for analytics endpoints
 *   <li>{@code employee} – DTOs for employee search and profile endpoints
 *   <li>{@code options} – DTOs for lookup/option endpoints
 *   <li>{@code profile} – DTOs for user profile management
 *   <li>{@code project} – DTOs for project-related endpoints
 *   <li>{@code shared} – Reusable DTO fragments and metadata
 *   <li>{@code skill} – DTOs for skill creation/update and profile skills
 *   <li>{@code webhook} – DTOs for webhook payloads (e.g., Keycloak)
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto;
