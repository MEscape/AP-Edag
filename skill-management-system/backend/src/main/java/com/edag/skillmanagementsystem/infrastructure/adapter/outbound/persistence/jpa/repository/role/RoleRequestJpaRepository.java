package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.role;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.role.RoleRequestEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link RoleRequestEntity} persistence operations.
 *
 * <p>Provides CRUD functionality and optimized fetch behavior for role request workflows. Uses
 * {@link EntityGraph} to prevent N+1 query problems when loading associated user or reviewer
 * entities.
 *
 * @since 1.0.0
 */
@Repository
public interface RoleRequestJpaRepository extends JpaRepository<RoleRequestEntity, UUID> {

  /**
   * Retrieves all role requests submitted by a specific user, sorted by creation timestamp in
   * descending order.
   *
   * <p>This method is typically used to display a user's role request history or determine their
   * most recent request.
   *
   * @param userId the ID of the requesting user
   * @return a list of {@link RoleRequestEntity} sorted by newest first
   */
  List<RoleRequestEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);

  /**
   * Checks whether a user already has a role request in the specified status.
   *
   * <p>Primarily used to determine whether a user has an open or pending request before allowing
   * them to submit a new one.
   *
   * @param userId the ID of the user
   * @param status the target request status (e.g., {@code PENDING})
   * @return {@code true} if a matching request exists, otherwise {@code false}
   */
  boolean existsByUserIdAndStatus(UUID userId, RequestStatus status);

  /**
   * Retrieves a role request by its ID and eagerly loads related user and reviewer details.
   *
   * <p>The {@link EntityGraph} ensures that:
   *
   * <ul>
   *   <li><strong>user</strong> – the requester information is fetched
   *   <li><strong>reviewer</strong> – the admin who processed the request is fetched
   * </ul>
   *
   * <p>This avoids lazy loading during serialization or business logic operations that require full
   * request context.
   *
   * @param id the unique ID of the role request
   * @return an {@link Optional} containing the role request if found
   */
  @EntityGraph(attributePaths = {"user", "reviewer"})
  Optional<RoleRequestEntity> findById(UUID id);

  /**
   * Retrieves all role requests with pagination and sorting, eagerly loading associated user and
   * reviewer entities to prevent N+1 query issues.
   *
   * <p>The {@link EntityGraph} ensures that:
   *
   * <ul>
   *   <li><strong>user</strong> – the requester information is fetched
   *   <li><strong>reviewer</strong> – the admin who processed the request is fetched
   * </ul>
   *
   * @param pageable pagination and sorting parameters
   * @return a paginated list of role requests with related user/reviewer details eagerly loaded
   */
  @EntityGraph(attributePaths = {"user", "reviewer"})
  Page<RoleRequestEntity> findAll(Pageable pageable);

  /**
   * Retrieves all role requests that match the given status with pagination and sorting.
   * Eagerly loads associated user and reviewer entities to prevent N+1 query issues.
   *
   * <p>The {@link EntityGraph} ensures that:
   *
   * <ul>
   *   <li><strong>user</strong> – the requester information is fetched
   *   <li><strong>reviewer</strong> – the admin who processed the request is fetched
   * </ul>
   *
   * @param status the status to filter by
   * @param pageable pagination and sorting parameters
   * @return a paginated list of role requests with the specified status
   */
  @EntityGraph(attributePaths = {"user", "reviewer"})
  Page<RoleRequestEntity> findByStatus(RequestStatus status, Pageable pageable);
}
