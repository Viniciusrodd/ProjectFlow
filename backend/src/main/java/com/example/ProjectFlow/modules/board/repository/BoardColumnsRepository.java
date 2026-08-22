
// packages
package com.example.ProjectFlow.modules.board.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsResponseDTO;

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


   // get all board columns
   public List<BoardColumnsResponseDTO> getAllColumnsByBoardId(UUID boardId) {
      List<BoardColumnsEntity> columns = this.entityManager
         .createQuery("SELECT c FROM BoardColumnsEntity c WHERE c.board.id = :boardId", BoardColumnsEntity.class)
         .setParameter("boardId", boardId)
         .getResultList();

      List<BoardColumnsResponseDTO> allColumns = new ArrayList<>();

      for(BoardColumnsEntity column : columns) {
         allColumns.add(BoardColumnsResponseDTO.get(column));
      }

      return allColumns;
   }   

}