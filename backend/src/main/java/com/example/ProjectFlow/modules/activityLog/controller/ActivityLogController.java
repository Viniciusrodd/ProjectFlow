
// packages
package com.example.ProjectFlow.modules.activityLog.controller;

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

}