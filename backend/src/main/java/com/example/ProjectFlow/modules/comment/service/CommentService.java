
// packages
package com.example.ProjectFlow.modules.comment.service;

// imports
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import jakarta.persistence.NoResultException;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.comment.repository.CommentRepository;

// import validator
import com.example.ProjectFlow.modules.comment.validator.CommentValidator;

// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentDeleteDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class CommentService {
   
   // properties
   private final CommentRepository commentRepository;
   private final CommentValidator commentValidator;
   private final TaskService taskService;
   private final UserService userService;


   // constructor - dependency injection
   public CommentService(
      CommentRepository commentRepository,
      CommentValidator commentValidator,
      TaskService taskService,
      UserService userService
   ) {
      this.commentRepository = commentRepository;
      this.commentValidator = commentValidator;
      this.taskService = taskService;
      this.userService = userService;
   }


   // comment creation
   @Transactional
   public CommentResponseDTO create(CommentDTO data) {
      this.commentValidator.taskIdValidate(data.taskId());
      this.commentValidator.authorIdValidate(data.authorId());
      this.commentValidator.contentValidate(data.content());

      // get task data
      TasksEntity task = this.taskService.getEntityById(data.taskId());

      // get author data
      UserEntity user = this.userService.getEntityById(data.authorId());

      return this.commentRepository.create(data, task, user);
   }


   // get all
   public List<CommentResponseDTO> getAll() {
      List<CommentResponseDTO> comments = this.commentRepository.getAll();

      if(comments.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return comments;
   }


   // get by id
   public CommentResponseDTO getById(UUID id) {
      this.commentValidator.idValidate(id);

      try {
         return this.commentRepository.getById(id);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public CommentEntity getEntityById(UUID id) {
      this.commentValidator.idValidate(id);

      try {
         return this.commentRepository.getEntityById(id);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get comments by task id
   public List<CommentResponseDTO> getByTaskId(UUID taskId) {
      this.commentValidator.taskIdValidate(taskId);

      List<CommentResponseDTO> comments = this.commentRepository.getByTaskId(taskId);

      if(comments.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return comments;
   }


   // get comments by author id
   public List<CommentResponseDTO> getByAuthorId(UUID authorId) {
      this.commentValidator.authorIdValidate(authorId);

      List<CommentResponseDTO> comments = this.commentRepository.getByAuthorId(authorId);

      if(comments.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return comments;
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.commentValidator.idValidate(id);

      boolean exist = this.commentRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // update comment content
   @Transactional
   public CommentResponseDTO updateContent(UUID id, String content) {
      this.commentValidator.idValidate(id);
      this.commentValidator.contentValidate(content);

      try {
         return this.commentRepository.updateContent(id, content);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete comment
   @Transactional
   public CommentDeleteDTO delete(UUID id) {
      this.commentValidator.idValidate(id);

      try {
         return this.commentRepository.delete(id);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}