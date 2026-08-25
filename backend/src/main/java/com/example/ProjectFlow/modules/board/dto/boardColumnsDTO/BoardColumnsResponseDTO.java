
// packages
package com.example.ProjectFlow.modules.board.dto.boardColumnsDTO;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;


public record BoardColumnsResponseDTO (

   UUID id,
   UUID boardId,
   BoardEnum name,
   Integer position,
   String color

) {

   public static BoardColumnsResponseDTO get(BoardColumnsEntity document) {
      return new BoardColumnsResponseDTO(
         document.getId(),
         document.getBoardId(),
         document.getName(),
         document.getPosition(),
         document.getColor()
      );
   }

}