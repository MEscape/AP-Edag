package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.user;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for managing {@link UserEntity} persistence operations.
 *
 * <p>This repository provides methods to query, persist, and delete {@link UserEntity} instances in
 * the underlying database. It serves as the data access layer implementation for the domain {@code
 * UserRepository} outbound port.
 *
 * @see com.edag.skillmanagementsystem.domain.port.outbound.UserRepository
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

  /**
   * Searches for users by username or email with case-insensitive partial matching.
   *
   * @param username the username search term
   * @param email the email search term
   * @param pageable pagination and sorting parameters
   * @return a page of matching users
   */
  Page<UserEntity> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
      String username, String email, Pageable pageable);
}
