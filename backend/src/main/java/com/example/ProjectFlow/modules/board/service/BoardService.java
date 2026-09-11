
// packages
package com.example.ProjectFlow.modules.board.service;

import java.util.ArrayList;
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

// import DTOs
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

// import mapper
import com.example.ProjectFlow.modules.board.mapper.BoardMapper;


@Service
public class BoardService {
 
   // properties
   private final BoardRepository boardRepository;
   private final BoardValidator boardValidator;
   private final ProjectService projectService;
   private final BoardMapper boardMapper;


   // constructor - dependency injection
   public BoardService(
      BoardRepository boardRepository,
      BoardValidator boardValidator,
      ProjectService projectService,
      BoardMapper boardMapper
   ) {
      this.boardRepository = boardRepository;
      this.boardValidator = boardValidator;
      this.projectService = projectService;
      this.boardMapper = boardMapper;
   }


   // board creation
   @Transactional
   public BoardResponseDTO create(BoardDTO data) {
      this.boardValidator.projectIdValidate(data.projectId());
      this.boardValidator.nameValidate(data.name());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      // creation
      BoardEntity boardEntity = this.boardRepository.create(data, project);

      return this.boardMapper.toBoardResponseDTO(boardEntity);
   }


   // get all
   public List<BoardResponseDTO> getAll() {
      List<BoardEntity> boardsEntity = this.boardRepository.getAll();
      
      if(boardsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadros Kanban não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<BoardResponseDTO> boards = new ArrayList<>();
      for(BoardEntity board : boardsEntity) {
         boards.add(this.boardMapper.toBoardResponseDTO(board));
      }

      return boards;
   }


   // get by id
   public BoardResponseDTO getById(UUID id) {
      this.boardValidator.idValidate(id);

      try {
         BoardEntity boardEntity = this.boardRepository.getById(id);

         return this.boardMapper.toBoardResponseDTO(boardEntity);
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


   // get board by project id
   public BoardResponseDTO getByProjectId(UUID projectId) {
      this.boardValidator.projectIdValidate(projectId);

      try {
         BoardEntity boardEntity = this.boardRepository.getByProjectId(projectId);

         return this.boardMapper.toBoardResponseDTO(boardEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Quadro Kanban não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update board name
   @Transactional
   public BoardResponseDTO updateName(UUID id, String name) {
      this.boardValidator.idValidate(id);
      this.boardValidator.nameValidate(name);

      try {
         BoardEntity boardEntity = this.boardRepository.updateName(id, name);

         return this.boardMapper.toBoardResponseDTO(boardEntity);
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
         BoardEntity boardEntity = this.boardRepository.delete(id);

         return this.boardMapper.toBoardDeletedDTO(boardEntity);
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