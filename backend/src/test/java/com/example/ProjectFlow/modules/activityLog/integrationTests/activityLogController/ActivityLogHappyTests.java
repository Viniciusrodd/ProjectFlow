
// packages
package com.example.ProjectFlow.modules.activityLog.integrationTests.activityLogController;

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
import com.example.ProjectFlow.modules.activityLog.controller.ActivityLogController;

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogResponseDTO;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(ActivityLogController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - ActivityLogController")
public class ActivityLogHappyTests {

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
   private ActivityLogService activityLogService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /activityLog - should return created activity log and status 201")
   void create() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      ActivityLogDTO activityDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationId)
         .projectId(projectId)
         .taskId(taskId)
         .userId(userId)
         .action(ActivityActionEnum.TAREFA_CRIADA)
         .description("Task created")
         .build();
 
      // activity response data
      ActivityLogResponseDTO activityResponseDTO = new ActivityLogResponseDTO(
         "activity123", organizationId, projectId, taskId, userId,
         activityDTO.action(), activityDTO.description()
      );
 
      // stubs
      when(this.activityLogService.create(activityDTO)).thenReturn(activityResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/activityLog")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(activityDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(activityResponseDTO.id()))
         .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.action").value(activityResponseDTO.action().name().toLowerCase()))
         .andExpect(jsonPath("$.data.description").value(activityResponseDTO.description()));
 
      // checks
      verify(this.activityLogService).create(activityDTO);
   }
 
 
   @Test
   @DisplayName("GET - /activityLog/{id} - should return the activity log and status 200")
   void getActivityById() throws Exception {
      // activity data
      String activityId = "activity123";
      ActivityLogResponseDTO activity = new ActivityLogResponseDTO(
         activityId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         ActivityActionEnum.TAREFA_CRIADA, "Task created"
      );
 
      // stubs
      when(this.activityLogService.getById(activityId)).thenReturn(activity);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/activityLog/" + activityId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(activity.id()))
         .andExpect(jsonPath("$.data.organizationId").value(activity.organizationId().toString()))
         .andExpect(jsonPath("$.data.projectId").value(activity.projectId().toString()))
         .andExpect(jsonPath("$.data.taskId").value(activity.taskId().toString()))
         .andExpect(jsonPath("$.data.userId").value(activity.userId().toString()))
         .andExpect(jsonPath("$.data.action").value(activity.action().name().toLowerCase()))
         .andExpect(jsonPath("$.data.description").value(activity.description()));
 
      // checks
      verify(this.activityLogService).getById(activityId);
   }
 
 
   @Test
   @DisplayName("GET - /activitiesLog/{documentId}/document - should return all activities of the document and status 200")
   void getActivitiesByDocumentId() throws Exception {
      // document id
      UUID documentId = UUID.randomUUID();
      String doc = "t";
 
      // activities data
      List<ActivityLogResponseDTO> activities = List.of(
         new ActivityLogResponseDTO(
            "activity1", UUID.randomUUID(), UUID.randomUUID(), documentId, UUID.randomUUID(),
            ActivityActionEnum.TAREFA_CRIADA, "Task created"
         ),
         new ActivityLogResponseDTO(
            "activity2", UUID.randomUUID(), UUID.randomUUID(), documentId, UUID.randomUUID(),
            ActivityActionEnum.TAREFA_ATUALIZADA, "Task updated"
         )
      );
 
      // stubs
      when(this.activityLogService.getAllByDocumentId(documentId, doc)).thenReturn(activities);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/activitiesLog/" + documentId + "/document")
         .param("doc", doc)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(activities.size()))
         .andExpect(jsonPath("$.data[0].id").value(activities.get(0).id()))
         .andExpect(jsonPath("$.data[0].taskId").value(documentId.toString()))
         .andExpect(jsonPath("$.data[1].id").value(activities.get(1).id()))
         .andExpect(jsonPath("$.data[1].taskId").value(documentId.toString()));
 
      // checks
      verify(this.activityLogService).getAllByDocumentId(documentId, doc);
   }
 
 
   @Test
   @DisplayName("DELETE - /activitiesLog/{documentId}/document - should return status 200 when deleting all activities")
   void deleteActivitiesByDocumentId() throws Exception {
      // document id
      UUID documentId = UUID.randomUUID();
      String doc = "p";
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/activitiesLog/" + documentId + "/document")
         .param("doc", doc)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.activityLogService).deleteByDocumentId(documentId, doc);
   }
 
 
   @Test
   @DisplayName("DELETE - /activityLog/{id} - should return status 200 when deleting the activity log")
   void deleteActivityById() throws Exception {
      // activity id
      String activityId = "activity123";
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/activityLog/" + activityId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.activityLogService).deleteActivityLog(activityId);
   }

}