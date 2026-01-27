package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeSkillEntity;
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
 * JPA entity representing an individual skill in the master skill catalog.
 *
 * <p>Skills define the standardized capabilities that employees can possess. Each skill belongs to
 * a {@link SkillCategoryEntity} and may be assigned to employees through {@link
 * EmployeeSkillEntity}. Skills can be activated or deactivated without affecting historical
 * associations.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code category_id, is_active}: optimized for filtering available skills within a specific
 *       category.
 * </ul>
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "skills",
    indexes = {@Index(name = "idx_skills_category_active", columnList = "category_id, is_active")})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SkillEntity extends BaseAuditEntity {

  /** Unique identifier of the skill in the master catalog. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /**
   * Human-readable name of the skill.
   *
   * <p>Names must remain unique to ensure clarity across the catalog and prevent collisions during
   * assignment or analytics.
   */
  @Column(name = "name", nullable = false, unique = true, length = 128)
  private String name;

  /**
   * Category to which this skill belongs.
   *
   * <p>Used for grouping, filtering, and organizing skills throughout the system.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "category_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_skills_category"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private SkillCategoryEntity category;

  /**
   * Whether this skill is currently active and available for selection.
   *
   * <p>Deactivated skills remain for historical records but cannot be newly assigned.
   */
  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  /**
   * All employee–skill relationships referencing this skill.
   *
   * <p>Primarily used for analytics, skill usage tracking, and administrative purposes.
   */
  @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
  @BatchSize(size = 50)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<EmployeeSkillEntity> employeeSkills = new ArrayList<>();
}
