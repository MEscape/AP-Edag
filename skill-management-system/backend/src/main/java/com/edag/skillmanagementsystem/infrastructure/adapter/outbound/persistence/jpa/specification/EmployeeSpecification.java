package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.specification;

import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillCategoryEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Builds dynamic JPA {@link Specification} instances for filtering {@link EmployeeEntity} data
 * based on {@link EmployeeSearchCriteria}.
 *
 * <p>Supports text search, skill filtering, location, availability, and experience filters.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeSpecification {

  /**
   * Creates a JPA Specification from the given search criteria.
   *
   * <p>Automatically filters out incomplete profiles. Only employees with 100% completion rate (all
   * fields filled: position, location, skills, bio, experience) are included in results.
   *
   * @param criteria the search criteria
   * @return a JPA Specification for filtering employees
   */
  public static Specification<EmployeeEntity> fromCriteria(final EmployeeSearchCriteria criteria) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // --- Exclude incomplete profiles ---
      predicates.add(buildCompleteProfileFilter(root, cb));

      // Create user join upfront (needed for both search and ordering)
      Join<EmployeeEntity, UserEntity> userJoin = root.join("user", JoinType.LEFT);

      // --- Text search: name, email, position ---
      if (criteria.searchTerm() != null && !criteria.searchTerm().isBlank()) {
        String pattern = "%" + criteria.searchTerm().toLowerCase() + "%";

        Predicate firstName = cb.like(cb.lower(userJoin.get("firstName")), pattern);
        Predicate lastName = cb.like(cb.lower(userJoin.get("lastName")), pattern);
        Predicate email = cb.like(cb.lower(userJoin.get("email")), pattern);
        Predicate position = cb.like(cb.lower(root.get("position")), pattern);

        predicates.add(cb.or(firstName, lastName, email, position));
      }

      // --- Skills (by UUID) ---
      if (criteria.skillIds() != null && !criteria.skillIds().isEmpty()) {
        Join<EmployeeEntity, EmployeeSkillEntity> skillJoin =
            root.join("employeeSkills", JoinType.INNER);
        Join<EmployeeSkillEntity, SkillEntity> skillRef = skillJoin.join("skill", JoinType.INNER);
        predicates.add(skillRef.get("id").in(criteria.skillIds()));
      }

      // --- Skill categories (by UUID) ---
      if (criteria.skillCategoryIds() != null && !criteria.skillCategoryIds().isEmpty()) {
        Join<EmployeeEntity, EmployeeSkillEntity> skillJoin =
            root.join("employeeSkills", JoinType.INNER);
        Join<EmployeeSkillEntity, SkillEntity> skillRef = skillJoin.join("skill", JoinType.INNER);
        Join<SkillEntity, SkillCategoryEntity> categoryRef =
            skillRef.join("category", JoinType.INNER);
        predicates.add(categoryRef.get("id").in(criteria.skillCategoryIds()));
      }

      // --- Locations (by UUID) ---
      if (criteria.locationIds() != null && !criteria.locationIds().isEmpty()) {
        Join<EmployeeEntity, LocationEntity> locationJoin = root.join("location", JoinType.INNER);
        predicates.add(locationJoin.get("id").in(criteria.locationIds()));
      }

      // --- Availability ---
      if (criteria.availability() != null && !criteria.availability().isEmpty()) {
        predicates.add(root.get("availability").in(criteria.availability()));
      }

      // --- Minimum experience ---
      if (criteria.minExperience() != null && criteria.minExperience() > 0) {
        predicates.add(
            cb.greaterThanOrEqualTo(root.get("yearsOfExperience"), criteria.minExperience()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * Builds a predicate that filters out incomplete employee profiles.
   *
   * <p>Only profiles with 100% completion rate are included in employee discovery. A complete
   * profile must have: position, location, bio, skills, and experience > 0.
   *
   * @param root the root entity
   * @param cb the criteria builder
   * @return a predicate that includes only 100% complete profiles
   */
  private static Predicate buildCompleteProfileFilter(
      jakarta.persistence.criteria.Root<EmployeeEntity> root,
      jakarta.persistence.criteria.CriteriaBuilder cb) {

    // All fields must be present for 100% completion
    Predicate hasPosition = cb.isNotNull(root.get("position"));
    Predicate hasLocation = cb.isNotNull(root.get("location"));
    Predicate hasSkills = cb.isNotEmpty(root.get("employeeSkills"));
    Predicate hasBio =
        cb.and(cb.isNotNull(root.get("bio")), cb.notEqual(cb.trim(root.get("bio")), ""));
    Predicate hasExperience = cb.greaterThan(root.get("yearsOfExperience"), 0);

    // Profile must have ALL of: position, location, skills, bio, and experience
    return cb.and(hasPosition, hasLocation, hasSkills, hasBio, hasExperience);
  }
}
