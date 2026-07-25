
// packages
package com.example.ProjectFlow.modules.organization.entity;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDelete;

// import entities
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


@Entity
@Table(name = "organizations")
@SQLRestriction("deleted_at IS NULL")
public class OrganizationEntity implements SoftDelete {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // N(organizations) : 1(owner user)
   @ManyToOne(fetch = FetchType.LAZY) // only necessary fields -> better performance 
   @JoinColumn(name = "owner_id", nullable = false)
   private UserEntity owner;

   // 1(organization) : N(organization_members)
   @OneToMany(mappedBy = "organization")
   private List<OrganizationMembersEntity> members = new ArrayList<>();

   // 1(organization) : N(projects)
   @OneToMany(mappedBy = "organization")
   private List<ProjectEntity> projects = new ArrayList<>();

   @Column(nullable = false, length = 120)
   private String name;

   @Column(length = 700)
   private String description;

   @Column(name = "logo_image_id")
   private String logoImageId; // mongo ref.

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;

   // constructor
   protected OrganizationEntity() {}

   // getters
   public Long getId() { return this.id; }
   public UserEntity getOwner() { return this.owner; }
   public List<OrganizationMembersEntity> getMembers() { return this.members; }
   public List<ProjectEntity> getProjects() { return this.projects; }
   public String getName() { return this.name; }
   public String getDescription() { return this.description; }
   public String getLogoImageId() { return this.logoImageId; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }

   // setters
   public void setId(Long id) { this.id = id; }
   public void setName(String name) { this.name = name; }
   public void setDescription(String description) { this.description = description; }
   public void setLogoImageId(String logoImageId) { this.logoImageId = logoImageId; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}