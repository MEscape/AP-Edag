/**
 * Port interfaces defining the domain's inbound and outbound contracts.
 *
 * <p>Ports define the minimal interfaces the application layer expects from external systems
 * (outbound) and the interface through which inbound requests invoke domain use cases.
 * Implementations of outbound ports are provided by infrastructure adapters.
 *
 * <h3>Subpackages</h3>
 *
 * <ul>
 *   <li>{@code inbound} – Interfaces used by application layer to invoke domain use cases
 *   <li>{@code outbound} – Interfaces that infrastructure adapters implement to provide
 *       persistence, messaging, etc.
 * </ul>
 *
 * @since 1.0.0
 */
package com.edag.skillmanagementsystem.domain.port;
