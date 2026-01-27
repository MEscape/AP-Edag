package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.employee;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting employee-related persistence entities into corresponding domain
 * models.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeMapper {

  /**
   * Converts an {@link EmployeeEntity} into an {@link Employee} domain model, including a
   * summarized list of the employee's top skills.
   *
   * <p>This method is intended for list or overview views, where only a subset of employee data is
   * required. It does not include the full employee profile.
   *
   * @param entity the employee entity; required to have related user, position, and location loaded
   * @param skills the list of top employee skills (commonly the top 3–5 based on proficiency score)
   * @return the mapped {@link Employee} domain model
   */
  public static Employee entityToDomain(EmployeeEntity entity, List<EmployeeSkillEntity> skills) {

    return Employee.builder()
        .userId(entity.getUserId())
        .email(entity.getUser().getEmail())
        .firstName(entity.getUser().getFirstName())
        .lastName(entity.getUser().getLastName())
        .position(entity.getPosition() != null ? entity.getPosition().getName() : null)
        .location(entity.getLocation() != null ? entity.getLocation().getName() : null)
        .availability(entity.getAvailability())
        .yearsOfExperience(entity.getYearsOfExperience().doubleValue())
        .skills(mapEmployeeSkills(skills))
        .totalSkills(entity.getStatistics().getTotalSkills())
        .totalProjects(entity.getStatistics().getTotalProjects())
        .build();
  }

  /**
   * Converts a list of {@link EmployeeSkillEntity} objects into a list of {@link ProfileSkill}
   * domain objects.
   *
   * @param employeeSkills the list of employee skill entities; must have the associated skill and
   *     category preloaded
   * @return the mapped list of profile skills
   */
  private static List<ProfileSkill> mapEmployeeSkills(List<EmployeeSkillEntity> employeeSkills) {
    return employeeSkills.stream().map(EmployeeMapper::mapEmployeeSkill).toList();
  }

  /**
   * Maps a single {@link EmployeeSkillEntity} to a {@link ProfileSkill} domain model.
   *
   * @param employeeSkill the employee skill entity
   * @return the mapped profile skill
   */
  private static ProfileSkill mapEmployeeSkill(EmployeeSkillEntity employeeSkill) {
    return ProfileSkill.builder()
        .id(employeeSkill.getSkill().getId())
        .skillName(employeeSkill.getSkill().getName())
        .category(employeeSkill.getSkill().getCategory().getName())
        .proficiencyScore(employeeSkill.getProficiencyScore())
        .yearsOfExperience(employeeSkill.getYearsOfExperience().doubleValue())
        .build();
  }
}
