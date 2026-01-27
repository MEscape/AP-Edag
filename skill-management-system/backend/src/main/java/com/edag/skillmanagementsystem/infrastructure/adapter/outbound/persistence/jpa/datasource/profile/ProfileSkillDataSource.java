package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.datasource.profile;

import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.domain.port.outbound.ProfileSkillRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.employee.EmployeeSkillJpaRepository;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.repository.option.SkillJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data source implementation for managing profile skills of users.
 *
 * <p>Provides operations for adding, updating, and deleting skills associated with a user,
 * including validation and entity transformation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSkillDataSource implements ProfileSkillRepository {

  private final EmployeeJpaRepository employeeRepository;
  private final EmployeeSkillJpaRepository employeeSkillRepository;
  private final SkillJpaRepository skillRepository;

  @Override
  @Transactional
  public Optional<ProfileSkill> addSkill(
      UUID userId,
      UUID skillId,
      int proficiencyScore,
      double yearsOfExperience,
      LocalDate lastUsed) {

    log.debug("Adding skill {} to user {}", skillId, userId);

    Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUserId(userId);
    if (employeeOpt.isEmpty()) {
      log.debug("Employee not found for user: {}", userId);
      return Optional.empty();
    }

    Optional<SkillEntity> skillEntityOpt = skillRepository.findById(skillId);
    if (skillEntityOpt.isEmpty()) {
      log.debug("Skill not found: {}", skillId);
      return Optional.empty();
    }

    EmployeeEntity employee = employeeOpt.get();
    SkillEntity skillEntity = skillEntityOpt.get();

    // Check if skill is active
    if (!skillEntity.isActive()) {
      log.debug("Skill is not active: {}", skillId);
      return Optional.empty();
    }

    // Check if skill already exists for this employee
    if (employeeSkillRepository.existsByEmployeeUserIdAndSkillId(userId, skillId)) {
      log.debug("Skill already exists for employee");
      return Optional.empty();
    }

    // Create employee skill
    EmployeeSkillEntity employeeSkill =
        EmployeeSkillEntity.builder()
            .employee(employee)
            .skill(skillEntity)
            .proficiencyScore(proficiencyScore)
            .yearsOfExperience(BigDecimal.valueOf(yearsOfExperience))
            .lastUsed(lastUsed)
            .build();

    employeeSkillRepository.save(employeeSkill);
    log.info("Successfully added skill {} to user {}", skillId, userId);

    return Optional.of(ProfileMapper.skillEntityToDomain(employeeSkill));
  }

  @Override
  @Transactional
  public Optional<ProfileSkill> updateSkill(
      UUID userId,
      UUID skillId,
      Integer proficiencyScore,
      Double yearsOfExperience,
      LocalDate lastUsed) {

    log.debug("Updating skill {} for user {}", skillId, userId);

    Optional<EmployeeSkillEntity> employeeSkillOpt =
        employeeSkillRepository.findBySkillIdAndEmployeeUserId(skillId, userId);

    if (employeeSkillOpt.isEmpty()) {
      log.debug("Skill not found for user: {} and skill: {}", userId, skillId);
      return Optional.empty();
    }

    EmployeeSkillEntity employeeSkill = employeeSkillOpt.get();

    // Update fields if provided
    if (proficiencyScore != null) {
      employeeSkill.setProficiencyScore(proficiencyScore);
    }
    if (yearsOfExperience != null) {
      employeeSkill.setYearsOfExperience(BigDecimal.valueOf(yearsOfExperience));
    }
    if (lastUsed != null) {
      employeeSkill.setLastUsed(lastUsed);
    }

    employeeSkillRepository.save(employeeSkill);
    log.info("Successfully updated skill {} for user {}", skillId, userId);

    return Optional.of(ProfileMapper.skillEntityToDomain(employeeSkill));
  }

  @Override
  @Transactional
  public boolean deleteSkill(UUID userId, UUID skillId) {
    log.debug("Deleting skill {} for user {}", skillId, userId);

    Optional<EmployeeSkillEntity> employeeSkillOpt =
        employeeSkillRepository.findBySkillIdAndEmployeeUserId(skillId, userId);

    if (employeeSkillOpt.isEmpty()) {
      log.debug("Skill not found for deletion: {} for user: {}", skillId, userId);
      return false;
    }

    employeeSkillRepository.delete(employeeSkillOpt.get());
    log.info("Successfully deleted skill {} for user {}", skillId, userId);
    return true;
  }
}
