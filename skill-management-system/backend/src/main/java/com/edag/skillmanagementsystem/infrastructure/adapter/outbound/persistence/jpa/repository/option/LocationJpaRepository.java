package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link LocationEntity} persistence operations.
 *
 * <p>Provides CRUD operations and derived queries for managing master location data.
 */
@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, UUID> {

  /**
   * Finds all active locations.
   *
   * @return list of active locations
   */
  List<LocationEntity> findByActiveTrue();
}
