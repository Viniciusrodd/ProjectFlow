
// packages
package com.example.ProjectFlow.modules.project.entity;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Entity
@Table(
   name = "project_members",
   uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "project_id" }) }
)
@SQLRestriction("deleted_at IS NULL")
public class ProjectMembersEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "UUID")
   private UUID id;

   // N(users) : 1(project_members)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private UserEntity user;
   
   // N(projects) : 1(project_members)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "project_id", nullable = false)
   private ProjectEntity project;


   //// fields
   

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private RoleEnum role;

   @CreationTimestamp
   @Column(name = "joined_at", updatable = false)
   private LocalDateTime joinedAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor
   protected ProjectMembersEntity(){}


   // getters
   public UUID getId() { return this.id; }
   public UserEntity getUser() { return this.user; }
   public ProjectEntity getProject() { return this.project; }
   public RoleEnum getRole() { return this.role; }
   public LocalDateTime getJoinedAt() { return this.joinedAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setUser(UserEntity user) { this.user = user; }
   public void setProject(ProjectEntity project) { this.project = project; }
   public void setRole(RoleEnum role) { this.role = role; }
   public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; } 


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}