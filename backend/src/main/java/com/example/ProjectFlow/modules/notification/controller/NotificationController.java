
// packages
package com.example.ProjectFlow.modules.notification.controller;

// imports
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.example.ProjectFlow.modules.notification.service.NotificationService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class NotificationController {
 
   // properties
   private final NotificationService notificationService;

   // constructor - dependency injection
   public NotificationController(NotificationService notificationService) {
      this.notificationService = notificationService;
   }


   // notification creation
   @PostMapping(value = "/notification/{userId}")
   @Operation(summary = "Notification creation")
   public ResponseEntity<ApiResponse<NotificationResponseDTO>> create(
      @PathVariable UUID userId,
      @RequestBody NotificationDTO data
   ) {
      NotificationResponseDTO notificationData = this.notificationService.create(userId, data);

      ApiResponse<NotificationResponseDTO> response = new ApiResponse.Builder<NotificationResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(notificationData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }  


   // get notification by id
   @GetMapping(value = "/notification/{id}")
   @Operation(summary = "Get notification")
   public ResponseEntity<ApiResponse<NotificationResponseDTO>> getNotificationById(@PathVariable String id) {
      NotificationResponseDTO notificationData = this.notificationService.getById(id);

      ApiResponse<NotificationResponseDTO> response = new ApiResponse.Builder<NotificationResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(notificationData)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}