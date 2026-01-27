package com.edag.skillmanagementsystem.domain.port.inbound;

import java.util.UUID;

/**
 * Defines the use case for synchronizing user data based on Keycloak events.
 *
 * <p>Implementations of this interface are responsible for handling create, update, and delete
 * operations originating from Keycloak, ensuring that user data within the Skill Management System
 * stays consistent with Keycloak.
 */
public interface UserSyncService {

  /**
   * Handles the creation of a new user based on a Keycloak "CREATE" event.
   *
   * @param command the command object containing the user's details to create
   */
  void createUser(CreateUserCommand command);

  /**
   * Handles updates to an existing user based on a Keycloak "UPDATE" event.
   *
   * @param command the command object containing the updated user details
   */
  void updateUser(UpdateUserCommand command);

  /**
   * Handles the deletion of a user based on a Keycloak "DELETE" event.
   *
   * @param command the command object containing the ID of the user to delete
   */
  void deleteUser(DeleteUserCommand command);

  /**
   * Command object representing the data required to create a new user.
   *
   * @param keycloakId the unique identifier of the user in Keycloak
   * @param username the username of the new user
   * @param email the email address of the new user
   * @param firstName the first name of the new user
   * @param lastName the last name of the new user
   */
  record CreateUserCommand(
      UUID keycloakId, String username, String email, String firstName, String lastName) {}

  /**
   * Command object representing the data required to update an existing user.
   *
   * @param keycloakId the unique identifier of the user in Keycloak
   * @param username the updated username
   * @param email the updated email address
   * @param firstName the updated first name
   * @param lastName the updated last name
   */
  record UpdateUserCommand(
      UUID keycloakId, String username, String email, String firstName, String lastName) {}

  /**
   * Command object representing the data required to delete a user.
   *
   * @param keycloakId the unique identifier of the user in Keycloak
   */
  record DeleteUserCommand(UUID keycloakId) {}
}
