
// packages
package com.example.ProjectFlow.modules.notification.integrationTests.notificationController;

// imports
import java.util.UUID;
 
// jackson imports
import tools.jackson.databind.ObjectMapper;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.Mockito.doThrow;
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
import com.example.ProjectFlow.modules.notification.controller.NotificationController;
 
// import services
import com.example.ProjectFlow.modules.notification.service.NotificationService;
 
// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(NotificationController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - NotificationController")
public class NotificationBadTests {

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
 
   // replaces real services for spring context mock
   @MockitoBean 
   private NotificationService notificationService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /notification - should return 500 when database access fails")
   void create() throws Exception {
      // data - setup
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(UUID.randomUUID().toString())
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("You have a new task assigned")
         .build();
 
      // error message
      String errorMessage = "Erro interno: Erro na criação da notificação";
 
      // stubs
      when(this.notificationService.create(notificationDTO)).thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/notification")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(notificationDTO))
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.notificationService).create(notificationDTO);
   }
 
 
   @Test
   @DisplayName("GET - /notification/{id} - should return 404 when notification does not exist")
   void getNotificationById() throws Exception {
      // notification id
      String notificationId = "notification123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Notificação não existe";
 
      // stubs
      when(this.notificationService.getById(notificationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/notification/" + notificationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.notificationService).getById(notificationId);
   }
 
 
   @Test
   @DisplayName("GET - /user/{userId}/notifications - should return 404 when user has no notifications")
   void getUserNotifications() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Notificações não existem";
 
      // stubs
      when(this.notificationService.getAllByUserId(userId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/user/" + userId + "/notifications")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.notificationService).getAllByUserId(userId);
   }
 
 
   @Test
   @DisplayName("DELETE - /user/{userId}/notifications - should return 404 when user has no notifications")
   void deleteUserNotifications() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Notificações não existem";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.notificationService).deleteAllByUserId(userId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/user/" + userId + "/notifications")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.notificationService).deleteAllByUserId(userId);
   }
 
 
   @Test
   @DisplayName("DELETE - /notification/{id} - should return 404 when notification does not exist")
   void deleteNotification() throws Exception {
      // notification id
      String notificationId = "notification123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Notificação não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.notificationService).deleteNotification(notificationId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/notification/" + notificationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.notificationService).deleteNotification(notificationId);
   }

}