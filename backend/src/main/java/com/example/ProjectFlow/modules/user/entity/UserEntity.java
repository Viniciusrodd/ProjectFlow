
// packages
package com.example.ProjectFlow.modules.user.entity;

// imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;


@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL") // auto. filter not deleted registers
public class UserEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // 1(owner user) : N(organizations)
   @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
   private List<OrganizationEntity> ownedOrganizations = new ArrayList<>();

   // 1(user member) : N(organization_members)
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<OrganizationMembersEntity> organizationMemberships = new ArrayList<>();
   
   // 1(owner user) : N(projects)
   @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
   private List<ProjectEntity> ownedProjects = new ArrayList<>();

   // 1(user member) : N(project_members)
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<ProjectMembersEntity> projectMemberships = new ArrayList<>();

   // 1(owner user) : N(tasks)
   @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
   private List<TasksEntity> tasks = new ArrayList<>();

   // 1(author user) : N(comments)
   @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<CommentEntity> comments = new ArrayList<>(); 
 
   
   //// fields


   @Column(nullable = false, length = 120)
   private String name;

   @Column(nullable = false, length = 255, unique = true)
   private String email;

   @Column(nullable = false, length = 255)
   private String password;

   @Column(name = "profile_image_id")
   private String profileImageId; // mongo ref.
   
   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor
   public UserEntity() {}


   // getters
   public UUID getId() { return this.id; }
   public List<OrganizationEntity> getOwnedOrganizations() { return this.ownedOrganizations; }
   public List<OrganizationMembersEntity> getOrganizationMemberships() { return this.organizationMemberships; }
   public List<ProjectEntity> getOwnedProjects() { return this.ownedProjects; }
   public List<ProjectMembersEntity> getProjectMemberships() { return this.projectMemberships; }
   public List<TasksEntity> getTasks() { return this.tasks; }
   public List<CommentEntity> getComments() { return this.comments; }
   public String getName() { return this.name; }
   public String getEmail() { return this.email; }
   public String getPassword() { return this.password; }
   public String getProfileImageId() { return this.profileImageId; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setOwnedOrganizations(List<OrganizationEntity> ownedOrganizations) { this.ownedOrganizations = ownedOrganizations; }
   public void setOrganizationMemberships(List<OrganizationMembersEntity> organizationMemberships) { this.organizationMemberships = organizationMemberships; }   
   public void setOwnedProjects(List<ProjectEntity> ownedProjects) { this.ownedProjects = ownedProjects; }
   public void setProjectMemberships(List<ProjectMembersEntity> projectMemberships) { this.projectMemberships = projectMemberships; }   
   public void setTasks(List<TasksEntity> tasks) { this.tasks = tasks; }
   public void setComments(List<CommentEntity> comments) { this.comments = comments; }
   public void setName(String name) { this.name = name; }
   public void setEmail(String email) { this.email = email; }
   public void setPassword(String password) { this.password = password; }
   public void setProfileImageId(String profileImageId) { this.profileImageId = profileImageId; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}