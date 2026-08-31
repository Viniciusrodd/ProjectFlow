
// packages
package com.example.ProjectFlow.modules.project.entity;

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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;


@Entity
@Table(name = "projects")
@SQLRestriction("deleted_at IS NULL")
public class ProjectEntity implements SoftDeleteInterface {
   
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(projects) : 1(organization)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "organization_id", nullable = false)
   private OrganizationEntity organization;
   
   // N(projects) : 1(owner user)
   @ManyToOne(fetch = FetchType.LAZY) 
   @JoinColumn(name = "owner_id", nullable = false)
   private UserEntity owner;

   // 1(project) : N(project_members)
   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<ProjectMembersEntity> members = new ArrayList<>();

   // 1(project) : 1(board)
   @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private BoardEntity board;

   // 1(project) : N(tasks)
   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<TasksEntity> tasks = new ArrayList<>();

   // 1(project) : N(labels)
   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<LabelsEntity> labels = new ArrayList<>();
   

   //// fields
 

   @Column(nullable = false, length = 120)
   private String name;

   @Column(length = 700)
   private String description;
   
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private StatusEnum status;

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


   // constructor - empty
   public ProjectEntity() {}


   // constructor - builder
   public ProjectEntity(Builder builder) {
      setOrganization(builder.organization);
      setOwner(builder.owner);
      setName(builder.name);
      setDescription(builder.description);
      setStatus(builder.status);
   }


   // getters
   public UUID getId() { return this.id; }
   public OrganizationEntity getOrganization() { return this.organization; }
   public UUID getOrganizationId() { return this.organization.getId(); }
   public UserEntity getOwner() { return this.owner; }
   public UUID getOwnerId() { return this.owner.getId(); }
   public List<ProjectMembersEntity> getMembers() { return this.members; }
   public BoardEntity getBoard() { return this.board; }
   public List<TasksEntity> getTasks() { return this.tasks; }
   public List<LabelsEntity> getLabels() { return this.labels; }
   public String getName() { return this.name; }
   public String getDescription() { return this.description; }
   public StatusEnum getStatus() { return this.status; }
   public String getLogoImageId() { return this.logoImageId; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setOrganization(OrganizationEntity organization) { this.organization = organization; }
   public void setOwner(UserEntity owner) { this.owner = owner; }
   public void setMembers(List<ProjectMembersEntity> members) { this.members = members; }
   public void setBoard(BoardEntity board) { this.board = board; }
   public void setTasks(List<TasksEntity> tasks) { this.tasks = tasks; }
   public void setLabels(List<LabelsEntity> labels) { this.labels = labels; }
   public void setName(String name) { this.name = name; }
   public void setDescription(String description) { this.description = description; }
   public void setStatus(StatusEnum status) { this.status = status; }
   public void setLogoImageId(String logoImageId) { this.logoImageId = logoImageId; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }


   //// builder 


   public static class Builder {
      private OrganizationEntity organization;
      private UserEntity owner;
      private String name;
      private String description;
      private StatusEnum status;

      public Builder organization(OrganizationEntity organization) {
         this.organization = organization;
         return this;
      }

      public Builder owner(UserEntity owner) {
         this.owner = owner;
         return this;
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder status(StatusEnum status) {
         this.status = status;
         return this;
      }

      public ProjectEntity build() {
         return new ProjectEntity(this);
      }

   }

}