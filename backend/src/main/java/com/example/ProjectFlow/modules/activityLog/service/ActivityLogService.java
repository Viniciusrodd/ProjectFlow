
// packages
package com.example.ProjectFlow.modules.activityLog.service;

// imports
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.activityLog.repository.ActivityLogRepository;

// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.user.service.UserService;

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
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
// import mapper
import com.example.ProjectFlow.modules.activityLog.mapper.ActivityLogMapper;


@Service 
public class ActivityLogService {
 
   // properties
   private final OrganizationService organizationService;
   private final ProjectService projectService;
   private final TaskService taskService;
   private final UserService userService;
   private final ActivityLogRepository activityLogRepository;
   private final ActivityLogValidator activityLogValidator;
   private final ActivityLogMapper activityLogMapper;


   // constructor - dependency injection
   public ActivityLogService(
      OrganizationService organizationService,
      ProjectService projectService,
      TaskService taskService,
      UserService userService,
      ActivityLogRepository activityLogRepository,
      ActivityLogValidator activityLogValidator,
      ActivityLogMapper activityLogMapper
   ) {
      this.organizationService = organizationService;
      this.projectService = projectService;
      this.taskService = taskService;
      this.userService = userService;
      this.activityLogRepository = activityLogRepository;
      this.activityLogValidator = activityLogValidator;
      this.activityLogMapper = activityLogMapper;
   }


   // activity log creation
   @Transactional
   public ActivityLogResponseDTO create(ActivityLogDTO data) {
      this.userService.existsById(data.userId());
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

}