package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.user.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Outbound port for managing {@link User} persistence operations.
 *
 * <p>This repository abstracts access to the underlying data storage for user entities, enabling
 * create, read, update, and delete operations based on the user's Keycloak identifier.
 *
 * <p>Implementations of this interface are typically responsible for communication with the
 * database or an external persistence system.
 */
public interface UserRepository {

  /**
   * Saves the given {@link User} entity to the persistence layer.
   *
   * <p>If the user already exists, this operation updates the existing record. Otherwise, it
   * creates a new entry.
   *
   * @param user the user entity to save
   * @return the saved {@link User} instance (possibly updated with an assigned ID or persisted
   *     values)
   */
  User save(User user);

  /**
   * Finds a {@link User} by its unique Keycloak identifier.
   *
   * @param keycloakId the unique {@link UUID} identifying the user in Keycloak
   * @return an {@link Optional} containing the user if found, or empty if not found
   */
  Optional<User> findByKeycloakId(UUID keycloakId);

  /**
   * Deletes a {@link User} from the persistence layer by its Keycloak identifier.
   *
   * <p>If no user with the given identifier exists, this operation has no effect.
   *
   * @param keycloakId the unique {@link UUID} identifying the user in Keycloak
   */
  void deleteByKeycloakId(UUID keycloakId);

  /**
   * Checks whether a {@link User} exists in the persistence layer with the given Keycloak
   * identifier.
   *
   * @param keycloakId the unique {@link UUID} identifying the user in Keycloak
   * @return {@code true} if a user with the given identifier exists, otherwise {@code false}
   */
  boolean existsByKeycloakId(UUID keycloakId);

  /**
   * Retrieves a paginated list of all users.
   *
   * <p>Used for admin interfaces to display and manage users with pagination support.
   *
   * @param pageable pagination and sorting parameters
   * @return a page of user entities
   */
  Page<User> findAll(Pageable pageable);

  /**
   * Searches for users by username or email with pagination.
   *
   * <p>Performs a case-insensitive partial match on username and email fields.
   *
   * @param searchTerm the search term to match against username or email
   * @param pageable pagination and sorting parameters
   * @return a page of matching user entities
   */
  Page<User> searchByUsernameOrEmail(String searchTerm, Pageable pageable);
}
