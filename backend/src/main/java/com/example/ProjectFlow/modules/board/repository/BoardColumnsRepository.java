
// packages
package com.example.ProjectFlow.modules.board.repository;

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

}