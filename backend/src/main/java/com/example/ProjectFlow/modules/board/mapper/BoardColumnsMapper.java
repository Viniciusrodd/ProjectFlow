
// packages
package com.example.ProjectFlow.modules.board.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDeletedDTO;
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsResponseDTO;


@Component
public class BoardColumnsMapper {
 
   // from BoardColumnsEntity to BoardColumnsResponseDTO
   public BoardColumnsResponseDTO toBoardColumnsResponseDTO(BoardColumnsEntity entity) {
      return new BoardColumnsResponseDTO(
         entity.getId(),
         entity.getBoardId(),
         entity.getName(),
         entity.getPosition(),
         entity.getColor()
      );
   }


   // from BoardColumnsEntity to BoardColumnsDeletedDTO
   public BoardColumnsDeletedDTO toBoardColumnsDeletedDTO(BoardColumnsEntity entity) {
      return new BoardColumnsDeletedDTO(
         entity.getId(),
         entity.getBoardId(),
         entity.getName(),
         entity.getPosition(),
         entity.getColor(),
         entity.getDeletedAt()
      );
   }

}