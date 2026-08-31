
// packages
package com.example.ProjectFlow.modules.task.controller;

// imports
import java.util.List;
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksCompleteResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;

// import services
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class TaskController {
 
   // properties
   private final TaskService taskService;
   private final TaskLabelService taskLabelService;

   // constructor - dependency injection
   public TaskController(
      TaskService taskService,
      TaskLabelService taskLabelService
   ) {
      this.taskService = taskService;
      this.taskLabelService = taskLabelService;
   }


   // task creation
   @PostMapping("/task")
   @Operation(summary = "Create a task")
   public ResponseEntity<ApiResponse<TasksResponseDTO>> createTask(@RequestBody TasksDTO data) {
      TasksResponseDTO task = this.taskService.create(data);

      ApiResponse<TasksResponseDTO> response = new ApiResponse.Builder<TasksResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(task)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all tasks
   @GetMapping("/tasks")
   @Operation(summary = "Get all tasks")
   public ResponseEntity<ApiResponse<List<TasksCompleteResponseDTO>>> getAllTasks() {
      List<TasksCompleteResponseDTO> tasks = this.taskService.getAll();

      ApiResponse<List<TasksCompleteResponseDTO>> response = new ApiResponse.Builder<List<TasksCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(tasks)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get task by id
   @GetMapping(value = "/task/{id}")
   @Operation(summary = "Get task by id")
   public ResponseEntity<ApiResponse<TasksCompleteResponseDTO>> getTaskById(@PathVariable UUID id) {
      TasksCompleteResponseDTO task = this.taskService.getById(id);

      ApiResponse<TasksCompleteResponseDTO> response = new ApiResponse.Builder<TasksCompleteResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(task)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all tasks by project id
   @GetMapping(value = "/tasks/project/{projectId}")
   @Operation(summary = "Get all tasks by project id")
   public ResponseEntity<ApiResponse<List<TasksCompleteResponseDTO>>> getByProjectId(@PathVariable UUID projectId) {
      List<TasksCompleteResponseDTO> tasks = this.taskService.getByProjectId(projectId);

      ApiResponse<List<TasksCompleteResponseDTO>> response = new ApiResponse.Builder<List<TasksCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(tasks)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all tasks by board column id
   @GetMapping(value = "/tasks/column/{columnId}")
   @Operation(summary = "Get all tasks by board column id")
   public ResponseEntity<ApiResponse<List<TasksCompleteResponseDTO>>> getByColumnId(@PathVariable UUID columnId) {
      List<TasksCompleteResponseDTO> tasks = this.taskService.getByColumnId(columnId);

      ApiResponse<List<TasksCompleteResponseDTO>> response = new ApiResponse.Builder<List<TasksCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(tasks)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all tasks by owner id
   @GetMapping(value = "/tasks/owner/{ownerId}")
   @Operation(summary = "Get all tasks by owner id")
   public ResponseEntity<ApiResponse<List<TasksCompleteResponseDTO>>> getByOwnerId(@PathVariable UUID ownerId) {
      List<TasksCompleteResponseDTO> tasks = this.taskService.getByOwnerId(ownerId);

      ApiResponse<List<TasksCompleteResponseDTO>> response = new ApiResponse.Builder<List<TasksCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(tasks)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update column id - task position
   @PutMapping(value = "/task/{id}/column/{columnId}")
   @Operation(summary = "Update task position by column id")
   public ResponseEntity<ApiResponse<TasksCompleteResponseDTO>> updateColumn(
      @PathVariable UUID id,
      @PathVariable UUID columnId
   ) {
      TasksCompleteResponseDTO updatedTask = this.taskService.updateColumn(id, columnId);

      ApiResponse<TasksCompleteResponseDTO> response = new ApiResponse.Builder<TasksCompleteResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedTask)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update task
   @PutMapping(value = "/task/{id}")
   @Operation(summary = "Update task")
   public ResponseEntity<ApiResponse<TasksCompleteResponseDTO>> updateTask(
      @PathVariable UUID id,
      @RequestBody TasksUpdateDTO data
   ) {
      TasksCompleteResponseDTO updatedTask = this.taskService.update(id, data);

      ApiResponse<TasksCompleteResponseDTO> response = new ApiResponse.Builder<TasksCompleteResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedTask)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // set task complete
   @PutMapping(value = "/task/{id}/complete")
   @Operation(summary = "Set task complete")
   public ResponseEntity<ApiResponse<TasksCompleteResponseDTO>> taskComplete(@PathVariable UUID id) {
      TasksCompleteResponseDTO completedTask = this.taskService.taskComplete(id);

      ApiResponse<TasksCompleteResponseDTO> response = new ApiResponse.Builder<TasksCompleteResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(completedTask)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete task
   @DeleteMapping(value = "/task/{id}")
   @Operation(summary = "Delete task")
   public ResponseEntity<ApiResponse<TasksDeletedDTO>> deleteTask(@PathVariable UUID id) {
      TasksDeletedDTO deletedTask = this.taskService.delete(id);

      ApiResponse<TasksDeletedDTO> response = new ApiResponse.Builder<TasksDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(deletedTask)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   //// task labels


   // creating labels for tasks
   @PostMapping(value = "/task/{taskId}/label/{labelId}")
   @Operation(summary = "Creating labels for tasks")
   public ResponseEntity<ApiResponse<TaskLabelsResponseDTO>> createTaskLabel(
      @PathVariable UUID taskId,
      @PathVariable UUID labelId
   ) {
      TaskLabelsResponseDTO taskLabel = this.taskLabelService.create(taskId, labelId);

      ApiResponse<TaskLabelsResponseDTO> response = new ApiResponse.Builder<TaskLabelsResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(taskLabel)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}