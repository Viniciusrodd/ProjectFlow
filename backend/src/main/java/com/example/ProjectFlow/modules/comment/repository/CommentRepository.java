
// packages
package com.example.ProjectFlow.modules.comment.repository;

// imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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


   // get all
   public List<CommentResponseDTO> getAll() {
      List<CommentEntity> commentsDocument = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c ORDER BY c.createdAt ASC", CommentEntity.class)
         .getResultList();

      List<CommentResponseDTO> comments = new ArrayList<>();

      for(CommentEntity comment : commentsDocument) {
         comments.add(CommentResponseDTO.get(comment));
      }

      return comments;
   }


   // get by id
   public CommentResponseDTO getById(UUID id) throws NoResultException {
      CommentEntity comment = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return CommentResponseDTO.get(comment);
   }


   // get entity by id
   public CommentEntity getEntityById(UUID id) throws NoResultException {
      CommentEntity comment = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return comment;
   }


   // get comments by task id
   public List<CommentResponseDTO> getByTaskId(UUID taskId) {
      List<CommentEntity> commentsDocument = this.entityManager
         .createQuery(
            "SELECT c FROM CommentEntity c " +
            "WHERE c.task.id = :taskId " +
            "ORDER BY c.createdAt ASC ", 
            CommentEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();

      List<CommentResponseDTO> comments = new ArrayList<>();

      for(CommentEntity comment : commentsDocument) {
         comments.add(CommentResponseDTO.get(comment));
      }

      return comments;
   }


   // get comments by author id
   public List<CommentResponseDTO> getByAuthorId(UUID authorId) {
      List<CommentEntity> commentsDocument = this.entityManager
         .createQuery(
            "SELECT c FROM CommentEntity c " +
            "WHERE c.author.id = :authorId " +
            "ORDER BY c.createdAt ASC ", 
            CommentEntity.class
         )
         .setParameter("authorId", authorId)
         .getResultList();

      List<CommentResponseDTO> comments = new ArrayList<>();

      for(CommentEntity comment : commentsDocument) {
         comments.add(CommentResponseDTO.get(comment));
      }

      return comments;
   }

}