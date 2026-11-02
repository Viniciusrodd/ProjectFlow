
// packages
package com.example.ProjectFlow.modules.notification.integrationTests.notificationController;

// imports
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
import com.example.ProjectFlow.modules.notification.controller.NotificationController;

// import services
import com.example.ProjectFlow.modules.notification.service.NotificationService;

// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationResponseDTO;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(NotificationController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - NotificationController")
public class NotificationHappyTests {

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
   @DisplayName("POST - /notification - should return created notification and status 201")
   void create() throws Exception {
      // data - setup
      UUID userId = UUID.randomUUID();
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userId.toString())
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message("You have a new task assigned")
         .build();
 
      // notification response data
      NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO(
         "notification123", userId, notificationDTO.title(), notificationDTO.message(), false
      );
 
      // stubs
      when(this.notificationService.create(notificationDTO)).thenReturn(notificationResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/notification")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(notificationDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(notificationResponseDTO.id()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.title").value(notificationResponseDTO.title().name().toLowerCase()))
         .andExpect(jsonPath("$.data.message").value(notificationResponseDTO.message()))
         .andExpect(jsonPath("$.data.read").value(notificationResponseDTO.read()));
 
      // checks
      verify(this.notificationService).create(notificationDTO);
   }
 
 
   @Test
   @DisplayName("GET - /notification/{id} - should return the notification and status 200")
   void getNotificationById() throws Exception {
      // notification data
      String notificationId = "notification123";
      NotificationResponseDTO notification = new NotificationResponseDTO(
         notificationId, UUID.randomUUID(), NotificationTitleEnum.TAREFA_ATRIBUIDA,
         "You have a new task assigned", true
      );
 
      // stubs
      when(this.notificationService.getById(notificationId)).thenReturn(notification);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/notification/" + notificationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(notification.id()))
         .andExpect(jsonPath("$.data.userId").value(notification.userId().toString()))
         .andExpect(jsonPath("$.data.title").value(notification.title().name().toLowerCase()))
         .andExpect(jsonPath("$.data.message").value(notification.message()))
         .andExpect(jsonPath("$.data.read").value(notification.read()));
 
      // checks
      verify(this.notificationService).getById(notificationId);
   }
 
 
   @Test
   @DisplayName("GET - /user/{userId}/notifications - should return all user notifications and status 200")
   void getUserNotifications() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // notifications data
      List<NotificationResponseDTO> notifications = List.of(
         new NotificationResponseDTO(
            "notification1", userId, NotificationTitleEnum.TAREFA_ATRIBUIDA,
            "Task assigned", false
         ),
         new NotificationResponseDTO(
            "notification2", userId, NotificationTitleEnum.TAREFA_CONCLUIDA,
            "Task completed", false
         )
      );
 
      // stubs
      when(this.notificationService.getAllByUserId(userId)).thenReturn(notifications);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/user/" + userId + "/notifications")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(notifications.size()))
         .andExpect(jsonPath("$.data[0].id").value(notifications.get(0).id()))
         .andExpect(jsonPath("$.data[0].userId").value(userId.toString()))
         .andExpect(jsonPath("$.data[1].id").value(notifications.get(1).id()))
         .andExpect(jsonPath("$.data[1].userId").value(userId.toString()));
 
      // checks
      verify(this.notificationService).getAllByUserId(userId);
   }
 
 
   @Test
   @DisplayName("DELETE - /user/{userId}/notifications - should return status 200 when deleting all notifications")
   void deleteUserNotifications() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/user/" + userId + "/notifications")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.notificationService).deleteAllByUserId(userId);
   }
 
 
   @Test
   @DisplayName("DELETE - /notification/{id} - should return status 200 when deleting the notification")
   void deleteNotification() throws Exception {
      // notification id
      String notificationId = "notification123";
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/notification/" + notificationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.notificationService).deleteNotification(notificationId);
   }

}