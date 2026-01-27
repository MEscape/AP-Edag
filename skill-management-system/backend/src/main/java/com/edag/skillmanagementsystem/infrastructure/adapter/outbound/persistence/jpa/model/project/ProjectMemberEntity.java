package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option.PositionEntity;
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
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing a project team member.
 *
 * <p>This junction table manages the many-to-many relationship between projects and employees,
 * allowing multiple employees to work on the same project with different roles/positions.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code project_id}: optimized for retrieving all members of a project
 *   <li>{@code employee_id}: optimized for retrieving all projects an employee is assigned to
 *   <li>{@code position_id}: optimized for filtering by role/position
 * </ul>
 *
 * <h3>Constraints</h3>
 *
 * <ul>
 *   <li>Unique constraint on (project_id, employee_id) prevents duplicate assignments
 * </ul>
 *
 * @since 1.0.0
 */
@Entity
@Table(
    name = "project_members",
    indexes = {
      @Index(name = "idx_project_members_project", columnList = "project_id"),
      @Index(name = "idx_project_members_employee", columnList = "employee_id"),
      @Index(name = "idx_project_members_position", columnList = "position_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_project_employee",
          columnNames = {"project_id", "employee_id"})
    })
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectMemberEntity extends BaseAuditEntity {

  /** Unique identifier of the project member assignment. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** The project this member is assigned to. */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "project_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_project_member_project"))
  @ToString.Exclude
  private ProjectEntity project;

  /** The employee assigned to this project. */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "employee_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_project_member_employee"))
  @ToString.Exclude
  private EmployeeEntity employee;

  /** The role/position held by the employee in this project. */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "position_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_project_member_position"))
  @ToString.Exclude
  private PositionEntity position;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProjectMemberEntity that)) {
      return false;
    }
    return project != null
        && employee != null
        && Objects.equals(project.getId(), that.project.getId())
        && Objects.equals(employee.getUserId(), that.employee.getUserId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        project != null ? project.getId() : null,
        employee != null ? employee.getUserId() : null);
  }
}
