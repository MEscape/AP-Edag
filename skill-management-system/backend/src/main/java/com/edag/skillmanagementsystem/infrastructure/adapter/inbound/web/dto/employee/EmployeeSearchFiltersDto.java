package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee;

import com.edag.skillmanagementsystem.domain.model.employee.EmployeeSearchCriteria;
import com.edag.skillmanagementsystem.domain.model.user.AvailabilityStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

/**
 * DTO for employee search filter parameters.
 *
 * <p>This record captures filter criteria from API requests and can be converted to a domain {@link
 * EmployeeSearchCriteria}. Accepts UUIDs for efficient filtering.
 */
@Schema(
    name = "EmployeeSearchFiltersDto",
    description =
        "Filter criteria for employee search requests, including search term, skills, "
            + "locations, availability, and minimum experience.")
public record EmployeeSearchFiltersDto(
    @Parameter(description = "Text search term for name, email, or position")
        @Schema(example = "John", nullable = true)
        String searchTerm,
    @Parameter(description = "List of skill IDs to filter by")
        @Schema(
            example =
                "[\"550e8400-e29b-41d4-a716-446655440001\", \"550e8400-e29b-41d4-a716-446655440002\"]",
            nullable = true)
        List<UUID> skillIds,
    @Parameter(description = "List of skill category IDs to filter by")
        @Schema(
            example =
                "[\"550e8400-e29b-41d4-a716-446655440010\", \"550e8400-e29b-41d4-a716-446655440011\"]",
            nullable = true)
        List<UUID> skillCategoryIds,
    @Parameter(description = "List of location IDs to filter by")
        @Schema(
            example =
                "[\"550e8400-e29b-41d4-a716-446655440020\", \"550e8400-e29b-41d4-a716-446655440021\"]",
            nullable = true)
        List<UUID> locationIds,
    @Parameter(
            description =
                "List of availability statuses (available, partially_available, unavailable)")
        @Schema(
            allowableValues = {"available", "partially_available", "unavailable"},
            example = "[\"available\"]",
            nullable = true)
        List<String> availability,
    @Parameter(description = "Minimum years of experience required")
        @Schema(example = "3", nullable = true)
        Integer minExperience) {

  /**
   * Converts this DTO to a domain {@link EmployeeSearchCriteria}.
   *
   * @return the domain search criteria
   */
  public EmployeeSearchCriteria toDomain() {
    List<AvailabilityStatus> availabilityStatuses =
        availability != null
            ? availability.stream().map(this::mapStringToAvailability).toList()
            : null;

    return new EmployeeSearchCriteria(
        searchTerm, skillIds, skillCategoryIds, locationIds, availabilityStatuses, minExperience);
  }

  private AvailabilityStatus mapStringToAvailability(final String status) {
    return switch (status.toLowerCase()) {
      case "available" -> AvailabilityStatus.AVAILABLE;
      case "partially_available" -> AvailabilityStatus.PARTIALLY_AVAILABLE;
      case "unavailable" -> AvailabilityStatus.UNAVAILABLE;
      default -> throw new IllegalArgumentException("Invalid availability status: " + status);
    };
  }
}
