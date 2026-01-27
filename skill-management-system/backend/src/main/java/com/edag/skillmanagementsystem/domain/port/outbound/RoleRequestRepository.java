package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for role request persistence operations.
 *
 * <p>Provides methods to store, retrieve, and query role change requests in the system.
 *
 * @since 1.0.0
 */
public interface RoleRequestRepository {

  /**
   * Saves a role request.
   *
   * @param roleRequest the role request to save
   * @return the saved role request with generated ID
   */
  RoleRequest save(RoleRequest roleRequest);

  /**
   * Finds a role request by its ID.
   *
   * @param id the role request ID
   * @return optional containing the role request if found
   */
  Optional<RoleRequest> findById(UUID id);

  /**
   * Finds all role requests for a specific user.
   *
   * @param userId the user ID
   * @return list of role requests for the user
   */
  List<RoleRequest> findByUserId(UUID userId);

  /**
   * Finds all role requests with a specific status, with pagination and sorting.
   *
   * @param status the request status
   * @param pageable pagination and sorting parameters
   * @return paginated list of role requests with the given status
   */
  Page<RoleRequest> findByStatus(RequestStatus status, Pageable pageable);

  /**
   * Retrieves all role requests with pagination and sorting.
   *
   * @param pageable pagination and sorting parameters
   * @return paginated list of role requests
   */
  Page<RoleRequest> findAll(Pageable pageable);

  /**
   * Checks if a user has a pending request for a specific role.
   *
   * @param userId the user ID
   * @param status the request status
   * @return true if a pending request exists
   */
  boolean existsByUserIdAndStatus(UUID userId, RequestStatus status);

  /**
   * Deletes a role request by its ID.
   *
   * @param id the role request ID
   */
  void deleteById(UUID id);
}
