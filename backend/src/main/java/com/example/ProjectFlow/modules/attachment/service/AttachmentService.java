
// packages
package com.example.ProjectFlow.modules.attachment.service;

// imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.attachment.repository.AttachmentRepository;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import validator
import com.example.ProjectFlow.modules.attachment.validator.AttachmentValidator;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;

// import DTO
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;

// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


@Service 
public class AttachmentService {
 
   // properties
   private final UserService userService;
   private final TaskService taskService;
   private final ActivityLogService activityLogService;
   private final NotificationService notificationService;
   private final AttachmentRepository attachmentRepository;
   private final AttachmentValidator attachmentValidator;
   private final AttachmentMapper attachmentMapper;


   // constructor - dependency injection
   public AttachmentService(
      UserService userService,
      TaskService taskService,
      ActivityLogService activityLogService,
      NotificationService notificationService,
      AttachmentRepository attachmentRepository,
      AttachmentValidator attachmentValidator,
      AttachmentMapper attachmentMapper
   ) {
      this.userService = userService;
      this.taskService = taskService;
      this.activityLogService = activityLogService;
      this.notificationService = notificationService;
      this.attachmentRepository = attachmentRepository;
      this.attachmentValidator = attachmentValidator;
      this.attachmentMapper = attachmentMapper;
   }


   // task attachment upload
   @Transactional 
   public AttachmentResponseDTO uploadAttachment(UUID taskId, UUID uploadedBy, MultipartFile file) {
      this.userService.existsById(uploadedBy);
      this.attachmentValidator.validate(file);

      try {
         // document - setup
         AttachmentDocument document = new AttachmentDocument.Builder()
            .taskId(taskId)
            .uploadedBy(uploadedBy)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         AttachmentDocument savedDocument = this.attachmentRepository.save(document);

         // get task
         TasksEntity taskEntity = this.taskService.getEntityById(taskId); 

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .taskId(taskEntity.getId())
            .attachmentId(UUID.fromString(savedDocument.getId()))
            .action(ActivityActionEnum.ANEXO_DA_TAREFA_ADICIONADO)
            .description(
               "Anexo da tarefa: " + taskEntity.getTitle() + 
               ", criado no projeto: " + taskEntity.getProject().getName()
            )
            .build();
         
         this.activityLogService.create(activityLog);

         // notification - registering
         for(ProjectMembersEntity notifyMember : taskEntity.getProject().getMembers()) {
            NotificationDTO notification = new NotificationDTO.Builder()
               .userId(notifyMember.getUser().getId().toString())
               .title(NotificationTitleEnum.ANEXO_ADICIONADO_A_TAREFA)
               .message(
                  "Anexo da tarefa: " + taskEntity.getTitle() + 
                  ", criado no projeto: " + taskEntity.getProject().getName()
               )
               .build();
            
            this.notificationService.create(notification);
         }

         return this.attachmentMapper.toAttachmentResponseDTO(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha ao processar anexo de tarefa: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get task attachment by id
   public AttachmentDocument getTaskAttachmentById(String id) {
      this.attachmentValidator.idValidate(id);

      AttachmentDocument attachment = this.attachmentRepository.findById(id).orElse(null);
      if(attachment == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Anexo de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return attachment;
   }


   // exist task attachments
   public boolean existsTaskAttachment(String id) {
      this.attachmentValidator.idValidate(id);

      boolean exist = this.attachmentRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Anexo de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // get all task attachments
   public List<AttachmentResponseDTO> getAllTaskAttachments(UUID taskId) {
      this.taskService.existsById(taskId);

      List<AttachmentDocument> attachmentsDocument = this.attachmentRepository.findByTaskId(taskId);
      if(attachmentsDocument.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Anexos de tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<AttachmentResponseDTO> attachments = new ArrayList<>();
      for(AttachmentDocument attachment : attachmentsDocument) {
         attachments.add(this.attachmentMapper.toAttachmentResponseDTO(attachment));
      }

      return attachments;
   }


   // delete all attachments by task
   public void deleteByTaskId(UUID taskId) {
      // task attachment - validation
      if(this.attachmentRepository.findByTaskId(taskId).isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Anexos de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // remove
      this.attachmentRepository.deleteAllByTaskId(taskId);

      // get task
      TasksEntity taskEntity = this.taskService.getEntityById(taskId); 

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .action(ActivityActionEnum.ANEXO_DA_TAREFA_REMOVIDO)
         .description(
            "Anexos da tarefa: " + taskEntity.getTitle() + 
            ", deletados do projeto: " + taskEntity.getProject().getName()
         )
         .build();
         
      this.activityLogService.create(activityLog);
      
      // notification - registering
      for(ProjectMembersEntity notifyMember : taskEntity.getProject().getMembers()) {
         NotificationDTO notification = new NotificationDTO.Builder()
            .userId(notifyMember.getUser().getId().toString())
            .title(NotificationTitleEnum.ANEXOS_REMOVIDOS_DA_TAREFA)
            .message(
               "Anexos da tarefa: " + taskEntity.getTitle() + 
               ", deletados do projeto: " + taskEntity.getProject().getName()
            )
            .build();
         
         this.notificationService.create(notification);
      }
   }


   // delete task attachment
   public void deleteTaskAttachment(String id) {
      this.attachmentValidator.idValidate(id);

      // task attachment - validation
      if(this.attachmentRepository.findById(id) == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Anexo de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // remove
      this.attachmentRepository.deleteById(id);

      // get task
      UUID taskId = this.getTaskAttachmentById(id).getTaskId();
      TasksEntity taskEntity = this.taskService.getEntityById(taskId);

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .attachmentId(UUID.fromString(id))
         .action(ActivityActionEnum.ANEXO_DA_TAREFA_REMOVIDO)
         .description(
            "Anexo da tarefa: " + taskEntity.getTitle() + 
            ", deletado do projeto: " + taskEntity.getProject().getName()
         )
         .build();
         
      this.activityLogService.create(activityLog);

      // notification - registering
      for(ProjectMembersEntity notifyMember : taskEntity.getProject().getMembers()) {
         NotificationDTO notification = new NotificationDTO.Builder()
            .userId(notifyMember.getUser().getId().toString())
            .title(NotificationTitleEnum.ANEXO_REMOVIDO_DA_TAREFA)
            .message(
               "Anexo da tarefa: " + taskEntity.getTitle() + 
               ", deletado do projeto: " + taskEntity.getProject().getName()
            )
            .build();
         
         this.notificationService.create(notification);
      }
   }

}