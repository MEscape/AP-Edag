package com.edag.skillmanagementsystem.domain.model.user;

/**
 * Represents the availability status of an employee.
 *
 * <p>This enumeration defines the possible availability states for employees in the system, which
 * can be used for resource planning and project staffing.
 */
public enum AvailabilityStatus {
  /** The employee is fully available for new projects or assignments. */
  AVAILABLE,

  /** The employee has limited availability or is partially allocated to other work. */
  PARTIALLY_AVAILABLE,

  /** The employee is currently unavailable for new projects or assignments. */
  UNAVAILABLE
}
