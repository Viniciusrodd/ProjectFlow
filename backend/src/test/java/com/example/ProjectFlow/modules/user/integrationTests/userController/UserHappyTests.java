
// packages
package com.example.ProjectFlow.modules.user.integrationTests.userController;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
 
// jackson imports
import tools.jackson.databind.ObjectMapper;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.Mockito.verify;
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
import com.example.ProjectFlow.modules.user.controller.UserController;
 
// import services
import com.example.ProjectFlow.modules.user.service.UserService;
 
// import DTOs
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDeletedDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(UserController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - UserController")
public class UserHappyTests {
 
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
   private UserService userService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("GET - /users - should return all users and status 200")
   void getAllUsers() throws Exception {
      // users data
      List<UserProfileDTO> users = List.of(
         new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null),
         new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", "profileImage123")
      );
 
      // stubs
      when(this.userService.getAll()).thenReturn(users);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/users")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(users.size()))
         .andExpect(jsonPath("$.data[0].id").value(users.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].name").value(users.get(0).name()))
         .andExpect(jsonPath("$.data[0].email").value(users.get(0).email()))
         .andExpect(jsonPath("$.data[1].id").value(users.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].name").value(users.get(1).name()))
         .andExpect(jsonPath("$.data[1].email").value(users.get(1).email()))
         .andExpect(jsonPath("$.data[1].profileImageId").value(users.get(1).profileImageId()));
 
      // checks
      verify(this.userService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /user/{id} - should return the user and status 200")
   void getById() throws Exception {
      // user data
      UUID userId = UUID.randomUUID();
      UserProfileDTO user = new UserProfileDTO(
         userId, "vinicius@gmail.com", "vinicius", "profileImage123"
      );
 
      // stubs
      when(this.userService.getById(userId)).thenReturn(user);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(user.id().toString()))
         .andExpect(jsonPath("$.data.name").value(user.name()))
         .andExpect(jsonPath("$.data.email").value(user.email()))
         .andExpect(jsonPath("$.data.profileImageId").value(user.profileImageId()));
 
      // checks
      verify(this.userService).getById(userId);
   }


   @Test
   @DisplayName("PUT - /user/{userId} - should return status 200 when updating (only the name)")
   void updateUser() throws Exception {
      // update data
      UUID userId = UUID.randomUUID();
      UserUpdateDTO updateDTO = new UserUpdateDTO("vinicius updated", null, null);
 
      // update response data
      UserProfileDTO updatedUser = new UserProfileDTO(
         userId, "vinicius@gmail.com", updateDTO.name(), null
      );
 
      // stubs
      when(this.userService.update(userId, updateDTO)).thenReturn(updatedUser);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedUser.id().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedUser.name()))
         .andExpect(jsonPath("$.data.email").value(updatedUser.email()));
   
      // checks
      verify(this.userService).update(userId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /user/{userId} - should return the deleted user and status 200")
   void deleteUser() throws Exception {
      // delete data
      UUID userId = UUID.randomUUID();
      UserDeletedDTO deletedUser = new UserDeletedDTO(
         userId, "vinicius", "vinicius@gmail.com", LocalDateTime.now()
      );
 
      // stubs
      when(this.userService.delete(userId)).thenReturn(deletedUser);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedUser.id().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedUser.name()))
         .andExpect(jsonPath("$.data.email").value(deletedUser.email()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.userService).delete(userId);
   }

}