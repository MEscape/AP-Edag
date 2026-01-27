package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.project;

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
 * Join entity representing skills/technologies used in a specific project.
 *
 * <p>This entity creates a many-to-many relationship between projects and skills, allowing proper
 * tracking of which technologies were actually used in each project.
 */
@Entity
@Table(
    name = "project_skills",
    indexes = {
      @Index(name = "idx_project_skills_project", columnList = "project_id"),
      @Index(name = "idx_project_skills_skill", columnList = "skill_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_project_skill",
          columnNames = {"project_id", "skill_id"})
    })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class ProjectSkillEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "project_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_project_skill_project"))
  @ToString.Exclude
  private ProjectEntity project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "skill_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_project_skill_skill"))
  @ToString.Exclude
  private SkillEntity skill;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProjectSkillEntity that)) {
      return false;
    }
    return project != null
        && skill != null
        && Objects.equals(project.getId(), that.project.getId())
        && Objects.equals(skill.getId(), that.skill.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        project != null ? project.getId() : null, skill != null ? skill.getId() : null);
  }
}
