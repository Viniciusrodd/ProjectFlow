
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TasksValidator;

// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksCompleteResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskMapper;


@Service
public class TaskService {
   
   // properties
   private final TaskRepository taskRepository;
   private final TasksValidator tasksValidator;
   private final ProjectService projectService;
   private final BoardColumnService boardColumnService;
   private final UserService userService;
   private final TaskMapper taskMapper;


   // constructor - dependency injection
   public TaskService(
      TaskRepository taskRepository,
      TasksValidator tasksValidator,
      ProjectService projectService,
      BoardColumnService boardColumnService,
      UserService userService,
      TaskMapper taskMapper
   ) {
      this.taskRepository = taskRepository;
      this.tasksValidator = tasksValidator;
      this.projectService = projectService;
      this.boardColumnService = boardColumnService;
      this.userService = userService;
      this.taskMapper = taskMapper;
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

      // creation
      TasksEntity tasksEntity = this.taskRepository.create(data, project, column, owner);

      return this.taskMapper.toTasksResponseDTO(tasksEntity);
   }


   // get all
   public List<TasksCompleteResponseDTO> getAll() {
      List<TasksEntity> tasksEntity = this.taskRepository.getAll();

      if(tasksEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();
      for(TasksEntity task : tasksEntity) {
         tasks.add(this.taskMapper.toTasksCompleteResponseDTO(task));
      }

      return tasks;
   }


   // get by id
   public TasksCompleteResponseDTO getById(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         TasksEntity tasksEntity = this.taskRepository.getById(id);

         return this.taskMapper.toTasksCompleteResponseDTO(tasksEntity);
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


   // get tasks by project id
   public List<TasksCompleteResponseDTO> getByProjectId(UUID projectId) {
      this.tasksValidator.projectIdValidate(projectId);

      List<TasksEntity> tasksEntity = this.taskRepository.getByProjectId(projectId);

      if(tasksEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();
      for(TasksEntity task : tasksEntity) {
         tasks.add(this.taskMapper.toTasksCompleteResponseDTO(task));
      }

      return tasks;     
   }


   // get tasks by board column id
   public List<TasksCompleteResponseDTO> getByColumnId(UUID columnId) {
      this.tasksValidator.columnIdValidate(columnId);

      List<TasksEntity> tasksEntity = this.taskRepository.getByColumnId(columnId);

      if(tasksEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();
      for(TasksEntity task : tasksEntity) {
         tasks.add(this.taskMapper.toTasksCompleteResponseDTO(task));
      }

      return tasks;     
   }


   // get tasks by owner id
   public List<TasksCompleteResponseDTO> getByOwnerId(UUID ownerId) {
      this.tasksValidator.ownerIdValidate(ownerId);

      List<TasksEntity> tasksEntity = this.taskRepository.getByOwnerId(ownerId);

      if(tasksEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefas não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();
      for(TasksEntity task : tasksEntity) {
         tasks.add(TasksCompleteResponseDTO.get(task));
      }

      return tasks;     
   }


   // update column id - task position
   @Transactional
   public TasksCompleteResponseDTO updateColumn(UUID id, UUID columnId) {
      this.tasksValidator.idValidate(id);
      this.tasksValidator.columnIdValidate(columnId);

      // board column existence - check
      BoardColumnsEntity boardColumnEntity = this.boardColumnService.getEntityById(columnId);

      try {
         TasksEntity taskEntity = this.taskRepository.updateColumn(id, boardColumnEntity);

         return this.taskMapper.toTasksCompleteResponseDTO(taskEntity);
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
         TasksEntity taskEntity = this.taskRepository.update(id, data);

         return this.taskMapper.toTasksCompleteResponseDTO(taskEntity);
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
         TasksEntity taskEntity = this.taskRepository.taskComplete(id);

         return this.taskMapper.toTasksCompleteResponseDTO(taskEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   } 


   // delete task
   @Transactional
   public TasksDeletedDTO delete(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         TasksEntity taskEntity = this.taskRepository.delete(id);

         return this.taskMapper.toTasksDeletedDTO(taskEntity);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.tasksValidator.idValidate(id);

      try {
         return this.taskRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Tarefa não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}