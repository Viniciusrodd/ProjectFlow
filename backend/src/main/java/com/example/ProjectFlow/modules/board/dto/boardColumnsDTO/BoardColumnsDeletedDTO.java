
// packages
package com.example.ProjectFlow.modules.board.dto.boardColumnsDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;


public record BoardColumnsDeletedDTO (

   UUID id,
   UUID boardId,
   BoardEnum name,
   Integer position,
   String color,
   LocalDateTime deletedAt

) {

   public static BoardColumnsDeletedDTO get(BoardColumnsEntity document) {
      return new BoardColumnsDeletedDTO(
         document.getId(),
         document.getBoardId(),
         document.getName(),
         document.getPosition(),
         document.getColor(),
         document.getDeletedAt()
      );
   }

}