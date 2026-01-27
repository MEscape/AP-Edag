package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project;

import com.edag.skillmanagementsystem.domain.model.project.ProjectStatus;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
 * JPA entity representing a project.
 *
 * <p>This entity captures project metadata such as status, duration, and associated technologies.
 * Employee assignments are managed through the {@link ProjectMemberEntity} junction table, allowing
 * multiple employees to work on the same project.
 *
 * <h3>Indexing</h3>
 *
 * <ul>
 *   <li>{@code start_date}: optimized for retrieving recent projects
 *   <li>{@code status}: optimized for filtering projects by current status
 *   <li>{@code created_by_user_id}: optimized for querying projects by creator (manager)
 * </ul>
 *
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "projects",
    indexes = {
      @Index(name = "idx_projects_start_date", columnList = "start_date"),
      @Index(name = "idx_projects_status", columnList = "status"),
      @Index(name = "idx_projects_created_by_user", columnList = "created_by_user_id")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectEntity extends BaseAuditEntity {

  /** Unique identifier of the project. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  /** Display name of the project. */
  @Column(name = "name", nullable = false)
  private String name;

  /** Optional free-text description of the project. */
  @Column(name = "description", columnDefinition = "TEXT", length = 2000)
  private String description;

  /** Current lifecycle status of the project. */
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 32)
  @Builder.Default
  private ProjectStatus status = ProjectStatus.PLANNED;

  /** The project start date. */
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /** The project completion date (optional). */
  @Column(name = "end_date")
  private LocalDate endDate;

  /** Client or customer associated with the project (optional). */
  @Column(name = "client")
  private String client;

  /** Number of team members involved in the project (optional). */
  @Column(name = "team_size")
  private Integer teamSize;

  /** User (manager) who created this project assignment. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "created_by_user_id",
      foreignKey = @ForeignKey(name = "fk_project_created_by_user"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity createdByUser;

  /** Team members assigned to this project with their roles. */
  @OneToMany(
      mappedBy = "project",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @BatchSize(size = 20)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<ProjectMemberEntity> projectMembers = new HashSet<>();

  /** Technologies or skills used in the project, represented as project-skill relationships. */
  @OneToMany(
      mappedBy = "project",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @BatchSize(size = 20)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<ProjectSkillEntity> projectSkills = new HashSet<>();
}
