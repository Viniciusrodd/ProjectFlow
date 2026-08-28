
// packages
package com.example.ProjectFlow.modules.comment.service;

// imports
import org.springframework.stereotype.Service;

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
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions

// import constants


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

}