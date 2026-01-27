/**
 * REST controllers for the inbound web adapter.
 *
 * <p>Controllers expose HTTP endpoints for client applications and forward requests to application
 * services. They are responsible for request mapping, validation at the boundary, and translating
 * application responses into HTTP responses (status codes, headers, payloads).
 *
 * <p>Controllers should remain thin; business logic belongs to the application/service layer. Use
 * DTOs from the {@code dto} package for request and response payloads.
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;
