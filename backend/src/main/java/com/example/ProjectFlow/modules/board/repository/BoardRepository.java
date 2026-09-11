
// packages
package com.example.ProjectFlow.modules.board.repository;

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
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDTO;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


@Repository
public class BoardRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // board creation
   @Transactional
   public BoardEntity create(BoardDTO data, ProjectEntity projectEntity) {
      BoardEntity board = new BoardEntity.Builder()
         .project(projectEntity)
         .name(data.name())
         .build();

      this.entityManager.persist(board);

      return board;
   }


   // get all
   public List<BoardEntity> getAll() {
      List<BoardEntity> boardsEntity = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b ORDER BY b.createdAt ASC", BoardEntity.class)
         .getResultList();

      return boardsEntity;
   }


   // get by id
   public BoardEntity getById(UUID id) throws NoResultException {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return board;
   }


   // get entity by id
   public BoardEntity getEntityById(UUID id) throws NoResultException {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return board;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(b) FROM BoardEntity b WHERE b.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // get board by project id
   public BoardEntity getByProjectId(UUID projectId) {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.project.id = :projectId", BoardEntity.class)
         .setParameter("projectId", projectId)
         .getSingleResult();

      return board;
   }


   // update board name
   @Transactional
   public BoardEntity updateName(UUID id, String name) {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      board.setName(name);

      return board;
   }


   // delete board
   @Transactional
   public BoardEntity delete(UUID id) throws NoResultException {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      board.setDeletedAt(LocalDateTime.now());

      return board;
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return board.isDeleted();
   }


}