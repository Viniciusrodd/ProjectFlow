
// packages
package com.example.ProjectFlow.modules.activityLog.service;

// imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.activityLog.repository.ActivityLogRepository;

// import validator
import com.example.ProjectFlow.modules.activityLog.validator.ActivityLogValidator;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.activityLog.document.ActivityLogDocument;

// import DTO
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogResponseDTO;

// import mapper
import com.example.ProjectFlow.modules.activityLog.mapper.ActivityLogMapper;


@Service 
public class ActivityLogService {
 
   // properties
   private final ActivityLogRepository activityLogRepository;
   private final ActivityLogValidator activityLogValidator;
   private final ActivityLogMapper activityLogMapper;


   // constructor - dependency injection
   public ActivityLogService(
      ActivityLogRepository activityLogRepository,
      ActivityLogValidator activityLogValidator,
      ActivityLogMapper activityLogMapper
   ) {
      this.activityLogRepository = activityLogRepository;
      this.activityLogValidator = activityLogValidator;
      this.activityLogMapper = activityLogMapper;
   }


   // activity log creation
   @Transactional
   public ActivityLogResponseDTO create(ActivityLogDTO data) {
      this.activityLogValidator.actionValidate(data.action().toString());
      if(data.description() != null && !data.description().isEmpty()) {
         this.activityLogValidator.descriptionValidate(data.description());
      }

      try {
         // document setup
         ActivityLogDocument document = new ActivityLogDocument.Builder()
            .organizationId(data.organizationId())
            .projectId(data.projectId())
            .taskId(data.taskId())
            .userId(data.userId())
            .action(data.action())
            .description(data.description())
            .createdAt(LocalDateTime.now())
            .build();
   
         // save document - mongodb
         ActivityLogDocument savedDocument = this.activityLogRepository.save(document);
   
         return this.activityLogMapper.toActivityLogResponseDTO(savedDocument);
      }
      catch (DataAccessException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Erro na criação do registro de atividade: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get activity log by id
   public ActivityLogResponseDTO getById(String id) {
      this.activityLogValidator.idValidate(id);

      ActivityLogDocument document = this.activityLogRepository.findById(id).orElse(null);
      if(document == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registro de atividade não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return this.activityLogMapper.toActivityLogResponseDTO(document);
   }


   // get activity log document by id
   public ActivityLogDocument getDocumentById(String id) {
      this.activityLogValidator.idValidate(id);

      ActivityLogDocument document = this.activityLogRepository.findById(id).orElse(null);
      if(document == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registro de atividade não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return document;
   }


   // exists by id
   public boolean existsById(String id) {
      this.activityLogValidator.idValidate(id);

      boolean exist = this.activityLogRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registro de atividade não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // get all activities log by document id
   public List<ActivityLogResponseDTO> getAllByDocumentId(UUID documentId, String document) {
      List<ActivityLogDocument> activitiesDocument;

      switch(document) {
         // organization
         case "o" -> activitiesDocument = activityLogRepository.findByOrganizationId(documentId);
         
         // project
         case "p" -> activitiesDocument = activityLogRepository.findByProjectId(documentId);
         
         // task
         case "t" -> activitiesDocument = activityLogRepository.findByTaskId(documentId);
         
         // user
         case "u" -> activitiesDocument = activityLogRepository.findByUserId(documentId);

         default -> throw MultiExceptions.badRequest(
            ResponseMessages.BAD_REQUEST + ": Tipo de documento inválido"
         );
      }

      // empty document
      if(activitiesDocument.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registros de atividade não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<ActivityLogResponseDTO> activities = new ArrayList<>();
      for(ActivityLogDocument activity : activitiesDocument) {
         activities.add(this.activityLogMapper.toActivityLogResponseDTO(activity));
      }

      return activities;
   }


   // activities log exists by document id
   public boolean existsByDocumentId(UUID documentId, String document) {
      boolean exist;

      switch(document) {
         // organization
         case "o" -> exist = activityLogRepository.existsByOrganizationId(documentId);
         
         // project
         case "p" -> exist = activityLogRepository.existsByProjectId(documentId);
         
         // task
         case "t" -> exist = activityLogRepository.existsByTaskId(documentId);
         
         // user
         case "u" -> exist = activityLogRepository.existsByUserId(documentId);

         default -> throw MultiExceptions.badRequest(
            ResponseMessages.BAD_REQUEST + ": Tipo de documento inválido"
         );
      }

      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registros de atividade não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // delete all activities log by document id
   public void deleteByDocumentId(UUID documentId, String document) {
      switch(document) {
         // organization
         case "o" -> activityLogRepository.deleteAllByOrganizationId(documentId);
         
         // project
         case "p" -> activityLogRepository.deleteAllByProjectId(documentId);
         
         // task
         case "t" -> activityLogRepository.deleteAllByTaskId(documentId);
         
         // user
         case "u" -> activityLogRepository.deleteAllByUserId(documentId);

         default -> throw MultiExceptions.badRequest(
            ResponseMessages.BAD_REQUEST + ": Tipo de documento inválido"
         );
      }
   }


   // delete activity log
   public void deleteActivityLog(String id) {
      this.activityLogValidator.idValidate(id);

      // activity log - validation
      if(this.activityLogRepository.findById(id) == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Registro de atividade não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // delete
      this.activityLogRepository.deleteById(id);
   }

}