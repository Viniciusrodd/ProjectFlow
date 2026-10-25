
// packages
package com.example.ProjectFlow.modules.attachment.unitTests;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.attachment.repository.AttachmentRepository;
 
// import validator
import com.example.ProjectFlow.modules.attachment.validator.AttachmentValidator;
 
// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
 
// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - AttachmentService")
public class AttachmentsHappyTests {

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
   @DisplayName("uploadAttachment() - must upload a task attachment")
   void uploadAttachment() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
      UUID taskId = UUID.randomUUID();
      UUID uploadedBy = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // binary - setup
      byte[] binary = {1, 2, 3};
 
      // file - setup
      MultipartFile file = new MockMultipartFile(
         "file",
         "filename.png",
         "image/png",
         binary
      );
 
      // user - setup
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
 
      // attachment - setup
      AttachmentDocument attachmentDocument = new AttachmentDocument.Builder()
         .id(attachmentId)
         .taskId(taskId)
         .uploadedBy(uploadedBy)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();
 
      // DTO's - setup
      AttachmentResponseDTO attachmentResponseDTO = new AttachmentResponseDTO(
         attachmentId, taskId, uploadedBy,
         attachmentDocument.getFileName(), attachmentDocument.getMimeType(),
         attachmentDocument.getSize(), attachmentDocument.getUploadDate()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .attachmentId(UUID.fromString(attachmentDocument.getId()))
         .action(ActivityActionEnum.ANEXO_DA_TAREFA_ADICIONADO)
         .description(
            "Anexo da tarefa: " + taskEntity.getTitle() +
            ", criado no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.ANEXO_ADICIONADO_A_TAREFA)
         .message(
            "Anexo da tarefa: " + taskEntity.getTitle() +
            ", criado no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.attachmentRepository.save(any(AttachmentDocument.class))).thenReturn(attachmentDocument);
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
      when(this.attachmentMapper.toAttachmentResponseDTO(attachmentDocument)).thenReturn(attachmentResponseDTO);
 
      // act and assert
      AttachmentResponseDTO result = this.attachmentService.uploadAttachment(taskId, uploadedBy, file);
      assertThat(result)
         .isNotNull()
         .isEqualTo(attachmentResponseDTO);
 
      // checks
      verify(this.userService, times(1)).existsById(uploadedBy);
      verify(this.attachmentValidator, times(1)).validate(file);
 
      verify(this.attachmentRepository, times(1)).save(any(AttachmentDocument.class));
      verify(this.taskService, times(1)).getEntityById(taskId);
 
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.attachmentMapper, times(1)).toAttachmentResponseDTO(attachmentDocument);
   }


   @Test
   @DisplayName("getTaskAttachmentById() - must return task attachment by id")
   void getTaskAttachmentById() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
 
      // attachment - setup
      AttachmentDocument attachmentDocument = new AttachmentDocument.Builder()
         .id(attachmentId)
         .build();
 
      // stubs
      when(this.attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachmentDocument));
 
      // act and assert
      AttachmentDocument result = this.attachmentService.getTaskAttachmentById(attachmentId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(attachmentDocument);
 
      // checks
      verify(this.attachmentValidator, times(1)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(1)).findById(attachmentId);
   }
 
 
   @Test
   @DisplayName("existsTaskAttachment() - must check if task attachment exist by id")
   void existsTaskAttachment() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
 
      // stubs
      when(this.attachmentRepository.existsById(attachmentId)).thenReturn(true);
 
      // act and assert
      boolean result = this.attachmentService.existsTaskAttachment(attachmentId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.attachmentValidator, times(1)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(1)).existsById(attachmentId);
   }
 
 
   @Test
   @DisplayName("getAllTaskAttachments() - must return all attachments by task id")
   void getAllTaskAttachments() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
      UUID taskId = UUID.randomUUID();
      UUID uploadedBy = UUID.randomUUID();
 
      // attachment - setup
      AttachmentDocument attachmentDocument = new AttachmentDocument.Builder()
         .id(attachmentId)
         .taskId(taskId)
         .uploadedBy(uploadedBy)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .build();
 
      // list - setup
      List<AttachmentDocument> documentList = List.of(attachmentDocument);
 
      // DTO - setup
      AttachmentResponseDTO attachmentResponseDTO = new AttachmentResponseDTO(
         attachmentId, taskId, uploadedBy,
         attachmentDocument.getFileName(), attachmentDocument.getMimeType(),
         attachmentDocument.getSize(), attachmentDocument.getUploadDate()
      );
 
      // stubs
      when(this.taskService.existsById(taskId)).thenReturn(true);
      when(this.attachmentRepository.findByTaskId(taskId)).thenReturn(documentList);
      when(this.attachmentMapper.toAttachmentResponseDTO(attachmentDocument)).thenReturn(attachmentResponseDTO);
 
      // act and assert
      List<AttachmentResponseDTO> result = this.attachmentService.getAllTaskAttachments(taskId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(attachmentResponseDTO);
 
      // checks
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.attachmentRepository, times(1)).findByTaskId(taskId);
      verify(this.attachmentMapper, times(1)).toAttachmentResponseDTO(attachmentDocument);
   }


   @Test
   @DisplayName("deleteByTaskId() - must delete all attachments by task id")
   void deleteByTaskId() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // user - setup
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
 
      // attachment - setup
      AttachmentDocument attachmentDocument = new AttachmentDocument.Builder()
         .id(attachmentId)
         .taskId(taskId)
         .build();
 
      // list - setup
      List<AttachmentDocument> documentList = List.of(attachmentDocument);
 
      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .action(ActivityActionEnum.ANEXO_DA_TAREFA_REMOVIDO)
         .description(
            "Anexos da tarefa: " + taskEntity.getTitle() +
            ", deletados do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.ANEXOS_REMOVIDOS_DA_TAREFA)
         .message(
            "Anexos da tarefa: " + taskEntity.getTitle() +
            ", deletados do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.attachmentRepository.findByTaskId(taskId)).thenReturn(documentList);
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
 
      // act
      assertThatCode(() -> this.attachmentService.deleteByTaskId(taskId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.attachmentRepository, times(1)).findByTaskId(taskId);
      verify(this.attachmentRepository, times(1)).deleteAllByTaskId(taskId);
      verify(this.taskService, times(1)).getEntityById(taskId);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
   }
 
 
   @Test
   @DisplayName("deleteTaskAttachment() - must delete a task attachment")
   void deleteTaskAttachment() {
      // id's - setup
      String attachmentId = UUID.randomUUID().toString();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // user - setup
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
 
      // attachment - setup
      AttachmentDocument attachmentDocument = new AttachmentDocument.Builder()
         .id(attachmentId)
         .taskId(taskId)
         .build();
 
      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .attachmentId(UUID.fromString(attachmentId))
         .action(ActivityActionEnum.ANEXO_DA_TAREFA_REMOVIDO)
         .description(
            "Anexo da tarefa: " + taskEntity.getTitle() +
            ", deletado do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.ANEXO_REMOVIDO_DA_TAREFA)
         .message(
            "Anexo da tarefa: " + taskEntity.getTitle() +
            ", deletado do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs (the document no longer exists after the delete, like in the real database)
      when(this.attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachmentDocument));
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
 
      // act
      assertThatCode(() -> this.attachmentService.deleteTaskAttachment(attachmentId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.attachmentValidator, times(2)).idValidate(attachmentId);
      verify(this.attachmentRepository, times(2)).findById(attachmentId);

      verify(this.taskService, times(1)).getEntityById(taskId);      
      verify(this.attachmentRepository, times(1)).deleteById(attachmentId);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
   }

}