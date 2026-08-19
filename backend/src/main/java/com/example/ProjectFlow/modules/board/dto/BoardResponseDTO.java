
// packages
package com.example.ProjectFlow.modules.board.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;


public record BoardResponseDTO (

   UUID id,
   UUID projectId,
   String name
   
) {

   public static BoardResponseDTO get(BoardEntity document) {
      return new BoardResponseDTO(
         document.getId(),
         document.getProjectId(),
         document.getName()
      );
   }

}