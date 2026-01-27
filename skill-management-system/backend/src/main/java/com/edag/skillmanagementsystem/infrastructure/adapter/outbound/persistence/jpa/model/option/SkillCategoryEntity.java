package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.option;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
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
 * JPA entity representing a skill category in the master catalog.
 *
 * <p>Categories group related skills (e.g., "Backend", "Frontend", "DevOps"). They provide
 * structure for skill management, analytics, and filtering.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code is_active}: supports filtering only usable categories.
 *   <li>{@code name}: supports fast lookup of specific category names.
 * </ul>
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "skill_categories",
    indexes = {
      @Index(name = "idx_skill_categories_active", columnList = "is_active"),
      @Index(name = "idx_skill_categories_name", columnList = "name")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SkillCategoryEntity extends BaseAuditEntity {

  /** Unique identifier of the skill category. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** Name of the skill category (unique). */
  @Column(name = "name", nullable = false, unique = true, length = 128)
  private String name;

  /** Whether this category is currently active. */
  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  /** Skills belonging to this category. */
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @BatchSize(size = 50)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  private List<SkillEntity> skills = new ArrayList<>();
}
