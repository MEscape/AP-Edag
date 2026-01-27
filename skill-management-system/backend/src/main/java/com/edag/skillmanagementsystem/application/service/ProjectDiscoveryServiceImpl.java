package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.project.ProjectNotFoundException;
import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.model.project.ProjectFilterOptions;
import com.edag.skillmanagementsystem.domain.model.project.ProjectSearchCriteria;
import com.edag.skillmanagementsystem.domain.port.inbound.ProjectDiscoveryService;
import com.edag.skillmanagementsystem.domain.port.outbound.OptionsRepository;
import com.edag.skillmanagementsystem.domain.port.outbound.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the project discovery service.
 *
 * <p>This service orchestrates project search and retrieval operations by delegating to the project
 * repository. It provides a clean interface for the presentation layer while keeping business logic
 * separate from infrastructure concerns.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectDiscoveryServiceImpl implements ProjectDiscoveryService {

  private final ProjectRepository projectRepository;
  private final OptionsRepository optionsRepository;

  @Override
  public Page<Project> searchManagerProjects(
      final ProjectSearchCriteria criteria, final Pageable pageable) {
    log.debug("Searching manager projects with criteria: {} and pageable: {}", criteria, pageable);

    Page<Project> results = projectRepository.searchProjects(criteria, pageable);

    log.info(
        "Found {} projects out of {} total for search criteria",
        results.getNumberOfElements(),
        results.getTotalElements());

    return results;
  }

  @Override
  public Project getProjectById(final UUID projectId, final UUID managerId) {
    log.debug("Retrieving project with id: {} for manager: {}", projectId, managerId);

    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

    log.info("Successfully retrieved project: {} for manager: {}", projectId, managerId);

    return project;
  }

  @Override
  public ProjectFilterOptions getFilterOptions(final UUID managerId) {
    log.debug("Retrieving project filter options for manager: {}", managerId);

    var skills = optionsRepository.findAllActiveSkills();

    log.info("Retrieved filter options: {} skills for manager: {}", skills.size(), managerId);

    return new ProjectFilterOptions(skills);
  }

  @Override
  public Page<Project> getRecentProjects(final UUID userId, final Pageable pageable) {
    log.debug("Retrieving recent projects for user: {} with pageable: {}", userId, pageable);

    Page<Project> results = projectRepository.getRecentProjects(userId, pageable);

    log.info(
        "Found {} recent projects out of {} total for user: {}",
        results.getNumberOfElements(),
        results.getTotalElements(),
        userId);

    return results;
  }
}
