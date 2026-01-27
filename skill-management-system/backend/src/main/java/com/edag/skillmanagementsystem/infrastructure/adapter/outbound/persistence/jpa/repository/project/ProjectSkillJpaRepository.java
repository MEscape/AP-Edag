package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.project;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectSkillEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link ProjectSkillEntity} persistence operations.
 *
 * <p>Provides optimized batch loading methods to prevent N+1 query issues when displaying project
 * lists with their associated skills.
 */
@Repository
public interface ProjectSkillJpaRepository extends JpaRepository<ProjectSkillEntity, UUID> {

  /**
   * Retrieves the top 5 skills for each project in the given list of project IDs. Results are
   * optimized for bulk loading across multiple projects in a single query.
   *
   * <p>This method prevents N+1 queries when rendering paginated project lists by loading all
   * necessary skills upfront.
   *
   * <p>The {@link EntityGraph} eagerly loads:
   *
   * <ul>
   *   <li><strong>skill</strong> – the associated skill/technology details
   * </ul>
   *
   * <p>The query uses a subquery with LIMIT to fetch only the first 5 skills per project, ordered
   * by skill ID for consistency.
   *
   * @param projectIds the list of project IDs for which to retrieve skills
   * @return a list of project skills, with up to 5 skills per project
   */
  @EntityGraph(attributePaths = {"skill"})
  @Query(
      """
              SELECT ps FROM ProjectSkillEntity ps
              WHERE ps.project.id IN :projectIds
              AND ps.id IN (
                  SELECT ps2.id FROM ProjectSkillEntity ps2
                  WHERE ps2.project.id = ps.project.id
                  ORDER BY ps2.id
                  LIMIT 5
              )
              ORDER BY ps.project.id, ps.id
              """)
  List<ProjectSkillEntity> findTop5SkillsForProjects(@Param("projectIds") List<UUID> projectIds);
}
