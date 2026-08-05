
// packages
package com.example.ProjectFlow.modules.board.entity;

// imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


@Entity
@Table(name = "board")
@SQLRestriction("deleted_at IS NULL")
public class BoardEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "UUID")
   private UUID id;

   // 1(project) : 1(board)
   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "project_id", nullable = false)
   private ProjectEntity project;

   // 1(board) : N(board_columns)
   @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<BoardColumnsEntity> boardColumns = new ArrayList<>();


   //// fields


   @Column(nullable = false, length = 120)
   private String name;

   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // getters
   public UUID getId() { return this.id; }
   public ProjectEntity getProject() { return this.project; }
   public List<BoardColumnsEntity> getBoardColumns() { return this.boardColumns; }
   public String getName() { return this.name; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setProject(ProjectEntity project) { this.project = project; }
   public void setBoardColumns(List<BoardColumnsEntity> boardColumns) { this.boardColumns = boardColumns; }
   public void setName(String name) { this.name = name; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}