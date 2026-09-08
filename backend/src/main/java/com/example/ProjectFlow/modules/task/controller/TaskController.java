
// packages
package com.example.ProjectFlow.modules.task.controller;

// imports
import java.util.List;
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;

// import services
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;
import com.example.ProjectFlow.modules.task.service.TaskChecklistService;

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
   private final TaskChecklistService taskChecklistService;

   // constructor - dependency injection
   public TaskController(
      TaskService taskService,
      TaskLabelService taskLabelService,
      TaskChecklistService taskChecklistService
   ) {
      this.taskService = taskService;
      this.taskLabelService = taskLabelService;
      this.taskChecklistService = taskChecklistService;
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


   // creating task label relation
   @PostMapping(value = "/task/{taskId}/label/{labelId}")
   @Operation(summary = "Creating task label relation")
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


   // get all labels by task id
   @GetMapping(value = "/task/{taskId}/labels")
   @Operation(summary = "Get all labels by task id")
   public ResponseEntity<ApiResponse<List<LabelsByTaskResponseDTO>>> getByTaskId(@PathVariable UUID taskId) {
      List<LabelsByTaskResponseDTO> labels = this.taskLabelService.getAllByTaskId(taskId);

      ApiResponse<List<LabelsByTaskResponseDTO>> response = new ApiResponse.Builder<List<LabelsByTaskResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(labels)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all tasks by label id
   @GetMapping(value = "/tasks/label/{labelId}")
   @Operation(summary = "Get all tasks by label id")
   public ResponseEntity<ApiResponse<List<TasksByLabelResponseDTO>>> getByLabelId(@PathVariable UUID labelId) {
      List<TasksByLabelResponseDTO> tasks = this.taskLabelService.getAllByLabelId(labelId);

      ApiResponse<List<TasksByLabelResponseDTO>> response = new ApiResponse.Builder<List<TasksByLabelResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(tasks)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // remove task label relation
   @DeleteMapping(value = "/task/label/{id}")
   @Operation(summary = "Remove task label relation")
   public ResponseEntity<ApiResponse<TaskLabelsDeletedDTO>> removeTaskLabel(@PathVariable UUID id) {
      TaskLabelsDeletedDTO relationRemoved = this.taskLabelService.removeRelation(id);

      ApiResponse<TaskLabelsDeletedDTO> response = new ApiResponse.Builder<TaskLabelsDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(relationRemoved)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   //// task checklist


   // creating task checklist item
   @PostMapping(value = "/task/{taskId}/checklist")
   @Operation(summary = "Creating task checklist item")
   public ResponseEntity<ApiResponse<TaskChecklistResponseDTO>> createTaskChecklist(
      @PathVariable UUID taskId,
      @RequestBody TaskChecklistDTO data
   ) {
      TaskChecklistResponseDTO taskChecklist = this.taskChecklistService.create(taskId, data);

      ApiResponse<TaskChecklistResponseDTO> response = new ApiResponse.Builder<TaskChecklistResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(taskChecklist)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all checklist items by task id
   @GetMapping(value = "/task/{taskId}/checklist/items")
   @Operation(summary = "Get all checklist items by task id")
   public ResponseEntity<ApiResponse<List<TaskChecklistResponseDTO>>> getChecklistByTaskId(
      @PathVariable UUID taskId
   ) {
      List<TaskChecklistResponseDTO> items = this.taskChecklistService.getAllByTaskId(taskId);

      ApiResponse<List<TaskChecklistResponseDTO>> response = new ApiResponse.Builder<List<TaskChecklistResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(items)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get checklist item by id
   @GetMapping(value = "/task/checklist/item/{id}")
   @Operation(summary = "Get checklist item by id")
   public ResponseEntity<ApiResponse<TaskChecklistResponseDTO>> getChecklistItemById(
      @PathVariable UUID id
   ) {
      TaskChecklistResponseDTO item = this.taskChecklistService.getById(id);

      ApiResponse<TaskChecklistResponseDTO> response = new ApiResponse.Builder<TaskChecklistResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(item)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all checklist items
   @GetMapping(value = "/task/checklist/items")
   @Operation(summary = "Get all checklist items")
   public ResponseEntity<ApiResponse<List<TaskChecklistResponseDTO>>> getAllChecklistItems() {
      List<TaskChecklistResponseDTO> items = this.taskChecklistService.getAll();

      ApiResponse<List<TaskChecklistResponseDTO>> response = new ApiResponse.Builder<List<TaskChecklistResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(items)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update task checklist item
   @PutMapping(value = "/task/checklist/item/{id}")
   @Operation(summary = "Update task checklist item")
   public ResponseEntity<ApiResponse<TaskChecklistResponseDTO>> updateChecklistItem(
      @PathVariable UUID id,
      @RequestBody TaskChecklistUpdateDTO data
   ) {
      TaskChecklistResponseDTO updatedItem = this.taskChecklistService.update(id, data);

      ApiResponse<TaskChecklistResponseDTO> response = new ApiResponse.Builder<TaskChecklistResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedItem)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update completed field of task checklist item
   @PutMapping(value = "/task/checklist/item/{id}/completed")
   @Operation(summary = "Update completed field of task checklist item")
   public ResponseEntity<ApiResponse<TaskChecklistResponseDTO>> setChecklistItemCompleted(
      @PathVariable UUID id,
      @RequestParam boolean c
   ) {
      TaskChecklistResponseDTO updatedItem = this.taskChecklistService.setCompleted(id, c);

      ApiResponse<TaskChecklistResponseDTO> response = new ApiResponse.Builder<TaskChecklistResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedItem)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete task checklist item
   @DeleteMapping(value = "/task/checklist/item/{id}")
   @Operation(summary = "Delete task checklist item")
   public ResponseEntity<ApiResponse<TaskChecklistDeletedDTO>> deleteChecklistItem(@PathVariable UUID id) {
      TaskChecklistDeletedDTO deletedItem = this.taskChecklistService.delete(id);

      ApiResponse<TaskChecklistDeletedDTO> response = new ApiResponse.Builder<TaskChecklistDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(deletedItem)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}