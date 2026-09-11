
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskChecklistMapper;


@Service
public class TaskChecklistService {
 
   // properties
   private final TaskChecklistRepository taskChecklistRepository;
   private final TaskChecklistValidator taskChecklistValidator;
   private final TaskService taskService;
   private final TaskChecklistMapper taskChecklistMapper;
   
   // constructor - dependency injection
   public TaskChecklistService(
      TaskChecklistRepository taskChecklistRepository,
      TaskChecklistValidator taskChecklistValidator,
      TaskService taskService,
      TaskChecklistMapper taskChecklistMapper
   ) {
      this.taskChecklistRepository = taskChecklistRepository;
      this.taskChecklistValidator = taskChecklistValidator;
      this.taskService = taskService;
      this.taskChecklistMapper = taskChecklistMapper;
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

      // creation
      TaskChecklistEntity taskChecklistEntity = this.taskChecklistRepository.create(task, data);

      return this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity);
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

      List<TaskChecklistEntity> itemsEntity = this.taskChecklistRepository.getAllByTaskId(taskId);

      if(itemsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Items de checklist da tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TaskChecklistResponseDTO> items = new ArrayList<>();
      for(TaskChecklistEntity item : itemsEntity) {
         items.add(this.taskChecklistMapper.toTaskChecklistResponseDTO(item));
      }

      return items;
   }


   // get checklist item by id
   public TaskChecklistResponseDTO getById(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      try {
         TaskChecklistEntity itemEntity = this.taskChecklistRepository.getById(id);

         return this.taskChecklistMapper.toTaskChecklistResponseDTO(itemEntity);
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
      List<TaskChecklistEntity> itemsEntity = this.taskChecklistRepository.getAll();

      if(itemsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Items de checklist da tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TaskChecklistResponseDTO> items = new ArrayList<>();
      for(TaskChecklistEntity item : itemsEntity) {
         items.add(TaskChecklistResponseDTO.get(item));
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


   // update task checklist item
   @Transactional
   public TaskChecklistResponseDTO update(UUID id, TaskChecklistUpdateDTO data) {
      this.taskChecklistValidator.idValidate(id);
      this.taskChecklistValidator.updateValidations(data);

      try {
         TaskChecklistEntity itemEntity = this.taskChecklistRepository.update(id, data);

         return this.taskChecklistMapper.toTaskChecklistResponseDTO(itemEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update completed field of task checklist item
   @Transactional
   public TaskChecklistResponseDTO setCompleted(UUID id, boolean completed) {
      this.taskChecklistValidator.idValidate(id);

      try {
         TaskChecklistEntity itemEntity = this.taskChecklistRepository.setCompleted(id, completed);

         return this.taskChecklistMapper.toTaskChecklistResponseDTO(itemEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete task checklist item
   @Transactional 
   public TaskChecklistDeletedDTO delete(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      try {
         TaskChecklistEntity itemEntity = this.taskChecklistRepository.delete(id);

         return this.taskChecklistMapper.toTaskChecklistDeletedDTO(itemEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.taskChecklistValidator.idValidate(id);

      try {
         return this.taskChecklistRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Item de checklist da tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}