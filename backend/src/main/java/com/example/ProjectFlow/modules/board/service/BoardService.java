
// packages
package com.example.ProjectFlow.modules.board.service;

// imports
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.board.repository.BoardRepository;

// import validator
import com.example.ProjectFlow.modules.board.validator.BoardValidator;
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardResponseDTO;
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
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


   // get all
   public List<BoardResponseDTO> getAll() {
      List<BoardResponseDTO> boards = this.boardRepository.getAll();

      if(boards.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadros Kanban não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return boards;
   }


   // get by id
   public BoardResponseDTO getById(UUID id) {
      this.boardValidator.idValidate(id);

      try {
         return this.boardRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public BoardEntity getEntityById(UUID id) {
      this.boardValidator.idValidate(id);

      try {
         return this.boardRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get board by project id
   public BoardResponseDTO getByProjectId(UUID projectId) {
      this.boardValidator.projectIdValidate(projectId);

      try {
         return this.boardRepository.getByProjectId(projectId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.boardValidator.idValidate(id);

      boolean exist = this.boardRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // update board name
   @Transactional
   public BoardResponseDTO updateName(UUID id, String name) {
      this.boardValidator.idValidate(id);
      this.boardValidator.nameValidate(name);

      try {
         return this.boardRepository.updateName(id, name);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete board
   @Transactional
   public BoardDeletedDTO delete(UUID id) {
      this.boardValidator.idValidate(id);

      try {
         return this.boardRepository.delete(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      boardValidator.idValidate(id);

      try {
         return this.boardRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}