
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import java.util.List;
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.UserUpdateDTO;
import com.example.ProjectFlow.modules.user.dto.UserDeletedDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class UserController {
 
   // properties
   private UserService userService;

   // constructor - dependency injection
   public UserController(UserService userService) {
      this.userService = userService;
   }


   // get all users
   @GetMapping("/users")
   @Operation(summary = "Get all users")
   public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getAllUsers() {
      List<UserProfileDTO> users = this.userService.getAll();

      ApiResponse<List<UserProfileDTO>> response = new ApiResponse.Builder<List<UserProfileDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(users)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get by id
   @GetMapping(value = "/user/{id}")
   @Operation(summary = "Get user by id")
   public ResponseEntity<ApiResponse<UserProfileDTO>> getById(@PathVariable UUID id) {
      UserProfileDTO user = this.userService.getById(id);

      ApiResponse<UserProfileDTO> response = new ApiResponse.Builder<UserProfileDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(user)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update user
   @PutMapping(value = "/user/{userId}")
   @Operation(summary = "Update user")
   public ResponseEntity<ApiResponse<UserProfileDTO>> updateUser(
      @PathVariable UUID userId, 
      @RequestBody UserUpdateDTO data
   ) {
      UserProfileDTO user = this.userService.update(userId, data);

      ApiResponse<UserProfileDTO> response = new ApiResponse.Builder<UserProfileDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(user)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete user
   @DeleteMapping(value = "user/{userId}")
   @Operation(summary = "Delete user")
   public ResponseEntity<ApiResponse<UserDeletedDTO>> deleteUser(@PathVariable UUID userId) {
      UserDeletedDTO userDeleted = this.userService.delete(userId);

      ApiResponse<UserDeletedDTO> response = new ApiResponse.Builder<UserDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(userDeleted)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}