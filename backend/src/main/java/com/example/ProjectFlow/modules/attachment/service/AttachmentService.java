
// packages
package com.example.ProjectFlow.modules.attachment.service;

import java.util.ArrayList;
import java.util.List;
// imports
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

// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;


@Service 
public class AttachmentService {
 
   // properties
   private final UserService userService;
   private final TaskService taskService;
   private final AttachmentRepository attachmentRepository;
   private final AttachmentValidator attachmentValidator;
   private final AttachmentMapper attachmentMapper;


   // constructor - dependency injection
   public AttachmentService(
      UserService userService,
      TaskService taskService,
      AttachmentRepository attachmentRepository,
      AttachmentValidator attachmentValidator,
      AttachmentMapper attachmentMapper
   ) {
      this.userService = userService;
      this.taskService = taskService;
      this.attachmentRepository = attachmentRepository;
      this.attachmentValidator = attachmentValidator;
      this.attachmentMapper = attachmentMapper;
   }


   // task attachment upload
   @Transactional 
   public AttachmentResponseDTO uploadAttachment(UUID taskId, UUID uploadedBy, MultipartFile file) {
      this.taskService.existsById(taskId);
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


   // get all task attachments
   public List<AttachmentResponseDTO> getAllTaskAttachments(UUID taskId) {
      this.taskService.existsById(taskId);

      List<AttachmentDocument> attachmentsDocument = this.attachmentRepository.findAll();
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

}