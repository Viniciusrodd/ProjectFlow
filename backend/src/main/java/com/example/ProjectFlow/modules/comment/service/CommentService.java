
// packages
package com.example.ProjectFlow.modules.comment.service;

// imports
import java.util.ArrayList;
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
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentDeleteDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.comment.mapper.CommentMapper;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


@Service
public class CommentService {
   
   // properties
   private final CommentRepository commentRepository;
   private final CommentValidator commentValidator;
   private final TaskService taskService;
   private final UserService userService;
   private final ActivityLogService activityLogService;
   private final NotificationService notificationService;
   private final CommentMapper commentMapper;


   // constructor - dependency injection
   public CommentService(
      CommentRepository commentRepository,
      CommentValidator commentValidator,
      TaskService taskService,
      UserService userService,
      ActivityLogService activityLogService,
      NotificationService notificationService,
      CommentMapper commentMapper
   ) {
      this.commentRepository = commentRepository;
      this.commentValidator = commentValidator;
      this.taskService = taskService;
      this.userService = userService;
      this.activityLogService = activityLogService;
      this.notificationService = notificationService;
      this.commentMapper = commentMapper;
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

      // creation
      CommentEntity commentEntity = this.commentRepository.create(data, task, user);

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
         .commentId(commentEntity.getId())
         .userId(commentEntity.getAuthor().getId())
         .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_CRIADO)
         .description(
            "Comentário da tarefa: " + commentEntity.getTask().getTitle() + 
            ", criado por: " + commentEntity.getAuthor().getName() +
            ", no projeto: " + task.getProject().getName()
         )
         .build();

      this.activityLogService.create(activityLog);

      // notification - registering
      for(ProjectMembersEntity notifyMember : task.getProject().getMembers()) {
         NotificationDTO notification = new NotificationDTO.Builder()
            .userId(notifyMember.getUser().getId().toString())
            .title(NotificationTitleEnum.COMENTARIO_ADICIONADO_A_TAREFA)
            .message(
               "Comentário adicionado a tarefa: " + commentEntity.getTask().getTitle() + 
               ", por: " + commentEntity.getAuthor().getName() +
               ", no projeto: " + task.getProject().getName()
            )
            .build();
         
         this.notificationService.create(notification);
      }

      return this.commentMapper.toCommentResponseDTO(commentEntity);
   }


   // get all
   public List<CommentResponseDTO> getAll() {
      List<CommentEntity> commentsEntity = this.commentRepository.getAll();

      if(commentsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<CommentResponseDTO> comments = new ArrayList<>();
      for(CommentEntity comment : commentsEntity) {
         comments.add(this.commentMapper.toCommentResponseDTO(comment));
      }

      return comments;
   }


   // get by id
   public CommentResponseDTO getById(UUID id) {
      this.commentValidator.idValidate(id);

      try {
         CommentEntity commentEntity = this.commentRepository.getById(id);

         return this.commentMapper.toCommentResponseDTO(commentEntity);
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


   // get comments by task id
   public List<CommentResponseDTO> getByTaskId(UUID taskId) {
      this.commentValidator.taskIdValidate(taskId);

      List<CommentEntity> commentsEntity = this.commentRepository.getByTaskId(taskId);

      if(commentsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<CommentResponseDTO> comments = new ArrayList<>();
      for(CommentEntity comment : commentsEntity) {
         comments.add(this.commentMapper.toCommentResponseDTO(comment));
      }

      return comments;
   }


   // get comments by author id
   public List<CommentResponseDTO> getByAuthorId(UUID authorId) {
      this.commentValidator.authorIdValidate(authorId);

      List<CommentEntity> commentsEntity = this.commentRepository.getByAuthorId(authorId);

      if(commentsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<CommentResponseDTO> comments = new ArrayList<>();
      for(CommentEntity comment : commentsEntity) {
         comments.add(this.commentMapper.toCommentResponseDTO(comment));
      }

      return comments;
   }


   // update comment content
   @Transactional
   public CommentResponseDTO updateContent(UUID id, String content) {
      this.commentValidator.idValidate(id);
      this.commentValidator.contentValidate(content);

      try {
         CommentEntity commentEntity = this.commentRepository.updateContent(id, content);

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .commentId(commentEntity.getId())
            .userId(commentEntity.getAuthor().getId())
            .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_ATUALIZADO)
            .description(
               "Comentário da tarefa: " + commentEntity.getTask().getTitle() + 
               ", atualizado por: " + commentEntity.getAuthor().getName()
            )
            .build();

         this.activityLogService.create(activityLog);

         return this.commentMapper.toCommentResponseDTO(commentEntity);
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
         CommentEntity commentEntity = this.commentRepository.delete(id);

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .commentId(commentEntity.getId())
            .userId(commentEntity.getAuthor().getId())
            .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_REMOVIDO)
            .description(
               "Comentário da tarefa: " + commentEntity.getTask().getTitle() + 
               ", deletado por: " + commentEntity.getAuthor().getName()
            )
            .build();

         this.activityLogService.create(activityLog);

         return this.commentMapper.toCommentDeleteDTO(commentEntity);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.commentValidator.idValidate(id);

      try {
         return this.commentRepository.isDeleted(id);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Comentário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}