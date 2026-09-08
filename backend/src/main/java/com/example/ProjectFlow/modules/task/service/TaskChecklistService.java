
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskChecklistRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TaskChecklistValidator;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;
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
      this.checkPositionExistence(data.position(), taskId);

      return this.taskChecklistRepository.create(task, data);
   }


   // check if position already exist
   public void checkPositionExistence(int position, UUID taskId) {
      boolean exist = this.taskChecklistRepository.checkPositionExistence(position, taskId);
      if(exist) {
         throw MultiExceptions.invalid(String.format(
            "%s: A posição '%s' já existe na tarefa",
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


   // get checklist item by id
   public TaskChecklistResponseDTO getById(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      try {
         return this.taskChecklistRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get all checklist items
   public List<TaskChecklistResponseDTO> getAll() {
      List<TaskChecklistResponseDTO> items = this.taskChecklistRepository.getAll();

      if(items.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Items de checklist da tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return items;
   }


   // get entity by id
   public TaskChecklistEntity getEntityById(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      try {
         return this.taskChecklistRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      boolean exist = this.taskChecklistRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }

}