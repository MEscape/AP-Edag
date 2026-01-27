package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.user.EmployeeNotFoundException;
import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeFilterOptions;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import com.edag.skillmanagementsystem.domain.model.option.Location;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.option.SkillCategory;
import com.edag.skillmanagementsystem.domain.port.inbound.EmployeeDiscoveryService;
import com.edag.skillmanagementsystem.domain.port.outbound.EmployeeRepository;
import com.edag.skillmanagementsystem.domain.port.outbound.OptionsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the employee discovery service.
 *
 * <p>This service orchestrates employee search and filtering operations by delegating to the
 * employee repository. It provides a clean interface for the presentation layer while keeping
 * business logic separate from infrastructure concerns.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeDiscoveryServiceImpl implements EmployeeDiscoveryService {

  private final EmployeeRepository employeeRepository;
  private final OptionsRepository optionsRepository;

  @Override
  public Page<Employee> searchEmployees(
      final EmployeeSearchCriteria criteria, final Pageable pageable) {
    log.debug("Searching employees with criteria: {} and pageable: {}", criteria, pageable);

    Page<Employee> results = employeeRepository.findByCriteria(criteria, pageable);

    log.info(
        "Found {} employees out of {} total for search criteria",
        results.getNumberOfElements(),
        results.getTotalElements());

    return results;
  }

  @Override
  public Employee getEmployeeById(final UUID id) {
    log.debug("Retrieving employee with id: {}", id);

    Employee employee =
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

    log.info("Successfully retrieved employee: {}", id);

    return employee;
  }

  @Override
  public EmployeeFilterOptions getFilterOptions() {
    log.debug("Retrieving employee filter options");

    List<Location> locations = optionsRepository.findAllActiveLocations();
    List<Skill> skills = optionsRepository.findAllActiveSkills();
    List<SkillCategory> categories = optionsRepository.findAllActiveSkillCategories();

    log.info(
        "Retrieved filter options: {} skills, {} locations, {} categories",
        skills.size(),
        locations.size(),
        categories.size());

    return new EmployeeFilterOptions(skills, locations, categories);
  }
}
