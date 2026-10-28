
// packages
package com.example.ProjectFlow.modules.auth.integrationTests.authController;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

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
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginResponseDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(AuthController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - AuthController")
public class AuthHappyTests {
   
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
   @DisplayName("POST - /register - should return created user and status 201")
   void register() throws Exception {
      // register data
      RegisterDTO registerDTO = new RegisterDTO(
         "vinicius", "vinicius@gmail.com", "vinicius123"
      );

      // register response data
      RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(
         UUID.randomUUID(), registerDTO.name(), 
         registerDTO.email(), LocalDateTime.now()
      );

      // stubs
      when(this.authService.register(registerDTO)).thenReturn(registerResponseDTO);

      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/register")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(registerDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(registerResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.name").value(registerResponseDTO.name()))
         .andExpect(jsonPath("$.data.email").value(registerResponseDTO.email()));
   }

   
   @Test
   @DisplayName("POST - /login - should return a logged user and status 200")
   void login() throws Exception {
      // login data
      LoginDTO loginDTO = new LoginDTO(
         "vinicius@gmail.com", "vinicius123"
      );
      
      // login response data
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
         UUID.randomUUID(), "vinicius", 
         loginDTO.email(), "token12345"
      );

      // stubs
      when(this.authService.login(loginDTO)).thenReturn(loginResponseDTO);

      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/login")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(loginDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(loginResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.name").value(loginResponseDTO.name()))
         .andExpect(jsonPath("$.data.email").value(loginResponseDTO.email()))
         .andExpect(jsonPath("$.data.token").value(loginResponseDTO.token()));   
   }
   
}