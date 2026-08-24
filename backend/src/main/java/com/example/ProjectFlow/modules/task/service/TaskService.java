
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TasksValidator;
import com.example.ProjectFlow.modules.task.dto.TasksCompleteResponseDTO;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.TasksUpdateDTO;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class TaskService {
   
   // properties
   private final TaskRepository taskRepository;
   private final TasksValidator tasksValidator;
   private final ProjectService projectService;
   private final BoardColumnService boardColumnService;
   private final UserService userService;


   // constructor - dependency injection
   public TaskService(
      TaskRepository taskRepository,
      TasksValidator tasksValidator,
      ProjectService projectService,
      BoardColumnService boardColumnService,
      UserService userService
   ) {
      this.taskRepository = taskRepository;
      this.tasksValidator = tasksValidator;
      this.projectService = projectService;
      this.boardColumnService = boardColumnService;
      this.userService = userService;
   }


   // task creation
   @Transactional
   public TasksResponseDTO create(TasksDTO data) {
      this.tasksValidator.projectIdValidate(data.projectId());
      this.tasksValidator.columnIdValidate(data.columnId());
      this.tasksValidator.ownerIdValidate(data.ownerId());
      this.tasksValidator.titleValidate(data.title());
      this.tasksValidator.descriptionValidate(data.description());
      this.tasksValidator.priorityValidate(data.priority());
      this.tasksValidator.dueDateValidate(data.dueDate());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      // get board column data
      BoardColumnsEntity column = this.boardColumnService.getEntityById(data.columnId());

      // get owner data
      UserEntity owner = this.userService.getEntityById(data.ownerId());

      return this.taskRepository.create(data, project, column, owner);
   }


   // get all
   public List<TasksCompleteResponseDTO> getAll() {
      List<TasksCompleteResponseDTO> tasks = this.taskRepository.getAll();

      if(tasks.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return tasks;
   }


   // get by id
   public TasksCompleteResponseDTO getById(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         return this.taskRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public TasksEntity getEntityById(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         return this.taskRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get tasks by project id
   public List<TasksCompleteResponseDTO> getByProjectId(UUID projectId) {
      this.tasksValidator.projectIdValidate(projectId);

      List<TasksCompleteResponseDTO> tasks = this.taskRepository.getByProjectId(projectId);

      if(tasks.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return tasks;     
   }


   // get tasks by board column id
   public List<TasksCompleteResponseDTO> getByColumnId(UUID columnId) {
      this.tasksValidator.columnIdValidate(columnId);

      List<TasksCompleteResponseDTO> tasks = this.taskRepository.getByColumnId(columnId);

      if(tasks.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return tasks;     
   }


   // get tasks by owner id
   public List<TasksCompleteResponseDTO> getByOwnerId(UUID ownerId) {
      this.tasksValidator.ownerIdValidate(ownerId);

      List<TasksCompleteResponseDTO> tasks = this.taskRepository.getByOwnerId(ownerId);

      if(tasks.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return tasks;     
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.tasksValidator.idValidate(id);

      boolean exist = this.taskRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // update column id - task position
   @Transactional
   public TasksCompleteResponseDTO updateColumn(UUID id, UUID columnId) {
      this.tasksValidator.idValidate(id);
      this.tasksValidator.columnIdValidate(columnId);

      // board column existence - check
      BoardColumnsEntity boardColumnEntity = this.boardColumnService.getEntityById(columnId);

      try {
         return this.taskRepository.updateColumn(id, boardColumnEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update task
   @Transactional
   public TasksCompleteResponseDTO update(UUID id, TasksUpdateDTO data) {
      this.tasksValidator.idValidate(id);
      this.tasksValidator.updateValidations(data);

      try {
         return this.taskRepository.update(id, data);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // set complete task
   @Transactional
   public TasksCompleteResponseDTO taskComplete(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         return this.taskRepository.taskComplete(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }   

}