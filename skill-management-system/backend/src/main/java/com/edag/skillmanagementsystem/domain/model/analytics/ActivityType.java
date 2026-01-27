package com.edag.skillmanagementsystem.domain.model.analytics;

/**
 * Enumeration of possible activity types in the system.
 *
 * <p>Defines the different categories of user activities that are tracked and displayed in the
 * activity feed. This includes skill management, project lifecycle, team management, role requests,
 * and user account activities.
 */
public enum ActivityType {

  // Skill-related activities
  SKILL_ADDED,
  SKILL_UPDATED,
  SKILL_REMOVED,

  // Project-related activities
  PROJECT_CREATED,
  PROJECT_STARTED, // maps to status ACTIVE
  PROJECT_COMPLETED, // maps to status COMPLETED
  PROJECT_PLANNED, // maps to status PLANNED
  PROJECT_UPDATED,
  PROJECT_DELETED,

  // Project member activities
  PROJECT_MEMBER_ADDED,
  PROJECT_MEMBER_REMOVED,
  PROJECT_MEMBER_ROLE_CHANGED,

  // Project skill/technology activities
  PROJECT_SKILL_ADDED,
  PROJECT_SKILL_REMOVED,

  // Role request activities
  ROLE_REQUEST_CREATED,
  ROLE_REQUEST_APPROVED,
  ROLE_REQUEST_REJECTED,
  ROLE_REQUEST_CANCELLED,
  ROLE_REQUEST_REVIEWED,
  ROLE_REQUEST_UPDATED,

  // Profile-related activities
  PROFILE_CREATED,
  PROFILE_UPDATED,

  // User account activities
  USER_ACCOUNT_CREATED,
  USER_EMAIL_CHANGED,
  USER_NAME_CHANGED,
  USER_ACCOUNT_DELETED
}