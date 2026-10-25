
// packages
package com.example.ProjectFlow.modules.attachment.unitTests;

// imports
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.attachment.repository.AttachmentRepository;
 
// import validator
import com.example.ProjectFlow.modules.attachment.validator.AttachmentValidator;
 
// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;
 
// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;
 
// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - AttachmentService")
public class AttachmentsBadTests {
 
   // mock dependencies
   @Mock
   private UserService userService;
   @Mock
   private TaskService taskService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private AttachmentRepository attachmentRepository;
   @Mock
   private AttachmentValidator attachmentValidator;
   @Mock
   private AttachmentMapper attachmentMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private AttachmentService attachmentService;


   @Test
   @DisplayName("uploadAttachment() - must throw when file cannot be read")
   void uploadAttachment() throws IOException {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID uploadedBy = UUID.randomUUID();
 
      // file - setup
      MultipartFile file = mock(MultipartFile.class);
 
      // stubs
      when(file.getBytes()).thenThrow(new IOException("erro de leitura"));
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.uploadAttachment(taskId, uploadedBy, file))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Falha ao processar anexo de tarefa: erro de leitura");
 
      // checks
      verify(this.userService, times(1)).existsById(uploadedBy);
      verify(this.attachmentValidator, times(1)).validate(file);
 
      // no checks
      verifyNoInteractions(this.attachmentRepository);
      verifyNoInteractions(this.taskService);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.attachmentMapper);
   }


   @Test
   @DisplayName("getTaskAttachmentById() - must throw task attachment not found")
   void getTaskAttachmentById() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
 
      // stubs
      when(this.attachmentRepository.findById(attachmentId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.getTaskAttachmentById(attachmentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Anexo de tarefa não existe");
 
      // checks
      verify(this.attachmentValidator, times(1)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(1)).findById(attachmentId);
   }
 
 
   @Test
   @DisplayName("existsTaskAttachment() - must throw when task attachment does not exist")
   void existsTaskAttachment() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
 
      // stubs
      when(this.attachmentRepository.existsById(attachmentId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.existsTaskAttachment(attachmentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Anexo de tarefa não existe");
 
      // checks
      verify(this.attachmentValidator, times(1)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(1)).existsById(attachmentId);
   }


   @Test
   @DisplayName("getAllTaskAttachments() - must throw when attachment list is empty")
   void getAllTaskAttachmentsEmptyList() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // list - setup
      List<AttachmentDocument> documentList = List.of();
 
      // stubs
      when(this.taskService.existsById(taskId)).thenReturn(true);
      when(this.attachmentRepository.findByTaskId(taskId)).thenReturn(documentList);
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.getAllTaskAttachments(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Anexos de tarefa não existem");
 
      // checks
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.attachmentRepository, times(1)).findByTaskId(taskId);
 
      // no checks
      verifyNoInteractions(this.attachmentMapper);
   }
 
 
   @Test
   @DisplayName("deleteByTaskId() - must throw when task has no attachments")
   void deleteByTaskId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // list - setup
      List<AttachmentDocument> documentList = List.of();
 
      // stubs
      when(this.attachmentRepository.findByTaskId(taskId)).thenReturn(documentList);
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.deleteByTaskId(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Anexos de tarefa não existem");
 
      // checks
      verify(this.attachmentRepository, times(1)).findByTaskId(taskId);
 
      // no checks
      verify(this.attachmentRepository, never()).deleteAllByTaskId(any());
      verifyNoInteractions(this.taskService);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
   }
 
 
   @Test
   @DisplayName("deleteTaskAttachment() - must throw when task attachment does not exist")
   void deleteTaskAttachment() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
 
      // stubs
      when(this.attachmentRepository.findById(attachmentId)).thenReturn(null);
 
      // act and assert
      assertThatThrownBy(() -> this.attachmentService.deleteTaskAttachment(attachmentId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Anexo de tarefa não existe");
 
      // checks
      verify(this.attachmentValidator, times(1)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(1)).findById(attachmentId);
 
      // no checks
      verify(this.attachmentRepository, never()).deleteById(any());
      verifyNoInteractions(this.taskService);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
   }

}