package com.edag.skillmanagementsystem.domain.exception.user;

import com.edag.skillmanagementsystem.domain.exception.shared.ResourceNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when an employee cannot be found.
 *
 * <p>This exception is raised when an employee referenced by a given identifier does not exist in
 * the system. It should be thrown by domain or application services when attempting to retrieve or
 * operate on an employee entity that is missing, deleted, or otherwise unavailable.
 *
 * <p>The exception uses an i18n message key and the employee identifier as a message argument.
 */
@SuppressWarnings("java:S110")
public class EmployeeNotFoundException extends ResourceNotFoundException {

  /**
   * Constructs a new {@code EmployeeNotFoundException} for the specified employee ID.
   *
   * @param employeeId the unique identifier of the employee that could not be found
   */
  public EmployeeNotFoundException(UUID employeeId) {
    super("error.employee.not.found.id", employeeId);
  }
}
