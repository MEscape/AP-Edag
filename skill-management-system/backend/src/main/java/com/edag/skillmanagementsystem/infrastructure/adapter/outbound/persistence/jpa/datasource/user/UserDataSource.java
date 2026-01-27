package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.user;

import com.edag.skillmanagementsystem.domain.model.user.User;
import com.edag.skillmanagementsystem.domain.port.outbound.UserRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.user.UserJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data source for managing the profile skills of users.
 *
 * <p>Handles adding, updating, and deleting skills, including validation, persistence, and mapping
 * to domain models.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataSource implements UserRepository {

  private final UserJpaRepository userRepository;

  @Override
  @Transactional
  public User save(User user) {
    log.debug("Saving user: {}", user.id());

    UserEntity entity = UserMapper.domainToEntity(user);
    UserEntity saved = userRepository.save(entity);

    return UserMapper.entityToDomain(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByKeycloakId(UUID keycloakId) {
    log.debug("Finding user by keycloak ID: {}", keycloakId);

    return userRepository.findById(keycloakId).map(UserMapper::entityToDomain);
  }

  @Override
  @Transactional
  public void deleteByKeycloakId(UUID keycloakId) {
    log.debug("Deleting user by keycloak ID: {}", keycloakId);

    userRepository.deleteById(keycloakId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByKeycloakId(UUID keycloakId) {
    return userRepository.existsById(keycloakId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> findAll(Pageable pageable) {
    log.debug("Finding all users with pagination: {}", pageable);

    return userRepository.findAll(pageable).map(UserMapper::entityToDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> searchByUsernameOrEmail(String searchTerm, Pageable pageable) {
    log.debug("Searching users by term '{}' with pagination: {}", searchTerm, pageable);

    return userRepository
        .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            searchTerm, searchTerm, pageable)
        .map(UserMapper::entityToDomain);
  }
}
