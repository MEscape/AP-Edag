/**
 * Application layer – use case implementations and application configuration.
 *
 * <p>The classes in this package implement the application's use cases and coordinate interactions
 * between the domain and infrastructure layers. They orchestrate transactions, call outbound
 * adapters, and expose operations used by the inbound adapters (web/API). Keep domain logic inside
 * the domain layer; application services should be focused on orchestration and
 * application-specific validation.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code service} – Implementations of application use cases and service facades
 *   <li>{@code config} – Spring configuration classes (security, i18n, OpenAPI, etc.)
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.application;
