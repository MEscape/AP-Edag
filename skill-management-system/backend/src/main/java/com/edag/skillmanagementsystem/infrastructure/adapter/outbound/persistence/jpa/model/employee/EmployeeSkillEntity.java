package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.SkillEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JPA entity representing an employee's proficiency in a specific skill.
 *
 * <p>This entity forms the link between an {@link EmployeeEntity} and a {@link SkillEntity},
 * storing additional metadata such as proficiency score, years of experience, and when the skill
 * was last used.
 *
 * <p>Each employee–skill pair is unique, enforced via a database-level unique constraint.
 */
@Entity
@Table(
    name = "employee_skills",
    indexes = {
      @Index(name = "idx_employee_skills_employee", columnList = "employee_id"),
      @Index(name = "idx_employee_skills_skill", columnList = "skill_id"),
      @Index(
          name = "idx_employee_skills_skill_proficiency",
          columnList = "skill_id, proficiency_score DESC")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_employee_skill",
          columnNames = {"employee_id", "skill_id"})
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmployeeSkillEntity {

  /** Unique identifier for the employee–skill relationship entry. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /**
   * The employee who possesses this skill.
   *
   * <p>Loaded lazily to avoid unnecessary joins when loading skills independently.
   */
  @NotNull(message = "Employee is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "employee_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_employee_skill_employee"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private EmployeeEntity employee;

  /**
   * The skill assigned to the employee.
   *
   * <p>Loaded lazily by default, but may be eagerly fetched in skill-loading queries using an
   * {@link org.springframework.data.jpa.repository.EntityGraph}.
   */
  @NotNull(message = "Skill is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "skill_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_employee_skill_skill"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private SkillEntity skill;

  /**
   * The employee's proficiency score for the skill, ranging from 0 to 100.
   *
   * <p>Represents how capable the employee is at performing tasks using this skill.
   */
  @Column(name = "proficiency_score", nullable = false)
  private int proficiencyScore;

  /**
   * Total years of experience the employee has with this skill.
   *
   * <p>Used for ranking, filtering, or displaying seniority in specific competencies.
   */
  @Column(name = "years_of_experience", nullable = false, precision = 3, scale = 1)
  @Builder.Default
  private BigDecimal yearsOfExperience = BigDecimal.ZERO;

  /**
   * The date when the employee last used this skill in a meaningful context.
   *
   * <p>May be {@code null} if the skill has not been used recently or if the information is not
   * tracked for this employee.
   */
  @Column(name = "last_used")
  private LocalDate lastUsed;
}
