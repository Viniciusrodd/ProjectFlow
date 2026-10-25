
// packages
package com.example.ProjectFlow.modules.comment.unitTests;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.comment.repository.CommentRepository;
 
// import validator
import com.example.ProjectFlow.modules.comment.validator.CommentValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentDeleteDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.comment.service.CommentService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
 
// import mapper
import com.example.ProjectFlow.modules.comment.mapper.CommentMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - CommentService")
public class CommentHappyTests {
 
   // mock dependencies
   @Mock
   private CommentRepository commentRepository;
   @Mock
   private CommentValidator commentValidator;
   @Mock
   private TaskService taskService;
   @Mock
   private UserService userService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private CommentMapper commentMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private CommentService commentService;
 
 
   @Test
   @DisplayName("create() - must create a comment")
   void create() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // users - setup
      UserEntity authorEntity = new UserEntity.Builder()
         .id(authorId)
         .name("user x")
         .build();
 
      UserEntity memberUserEntity = new UserEntity.Builder()
         .id(memberUserId)
         .name("member x")
         .build();
 
      // project member - setup
      ProjectMembersEntity projectMemberEntity = new ProjectMembersEntity.Builder()
         .user(memberUserEntity)
         .build();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();
      projectEntity.setMembers(List.of(projectMemberEntity));
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .title("task x")
         .build();
 
      // comment - setup (id is not part of the builder)
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .task(taskEntity)
         .author(authorEntity)
         .content("comment x")
         .build();
 
      // DTO's - setup
      CommentDTO commentDTO = new CommentDTO(
         taskId, authorId, commentEntity.getContent()
      );
 
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .commentId(commentEntity.getId())
         .userId(commentEntity.getAuthor().getId())
         .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_CRIADO)
         .description(
            "Comentário da tarefa: " + commentEntity.getTask().getTitle() +
            ", criado por: " + commentEntity.getAuthor().getName() +
            ", no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.COMENTARIO_ADICIONADO_A_TAREFA)
         .message(
            "Comentário adicionado a tarefa: " + commentEntity.getTask().getTitle() +
            ", por: " + commentEntity.getAuthor().getName() +
            ", no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
      when(this.userService.getEntityById(authorId)).thenReturn(authorEntity);
      when(this.commentRepository.create(commentDTO, taskEntity, authorEntity)).thenReturn(commentEntity);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      CommentResponseDTO result = this.commentService.create(commentDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(commentResponseDTO);
 
      // checks
      verify(this.commentValidator, times(1)).taskIdValidate(taskId);
      verify(this.commentValidator, times(1)).authorIdValidate(authorId);
      verify(this.commentValidator, times(1)).contentValidate(commentDTO.content());
 
      verify(this.taskService, times(1)).getEntityById(taskId);
      verify(this.userService, times(1)).getEntityById(authorId);
      verify(this.commentRepository, times(1)).create(commentDTO, taskEntity, authorEntity);
 
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }


   @Test
   @DisplayName("getAll() - must return all comments")
   void getAll() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .content("comment x")
         .build();
 
      // list - setup
      List<CommentEntity> commentList = List.of(commentEntity);
 
      // DTO - setup
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      // stubs
      when(this.commentRepository.getAll()).thenReturn(commentList);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      List<CommentResponseDTO> result = this.commentService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(commentResponseDTO);
 
      // checks
      verify(this.commentRepository, times(1)).getAll();
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }
 
 
   @Test
   @DisplayName("getById() - must return comment by id")
   void getById() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .content("comment x")
         .build();
 
      // DTO - setup
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      // stubs
      when(this.commentRepository.getById(commentId)).thenReturn(commentEntity);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      CommentResponseDTO result = this.commentService.getById(commentId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(commentResponseDTO);
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).getById(commentId);
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must return comment entity by id")
   void getEntityById() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .build();
 
      // stubs
      when(this.commentRepository.getEntityById(commentId)).thenReturn(commentEntity);
 
      // act and assert
      CommentEntity result = this.commentService.getEntityById(commentId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(commentEntity);
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).getEntityById(commentId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if comment exist by id")
   void existsById() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.existsById(commentId)).thenReturn(true);
 
      // act and assert
      boolean result = this.commentService.existsById(commentId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).existsById(commentId);
   }
 
 
   @Test
   @DisplayName("getByTaskId() - must return all comments by task id")
   void getByTaskId() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .content("comment x")
         .build();
 
      // list - setup
      List<CommentEntity> commentList = List.of(commentEntity);
 
      // DTO - setup
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      // stubs
      when(this.commentRepository.getByTaskId(taskId)).thenReturn(commentList);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      List<CommentResponseDTO> result = this.commentService.getByTaskId(taskId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(commentResponseDTO);
 
      // checks
      verify(this.commentValidator, times(1)).taskIdValidate(taskId);
      verify(this.commentRepository, times(1)).getByTaskId(taskId);
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }
 
 
   @Test
   @DisplayName("getByAuthorId() - must return all comments by author id")
   void getByAuthorId() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .content("comment x")
         .build();
 
      // list - setup
      List<CommentEntity> commentList = List.of(commentEntity);
 
      // DTO - setup
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      // stubs
      when(this.commentRepository.getByAuthorId(authorId)).thenReturn(commentList);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      List<CommentResponseDTO> result = this.commentService.getByAuthorId(authorId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(commentResponseDTO);
 
      // checks
      verify(this.commentValidator, times(1)).authorIdValidate(authorId);
      verify(this.commentRepository, times(1)).getByAuthorId(authorId);
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }


   @Test
   @DisplayName("updateContent() - must update comment content")
   void updateContent() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // data to update
      String newContent = "comment atualizado";
 
      // author - setup
      UserEntity authorEntity = new UserEntity.Builder()
         .id(authorId)
         .name("user x")
         .build();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .build();
 
      // comment - setup (already updated)
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .task(taskEntity)
         .author(authorEntity)
         .content(newContent)
         .build();
 
      // DTO's - setup
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         commentId, taskId, authorId, commentEntity.getContent()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .commentId(commentEntity.getId())
         .userId(commentEntity.getAuthor().getId())
         .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_ATUALIZADO)
         .description(
            "Comentário da tarefa: " + commentEntity.getTask().getTitle() +
            ", atualizado por: " + commentEntity.getAuthor().getName()
         )
         .build();
 
      // stubs
      when(this.commentRepository.updateContent(commentId, newContent)).thenReturn(commentEntity);
      when(this.commentMapper.toCommentResponseDTO(commentEntity)).thenReturn(commentResponseDTO);
 
      // act and assert
      CommentResponseDTO result = this.commentService.updateContent(commentId, newContent);
      assertThat(result)
         .isNotNull()
         .isEqualTo(commentResponseDTO);
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentValidator, times(1)).contentValidate(newContent);
      verify(this.commentRepository, times(1)).updateContent(commentId, newContent);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.commentMapper, times(1)).toCommentResponseDTO(commentEntity);
   }
 
 
   @Test
   @DisplayName("delete() - must delete a comment")
   void delete() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // author - setup
      UserEntity authorEntity = new UserEntity.Builder()
         .id(authorId)
         .name("user x")
         .build();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .build();
 
      // comment - setup
      CommentEntity commentEntity = new CommentEntity.Builder()
         .id(commentId)
         .task(taskEntity)
         .author(authorEntity)
         .content("comment x")
         .build();
 
      // DTO's - setup
      CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(
         commentId, taskId, authorId, commentEntity.getContent(),
         LocalDateTime.now()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .commentId(commentEntity.getId())
         .userId(commentEntity.getAuthor().getId())
         .action(ActivityActionEnum.COMENTARIO_DA_TAREFA_REMOVIDO)
         .description(
            "Comentário da tarefa: " + commentEntity.getTask().getTitle() +
            ", deletado por: " + commentEntity.getAuthor().getName()
         )
         .build();
 
      // stubs
      when(this.commentRepository.delete(commentId)).thenReturn(commentEntity);
      when(this.commentMapper.toCommentDeleteDTO(commentEntity)).thenReturn(commentDeleteDTO);
 
      // act and assert
      CommentDeleteDTO result = this.commentService.delete(commentId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(commentDeleteDTO);
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).delete(commentId);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.commentMapper, times(1)).toCommentDeleteDTO(commentEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if comment is deleted")
   void isDeleted() {
      // id's - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.isDeleted(commentId)).thenReturn(true);
 
      // act and assert
      boolean result = this.commentService.isDeleted(commentId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).isDeleted(commentId);
   }

}