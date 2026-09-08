
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskChecklistRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TaskChecklistValidator;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


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
      
      // check column position existence
      this.checkColumnPositionExistence(data.position());

      return this.taskChecklistRepository.create(task, data);
   }


   // check if column position already exist
   public void checkColumnPositionExistence(int position) {
      boolean exist = this.taskChecklistRepository.checkColumnPositionExistence(position);
      if(exist) {
         throw MultiExceptions.invalid(String.format(
            "%s: A coluna de posição '%s' já existe",
            ResponseMessages.INVALID_DATA,
            position
         ));
      }
   }


   // get all checklist items by task id
   public List<TaskChecklistResponseDTO> getAllByTaskId(UUID taskId) {
      this.taskChecklistValidator.taskIdValidate(taskId);

      // task existence - check
      this.taskService.existsById(taskId);

      List<TaskChecklistResponseDTO> items = this.taskChecklistRepository.getAllByTaskId(taskId);

      if(items.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Items de checklist da tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return items;
   }

}