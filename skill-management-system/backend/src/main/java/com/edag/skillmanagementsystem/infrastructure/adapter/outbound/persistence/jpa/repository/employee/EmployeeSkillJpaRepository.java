package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link EmployeeSkillEntity} persistence operations.
 *
 * <p>Provides optimized skill lookup methods for employees and uses {@link EntityGraph} to eagerly
 * fetch related skill metadata (skill + category) in order to prevent N+1 query issues when loading
 * employee skill sets.
 */
@Repository
public interface EmployeeSkillJpaRepository extends JpaRepository<EmployeeSkillEntity, UUID> {

  /**
   * Retrieves all skills assigned to an employee, identified by the user's ID, and eagerly loads
   * related skill metadata.
   *
   * <p>The applied {@link EntityGraph} ensures the following associations are fetched eagerly:
   *
   * <ul>
   *   <li><strong>skill</strong> – the referenced skill entry
   *   <li><strong>skill.category</strong> – the skill's category for classification
   * </ul>
   *
   * <p>Other associations, such as the {@code employee} reference or any nested relationships
   * within the category or skill, remain lazily loaded.
   *
   * @param userId the ID of the employee's user account
   * @return a list of skills assigned to the employee, including their skill and category details
   */
  @EntityGraph(attributePaths = {"skill", "skill.category"})
  List<EmployeeSkillEntity> findWithDetailsByEmployeeUserId(UUID userId);

  /**
   * Retrieves a single employee skill by its ID and the employee's user ID, eagerly loading
   * associated skill metadata.
   *
   * <p>The {@link EntityGraph} eagerly loads:
   *
   * <ul>
   *   <li><strong>skill</strong> – the referenced skill
   *   <li><strong>skill.category</strong> – the category to which the skill belongs
   * </ul>
   *
   * @param id the unique identifier of the employee–skill entry
   * @param employeeUserId the user ID of the employee to whom the skill belongs
   * @return an {@link Optional} containing the matching employee skill if found
   */
  @EntityGraph(attributePaths = {"skill", "skill.category"})
  Optional<EmployeeSkillEntity> findBySkillIdAndEmployeeUserId(UUID id, UUID employeeUserId);

  /**
   * Retrieves all skills for an employee, sorted by proficiency score in descending order, and
   * eagerly loads related skill metadata.
   *
   * <p>The {@link EntityGraph} eagerly loads:
   *
   * <ul>
   *   <li><strong>skill</strong> – the associated skill entry
   *   <li><strong>skill.category</strong> – classification category of the skill
   * </ul>
   *
   * @param employeeUserId the employee's user ID
   * @return a sorted list of employee skill entries, highest proficiency first
   */
  @EntityGraph(attributePaths = {"skill", "skill.category"})
  List<EmployeeSkillEntity> findByEmployeeUserIdOrderByProficiencyScoreDesc(UUID employeeUserId);

  /**
   * Checks whether an employee has been assigned a specific skill.
   *
   * @param employeeUserId the user ID of the employee
   * @param skillId the skill's unique identifier
   * @return {@code true} if the employee is associated with the given skill, otherwise {@code
   *     false}
   */
  boolean existsByEmployeeUserIdAndSkillId(UUID employeeUserId, UUID skillId);

  /**
   * Retrieves the top three skills (highest proficiency scores) for each employee in the given list
   * of user IDs. Results are grouped by employee user ID.
   *
   * <p>This method is optimized for bulk loading of top skills across multiple employees in a
   * single query, avoiding the N+1 query problem when rendering paginated employee lists.
   *
   * <p>The returned {@link Map} contains:
   *
   * <ul>
   *   <li><strong>key</strong>: the employee's {@link UUID} (userId)
   *   <li><strong>value</strong>: a list of up to three {@link EmployeeSkillEntity} items, ordered
   *       by proficiency score in descending order
   * </ul>
   *
   * <p>The {@link EntityGraph} eagerly loads:
   *
   * <ul>
   *   <li><strong>skill</strong> – the associated skill object
   * </ul>
   *
   * @param userIds the list of employee user IDs for whom the top skills should be retrieved
   * @return a map grouping each employee's top three skills by their user ID
   */
  @EntityGraph(attributePaths = {"skill"})
  @Query(
      """
        SELECT es FROM EmployeeSkillEntity es
        WHERE es.employee.userId IN :userIds
        AND es.id IN (
            SELECT es2.id FROM EmployeeSkillEntity es2
            WHERE es2.employee.userId = es.employee.userId
            ORDER BY es2.proficiencyScore DESC
            LIMIT 3
        )
        ORDER BY es.employee.userId, es.proficiencyScore DESC
        """)
  List<EmployeeSkillEntity> findTop3SkillsForEmployees(@Param("userIds") List<UUID> userIds);
}
