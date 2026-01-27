package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link EmployeeEntity} persistence operations.
 *
 * <p>Provides CRUD functionality, specification support, and optimized fetch behavior through the
 * use of {@link EntityGraph} to reduce N+1 query issues.
 */
@Repository
public interface EmployeeJpaRepository
    extends JpaRepository<EmployeeEntity, UUID>, JpaSpecificationExecutor<EmployeeEntity> {

  /**
   * Retrieves an employee by its associated user ID and eagerly loads key related entities.
   *
   * <p>The {@link EntityGraph} ensures the following associations are fetched eagerly:
   *
   * <ul>
   *   <li><strong>user</strong> – the underlying user
   *   <li><strong>location</strong> – the employee's workplace or assigned office
   *   <li><strong>position</strong> – the employee's job role or title
   *   <li><strong>statistics</strong> – analytical metrics linked to the employee
   * </ul>
   *
   * <p>All other collections and associations (e.g., skills, projects) remain lazily loaded and are
   * not fetched by this method unless explicitly accessed.
   *
   * @param userId the employee's user ID
   * @return an {@link Optional} containing the matching employee if found
   */
  @EntityGraph(attributePaths = {"user", "location", "position", "statistics"})
  Optional<EmployeeEntity> findByUserId(UUID userId);
}
