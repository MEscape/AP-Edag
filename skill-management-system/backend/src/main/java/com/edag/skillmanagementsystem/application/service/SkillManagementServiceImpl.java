package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.exception.skill.DuplicateSkillException;
import com.edag.skillmanagementsystem.domain.exception.skill.InactiveSkillException;
import com.edag.skillmanagementsystem.domain.exception.skill.ProfileSkillNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.skill.SkillNotFoundException;
import com.edag.skillmanagementsystem.domain.exception.user.ProfileNotFoundException;
import com.edag.skillmanagementsystem.domain.model.option.Skill;
import com.edag.skillmanagementsystem.domain.model.skill.ProfileSkill;
import com.edag.skillmanagementsystem.domain.port.inbound.SkillManagementService;
import com.edag.skillmanagementsystem.domain.port.outbound.OptionsRepository;
import com.edag.skillmanagementsystem.domain.port.outbound.ProfileRepository;
import com.edag.skillmanagementsystem.domain.port.outbound.ProfileSkillRepository;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the SkillManagementService interface.
 *
 * <p>This service orchestrates skill management operations for user profiles, handling validation,
 * error handling, and coordination between repositories.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkillManagementServiceImpl implements SkillManagementService {

  private final ProfileSkillRepository profileSkillRepository;
  private final ProfileRepository profileRepository;
  private final OptionsRepository optionsRepository;

  @Override
  public ProfileSkill addSkill(
      UUID userId,
      UUID skillId,
      UUID categoryId,
      int proficiencyScore,
      double yearsOfExperience,
      LocalDate lastUsed) {

    log.debug(
        "Adding skillId '{}' to user: {} (score: {}, experience: {} years, lastUsed: {})",
        skillId,
        userId,
        proficiencyScore,
        yearsOfExperience,
        lastUsed);

    // Validate profile exists
    validateProfileExists(userId);

    // Find skill by name and category
    Skill skill =
        optionsRepository
            .findSkillByCategoryIdAndSkillId(categoryId, skillId)
            .orElseThrow(
                () -> {
                  log.warn("SkillId not found: '{}' in categoryId '{}'", skillId, categoryId);
                  return new SkillNotFoundException(skillId);
                });

    // Check if skill is active
    if (!skill.active()) {
      log.warn("Attempted to add inactive skill: {} ({})", skill.name(), skill.id());
      throw new InactiveSkillException(skill.name());
    }

    // Attempt to add skill
    return profileSkillRepository
        .addSkill(userId, skill.id(), proficiencyScore, yearsOfExperience, lastUsed)
        .orElseThrow(
            () -> {
              // If add fails, it's likely a duplicate
              log.warn(
                  "Failed to add skill '{}' to user {} - likely duplicate", skill.name(), userId);
              return new DuplicateSkillException(skill.name(), userId);
            });
  }

  @Override
  public ProfileSkill updateSkill(
      UUID userId,
      UUID skillId,
      Integer proficiencyScore,
      Double yearsOfExperience,
      LocalDate lastUsed) {

    log.debug(
        "Updating skill {} for user: {} (score: {}, experience: {}, lastUsed: {})",
        skillId,
        userId,
        proficiencyScore,
        yearsOfExperience,
        lastUsed);

    return profileSkillRepository
        .updateSkill(userId, skillId, proficiencyScore, yearsOfExperience, lastUsed)
        .orElseThrow(
            () -> {
              log.warn("Skill relationship not found: {} for user: {}", skillId, userId);
              return new ProfileSkillNotFoundException(skillId, userId);
            });
  }

  @Override
  public void deleteSkill(UUID userId, UUID skillId) {
    log.debug("Deleting skill {} from user: {}", skillId, userId);

    boolean deleted = profileSkillRepository.deleteSkill(userId, skillId);

    if (!deleted) {
      log.warn("Skill relationship not found for deletion: {} for user: {}", skillId, userId);
      throw new ProfileSkillNotFoundException(skillId, userId);
    }

    log.info("Successfully deleted skill {} from user: {}", skillId, userId);
  }

  /**
   * Validates that a user's profile exists.
   *
   * @param userId the user's unique identifier
   * @throws ProfileNotFoundException if the profile does not exist
   */
  private void validateProfileExists(UUID userId) {
    if (!profileRepository.existsByUserId(userId)) {
      log.warn("Profile not found for user: {}", userId);
      throw new ProfileNotFoundException(userId);
    }
  }
}
