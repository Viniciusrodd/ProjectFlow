
// packages
package com.example.ProjectFlow.modules.user.entity;

// imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
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
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDelete;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL") // auto. filter not deleted registers
public class UserEntity implements SoftDelete {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // 1(owner user) : N(organizations)
   @OneToMany(mappedBy = "owner")
   private List<OrganizationEntity> ownedOrganizations = new ArrayList<>();

   // 1(user member) : N(organization_members)
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<OrganizationMembersEntity> organizationMemberships = new ArrayList<>();

   // 1(owner user) : N(projects)
   @OneToMany(mappedBy = "owner")
   private List<ProjectEntity> ownedProjects = new ArrayList<>();

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
   protected UserEntity() {}

   // getters
   public Long getId() { return this.id; }
   public List<OrganizationEntity> getOwnedOrganizations() { return this.ownedOrganizations; }
   public List<OrganizationMembersEntity> getOrganizationMemberships() { return this.organizationMemberships; }
   public List<ProjectEntity> getOwnedProjects() { return this.ownedProjects; }
   public String getName() { return this.name; }
   public String getEmail() { return this.email; }
   public String getPassword() { return this.password; }
   public String getProfileImageId() { return this.profileImageId; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }

   // setters
   public void setId(Long id) { this.id = id; }
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