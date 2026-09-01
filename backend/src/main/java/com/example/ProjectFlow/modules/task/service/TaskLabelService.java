
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.NoResultException;
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
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


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

      return this.taskLabelsRepository.create(task, label);
   }


   // get all labels by task id
   public List<LabelsByTaskResponseDTO> getAllByTaskId(UUID taskId) {
      this.taskLabelsValidator.taskIdValidate(taskId);

      // task existence - check
      this.taskService.existsById(taskId);

      List<LabelsByTaskResponseDTO> labels = this.taskLabelsRepository.getAllByTaskId(taskId);

      if(labels.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiquetas de tarefa não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return labels;
   }


   // get all tasks by label id
   public List<TasksByLabelResponseDTO> getAllByLabelId(UUID labelId) {
      this.taskLabelsValidator.labelIdValidate(labelId);

      // label existence - check
      this.labelService.existsById(labelId);

      List<TasksByLabelResponseDTO> tasks = this.taskLabelsRepository.getAllByLabelId(labelId);

      if(tasks.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas da etiqueta não existem",
            ResponseMessages.NOT_FOUND
         ));
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

}