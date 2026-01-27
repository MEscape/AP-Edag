package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.specification;

import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectMemberEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectSkillEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Builds dynamic JPA {@link Specification} instances for filtering {@link ProjectEntity} data based
 * on {@link ProjectSearchCriteria}.
 *
 * <p>Supports filtering by creator, status, employee assignment, skills, and text search.
 *
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectSpecification {

  /**
   * Creates a JPA Specification from the given search criteria.
   *
   * @param criteria the search criteria
   * @return a JPA Specification for filtering projects
   */
  public static Specification<ProjectEntity> fromCriteria(final ProjectSearchCriteria criteria) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // --- Text search: project name, description, client ---
      if (criteria.searchTerm() != null && !criteria.searchTerm().isBlank()) {
        String pattern = "%" + criteria.searchTerm().toLowerCase() + "%";

        Predicate name = cb.like(cb.lower(root.get("name")), pattern);
        Predicate description = cb.like(cb.lower(root.get("description")), pattern);
        Predicate client = cb.like(cb.lower(root.get("client")), pattern);

        predicates.add(cb.or(name, description, client));
      }

      // --- Status filter ---
      if (criteria.statusList() != null && !criteria.statusList().isEmpty()) {
        predicates.add(root.get("status").in(criteria.statusList()));
      }

      // --- Employee filter (via projectMembers) ---
      if (criteria.employeeIds() != null && !criteria.employeeIds().isEmpty()) {
        Join<ProjectEntity, ProjectMemberEntity> memberJoin =
            root.join("projectMembers", JoinType.INNER);
        Join<ProjectMemberEntity, EmployeeEntity> employeeJoin =
            memberJoin.join("employee", JoinType.INNER);
        predicates.add(employeeJoin.get("userId").in(criteria.employeeIds()));
      }

      // --- Skills filter ---
      if (criteria.skillIds() != null && !criteria.skillIds().isEmpty()) {
        Join<ProjectEntity, ProjectSkillEntity> skillJoin =
            root.join("projectSkills", JoinType.INNER);
        Join<ProjectSkillEntity, SkillEntity> skillRef = skillJoin.join("skill", JoinType.INNER);
        predicates.add(skillRef.get("id").in(criteria.skillIds()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
