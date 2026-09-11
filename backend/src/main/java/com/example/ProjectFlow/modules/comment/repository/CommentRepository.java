
// packages
package com.example.ProjectFlow.modules.comment.repository;

// imports
import java.time.LocalDateTime;
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
   public CommentEntity create(
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

      return comment;
   }


   // get all
   public List<CommentEntity> getAll() {
      List<CommentEntity> commentsEntity = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c ORDER BY c.createdAt ASC", CommentEntity.class)
         .getResultList();

      return commentsEntity;
   }


   // get by id
   public CommentEntity getById(UUID id) throws NoResultException {
      CommentEntity comment = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return comment;
   }


   // get entity by id
   public CommentEntity getEntityById(UUID id) throws NoResultException {
      CommentEntity comment = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return comment;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(c) FROM CommentEntity c WHERE c.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // get comments by task id
   public List<CommentEntity> getByTaskId(UUID taskId) {
      List<CommentEntity> commentsEntity = this.entityManager
         .createQuery(
            "SELECT c FROM CommentEntity c " +
            "WHERE c.task.id = :taskId " +
            "ORDER BY c.createdAt ASC ", 
            CommentEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();

      return commentsEntity;
   }


   // get comments by author id
   public List<CommentEntity> getByAuthorId(UUID authorId) {
      List<CommentEntity> commentsEntity = this.entityManager
         .createQuery(
            "SELECT c FROM CommentEntity c " +
            "WHERE c.author.id = :authorId " +
            "ORDER BY c.createdAt ASC ", 
            CommentEntity.class
         )
         .setParameter("authorId", authorId)
         .getResultList();

      return commentsEntity;
   }


   // update comment content
   @Transactional
   public CommentEntity updateContent(UUID id, String content) throws NoResultException {
      CommentEntity commentEntity = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      commentEntity.setContent(content);

      return commentEntity;
   }


   // delete comment
   @Transactional
   public CommentEntity delete(UUID id) throws NoResultException {
      CommentEntity commentEntity = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      commentEntity.setDeletedAt(LocalDateTime.now());

      return commentEntity;
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      CommentEntity comment = this.entityManager
         .createQuery("SELECT c FROM CommentEntity c WHERE c.id = :id", CommentEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return comment.isDeleted();
   }

}