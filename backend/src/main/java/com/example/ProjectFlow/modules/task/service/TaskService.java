
// packages
package com.example.ProjectFlow.modules.task.service;

// imports
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.task.repository.TaskRepository;

// import validator
import com.example.ProjectFlow.modules.task.validator.TasksValidator;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.TasksResponseDTO;

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

// import constants


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

}