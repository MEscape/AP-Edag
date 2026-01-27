package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.project;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberName;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectMemberEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectSkillEntity;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting project persistence entities into corresponding domain models.
 *
 * <p>This mapper transforms full project data including timeline, technologies, and project members
 * into immutable domain objects. It is used by discovery and management data sources.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectMapper {

  /**
   * Converts a {@link ProjectEntity} to a domain {@link Project} with separately loaded skills.
   *
   * <p>This method maps:
   *
   * <ul>
   *   <li>Basic project fields (name, dates, status, client)
   *   <li>Technologies using provided {@link ProjectSkillEntity} list (typically top 5)
   *   <li>Project members with employee names and positions
   *   <li>The manager/creator of the project
   * </ul>
   *
   * <p><strong>Note:</strong> Skills are provided separately to avoid N+1 queries and cartesian
   * products. Use {@link #entityToDomain(ProjectEntity)} only when skills are already loaded via
   * EntityGraph.
   *
   * @param entity the fully loaded JPA project entity
   * @param skills the list of project skills (loaded separately for performance)
   * @return the mapped domain {@link Project}
   */
  public static Project entityToDomain(ProjectEntity entity, List<ProjectSkillEntity> skills) {
    return Project.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .status(entity.getStatus())
        .startDate(entity.getStartDate())
        .endDate(entity.getEndDate())
        .client(entity.getClient())
        .teamSize(entity.getTeamSize())
        .technologies(mapTechnologies(skills))
        .createdByUserId(entity.getCreatedByUser().getId())
        .members(mapMembers(entity.getProjectMembers()))
        .build();
  }

  /**
   * Converts a {@link ProjectEntity} to a domain {@link Project}.
   *
   * <p>This overload uses the skills already loaded in the entity via EntityGraph. Use this only
   * when you're certain skills are eagerly loaded (e.g., single project fetch).
   *
   * <p>For paginated lists, prefer {@link #entityToDomain(ProjectEntity, List)} to avoid N+1
   * issues.
   *
   * @param entity the fully loaded JPA project entity with skills
   * @return the mapped domain {@link Project}
   */
  public static Project entityToDomain(ProjectEntity entity) {
    return entityToDomain(entity, entity.getProjectSkills().stream().toList());
  }

  // ============================================================================
  // PRIVATE MAPPING HELPERS
  // ============================================================================

  /**
   * Maps a collection of {@link ProjectSkillEntity} into a simple set of technology/skill names.
   *
   * <p>This method accepts any collection type (Set, List) for flexibility with both EntityGraph
   * and separate query approaches.
   *
   * @param skillEntities the project skill entities
   * @return set of technology names
   */
  private static Set<String> mapTechnologies(Collection<ProjectSkillEntity> skillEntities) {
    if (skillEntities == null || skillEntities.isEmpty()) {
      return Set.of();
    }

    return skillEntities.stream()
        .map(skill -> skill.getSkill().getName())
        .collect(Collectors.toSet());
  }

  /**
   * Maps project members into {@link ProjectMemberName} objects.
   *
   * <p>This requires:
   *
   * <ul>
   *   <li>Employee's first/last name from {@link EmployeeEntity}
   *   <li>Position name associated with the membership
   * </ul>
   */
  private static Set<ProjectMemberName> mapMembers(Set<ProjectMemberEntity> memberEntities) {
    if (memberEntities == null || memberEntities.isEmpty()) {
      return Set.of();
    }

    return memberEntities.stream().map(ProjectMapper::mapMember).collect(Collectors.toSet());
  }

  /** Maps a single {@link ProjectMemberEntity} into a {@link ProjectMemberName} domain model. */
  private static ProjectMemberName mapMember(ProjectMemberEntity member) {
    EmployeeEntity employee = member.getEmployee();
    PositionEntity position = member.getPosition();

    String fullName = employee.getUser().getFirstName() + " " + employee.getUser().getLastName();

    return ProjectMemberName.builder()
        .employeeId(employee.getUserId())
        .employeeName(fullName)
        .positionId(position.getId())
        .positionName(position.getName())
        .build();
  }
}
