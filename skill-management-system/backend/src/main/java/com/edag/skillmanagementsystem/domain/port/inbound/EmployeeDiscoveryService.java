package com.edag.skillmanagementsystem.domain.port.inbound;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeFilterOptions;
import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Defines the contract for employee discovery and search operations.
 *
 * <p>This service provides capabilities for searching and filtering employees based on various
 * criteria such as skills, location, availability, and experience. It supports pagination and
 * sorting to handle large result sets efficiently.
 *
 * @since 1.0.0
 */
public interface EmployeeDiscoveryService {

  /**
   * Searches for employees based on the provided criteria.
   *
   * <p>This method supports complex filtering, pagination, and sorting to help users discover
   * employees with specific skills, availability, or other attributes.
   *
   * @param criteria the search criteria including filters for skills, location, availability, etc.
   * @param pageable the pagination and sorting information
   * @return a paginated list of {@link Employee} objects matching the criteria
   */
  Page<Employee> searchEmployees(EmployeeSearchCriteria criteria, Pageable pageable);

  /**
   * Retrieves a single employee by their unique identifier.
   *
   * @param id the unique identifier of the employee
   * @return an {@link Optional} containing the employee if found, or empty otherwise
   */
  Employee getEmployeeById(UUID id);

  /**
   * Retrieves all available filter options for employee search.
   *
   * <p>This method returns lists of unique values for skills, locations, and skill categories that
   * can be used to populate filter dropdowns in the user interface.
   *
   * @return an {@link EmployeeFilterOptions} object containing all available filter values
   */
  EmployeeFilterOptions getFilterOptions();
}
