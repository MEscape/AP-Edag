package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
 * JPA entity representing a standardized job position in the organization.
 *
 * <p>Positions classify employee roles such as "Software Engineer", "Team Lead", or "Project
 * Manager". They serve as a master lookup referenced by employee profiles and can be deactivated
 * without losing historical references.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code is_active}: enables quick filtering of available positions.
 *   <li>{@code name}: supports efficient lookup by exact or partial name.
 * </ul>
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "positions",
    indexes = {
      @Index(name = "idx_positions_active", columnList = "is_active"),
      @Index(name = "idx_positions_name", columnList = "name")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PositionEntity extends BaseAuditEntity {

  /** Unique identifier of the position. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** Display name of the position (unique). */
  @Column(name = "name", nullable = false, unique = true, length = 128)
  private String name;

  /** Whether this position is currently available for assignment. */
  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  /** All employees assigned to this position. */
  @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
  @BatchSize(size = 30)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<EmployeeEntity> employees = new ArrayList<>();
}
