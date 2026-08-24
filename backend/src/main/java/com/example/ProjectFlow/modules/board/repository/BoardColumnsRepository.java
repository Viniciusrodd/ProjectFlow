
// packages
package com.example.ProjectFlow.modules.board.repository;

// imports
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsResponseDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsUpdateDTO;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.board.entity.BoardEntity;

// import enum
import com.example.ProjectFlow.modules.board.enums.BoardEnum;


@Repository
public class BoardColumnsRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // create board column
   @Transactional
   public BoardColumnsResponseDTO create(
      BoardColumnsDTO data,
      BoardEntity boardeEntity
   ) {
      BoardColumnsEntity boardColumns = new BoardColumnsEntity.Builder()
         .board(boardeEntity)
         .name(BoardEnum.valueOf(data.name().trim().toUpperCase().replace(" ", "")))
         .position(data.position())
         .color(data.color())
         .build();

      this.entityManager.persist(boardColumns);

      return BoardColumnsResponseDTO.get(boardColumns);
   }


   // check if column name already exist
   public boolean checkColumnNameExistence(String name) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(c) FROM BoardColumnsEntity c WHERE c.name = :name", Long.class)
         .setParameter("name", BoardEnum.valueOf(name.trim().toUpperCase().replace(" ", "")))
         .getSingleResult();

      return count > 0;
   }


   // check if column position already exist
   public boolean checkColumnPositionExistence(int position) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(c) FROM BoardColumnsEntity c WHERE c.position = :position", Long.class)
         .setParameter("position", position)
         .getSingleResult();

      return count > 0;
   }


   // get all board columns by board id
   public List<BoardColumnsResponseDTO> getAllColumnsByBoardId(UUID boardId) {
      List<BoardColumnsEntity> columns = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.board.id = :boardId ORDER BY c.position ASC", BoardColumnsEntity.class)
         .setParameter("boardId", boardId)
         .getResultList();

      List<BoardColumnsResponseDTO> allColumns = new ArrayList<>();

      for(BoardColumnsEntity column : columns) {
         allColumns.add(BoardColumnsResponseDTO.get(column));
      }

      return allColumns;
   }


   // get board column by id
   public BoardColumnsResponseDTO getColumnById(UUID id) throws NoResultException {
      BoardColumnsEntity column = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.id = :id", BoardColumnsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return BoardColumnsResponseDTO.get(column);
   }


   // get entity by id
   public BoardColumnsEntity getEntityById(UUID id) throws NoResultException {
      BoardColumnsEntity column = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.id = :id", BoardColumnsEntity.class)
         .setParameter("id", id)
         .getSingleResult();
      
      return column;
   }


   // update board column
   @Transactional
   public BoardColumnsResponseDTO update(UUID id, BoardColumnsUpdateDTO data) throws NoResultException {
      BoardColumnsEntity column = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.id = :id", BoardColumnsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.name()).ifPresent(name -> 
         column.setName(BoardEnum.valueOf(name.trim().toUpperCase().replace(" ", "")))
      );
      Optional.ofNullable(data.position()).ifPresent(position -> column.setPosition(position));
      Optional.ofNullable(data.color()).ifPresent(color -> column.setColor(color));

      return BoardColumnsResponseDTO.get(column);
   }


   // delete board column
   @Transactional
   public BoardColumnsDeletedDTO delete(UUID id) throws NoResultException {
      BoardColumnsEntity column = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.id = :id", BoardColumnsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      column.setDeletedAt(LocalDateTime.now());

      return BoardColumnsDeletedDTO.get(column);
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      BoardColumnsEntity column = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.id = :id", BoardColumnsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return column.isDeleted();
   }

}