package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.project;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link ProjectEntity} persistence operations.
 *
 * <p>Provides CRUD operations and dynamic search capabilities via {@link JpaSpecificationExecutor}.
 * Includes optimized methods for retrieving project data with eager-loaded associations through the
 * use of {@link EntityGraph}s, reducing N+1 query issues.
 *
 * @since 1.0.0
 */
@Repository
public interface ProjectJpaRepository
    extends JpaRepository<ProjectEntity, UUID>, JpaSpecificationExecutor<ProjectEntity> {

  /**
   * Retrieves a paginated, distinct list of projects for the specified user, ordered by {@code
   * startDate} descending (most recent first).
   *
   * <p>The applied {@link EntityGraph} ensures the following associations are eagerly loaded:
   *
   * <ul>
   *   <li><strong>projectMembers</strong> – the employees assigned to the project
   *   <li><strong>projectMembers.employee</strong> – employee details of each member
   *   <li><strong>projectMembers.position</strong> – the project role of each employee
   *   <li><strong>createdByUser</strong> – the user who created the project entry
   * </ul>
   *
   * <p>This method is used when rendering project lists or cards, ensuring all necessary metadata
   * is fetched upfront to avoid lazy-loading related performance issues.
   *
   * @param userId the ID of the user (employee) whose projects should be fetched
   * @param pageable pagination and sorting configuration
   * @return a {@link Page} containing fully-hydrated {@link ProjectEntity} results
   */
  @EntityGraph(
      attributePaths = {
        "projectMembers",
        "projectMembers.employee",
        "projectMembers.position",
        "createdByUser"
      })
  Page<ProjectEntity> findDistinctByProjectMembersEmployeeUserIdOrderByStartDateDesc(
      UUID userId, Pageable pageable);

  /**
   * Retrieves a paginated list of projects matching the provided JPA {@link Specification}, with
   * all relevant associations eagerly loaded through an {@link EntityGraph}.
   *
   * <p>This method is primarily used for filtering and advanced searching with dynamic criteria.
   * Since the result is typically used to render UI lists or detailed project aggregates, the
   * following associations are eagerly fetched to prevent excessive lazy-loading:
   *
   * <ul>
   *   <li><strong>projectMembers</strong> – assigned team members
   *   <li><strong>projectMembers.employee</strong> – detailed employee data
   *   <li><strong>projectMembers.position</strong> – project role of each member
   *   <li><strong>projectSkills</strong> – technologies/skills related to the project
   *   <li><strong>projectSkills.skill</strong> – detailed skill metadata
   *   <li><strong>createdByUser</strong> – user who created the project
   * </ul>
   *
   * <p>By combining {@link Specification}-based filtering with an eager-loading entity graph, this
   * method prevents N+1 problems and ensures consistent performance across complex searches.
   *
   * @param spec dynamic filter definition based on {@link Specification}
   * @param pageable pagination and sorting configuration
   * @return a {@link Page} containing fully-hydrated project entities matching the criteria
   */
  @EntityGraph(
      attributePaths = {
        "projectMembers",
        "projectMembers.employee",
        "projectMembers.position",
        "createdByUser"
      })
  Page<ProjectEntity> findAll(Specification<ProjectEntity> spec, Pageable pageable);

  /**
   * Retrieves a single project by its UUID, with all relevant associations eagerly loaded through
   * an {@link EntityGraph}.
   *
   * <ul>
   *   <li><strong>projectMembers</strong> – assigned team members
   *   <li><strong>projectMembers.employee</strong> – detailed employee data
   *   <li><strong>projectMembers.position</strong> – project role of each member
   *   <li><strong>projectSkills</strong> – technologies/skills related to the project
   *   <li><strong>projectSkills.skill</strong> – detailed skill metadata
   *   <li><strong>createdByUser</strong> – user who created the project
   * </ul>
   *
   * <p>By combining {@link Specification}-based filtering with an eager-loading entity graph, this
   * method prevents N+1 problems and ensures consistent performance across complex searches.
   *
   * @param id the UUID of the project to retrieve
   * @return an {@link Optional} containing the fully-hydrated project entity if found
   */
  @EntityGraph(
      attributePaths = {
        "projectMembers",
        "projectMembers.employee",
        "projectMembers.position",
        "projectSkills",
        "projectSkills.skill",
        "createdByUser"
      })
  Optional<ProjectEntity> findById(UUID id);
}
