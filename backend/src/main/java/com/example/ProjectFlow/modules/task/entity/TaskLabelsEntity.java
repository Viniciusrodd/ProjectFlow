
// packages
package com.example.ProjectFlow.modules.task.entity;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

// jakarta imports
import jakarta.persistence.Entity;
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


@Entity
@Table(
   name = "task_labels", 
   uniqueConstraints = { @UniqueConstraint(columnNames = { "task_id", "label_id" }) } // (task + label id) combination must be unique
)
@SQLRestriction("deleted_at IS NULL")
public class TaskLabelsEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(tasks) : 1(task_labels)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "task_id", nullable = false)
   private TasksEntity task;

   // N(labels) : 1(task_labels)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "label_id", nullable = false)
   private LabelsEntity label;


   //// fields
   

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor
   protected TaskLabelsEntity() {}


   // getters
   public UUID getId() { return this.id; }
   public TasksEntity getTask() { return this.task; }
   public LabelsEntity getLabel() { return this.label; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setTask(TasksEntity task) { this.task = task; }
   public void setLabel(LabelsEntity label) { this.label = label; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
   

   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}