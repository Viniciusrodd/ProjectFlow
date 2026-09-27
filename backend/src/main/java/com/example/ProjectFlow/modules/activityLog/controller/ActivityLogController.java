
// packages
package com.example.ProjectFlow.modules.activityLog.controller;

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
import org.springframework.web.bind.annotation.RequestBody;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/activityLog")
public class ActivityLogController {
 
   // properties
   private final ActivityLogService activityLogService;

   // constructor - dependency injection
   public ActivityLogController(ActivityLogService activityLogService) {
      this.activityLogService = activityLogService;
   }


   // activity log creation
   @PostMapping()
   @Operation(summary = "Activity log creation")
   public ResponseEntity<ApiResponse<ActivityLogResponseDTO>> create(@RequestBody ActivityLogDTO data) {
      ActivityLogResponseDTO activityData = this.activityLogService.create(data);

      ApiResponse<ActivityLogResponseDTO> response = new ApiResponse.Builder<ActivityLogResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(activityData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get activity log by id
   @GetMapping(value = "/{id}")
   @Operation(summary = "Get activity log")
   public ResponseEntity<ApiResponse<ActivityLogResponseDTO>> getActivityById(@PathVariable String id) {
      ActivityLogResponseDTO activityData = this.activityLogService.getById(id);

      ApiResponse<ActivityLogResponseDTO> response = new ApiResponse.Builder<ActivityLogResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(activityData)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all activities log by document id
   @GetMapping(value = "/{documentId}/document")
   @Operation(summary = "Get all activities log by document id")
   public ResponseEntity<ApiResponse<List<ActivityLogResponseDTO>>> getActivitiesByDocumentId(
      @PathVariable UUID documentId,
      @RequestParam String doc
   ) {
      List<ActivityLogResponseDTO> activitiesData = this.activityLogService.getAllByDocumentId(documentId, doc);

      ApiResponse<List<ActivityLogResponseDTO>> response = new ApiResponse.Builder<List<ActivityLogResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(activitiesData)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete all activities log by document id
   @DeleteMapping(value = "/{documentId}/document")
   @Operation(summary = "Delete all activities log by document id")
   public ResponseEntity<ApiResponse<Void>> deleteActivitiesByDocumentId(
      @PathVariable UUID documentId,
      @RequestParam String doc
   ) {
      this.activityLogService.deleteByDocumentId(documentId, doc);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete activity log
   @DeleteMapping(value = "/{id}")
   @Operation(summary = "Delete activity log by id")
   public ResponseEntity<ApiResponse<Void>> deleteActivityById(@PathVariable String id) {
      this.activityLogService.deleteActivityLog(id);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}