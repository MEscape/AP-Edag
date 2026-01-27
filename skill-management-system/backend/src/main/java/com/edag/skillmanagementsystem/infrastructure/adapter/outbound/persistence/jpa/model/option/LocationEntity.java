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
 * JPA entity representing a workplace or office location in the master catalog.
 *
 * <p>Locations define where employees are based and serve as a standardized lookup referenced by
 * employee profiles. Each location can be activated/deactivated without removing historical
 * associations.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code is_active}: supports filtering active locations.
 *   <li>{@code name}: supports lookup and ordering by standardized location names.
 * </ul>
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "locations",
    indexes = {
      @Index(name = "idx_locations_active", columnList = "is_active"),
      @Index(name = "idx_locations_name", columnList = "name")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LocationEntity extends BaseAuditEntity {

  /** Unique identifier of the location. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** Human-readable name of the location (unique). */
  @Column(name = "name", nullable = false, unique = true, length = 128)
  private String name;

  /** Whether this location is currently selectable in the system. */
  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  /** Employees based at this location. */
  @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
  @BatchSize(size = 30)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  private List<EmployeeEntity> employees = new ArrayList<>();
}
