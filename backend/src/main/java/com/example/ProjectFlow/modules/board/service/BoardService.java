
// packages
package com.example.ProjectFlow.modules.board.service;

// imports
import java.util.List;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.board.repository.BoardRepository;

// import validator
import com.example.ProjectFlow.modules.board.validator.BoardValidator;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.BoardResponseDTO;

// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class BoardService {
 
   // properties
   private final BoardRepository boardRepository;
   private final BoardValidator boardValidator;
   private final ProjectService projectService;


   // constructor - dependency injection
   public BoardService(
      BoardRepository boardRepository,
      BoardValidator boardValidator,
      ProjectService projectService
   ) {
      this.boardRepository = boardRepository;
      this.boardValidator = boardValidator;
      this.projectService = projectService;
   }


   // board creation
   @Transactional
   public BoardResponseDTO create(BoardDTO data) {
      this.boardValidator.projectIdValidate(data.projectId());
      this.boardValidator.nameValidate(data.name());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      return this.boardRepository.create(data, project);
   }


   // get all boards
   public List<BoardResponseDTO> getAll() {
      List<BoardResponseDTO> boards = this.boardRepository.getAll();

      if(boards.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadros kanban não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return boards;
   }

}