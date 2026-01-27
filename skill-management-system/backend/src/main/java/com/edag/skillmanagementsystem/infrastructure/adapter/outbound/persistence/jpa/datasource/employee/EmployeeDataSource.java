package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.employee;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import com.edag.skillmanagementsystem.domain.port.outbound.EmployeeRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeSkillJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.specification.EmployeeSpecification;
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

/**
 * Data source implementation for employee-related persistence operations.
 *
 * <p>Provides searching and lookup functionality for employees and their top skills using JPA
 * repositories and specification-based filtering.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeDataSource implements EmployeeRepository {

  private final EmployeeJpaRepository employeeRepository;
  private final EmployeeSkillJpaRepository employeeSkillRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Employee> findByCriteria(EmployeeSearchCriteria criteria, Pageable pageable) {

    log.debug("Finding employees by criteria: {} with pageable: {}", criteria, pageable);

    Specification<EmployeeEntity> spec = EmployeeSpecification.fromCriteria(criteria);
    Page<EmployeeEntity> employeePage = employeeRepository.findAll(spec, pageable);

    if (employeePage.isEmpty()) {
      return employeePage.map(e -> EmployeeMapper.entityToDomain(e, List.of()));
    }

    // Collect all user IDs from page
    List<UUID> userIds = employeePage.stream().map(EmployeeEntity::getUserId).toList();

    // Load top skills in one query
    Map<UUID, List<EmployeeSkillEntity>> topSkillsMap =
        employeeSkillRepository.findTop3SkillsForEmployees(userIds).stream()
            .collect(Collectors.groupingBy(skill -> skill.getEmployee().getUserId()));

    // Map domain objects
    return employeePage.map(
        employee -> {
          List<EmployeeSkillEntity> topSkills =
              topSkillsMap.getOrDefault(employee.getUserId(), List.of());

          return EmployeeMapper.entityToDomain(employee, topSkills);
        });
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Employee> findById(UUID userId) {
    log.debug("Finding employee by id: {}", userId);

    return employeeRepository
        .findByUserId(userId)
        .map(
            employee -> {
              List<EmployeeSkillEntity> topSkills =
                  employeeSkillRepository.findByEmployeeUserIdOrderByProficiencyScoreDesc(userId);

              return EmployeeMapper.entityToDomain(employee, topSkills);
            });
  }
}
