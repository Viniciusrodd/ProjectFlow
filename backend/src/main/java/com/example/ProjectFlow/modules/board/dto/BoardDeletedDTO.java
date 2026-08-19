
// packages
package com.example.ProjectFlow.modules.board.dto;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;


public record BoardDeletedDTO (

   UUID id,
   UUID projectId,
   String name,
   LocalDateTime deletedAt

) {

   public static BoardDeletedDTO get(BoardEntity document) {
      return new BoardDeletedDTO(
         document.getId(),
         document.getProjectId(),
         document.getName(),
         document.getDeletedAt()
      );
   }

}