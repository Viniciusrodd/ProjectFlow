
// packages
package com.example.ProjectFlow.modules.board.service;

// imports
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.board.repository.BoardColumnsRepository;

// import validator
import com.example.ProjectFlow.modules.board.validator.BoardColumnsValidator;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsResponseDTO;

// import service

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;

// import enums

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class BoardColumnService {
 
   // properties
   private final BoardColumnsRepository boardColumnsRepository;
   private final BoardColumnsValidator boardColumnsValidator;
   private final BoardService boardService;
   
   // constructor - dependency injection  
   public BoardColumnService( 
      BoardColumnsRepository boardColumnsRepository,
      BoardColumnsValidator boardColumnsValidator,
      BoardService boardService
   ) {
      this.boardColumnsRepository = boardColumnsRepository;
      this.boardColumnsValidator = boardColumnsValidator;
      this.boardService = boardService;
   }


   // create board column
   @Transactional
   public BoardColumnsResponseDTO create(BoardColumnsDTO data) {
      this.boardColumnsValidator.boardIdValidate(data.boardId());
      this.boardColumnsValidator.nameValidate(data.name());
      this.boardColumnsValidator.positionValidate(data.position());
      this.boardColumnsValidator.colorValidate(data.color());

      // check column name existence
      this.checkColumnNameExistence(data.name());

      // check column position existence
      this.checkColumnPositionExistence(data.position());

      // get board data
      BoardEntity board = this.boardService.getEntityById(data.boardId());

      return this.boardColumnsRepository.create(data, board);
   }


   // check if column name already exist
   public void checkColumnNameExistence(String name) {
      boolean exist = this.boardColumnsRepository.checkColumnNameExistence(name);
      if(exist) {
         throw MultiExceptions.invalid(String.format(
            "%s: A coluna '%s' já existe",
            ResponseMessages.INVALID_DATA,
            name
         ));
      }
   }


   // check if column position already exist
   public void checkColumnPositionExistence(int position) {
      boolean exist = this.boardColumnsRepository.checkColumnPositionExistence(position);
      if(exist) {
         throw MultiExceptions.invalid(String.format(
            "%s: A coluna de posição '%s' já existe",
            ResponseMessages.INVALID_DATA,
            position
         ));
      }
   }

}