
// packages
package com.example.ProjectFlow.modules.auth.integrationTests.authController;

// jackson imports
import tools.jackson.databind.ObjectMapper;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// mockito imports
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;

// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import controller
import com.example.ProjectFlow.modules.auth.controller.AuthController;

// import services
import com.example.ProjectFlow.modules.auth.service.AuthService;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;


@WebMvcTest(AuthController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - AuthController")
public class AuthBadTests {
   
   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // to convert java objects <-> json
   @Autowired
   private ObjectMapper objectMapper;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;

   // replaces real service for spring context mock
   @MockitoBean 
   private AuthService authService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/auth";


   @Test
   @DisplayName("POST - /register - should return 409 when email is already registered")
   void register() throws Exception {
      // register data
      RegisterDTO registerDTO = new RegisterDTO(
         "vinicius", "vinicius@gmail.com", "vinicius123"
      );

      // error message
      String errorMessage = "Recurso já existe: Email já cadastrado";

      // stubs
      when(this.authService.register(registerDTO)).thenThrow(MultiExceptions.duplicate(errorMessage));

      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/register")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(registerDTO))
      )
         .andExpect(status().isConflict())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(409))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
   }


   @Test
   @DisplayName("POST - /login - should return 401 when user password is wrong")
   void login() throws Exception {
      // login data
      LoginDTO loginDTO = new LoginDTO(
         "vinicius@gmail.com", "vinicius123"
      );

      // error message
      String errorMessage = "Não autorizado: Senha incorreta";

      // stubs
      when(this.authService.login(loginDTO)).thenThrow(MultiExceptions.unauthorized(errorMessage));

      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/login")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(loginDTO))
      )
         .andExpect(status().isUnauthorized())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(401))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
   }

}