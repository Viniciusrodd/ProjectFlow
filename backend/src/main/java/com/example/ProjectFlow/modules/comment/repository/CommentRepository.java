
// packages
package com.example.ProjectFlow.modules.comment.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class CommentRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // comment creation
   @Transactional
   public CommentResponseDTO create(
      CommentDTO data,
      TasksEntity tasksEntity,
      UserEntity authorEntity
   ) {
      CommentEntity comment = new CommentEntity.Builder()
         .task(tasksEntity)
         .author(authorEntity)
         .content(data.content())
         .build();

      this.entityManager.persist(comment);

      return CommentResponseDTO.get(comment);
   }

}