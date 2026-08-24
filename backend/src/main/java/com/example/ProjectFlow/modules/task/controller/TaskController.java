
// packages
package com.example.ProjectFlow.modules.task.controller;

// imports

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.task.service.TaskService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.TasksResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class TaskController {
 
   // properties
   private final TaskService taskService;

   // constructor - dependency injection
   public TaskController(TaskService taskService) {
      this.taskService = taskService;
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

}