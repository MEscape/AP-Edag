package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.user;

import com.edag.skillmanagementsystem.domain.model.user.User;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Maps between JPA entities and domain models for users.
 *
 * <p>This mapper handles the transformation of user data between persistence entities and domain
 * objects, focusing on core user attributes without mixing profile or aggregate concerns.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

  /**
   * Converts a user entity to a User domain model.
   *
   * @param entity the user entity
   * @return the user domain model, or null if entity is null
   */
  public static User entityToDomain(UserEntity entity) {
    if (entity == null) {
      return null;
    }

    return User.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .email(entity.getEmail())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  /**
   * Converts a user domain model to a UserEntity.
   *
   * @param user the user domain model
   * @return the user entity, or null if user is null
   */
  public static UserEntity domainToEntity(User user) {
    if (user == null) {
      return null;
    }

    UserEntity entity =
        UserEntity.builder()
            .id(user.id())
            .username(user.username())
            .email(user.email())
            .firstName(user.firstName())
            .lastName(user.lastName())
            .build();

    // Set audit fields if present
    if (user.createdAt() != null) {
      entity.setCreatedAt(user.createdAt());
    }
    if (user.updatedAt() != null) {
      entity.setUpdatedAt(user.updatedAt());
    }

    return entity;
  }
}
