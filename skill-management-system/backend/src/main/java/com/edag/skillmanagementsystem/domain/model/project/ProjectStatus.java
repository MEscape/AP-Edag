package com.edag.skillmanagementsystem.domain.model.project;

/**
 * Enumeration of possible project statuses.
 *
 * <p>Defines the lifecycle states a project can be in within the skill management system.
 */
public enum ProjectStatus {
  /** The project is currently active and ongoing. */
  ACTIVE,

  /** The project has been completed. */
  COMPLETED,

  /** The project is planned but not yet started. */
  PLANNED
}
