
// packages
package com.example.ProjectFlow.modules.task.entity;

// imports
import java.time.LocalDateTime;
import java.time.LocalDate;
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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;


@Entity
@Table(name = "tasks")
@SQLRestriction("deleted_at IS NULL")
public class TasksEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(tasks) : 1(project)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "project_id", nullable = false)
   private ProjectEntity project;

   // N(tasks) : 1(board_column)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "column_id", nullable = false)
   private BoardColumnsEntity boardColumn;

   // N(tasks) : 1(owner user)
   @ManyToOne(fetch = FetchType.LAZY) 
   @JoinColumn(name = "owner_id", nullable = false)
   private UserEntity owner;

   // 1(task) : N(comments)
   @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<CommentEntity> comments = new ArrayList<>(); 

   // 1(task) : N(task_labels)
   @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<TaskLabelsEntity> labels = new ArrayList<>();

   // 1(task) : N(task_checklist)
   @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<TaskChecklistEntity> checklist = new ArrayList<>();


   //// fields 


   @Column(nullable = false, length = 150)
   private String title;

   @Column(length = 700)
   private String description;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private PriorityEnum priority;

   @Column(name = "due_date" ,nullable = false)
   private LocalDate dueDate;

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "completed_at")
   private LocalDateTime completedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor - empty
   public TasksEntity() {}


   // constructor - builder
   public TasksEntity(Builder builder) {
      setProject(builder.project);
      setBoardColumn(builder.boardColumn);
      setOwner(builder.owner);
      setTitle(builder.title);
      setDescription(builder.description);
      setPriority(builder.priority);
      setDueDate(builder.dueDate);
   }


   // getters
   public UUID getId() { return this.id; }
   public ProjectEntity getProject() { return this.project; }
   public UUID getProjectId() { return this.project.getId(); }
   public BoardColumnsEntity getBoardColumn() { return this.boardColumn; }
   public UUID getBoardColumnId() { return this.boardColumn.getId(); }
   public UserEntity getOwner() { return this.owner; }
   public UUID getOwnerId() { return this.owner.getId(); }
   public List<CommentEntity> getComments() { return this.comments; }
   public List<TaskChecklistEntity> getChecklist() { return this.checklist; }
   public List<TaskLabelsEntity> getLabels() { return this.labels; }
   public String getTitle() { return this.title; }
   public String getDescription() { return this.description; }
   public PriorityEnum getPriority() { return this.priority; }
   public LocalDate getDueDate() { return this.dueDate; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getCompletedAt() { return this.completedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setProject(ProjectEntity project) { this.project = project; }
   public void setBoardColumn(BoardColumnsEntity boardColumn) { this.boardColumn = boardColumn; }
   public void setOwner(UserEntity owner) { this.owner = owner; }
   public void setComments(List<CommentEntity> comments) { this.comments = comments; }
   public void setChecklist(List<TaskChecklistEntity> checklist) { this.checklist = checklist; }
   public void setLabels(List<TaskLabelsEntity> labels) { this.labels = labels; }
   public void setTitle(String title) { this.title = title; }
   public void setDescription(String description) { this.description = description; }
   public void setPriority(PriorityEnum priority) { this.priority = priority; }
   public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }


   //// builder


   public static class Builder {
      private ProjectEntity project;
      private BoardColumnsEntity boardColumn;
      private UserEntity owner;
      private String title;
      private String description;
      private PriorityEnum priority;
      private LocalDate dueDate;

      public Builder project(ProjectEntity project) {
         this.project = project;
         return this;
      }

      public Builder boardColumn(BoardColumnsEntity boardColumn) {
         this.boardColumn = boardColumn;
         return this;
      }

      public Builder owner(UserEntity owner) {
         this.owner = owner;
         return this;
      }

      public Builder title(String title) {
         this.title = title;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder priority(PriorityEnum priority) {
         this.priority = priority;
         return this;
      }

      public Builder dueDate(LocalDate dueDate) {
         this.dueDate = dueDate;
         return this;
      }

      public TasksEntity build() {
         return new TasksEntity(this);
      }
   }

}