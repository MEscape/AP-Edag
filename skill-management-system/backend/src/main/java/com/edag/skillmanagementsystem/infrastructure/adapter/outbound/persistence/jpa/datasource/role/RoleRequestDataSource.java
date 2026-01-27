package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.role;

import com.edag.skillmanagementsystem.domain.model.role.RequestStatus;
import com.edag.skillmanagementsystem.domain.model.role.RoleRequest;
import com.edag.skillmanagementsystem.domain.port.outbound.RoleRequestRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.role.RoleRequestEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.role.RoleRequestJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * JPA-based implementation of {@link RoleRequestRepository}.
 *
 * <p>Adapts the domain repository interface to Spring Data JPA operations.
 *
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleRequestDataSource implements RoleRequestRepository {

  private final RoleRequestJpaRepository jpaRepository;

  @Override
  public RoleRequest save(RoleRequest roleRequest) {
    log.debug(
        "Saving role request: userId={}, requestedRole={}",
        roleRequest.userId(),
        roleRequest.requestedRole());

    RoleRequestEntity entity = RoleRequestMapper.domainToEntity(roleRequest);
    RoleRequestEntity saved = jpaRepository.save(entity);

    return RoleRequestMapper.entityToDomain(saved);
  }

  @Override
  public Optional<RoleRequest> findById(UUID id) {
    log.debug("Finding role request by ID: {}", id);

    return jpaRepository.findById(id).map(RoleRequestMapper::entityToDomain);
  }

  @Override
  public List<RoleRequest> findByUserId(UUID userId) {
    log.debug("Finding role requests for user: {}", userId);

    return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(RoleRequestMapper::entityToDomain)
        .toList();
  }

  @Override
  public Page<RoleRequest> findByStatus(RequestStatus status, Pageable pageable) {
    log.debug("Finding role requests by status: {} with pagination: {}", status, pageable);

    return jpaRepository.findByStatus(status, pageable).map(RoleRequestMapper::entityToDomain);
  }

  @Override
  public Page<RoleRequest> findAll(Pageable pageable) {
    log.debug("Finding all role requests");

    return jpaRepository.findAll(pageable).map(RoleRequestMapper::entityToDomain);
  }

  @Override
  public boolean existsByUserIdAndStatus(UUID userId, RequestStatus status) {
    log.debug("Checking if user {} has request with status {}", userId, status);

    return jpaRepository.existsByUserIdAndStatus(userId, status);
  }

  @Override
  public void deleteById(UUID id) {
    log.debug("Deleting role request: {}", id);

    jpaRepository.deleteById(id);
  }
}
