package com.edag.skillmanagementsystem.domain.port.outbound;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for employee data access.
 *
 * <p>This port defines the contract for retrieving employee information from the persistence layer.
 * It supports complex search operations with filtering, pagination, and sorting capabilities.
 *
 * @since 1.0.0
 */
public interface EmployeeRepository {

  /**
   * Finds employees matching the specified search criteria.
   *
   * <p>Implementations should support filtering by multiple criteria simultaneously and apply
   * pagination and sorting as specified in the {@link Pageable} parameter.
   *
   * @param criteria the search criteria to apply
   * @param pageable the pagination and sorting configuration
   * @return a page of employees matching the criteria
   */
  Page<Employee> findByCriteria(EmployeeSearchCriteria criteria, Pageable pageable);

  /**
   * Finds a single employee by their unique identifier.
   *
   * @param id the employee's unique identifier
   * @return an {@link Optional} containing the employee if found
   */
  Optional<Employee> findById(UUID id);
}
