
// packages
package com.example.ProjectFlow.modules.comment.unitTests;

// imports
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.comment.repository.CommentRepository;
 
// import validator
import com.example.ProjectFlow.modules.comment.validator.CommentValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.comment.service.CommentService;
 
// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;
 
// import mapper
import com.example.ProjectFlow.modules.comment.mapper.CommentMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// jakarta imports
import jakarta.persistence.NoResultException;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - CommentService")
public class CommentBadTests {
   
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
   @DisplayName("create() - must throw when task does not exist")
   void create() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
 
      // error message - setup
      String errorMessage = "Recurso não encontrado: Tarefa não existe";

      // DTO - setup
      CommentDTO commentDTO = new CommentDTO(
         taskId, authorId, "comment x"
      );
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.taskService).getEntityById(taskId);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.create(commentDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.commentValidator, times(1)).taskIdValidate(taskId);
      verify(this.commentValidator, times(1)).authorIdValidate(authorId);
      verify(this.commentValidator, times(1)).contentValidate(commentDTO.content());
      verify(this.taskService, times(1)).getEntityById(taskId);
 
      // no checks
      verifyNoInteractions(this.userService);
      verifyNoInteractions(this.commentRepository);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.commentMapper);
   }


   @Test
   @DisplayName("getAll() - must throw when comment list is empty")
   void getAll() {
      // list - setup
      List<CommentEntity> commentList = List.of();
 
      // stubs
      when(this.commentRepository.getAll()).thenReturn(commentList);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.getAll())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentários não existem");
 
      // checks
      verify(this.commentRepository, times(1)).getAll();
 
      // no checks
      verifyNoInteractions(this.commentMapper);
   }
 
 
   @Test
   @DisplayName("getById() - must throw comment not found")
   void getById() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.getById(commentId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.getById(commentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).getById(commentId);
 
      // no checks
      verifyNoInteractions(this.commentMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw comment not found")
   void getEntityById() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.getEntityById(commentId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.getEntityById(commentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).getEntityById(commentId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when comment does not exist")
   void existsById() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.existsById(commentId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.existsById(commentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).existsById(commentId);
   }
 
 
   @Test
   @DisplayName("getByTaskId() - must throw when comment list is empty")
   void getByTaskId() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // list - setup
      List<CommentEntity> commentList = List.of();
 
      // stubs
      when(this.commentRepository.getByTaskId(taskId)).thenReturn(commentList);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.getByTaskId(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentários não existem");
 
      // checks
      verify(this.commentValidator, times(1)).taskIdValidate(taskId);
      verify(this.commentRepository, times(1)).getByTaskId(taskId);
 
      // no checks
      verifyNoInteractions(this.commentMapper);
   }
 
 
   @Test
   @DisplayName("getByAuthorId() - must throw when comment list is empty")
   void getByAuthorId() {
      // id - setup
      UUID authorId = UUID.randomUUID();
 
      // list - setup
      List<CommentEntity> commentList = List.of();
 
      // stubs
      when(this.commentRepository.getByAuthorId(authorId)).thenReturn(commentList);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.getByAuthorId(authorId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentários não existem");
 
      // checks
      verify(this.commentValidator, times(1)).authorIdValidate(authorId);
      verify(this.commentRepository, times(1)).getByAuthorId(authorId);
 
      // no checks
      verifyNoInteractions(this.commentMapper);
   }


   @Test
   @DisplayName("updateContent() - must throw when comment does not exist")
   void updateContent() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // data to update
      String newContent = "comment atualizado";
 
      // stubs
      when(this.commentRepository.updateContent(commentId, newContent)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.updateContent(commentId, newContent))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentValidator, times(1)).contentValidate(newContent);
      verify(this.commentRepository, times(1)).updateContent(commentId, newContent);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.commentMapper);
   }
 
 
   @Test
   @DisplayName("delete() - must throw when comment does not exist")
   void delete() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.delete(commentId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.delete(commentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).delete(commentId);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.commentMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when comment does not exist")
   void isDeleted() {
      // id - setup
      UUID commentId = UUID.randomUUID();
 
      // stubs
      when(this.commentRepository.isDeleted(commentId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.commentService.isDeleted(commentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Comentário não existe");
 
      // checks
      verify(this.commentValidator, times(1)).idValidate(commentId);
      verify(this.commentRepository, times(1)).isDeleted(commentId);
   }

}