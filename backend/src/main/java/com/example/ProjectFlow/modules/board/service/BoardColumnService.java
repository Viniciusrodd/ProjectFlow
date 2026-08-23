
// packages
package com.example.ProjectFlow.modules.board.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.board.repository.BoardColumnsRepository;

// import validator
import com.example.ProjectFlow.modules.board.validator.BoardColumnsValidator;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsResponseDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsUpdateDTO;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;

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


   // get all board columns by board id
   public List<BoardColumnsResponseDTO> getAllColumnsByBoardId(UUID boardId) {
      this.boardColumnsValidator.boardIdValidate(boardId);

      // board existence - check
      this.boardService.existsById(boardId);

      List<BoardColumnsResponseDTO> columns = this.boardColumnsRepository.getAllColumnsByBoardId(boardId);

      if(columns.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Colunas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return columns;
   }


   // get board column by id
   public BoardColumnsResponseDTO getColumnById(UUID id) {
      this.boardColumnsValidator.idValidate(id);

      try {
         return this.boardColumnsRepository.getColumnById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Coluna não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update board column
   @Transactional
   public BoardColumnsResponseDTO update(UUID id, BoardColumnsUpdateDTO data) {
      this.boardColumnsValidator.idValidate(id);
      this.boardColumnsValidator.updateValidations(data);

      // check column name existence
      if(data.name() != null) this.checkColumnNameExistence(data.name());

      // check column position existence
      if(data.position() != null) this.checkColumnPositionExistence(data.position()); 

      try {
         return this.boardColumnsRepository.update(id, data);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Coluna não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete board column
   @Transactional
   public BoardColumnsDeletedDTO delete(UUID id) {
      this.boardColumnsValidator.idValidate(id);

      try {
         return this.boardColumnsRepository.delete(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Coluna não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.boardColumnsValidator.idValidate(id);

      try {
         return this.boardColumnsRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Coluna não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}