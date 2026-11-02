
// packages
package com.example.ProjectFlow.modules.activityLog.integrationTests.activityLogController;

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
import com.example.ProjectFlow.modules.activityLog.controller.ActivityLogController;
 
// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
 
// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(ActivityLogController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - ActivityLogController")
public class ActivityLogBadTests {

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
   @DisplayName("POST - /activityLog - should return 500 when database access fails")
   void create() throws Exception {
      // data - setup
      ActivityLogDTO activityDTO = new ActivityLogDTO.Builder()
         .organizationId(UUID.randomUUID())
         .userId(UUID.randomUUID())
         .action(ActivityActionEnum.TAREFA_CRIADA)
         .description("Task created")
         .build();
 
      // error message
      String errorMessage = "Erro interno: Erro na criação do registro de atividade";
 
      // stubs
      when(this.activityLogService.create(activityDTO)).thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/activityLog")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(activityDTO))
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.activityLogService).create(activityDTO);
   }
 
 
   @Test
   @DisplayName("GET - /activityLog/{id} - should return 404 when activity log does not exist")
   void getActivityById() throws Exception {
      // activity id
      String activityId = "activity123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Registro de atividade não existe";
 
      // stubs
      when(this.activityLogService.getById(activityId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/activityLog/" + activityId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.activityLogService).getById(activityId);
   }
 
 
   @Test
   @DisplayName("GET - /activitiesLog/{documentId}/document - should return 404 when document has no activities")
   void getActivitiesByDocumentId() throws Exception {
      // document id
      UUID documentId = UUID.randomUUID();
      String doc = "t";
 
      // error message
      String errorMessage = "Recurso não encontrado: Registros de atividade não existem";
 
      // stubs
      when(this.activityLogService.getAllByDocumentId(documentId, doc))
         .thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/activitiesLog/" + documentId + "/document")
         .param("doc", doc)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.activityLogService).getAllByDocumentId(documentId, doc);
   }
 
 
   @Test
   @DisplayName("DELETE - /activitiesLog/{documentId}/document - should return 400 when document type is invalid")
   void deleteActivitiesByDocumentId() throws Exception {
      // document id
      UUID documentId = UUID.randomUUID();
      String invalidDoc = "x";
 
      // error message
      String errorMessage = "Requisição inválida: Tipo de documento inválido";
 
      // stubs
      doThrow(MultiExceptions.badRequest(errorMessage))
         .when(this.activityLogService).deleteByDocumentId(documentId, invalidDoc);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/activitiesLog/" + documentId + "/document")
         .param("doc", invalidDoc)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(400))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.activityLogService).deleteByDocumentId(documentId, invalidDoc);
   }
 
 
   @Test
   @DisplayName("DELETE - /activityLog/{id} - should return 404 when activity log does not exist")
   void deleteActivityById() throws Exception {
      // activity id
      String activityId = "activity123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Registro de atividade não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.activityLogService).deleteActivityLog(activityId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/activityLog/" + activityId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.activityLogService).deleteActivityLog(activityId);
   }

}