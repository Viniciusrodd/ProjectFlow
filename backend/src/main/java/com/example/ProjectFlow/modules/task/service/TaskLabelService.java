
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskLabelsRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TaskLabelsValidator;

// import service
import com.example.ProjectFlow.modules.labels.service.LabelService;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


@Service
public class TaskLabelService {
 
   // properties
   private final TaskLabelsRepository taskLabelsRepository;
   private final TaskLabelsValidator taskLabelsValidator;
   private final TaskService taskService;
   private final LabelService labelService;

   // constructor - dependency injection
   public TaskLabelService(
      TaskLabelsRepository taskLabelsRepository,
      TaskLabelsValidator taskLabelsValidator,
      TaskService taskService,
      LabelService labelService
   ) {
      this.taskLabelsRepository = taskLabelsRepository;
      this.taskLabelsValidator = taskLabelsValidator;
      this.taskService = taskService;
      this.labelService = labelService;
   }


   // creating labels for tasks
   @Transactional
   public TaskLabelsResponseDTO create(UUID taskId, UUID labelId) {
      this.taskLabelsValidator.taskIdValidate(taskId);
      this.taskLabelsValidator.labelIdValidate(labelId);

      // get task data
      TasksEntity task = this.taskService.getEntityById(taskId);

      // get label data
      LabelsEntity label = this.labelService.getEntityById(labelId);

      // task and label project - validation
      this.taskLabelsValidator.projectValidation(task.getProjectId(), label.getProjectId());

      return this.taskLabelsRepository.create(task, label);
   }

}