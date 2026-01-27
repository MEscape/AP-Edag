package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.employee;

import com.edag.skillmanagementsystem.domain.model.employee.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing an employee for API responses.
 *
 * <p>This record transfers employee data from the backend to the frontend, matching the TypeScript
 * interface expected by the client application.
 */
@Schema(
    name = "EmployeeDto",
    description =
        "Represents an employee with basic profile information, top skills, "
            + "and summary statistics such as projects and experience.")
public record EmployeeResponseDto(
    @Schema(
            description = "The unique identifier of the employee",
            example = "c62b7b68-22cd-4cb9-9d74-2ffb5c7e3a8b")
        UUID id,
    @Schema(description = "The employee's first name", example = "Anna") String firstName,
    @Schema(description = "The employee's last name", example = "Müller") String lastName,
    @Schema(description = "The employee's email address", example = "anna.mueller@edag.de")
        String email,
    @Schema(description = "The employee's current job position", example = "Software Engineer")
        String position,
    @Schema(description = "The employee's work location", example = "Munich") String location,
    @Schema(
            description = "The availability status of the employee",
            allowableValues = {"available", "partially_available", "unavailable"},
            example = "available")
        String availability,
    @Schema(
            description = "The employee's top 3 skills sorted by score.",
            example = "[{\"name\": \"Java\", \"score\": 95, \"yearsOfExperience\": 4}]")
        List<EmployeeSkillResponseDto> skills,
    @Schema(description = "The total number of skills the employee possesses", example = "12")
        int skillCount,
    @Schema(
            description = "The total number of projects the employee has participated in",
            example = "7")
        int totalProjects,
    @Schema(description = "The total years of professional experience", example = "5")
        double yearsOfExperience) {

  public static EmployeeResponseDto from(final Employee employee) {

    List<EmployeeSkillResponseDto> skillDtos =
        employee.skills().stream().map(EmployeeSkillResponseDto::from).toList();

    return new EmployeeResponseDto(
        employee.userId(),
        employee.firstName(),
        employee.lastName(),
        employee.email(),
        employee.position(),
        employee.location(),
        employee.availability().name().toLowerCase(),
        skillDtos,
        employee.totalSkills(),
        employee.totalProjects(),
        employee.yearsOfExperience());
  }
}
