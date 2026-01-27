package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.profile;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberName;
import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectMemberEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectSkillEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Maps between JPA entities and domain models for user profiles.
 *
 * <p>This mapper handles the transformation of employee data, skills, and projects from persistence
 * entities to domain objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileMapper {

  /**
   * Converts an EmployeeEntity along with its skills and projects to a UserProfile domain object.
   *
   * @param employee the employee entity
   * @param skills the list of employee skills with details
   * @param projects the list of projects with details
   * @return the mapped UserProfile domain object
   */
  public static UserProfile entityToDomain(
      EmployeeEntity employee, List<EmployeeSkillEntity> skills, List<ProjectEntity> projects) {
    return UserProfile.builder()
        .userId(employee.getUserId())
        .email(employee.getUser().getEmail())
        .firstName(employee.getUser().getFirstName())
        .lastName(employee.getUser().getLastName())
        .position(employee.getPosition() != null ? employee.getPosition().getName() : null)
        .positionId(employee.getPosition() != null ? employee.getPosition().getId() : null)
        .location(employee.getLocation() != null ? employee.getLocation().getName() : null)
        .locationId(employee.getLocation() != null ? employee.getLocation().getId() : null)
        .availability(employee.getAvailability())
        .joinDate(employee.getCreatedAt())
        .yearsOfExperience(employee.getYearsOfExperience().doubleValue())
        .bio(employee.getBio())
        .skills(mapProfileSkills(skills))
        .projects(mapProfileProjects(projects))
        .totalProjects(employee.getStatistics().getTotalProjects())
        .activeProjects(employee.getStatistics().getActiveProjects())
        .totalSkills(employee.getStatistics().getTotalSkills())
        .averageSkillScore(employee.getStatistics().getAverageSkillScore().doubleValue())
        .completionRate(employee.getCompletionRate())
        .lastUpdated(employee.getUpdatedAt())
        .build();
  }

  /**
   * Maps a single EmployeeSkillEntity to a domain ProfileSkill object.
   *
   * @param employeeSkill the employee skill entity
   * @return the mapped ProfileSkill domain object
   */
  public static ProfileSkill skillEntityToDomain(EmployeeSkillEntity employeeSkill) {
    return ProfileSkill.builder()
        .id(employeeSkill.getSkill().getId())
        .skillName(employeeSkill.getSkill().getName())
        .category(employeeSkill.getSkill().getCategory().getName())
        .proficiencyScore(employeeSkill.getProficiencyScore())
        .yearsOfExperience(employeeSkill.getYearsOfExperience().doubleValue())
        .lastUsed(employeeSkill.getLastUsed())
        .build();
  }

  /** Maps a list of EmployeeSkillEntity to domain ProfileSkill objects. */
  private static List<ProfileSkill> mapProfileSkills(List<EmployeeSkillEntity> employeeSkills) {
    return employeeSkills.stream().map(ProfileMapper::skillEntityToDomain).toList();
  }

  /** Maps a list of ProjectEntity to domain ProfileProject objects. */
  private static List<Project> mapProfileProjects(List<ProjectEntity> projects) {
    return projects.stream().map(ProfileMapper::mapProfileProject).toList();
  }

  /** Maps a single ProjectEntity to a domain ProfileProject object. */
  private static Project mapProfileProject(ProjectEntity project) {
    return Project.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .status(project.getStatus())
        .startDate(project.getStartDate())
        .endDate(project.getEndDate())
        .client(project.getClient())
        .teamSize(project.getTeamSize())
        .technologies(mapProjectSkills(project.getProjectSkills()))
        .members(mapProjectMembers(project.getProjectMembers()))
        .build();
  }

  /** Maps a list of project skill entities to a set of skill names. */
  private static Set<String> mapProjectSkills(Set<ProjectSkillEntity> projectSkills) {
    return projectSkills.stream().map(ProfileMapper::mapProjectSkill).collect(Collectors.toSet());
  }

  /** Maps a list of project member entities to a set of project member names. */
  private static Set<ProjectMemberName> mapProjectMembers(Set<ProjectMemberEntity> projectMembers) {
    return projectMembers.stream().map(ProfileMapper::mapProjectMember).collect(Collectors.toSet());
  }

  /** Maps a single project member entity to its project member name. */
  private static ProjectMemberName mapProjectMember(ProjectMemberEntity projectMember) {
    return ProjectMemberName.builder()
        .employeeId(projectMember.getEmployee().getUserId())
        .employeeName(
            projectMember.getEmployee().getUser().getFirstName()
                + " "
                + projectMember.getEmployee().getUser().getLastName())
        .positionId(projectMember.getPosition().getId())
        .positionName(projectMember.getPosition().getName())
        .build();
  }

  /** Maps a single project skill entity to its skill name. */
  private static String mapProjectSkill(ProjectSkillEntity projectSkill) {
    return projectSkill.getSkill().getName();
  }
}
