
// packages
package com.example.ProjectFlow.modules.auth.controller;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import services
import com.example.ProjectFlow.modules.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginDTO;
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginResponseDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterResponseDTO;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/auth")
public class AuthController {
 
   // properties
   private final AuthService authService;

   // constructor - dependency injection
   public AuthController(AuthService authService) {
      this.authService = authService;
   }

   
   // register
   @PostMapping("/register")
   @Operation(summary = "User register")
   public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterDTO data) {
      RegisterResponseDTO registeredUser = this.authService.register(data);

      ApiResponse<RegisterResponseDTO> response = new ApiResponse.Builder<RegisterResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(registeredUser)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // login
   @PostMapping("/login")
   @Operation(summary = "User login")
   public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO data) {
      LoginResponseDTO loggedUser = this.authService.login(data);

      ApiResponse<LoginResponseDTO> response = new ApiResponse.Builder<LoginResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.LOGIN_SUCCESS)
         .data(loggedUser)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}