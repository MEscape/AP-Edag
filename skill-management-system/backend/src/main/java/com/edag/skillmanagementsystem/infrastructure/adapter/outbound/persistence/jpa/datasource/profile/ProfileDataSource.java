package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.profile;

import com.edag.skillmanagementsystem.domain.model.user.AvailabilityStatus;
import com.edag.skillmanagementsystem.domain.model.user.UserProfile;
import com.edag.skillmanagementsystem.domain.port.outbound.ProfileRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeSkillJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.LocationJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.PositionJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.project.ProjectJpaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data source implementation for managing user profiles.
 *
 * <p>Provides operations for retrieving and updating profile information, including skills,
 * projects, position, location, availability, and biography.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileDataSource implements ProfileRepository {

  private final EmployeeJpaRepository employeeRepository;
  private final EmployeeSkillJpaRepository employeeSkillRepository;
  private final ProjectJpaRepository projectRepository;
  private final LocationJpaRepository locationRepository;
  private final PositionJpaRepository positionRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<UserProfile> findProfileByUserId(UUID userId) {
    log.debug("Finding profile for user: {}", userId);
    return employeeRepository
        .findByUserId(userId)
        .map(employee -> buildUserProfile(userId, employee));
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByUserId(UUID userId) {
    return employeeRepository.existsById(userId);
  }

  @Override
  @Transactional
  public Optional<UserProfile> updateProfileInfo(
      UUID userId,
      UUID positionId,
      UUID locationId,
      String availability,
      Double yearsOfExperience,
      String bio) {

    log.debug("Updating profile info for user: {}", userId);

    Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUserId(userId);
    if (employeeOpt.isEmpty()) {
      log.warn("Employee not found for user: {}", userId);
      return Optional.empty();
    }

    EmployeeEntity employee = employeeOpt.get();

    if (!updatePosition(employee, positionId, userId)) {
      return Optional.empty();
    }
    if (!updateLocation(employee, locationId, userId)) {
      return Optional.empty();
    }
    if (!updateAvailability(employee, availability, userId)) {
      return Optional.empty();
    }

    updateYearsOfExperience(employee, yearsOfExperience, userId);
    updateBio(employee, bio, userId);

    employeeRepository.save(employee);
    log.info("Profile updated successfully for user: {}", userId);

    return Optional.of(buildUserProfile(userId, employee));
  }

  /** Updates the employee's position if valid. */
  private boolean updatePosition(EmployeeEntity employee, UUID positionId, UUID userId) {
    if (positionId == null) {
      return true;
    }

    Optional<PositionEntity> positionEntity = positionRepository.findById(positionId);
    if (positionEntity.isEmpty()) {
      log.warn("Position not found with id: '{}' for user: {}", positionId, userId);
      return false;
    }

    if (!positionEntity.get().isActive()) {
      log.warn("Position is inactive with id: '{}' for user: {}", positionId, userId);
      return false;
    }

    employee.setPosition(positionEntity.get());
    log.debug("Updated position for user: {}", userId);
    return true;
  }

  /** Updates the employee's location if valid. */
  private boolean updateLocation(EmployeeEntity employee, UUID locationId, UUID userId) {
    if (locationId == null) {
      return true;
    }

    Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
    if (locationEntity.isEmpty()) {
      log.warn("Location not found with id: '{}' for user: {}", locationId, userId);
      return false;
    }

    if (!locationEntity.get().isActive()) {
      log.warn("Location is inactive with id: '{}' for user: {}", locationId, userId);
      return false;
    }

    employee.setLocation(locationEntity.get());
    log.debug("Updated location for user: {}", userId);
    return true;
  }

  /** Updates the employee's availability status if valid. */
  private boolean updateAvailability(EmployeeEntity employee, String availability, UUID userId) {
    if (availability == null || availability.isBlank()) {
      return true;
    }

    try {
      AvailabilityStatus status = AvailabilityStatus.valueOf(availability.trim().toUpperCase());
      employee.setAvailability(status);
      log.debug("Updated availability for user: {}", userId);
      return true;
    } catch (IllegalArgumentException e) {
      log.warn("Invalid availability: '{}' for user: {}", availability, userId);
      return false;
    }
  }

  /** Updates the biography text of the employee. */
  private void updateBio(EmployeeEntity employee, String bio, UUID userId) {
    if (bio == null) {
      return;
    }

    String newBio = bio.isBlank() ? null : bio.trim();
    employee.setBio(newBio);
    log.debug("Updated bio for user: {}", userId);
  }

  /** Updates the years of experience for the employee. */
  private void updateYearsOfExperience(
      EmployeeEntity employee, Double yearsOfExperience, UUID userId) {
    if (yearsOfExperience == null) {
      return;
    }

    employee.setYearsOfExperience(BigDecimal.valueOf(yearsOfExperience));
    log.debug("Updated years of experience to {} for user: {}", yearsOfExperience, userId);
  }

  /** Builds a full {@link UserProfile} using employee details, skills, and projects. */
  private UserProfile buildUserProfile(UUID userId, EmployeeEntity employee) {
    List<EmployeeSkillEntity> skills =
        employeeSkillRepository.findWithDetailsByEmployeeUserId(userId);

    Pageable pageable = PageRequest.of(0, 20);
    List<ProjectEntity> projects =
        projectRepository
            .findDistinctByProjectMembersEmployeeUserIdOrderByStartDateDesc(userId, pageable)
            .getContent();

    return ProfileMapper.entityToDomain(employee, skills, projects);
  }
}
