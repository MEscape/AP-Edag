package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.role;

import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.role.RoleRequestEntity;
import lombok.experimental.UtilityClass;

/**
 * Mapper for converting between {@link RoleRequest} domain model and {@link RoleRequestEntity} JPA
 * entity.
 *
 * @since 1.0.0
 */
@UtilityClass
public class RoleRequestMapper {

  /**
   * Converts a domain model to a JPA entity.
   *
   * @param domain the domain model
   * @return the JPA entity
   */
  public RoleRequestEntity domainToEntity(RoleRequest domain) {
    if (domain == null) {
      return null;
    }

    return RoleRequestEntity.builder()
        .id(domain.id())
        .userId(domain.userId())
        .requestedRole(domain.requestedRole())
        .status(domain.status())
        .reason(domain.reason())
        .reviewedBy(domain.reviewedBy())
        .reviewedAt(domain.reviewedAt())
        .adminComment(domain.adminComment())
        .build();
  }

  /**
   * Converts a JPA entity to a domain model.
   *
   * @param entity the JPA entity
   * @return the domain model
   */
  public RoleRequest entityToDomain(RoleRequestEntity entity) {
    if (entity == null) {
      return null;
    }

    return RoleRequest.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
        .email(entity.getUser() != null ? entity.getUser().getEmail() : null)
        .requestedRole(entity.getRequestedRole())
        .status(entity.getStatus())
        .reason(entity.getReason())
        .reviewedBy(entity.getReviewedBy())
        .reviewerUsername(entity.getReviewer() != null ? entity.getReviewer().getUsername() : null)
        .reviewedAt(entity.getReviewedAt())
        .adminComment(entity.getAdminComment())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
