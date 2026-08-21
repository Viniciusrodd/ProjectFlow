
// packages
package com.example.ProjectFlow.modules.board.entity;

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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;


@Entity
@Table(name = "board_columns")
@SQLRestriction("deleted_at IS NULL")
public class BoardColumnsEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(board_columns) : 1(board)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "board_id", nullable = false)
   private BoardEntity board;

   // 1(board_column) : N(tasks)
   @OneToMany(mappedBy = "boardColumn", fetch = FetchType.LAZY)
   private List<TasksEntity> tasks = new ArrayList<>();


   //// fields


   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private BoardEnum name;

   @Column(nullable = false)
   private int position;

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
   public BoardColumnsEntity() {}

   
   // constructor - builder
   public BoardColumnsEntity(Builder builder) {
      setBoard(builder.board);
      setName(builder.name);
      setPosition(builder.position);
      setColor(builder.color);
   }


   // getters
   public UUID getId() { return this.id; }
   public BoardEntity getBoard() { return this.board; }
   public UUID getBoardId() { return this.board.getId(); }
   public List<TasksEntity> getTasks() { return this.tasks; }
   public BoardEnum getName() { return this.name; } 
   public int getPosition() { return this.position; } 
   public String getColor() { return this.color; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }

   
   // setters
   public void setId(UUID id) { this.id = id; }
   public void setBoard(BoardEntity board) { this.board = board; }
   public void setTasks(List<TasksEntity> tasks) { this.tasks = tasks; }
   public void setName(BoardEnum name) { this.name = name; }
   public void setPosition(int position) { this.position = position; }
   public void setColor(String color) { this.color = color; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; } 


   //// builder


   public static class Builder {
      private BoardEntity board;
      private BoardEnum name;
      private int position;
      private String color;

      public Builder board(BoardEntity board) {
         this.board = board;
         return this;
      }

      public Builder name(BoardEnum name) {
         this.name = name;
         return this;
      }

      public Builder position(int position) {
         this.position = position;
         return this;
      }

      public Builder color(String color) {
         this.color = color;
         return this;
      }

      public BoardColumnsEntity build() {
         return new BoardColumnsEntity(this);
      }
   }

}