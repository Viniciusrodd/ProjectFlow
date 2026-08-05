
// packages
package com.example.ProjectFlow.modules.user.controller;

// imports
import java.util.List;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}