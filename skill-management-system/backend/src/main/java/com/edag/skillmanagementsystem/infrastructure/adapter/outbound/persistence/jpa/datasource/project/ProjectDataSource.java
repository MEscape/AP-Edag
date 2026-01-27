package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.project;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectMemberId;
import com.edag.skillmanagementsystem.domain.model.project.ProjectRequest;
import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import com.edag.skillmanagementsystem.domain.port.outbound.ProjectRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectMemberEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.PositionJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.SkillJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.project.ProjectJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.project.ProjectSkillJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.user.UserJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.specification.ProjectSpecification;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectDataSource implements ProjectRepository {

  private final ProjectJpaRepository projectRepository;
  private final UserJpaRepository userRepository;
  private final EmployeeJpaRepository employeeRepository;
  private final PositionJpaRepository positionRepository;
  private final SkillJpaRepository skillRepository;
  private final ProjectSkillJpaRepository projectSkillRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Project> searchProjects(ProjectSearchCriteria criteria, Pageable pageable) {
    log.debug("Searching projects with criteria: {}", criteria);

    Specification<ProjectEntity> spec = ProjectSpecification.fromCriteria(criteria);
    Page<ProjectEntity> projectPage = projectRepository.findAll(spec, pageable);

    return mapProjectsWithTopSkills(projectPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Project> getRecentProjects(UUID userId, Pageable pageable) {
    log.debug("Getting recent projects for user: {}", userId);

    Page<ProjectEntity> projectPage =
        projectRepository.findDistinctByProjectMembersEmployeeUserIdOrderByStartDateDesc(
            userId, pageable);

    return mapProjectsWithTopSkills(projectPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Project> findById(UUID projectId) {
    log.debug("Finding project by ID: {}", projectId);

    return projectRepository.findById(projectId).map(ProjectMapper::entityToDomain);
  }

  @Override
  @Transactional
  public void deleteById(UUID projectId) {
    log.debug("Deleting project: {}", projectId);
    projectRepository.deleteById(projectId);
  }

  @Override
  @Transactional
  public Optional<Project> save(ProjectRequest projectRequest, UUID managerId) {
    log.debug("Creating new project: {} by manager: {}", projectRequest.name(), managerId);

    // Validate manager exists
    if (!userRepository.existsById(managerId)) {
      log.warn("Cannot create project: manager '{}' does not exist", managerId);
      return Optional.empty();
    }

    // Create entity
    ProjectEntity entity = ProjectEntity.builder().build();

    // Set manager reference (already validated)
    entity.setCreatedByUser(userRepository.getReferenceById(managerId));

    // Set basic fields
    entity.setName(projectRequest.name());
    entity.setDescription(projectRequest.description());
    entity.setStatus(projectRequest.status());
    entity.setStartDate(projectRequest.startDate());
    entity.setEndDate(projectRequest.endDate());
    entity.setClient(projectRequest.client());

    // Set project members
    if (!setProjectMembers(entity, projectRequest.members())) {
      return Optional.empty();
    }

    // Set technologies/skills
    if (!setProjectSkillsByIds(entity, projectRequest.technologies())) {
      return Optional.empty();
    }

    log.debug(
        "About to save project with {} skills and {} members",
        entity.getProjectSkills().size(),
        entity.getProjectMembers().size());

    ProjectEntity saved = projectRepository.save(entity);
    log.info(
        "Project created with ID: {}, skills count: {}",
        saved.getId(),
        saved.getProjectSkills().size());

    return Optional.of(ProjectMapper.entityToDomain(saved));
  }

  @Override
  @Transactional
  public Optional<Project> update(UUID projectId, ProjectRequest update) {
    log.debug("Updating project: {}", projectId);

    Optional<ProjectEntity> entityOpt = projectRepository.findById(projectId);
    if (entityOpt.isEmpty()) {
      log.warn("Project not found: {}", projectId);
      return Optional.empty();
    }

    ProjectEntity entity = entityOpt.get();

    // Update only non-null fields
    if (update.name() != null) {
      entity.setName(update.name());
    }

    if (update.description() != null) {
      entity.setDescription(update.description());
    }

    if (update.status() != null) {
      entity.setStatus(update.status());
    }

    if (update.startDate() != null) {
      entity.setStartDate(update.startDate());
    }

    if (update.endDate() != null) {
      entity.setEndDate(update.endDate());
    }

    if (update.client() != null) {
      entity.setClient(update.client());
    }

    // Update project members if provided
    if (update.members() != null) {
      entity.getProjectMembers().clear();
      if (!update.members().isEmpty()) {
        projectRepository.flush();
        if (!setProjectMembers(entity, update.members())) {
          return Optional.empty();
        }
      }
    }

    // Update technologies/skills if provided
    if (update.technologies() != null) {
      entity.getProjectSkills().clear();
      if (!update.technologies().isEmpty()) {
        projectRepository.flush();
        if (!setProjectSkillsByIds(entity, update.technologies())) {
          return Optional.empty();
        }
      }
    }

    ProjectEntity saved = projectRepository.save(entity);
    log.info("Project updated with ID: {}", saved.getId());

    return Optional.of(ProjectMapper.entityToDomain(saved));
  }

  /**
   * Helper method to load a page of projects with their top 5 skills. Prevents code duplication
   * between different query methods.
   *
   * @param projectPage the page of project entities (without skills loaded)
   * @return the page of domain projects with top 5 skills
   */
  private Page<Project> mapProjectsWithTopSkills(Page<ProjectEntity> projectPage) {
    if (projectPage.isEmpty()) {
      return projectPage.map(entity -> ProjectMapper.entityToDomain(entity, List.of()));
    }

    // Collect all project IDs from page
    List<UUID> projectIds = projectPage.stream().map(ProjectEntity::getId).toList();

    // Load top 5 skills in one query
    Map<UUID, List<ProjectSkillEntity>> topSkillsMap =
        projectSkillRepository.findTop5SkillsForProjects(projectIds).stream()
            .collect(Collectors.groupingBy(skill -> skill.getProject().getId()));

    // Map to domain with skills
    return projectPage.map(
        project -> {
          List<ProjectSkillEntity> topSkills =
              topSkillsMap.getOrDefault(project.getId(), List.of());
          return ProjectMapper.entityToDomain(project, topSkills);
        });
  }

  /**
   * Sets project members for creation using ProjectCreate.ProjectMemberCreate with IDs.
   *
   * @param entity the project entity
   * @param members the list of members with employee and position IDs
   * @return true if successful, false if any validation fails
   */
  private boolean setProjectMembers(ProjectEntity entity, List<ProjectMemberId> members) {

    if (members == null || members.isEmpty()) {
      log.warn("Cannot create project: no members provided");
      return false;
    }

    for (ProjectMemberId member : members) {
      // Validate employee exists by ID
      Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(member.employeeId());
      if (employeeOpt.isEmpty()) {
        log.warn("Employee not found with ID: {}", member.employeeId());
        return false;
      }

      // Validate position exists by ID - use existsById for better performance
      if (!positionRepository.existsById(member.positionId())) {
        log.warn("Position not found with ID: {}", member.positionId());
        return false;
      }

      ProjectMemberEntity memberEntity =
          ProjectMemberEntity.builder()
              .project(entity)
              .employee(employeeOpt.get())
              .position(positionRepository.getReferenceById(member.positionId()))
              .build();

      entity.getProjectMembers().add(memberEntity);
    }

    log.debug("Set {} project members", members.size());
    return true;
  }

  /**
   * Sets project skills using skill IDs instead of names.
   *
   * @param entity the project entity
   * @param skillIds the list of skill IDs
   * @return true if successful, false if any skill not found
   */
  private boolean setProjectSkillsByIds(ProjectEntity entity, List<UUID> skillIds) {

    if (skillIds == null || skillIds.isEmpty()) {
      log.debug("No project skills to set");
      return true;
    }

    log.debug("Setting {} skills for project", skillIds.size());

    for (UUID skillId : skillIds) {
      // Validate skill exists by ID
      if (!skillRepository.existsById(skillId)) {
        log.warn("Skill not found with ID: {}", skillId);
        return false;
      }

      ProjectSkillEntity skillEntity =
          ProjectSkillEntity.builder()
              .project(entity)
              .skill(skillRepository.getReferenceById(skillId))
              .build();

      entity.getProjectSkills().add(skillEntity);
      log.debug(
          "Added skill {} to project, total skills now: {}",
          skillId,
          entity.getProjectSkills().size());
    }

    log.debug("Set {} project skills", skillIds.size());
    return true;
  }
}
