
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
import com.example.ProjectFlow.modules.task.repository.TaskLabelsRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TaskLabelsValidator;

// import service
import com.example.ProjectFlow.modules.labels.service.LabelService;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsDeletedDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskLabelsMapper;


@Service
public class TaskLabelService {
 
   // properties
   private final TaskLabelsRepository taskLabelsRepository;
   private final TaskLabelsValidator taskLabelsValidator;
   private final TaskService taskService;
   private final LabelService labelService;
   private final TaskLabelsMapper taskLabelsMapper;


   // constructor - dependency injection
   public TaskLabelService(
      TaskLabelsRepository taskLabelsRepository,
      TaskLabelsValidator taskLabelsValidator,
      TaskService taskService,
      LabelService labelService,
      TaskLabelsMapper taskLabelsMapper
   ) {
      this.taskLabelsRepository = taskLabelsRepository;
      this.taskLabelsValidator = taskLabelsValidator;
      this.taskService = taskService;
      this.labelService = labelService;
      this.taskLabelsMapper = taskLabelsMapper;
   }


   // creating task label relation
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

      // creation
      TaskLabelsEntity taskLabelsEntity = this.taskLabelsRepository.create(task, label);

      return this.taskLabelsMapper.toTaskLabelsResponseDTO(taskLabelsEntity);
   }


   // get all labels by task id
   public List<LabelsByTaskResponseDTO> getAllByTaskId(UUID taskId) {
      this.taskLabelsValidator.taskIdValidate(taskId);

      // task existence - check
      this.taskService.existsById(taskId);

      List<TaskLabelsEntity> taskLabelsEntity = this.taskLabelsRepository.getAllByTaskId(taskId);

      if(taskLabelsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiquetas de tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<LabelsByTaskResponseDTO> labels = new ArrayList<>();
      for(TaskLabelsEntity label : taskLabelsEntity) {
         labels.add(this.taskLabelsMapper.toLabelsByTaskResponseDTO(label));
      }

      return labels;
   }


   // get all tasks by label id
   public List<TasksByLabelResponseDTO> getAllByLabelId(UUID labelId) {
      this.taskLabelsValidator.labelIdValidate(labelId);

      // label existence - check
      this.labelService.existsById(labelId);

      List<TaskLabelsEntity> taskLabelsEntity = this.taskLabelsRepository.getAllByLabelId(labelId);

      if(taskLabelsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas da etiqueta não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TasksByLabelResponseDTO> tasks = new ArrayList<>();
      for(TaskLabelsEntity task : taskLabelsEntity) {
         tasks.add(this.taskLabelsMapper.toTasksByLabelResponseDTO(task));
      }

      return tasks;
   }


   // get entity by id
   public TaskLabelsEntity getEntityById(UUID id) {
      this.taskLabelsValidator.idValidate(id);

      try {
         return this.taskLabelsRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.taskLabelsValidator.idValidate(id);

      boolean exist = this.taskLabelsRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // exists by task and label id
   public boolean existsByTaskIdAndLabelId(UUID taskId, UUID labelId) {
      this.taskLabelsValidator.taskIdValidate(taskId);
      this.taskLabelsValidator.labelIdValidate(labelId);

      boolean exist = this.taskLabelsRepository.existsByTaskIdAndLabelId(taskId, labelId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // remove task label relation
   @Transactional
   public TaskLabelsDeletedDTO removeRelation(UUID id) {
      this.taskLabelsValidator.idValidate(id);

      try {
         TaskLabelsEntity taskLabelsEntity = this.taskLabelsRepository.removeRelation(id);

         return this.taskLabelsMapper.toTaskLabelsDeletedDTO(taskLabelsEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}