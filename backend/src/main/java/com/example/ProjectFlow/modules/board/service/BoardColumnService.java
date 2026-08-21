
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

// import constants


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

      // get board data
      BoardEntity board = this.boardService.getEntityById(data.boardId());

      return this.boardColumnsRepository.create(data, board);
   }

}