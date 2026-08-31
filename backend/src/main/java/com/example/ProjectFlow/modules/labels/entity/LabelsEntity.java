
// packages
package com.example.ProjectFlow.modules.labels.entity;

// imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;


@Entity
@Table(name = "labels")
@SQLRestriction("deleted_at IS NULL")
public class LabelsEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(labels) : 1(project)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "project_id", nullable = false)
   private ProjectEntity project;

   // 1(label) : N(task_labels)
   @OneToMany(mappedBy = "label", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<TaskLabelsEntity> tasks = new ArrayList<>();

   
   //// fields

   
   @Column(nullable = false, length = 60)
   private String name;

   @Column(nullable = false, length = 20)
   private String color;

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor - empty
   public LabelsEntity() {}


   // constructor - builder
   public LabelsEntity(Builder builder) {
      setProject(builder.project);
      setName(builder.name);
      setColor(builder.color);
   }


   // getters
   public UUID getId() { return this.id; }
   public ProjectEntity getProject() { return this.project; }
   public UUID getProjectId() { return this.project.getId(); }
   public List<TaskLabelsEntity> getTasks() { return this.tasks; }
   public String getName() { return this.name; }
   public String getColor() { return this.color; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setProject(ProjectEntity project) { this.project = project; }
   public void setTasks(List<TaskLabelsEntity> tasks) { this.tasks = tasks; }
   public void setName(String name) { this.name = name; }
   public void setColor(String color) { this.color = color; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }


   //// builder


   public static class Builder {
      private ProjectEntity project;
      private String name;
      private String color;

      public Builder project(ProjectEntity project) {
         this.project = project;
         return this;
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder color(String color) {
         this.color = color;
         return this;
      }

      public LabelsEntity build() {
         return new LabelsEntity(this);
      }
   }

}