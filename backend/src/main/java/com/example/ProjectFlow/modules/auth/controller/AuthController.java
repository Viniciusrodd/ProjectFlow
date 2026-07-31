
// packages
package com.example.ProjectFlow.modules.auth.controller;

// imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import services
import com.example.ProjectFlow.modules.auth.service.AuthService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.RegisterResponseDTO;


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
   public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterDTO data) {
      RegisterResponseDTO registeredUser = authService.register(data);

      ApiResponse<RegisterResponseDTO> response = new ApiResponse.Builder<RegisterResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(registeredUser)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}