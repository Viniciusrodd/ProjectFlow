
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskChecklistRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TaskChecklistValidator;

// import service

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import exceptions

// import constants


@Service
public class TaskChecklistService {
 
   // properties
   private final TaskChecklistRepository taskChecklistRepository;
   private final TaskChecklistValidator taskChecklistValidator;
   private final TaskService taskService;
   
   // constructor - dependency injection
   public TaskChecklistService(
      TaskChecklistRepository taskChecklistRepository,
      TaskChecklistValidator taskChecklistValidator,
      TaskService taskService
   ) {
      this.taskChecklistRepository = taskChecklistRepository;
      this.taskChecklistValidator = taskChecklistValidator;
      this.taskService = taskService;
   }


   // creating task checklist item
   @Transactional
   public TaskChecklistResponseDTO create(UUID taskId, TaskChecklistDTO data) {
      this.taskChecklistValidator.taskIdValidate(taskId);
      this.taskChecklistValidator.descriptionValidate(data.description());
      this.taskChecklistValidator.positionValidate(data.position());

      // get task data
      TasksEntity task = this.taskService.getEntityById(taskId);

      return this.taskChecklistRepository.create(task, data);
   }

}