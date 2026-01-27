package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.user.UserAlreadyExistsException;
import com.edag.skillmanagementsystem.domain.exception.user.UserNotFoundException;
import com.edag.skillmanagementsystem.domain.model.user.User;
import com.edag.skillmanagementsystem.domain.port.inbound.UserSyncService;
import com.edag.skillmanagementsystem.domain.port.outbound.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service implementing {@link UserSyncService}.
 *
 * <p>Synchronizes user data between Keycloak and the local Skill Management System. Handles user
 * creation, updates, and deletions triggered by Keycloak webhook events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSyncServiceImpl implements UserSyncService {

  private final UserRepository userRepository;

  @Override
  public void createUser(CreateUserCommand command) {
    log.info("Creating user with Keycloak ID: {}", command.keycloakId());

    if (userRepository.existsByKeycloakId(command.keycloakId())) {
      log.warn("User already exists with Keycloak ID: {}", command.keycloakId());
      throw new UserAlreadyExistsException(command.keycloakId());
    }

    User user =
        new User(
            command.keycloakId(),
            command.username(),
            command.email(),
            command.firstName(),
            command.lastName(),
            null,
            null);

    userRepository.save(user);
    log.info("User created successfully: {}", user.username());
  }

  @Override
  public void updateUser(UpdateUserCommand command) {
    log.info("Updating user with Keycloak ID: {}", command.keycloakId());

    User existingUser =
        userRepository
            .findByKeycloakId(command.keycloakId())
            .orElseThrow(() -> new UserNotFoundException(command.keycloakId()));

    User updatedUser =
        new User(
            existingUser.id(),
            command.username(),
            command.email(),
            command.firstName(),
            command.lastName(),
            existingUser.createdAt(),
            existingUser.updatedAt());

    userRepository.save(updatedUser);
    log.info("User updated successfully: {}", updatedUser.username());
  }

  @Override
  public void deleteUser(DeleteUserCommand command) {
    log.info("Deleting user with Keycloak ID: {}", command.keycloakId());

    if (!userRepository.existsByKeycloakId(command.keycloakId())) {
      log.warn("User not found with Keycloak ID: {}", command.keycloakId());
      throw new UserNotFoundException(command.keycloakId());
    }

    userRepository.deleteByKeycloakId(command.keycloakId());
    log.info("User deleted successfully with Keycloak ID: {}", command.keycloakId());
  }
}
