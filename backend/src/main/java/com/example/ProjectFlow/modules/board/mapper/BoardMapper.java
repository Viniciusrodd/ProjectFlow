
// packages
package com.example.ProjectFlow.modules.board.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardResponseDTO;


@Component 
public class BoardMapper {
 
   // from BoardEntity to BoardResponseDTO
   public BoardResponseDTO toBoardResponseDTO(BoardEntity entity) {
      return new BoardResponseDTO(
         entity.getId(),
         entity.getProjectId(),
         entity.getName()
      );
   }


   // from BoardEntity to BoardDeletedDTO
   public BoardDeletedDTO toBoardDeletedDTO(BoardEntity entity) {
      return new BoardDeletedDTO(
         entity.getId(),
         entity.getProjectId(),
         entity.getName(),
         entity.getDeletedAt()
      );
   }

}