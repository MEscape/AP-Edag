package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for administrative user management operations.
 *
 * <p>Provides methods for administrators to view and manage users in the system with pagination
 * support for large user bases.
 *
 * @since 1.0.0
 */
public interface UserAdminService {

  /**
   * Retrieves a paginated list of all users in the system.
   *
   * <p>Supports sorting and pagination for efficient display in admin interfaces.
   *
   * @param pageable pagination and sorting parameters
   * @return a page of user domain models
   */
  Page<User> getAllUsers(Pageable pageable);

  /**
   * Searches for users by username or email with pagination.
   *
   * <p>Performs a case-insensitive partial match on username and email fields.
   *
   * @param searchTerm the search term to match against username or email
   * @param pageable pagination and sorting parameters
   * @return a page of matching user domain models
   */
  Page<User> searchUsers(String searchTerm, Pageable pageable);
}
