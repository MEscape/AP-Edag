package com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.user;

import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.analytics.ActivityEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.base.BaseAuditEntity;
import com.edag.skillmanagementsystem.infrastructure.adapter.outbound.persistence.jpa.model.employee.EmployeeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
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
import org.hibernate.annotations.NaturalId;

/**
 * Represents a persistent user record within the skill management system.
 *
 * <p>This entity stores user information synchronized from Keycloak. Each user is uniquely
 * identified by a local UUID and linked to their Keycloak identity.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "users",
    indexes = {
      @Index(name = "idx_users_email", columnList = "email"),
      @Index(name = "idx_users_username", columnList = "username"),
      @Index(name = "idx_users_created_at", columnList = "created_at")
    })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity extends BaseAuditEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NaturalId
  @Column(name = "username", nullable = false, length = 128, unique = true)
  private String username;

  @NaturalId
  @Column(name = "email", nullable = false, length = 320, unique = true)
  private String email;

  @Column(name = "first_name", length = 128)
  private String firstName;

  @Column(name = "last_name", length = 128)
  private String lastName;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private EmployeeEntity employeeProfile;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @BatchSize(size = 10)
  @OrderBy("timestamp DESC")
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<ActivityEntity> activities = new ArrayList<>();
}
