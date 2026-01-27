package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.controller;

import com.edag.skillmanagementsystem.domain.model.project.Project;
import com.edag.skillmanagementsystem.domain.port.inbound.ProjectManagementService;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.CreateProjectRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.ProjectResponseDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.project.UpdateProjectRequestDto;
import com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.security.AuthorizationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing project assignments.
 *
 * <p>This controller provides endpoints for project creation, modification, and deletion. All
 * operations are restricted to users with the MANAGER role, and managers may only modify the
 * projects they originally created.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/management/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Project Management", description = "Manager APIs for creating and modifying projects")
public class ProjectManagementController {

  private final ProjectManagementService projectManagementService;
  private final AuthorizationUtils authorizationUtils;

  /**
   * Creates a new project assignment.
   *
   * <p>Only users with the MANAGER role may create projects. The authenticated manager becomes the
   * owner of the project and is the only user who may update or delete it.
   *
   * @param request the project creation request payload
   * @param principal the authenticated user principal
   * @return the created project
   */
  @Operation(
      summary = "Create project",
      description =
          "Creates a new project assignment. Only users with the MANAGER role may perform this "
              + "operation. The authenticated manager becomes the owner of the project.")
  @ApiResponse(
      responseCode = "201",
      description = "Project created successfully",
      content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
  @ApiResponse(responseCode = "400", description = "Invalid project data", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User does not have MANAGER role",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Related employee not found", content = @Content)
  @PostMapping
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ProjectResponseDto> createProject(
      @Valid @RequestBody CreateProjectRequestDto request, Principal principal) {

    log.debug("REST request to create project: {}", request.name());

    UUID managerId = authorizationUtils.resolveUserId("me", principal);
    Project project = projectManagementService.createProject(request.toDomain(), managerId);

    log.info("Project created with ID: {} by manager: {}", project.id(), managerId);

    return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponseDto.from(project));
  }

  /**
   * Updates an existing project.
   *
   * <p>Only the manager who created the project may update it. If the authenticated user is not the
   * creator, the request is rejected.
   *
   * @param projectId the unique identifier of the project
   * @param request the project update payload
   * @param principal the authenticated user principal
   * @return the updated project
   */
  @Operation(
      summary = "Update project",
      description =
          "Updates an existing project. Only the manager who originally created the project may "
              + "update it.")
  @ApiResponse(
      responseCode = "200",
      description = "Project updated successfully",
      content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
  @ApiResponse(responseCode = "400", description = "Invalid project data", content = @Content)
  @ApiResponse(
      responseCode = "403",
      description = "User is not the project creator or lacks MANAGER role",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
  @PutMapping("/{projectId}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ProjectResponseDto> updateProject(
      @Parameter(description = "Project ID") @PathVariable UUID projectId,
      @Valid @RequestBody UpdateProjectRequestDto request,
      Principal principal) {

    log.debug("REST request to update project: {}", projectId);

    UUID managerId = authorizationUtils.resolveUserId("me", principal);
    Project project =
        projectManagementService.updateProject(projectId, request.toDomain(), managerId);

    log.info("Project updated: {} by manager: {}", projectId, managerId);

    return ResponseEntity.ok(ProjectResponseDto.from(project));
  }

  /**
   * Deletes a project.
   *
   * <p>Only the manager who created the project may delete it. If the authenticated user is not the
   * creator, the request is denied.
   *
   * @param projectId the unique identifier of the project
   * @param principal the authenticated user principal
   * @return an empty response with status 204
   */
  @Operation(
      summary = "Delete project",
      description =
          "Deletes a project. Only the manager who created the project may perform this action.")
  @ApiResponse(responseCode = "204", description = "Project deleted successfully")
  @ApiResponse(
      responseCode = "403",
      description = "User is not the creator or lacks MANAGER role",
      content = @Content)
  @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
  @DeleteMapping("/{projectId}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<Void> deleteProject(
      @Parameter(description = "Project ID") @PathVariable UUID projectId, Principal principal) {

    log.debug("REST request to delete project: {}", projectId);

    UUID managerId = authorizationUtils.resolveUserId("me", principal);
    projectManagementService.deleteProject(projectId, managerId);

    log.info("Project deleted: {} by manager: {}", projectId, managerId);

    return ResponseEntity.noContent().build();
  }
}
