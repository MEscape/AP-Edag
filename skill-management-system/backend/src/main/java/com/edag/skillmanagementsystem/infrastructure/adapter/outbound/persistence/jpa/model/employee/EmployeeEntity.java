package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee;

import com.edag.skillmanagementsystem.domain.model.user.AvailabilityStatus;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.UserStatisticsEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.LocationEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project.ProjectMemberEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

/**
 * JPA entity representing an employee profile in the system.
 *
 * <p>This entity extends the basic {@link UserEntity} with organizational details such as position,
 * location, availability, experience, biography, assigned skills, and project memberships.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "employee_profiles",
    indexes = {
      @Index(name = "idx_employee_profiles_location", columnList = "location_id"),
      @Index(name = "idx_employee_profiles_position", columnList = "position_id"),
      @Index(name = "idx_employee_profiles_availability", columnList = "availability"),
      @Index(name = "idx_employee_profiles_experience", columnList = "years_of_experience")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmployeeEntity extends BaseAuditEntity {

  /** Unique identifier of the employee (shared with the related {@link UserEntity}). */
  @Id
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  /**
   * Underlying user account associated with the employee.
   *
   * <p>Loaded lazily and shares the same identifier via {@link MapsId}.
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_employee_user"))
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /**
   * Analytical statistics for this employee, such as activity metrics or computed summaries.
   *
   * <p>Loaded eagerly and mapped via shared user_id.
   */
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "user_id",
      insertable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_employee_statistics"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserStatisticsEntity statistics;

  /**
   * Job position or role assigned to the employee.
   *
   * <p>Represents organizational hierarchy or job category. Nullable until user completes their
   * profile.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_id", foreignKey = @ForeignKey(name = "fk_employee_position"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private PositionEntity position;

  /**
   * Workplace or office assignment of the employee.
   *
   * <p>Represents the location where the employee is based. Nullable until user completes their
   * profile.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_employee_location"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LocationEntity location;

  /** Current availability state of the employee (e.g., available, partially available). */
  @Enumerated(EnumType.STRING)
  @Column(name = "availability", nullable = false, length = 50)
  @Builder.Default
  private AvailabilityStatus availability = AvailabilityStatus.UNAVAILABLE;

  /** Total years of professional experience the employee has accumulated. */
  @Column(name = "years_of_experience", nullable = false, precision = 3, scale = 1)
  @Builder.Default
  private BigDecimal yearsOfExperience = BigDecimal.ZERO;

  /**
   * Short biography or description written by the employee.
   *
   * <p>Used for profile displays, internal directories, or portfolio generation.
   */
  @Column(name = "bio", columnDefinition = "TEXT", length = 500)
  private String bio;

  /**
   * Skills assigned to the employee, including proficiency and usage metadata.
   *
   * <p>Lazy-loaded collection with batch fetching for optimal performance.
   */
  @OneToMany(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @BatchSize(size = 50)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<EmployeeSkillEntity> employeeSkills = new ArrayList<>();

  /**
   * Project memberships for this employee.
   *
   * <p>Projects are now managed through a many-to-many relationship via the {@link
   * ProjectMemberEntity} junction table, allowing multiple employees to work on the same project.
   */
  @OneToMany(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @BatchSize(size = 20)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<ProjectMemberEntity> projectMemberships = new ArrayList<>();

  /**
   * Computes how complete the employee profile is based on several fields.
   *
   * @return a completion percentage from 0 to 100
   */
  public double getCompletionRate() {
    int filled = 0;

    if (position != null) {
      filled++;
    }
    if (location != null) {
      filled++;
    }
    if (availability != null) {
      filled++;
    }
    if (yearsOfExperience.compareTo(BigDecimal.ZERO) > 0) {
      filled++;
    }
    if (bio != null && !bio.isBlank()) {
      filled++;
    }
    if (employeeSkills != null && !employeeSkills.isEmpty()) {
      filled++;
    }

    return Math.round(((filled / 6.0) * 100) * 100.0) / 100.0;
  }
}
