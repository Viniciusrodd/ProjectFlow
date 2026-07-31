
// packages
package com.example.ProjectFlow.modules.task.entity;

// imports
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;


@Entity
@Table(name = "task_checklist")
@SQLRestriction("deleted_at IS NULL")
public class TaskChecklistEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   // N(task_checklist) : 1(task)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "task_id", nullable = false)
   private TasksEntity task;


   //// fields


   @Column(length = 700, nullable = false)
   private String description;

   @Column(nullable = false)
   private boolean completed;

   @Column(nullable = false)
   private int position;

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
   
   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor
   protected TaskChecklistEntity() {}


   // getters
   public Long getId() { return this.id; }
   public TasksEntity getTask() { return this.task; }
   public String getDescription() { return this.description; }
   public boolean getCompleted() { return this.completed; }
   public int getPosition() { return this.position; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; } 
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(Long id) { this.id = id; }
   public void setDescription(String description) { this.description = description; }
   public void setCompleted(boolean completed) { this.completed = completed; }
   public void setPosition(int position) { this.position = position; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}