
// packages
package com.example.ProjectFlow.modules.board.entity;

// imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
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
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDelete;

// import entities
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;


@Entity
@Table(name = "board_columns")
@SQLRestriction("deleted_at IS NULL")
public class BoardColumnsEntity implements SoftDelete {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

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


   // constructor
   protected BoardColumnsEntity() {}


   // getters
   public Long getId() { return this.id; }
   public BoardEntity getBoard() { return this.board; }
   public List<TasksEntity> getTasks() { return this.tasks; }
   public BoardEnum getName() { return this.name; } 
   public int getPosition() { return this.position; } 
   public String getColor() { return this.color; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }

   
   // setters
   public void setId(Long id) { this.id = id; }
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

}