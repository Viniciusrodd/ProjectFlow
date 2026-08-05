
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import java.util.List;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.UserUpdateDTO;

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
      List<UserProfileDTO> users = this.userService.getAllUsers();

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
   public ResponseEntity<ApiResponse<UserProfileDTO>> getById(@PathVariable Long id) {
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
      @PathVariable Long userId, 
      @RequestBody UserUpdateDTO data
   ) {
      System.out.println("------------------------------------------");
      System.out.println(data);
      System.out.println("------------------------------------------");

      UserProfileDTO user = this.userService.updateUser(userId, data);

      ApiResponse<UserProfileDTO> response = new ApiResponse.Builder<UserProfileDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(user)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}