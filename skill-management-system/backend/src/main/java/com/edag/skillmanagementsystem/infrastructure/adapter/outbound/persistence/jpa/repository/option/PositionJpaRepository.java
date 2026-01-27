package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link PositionEntity} persistence operations.
 *
 * <p>Provides CRUD operations and derived queries for managing master position data.
 */
@Repository
public interface PositionJpaRepository extends JpaRepository<PositionEntity, UUID> {

  /**
   * Finds all active positions.
   *
   * @return list of active positions
   */
  List<PositionEntity> findByActiveTrue();
}
