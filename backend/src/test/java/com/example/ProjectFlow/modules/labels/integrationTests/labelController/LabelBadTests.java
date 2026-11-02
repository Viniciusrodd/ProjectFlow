
// packages
package com.example.ProjectFlow.modules.labels.integrationTests.labelController;

// imports
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
import com.example.ProjectFlow.modules.labels.controller.LabelController;
 
// import services
import com.example.ProjectFlow.modules.labels.service.LabelService;
 
// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(LabelController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - LabelController")
public class LabelBadTests {
 
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
   private LabelService labelService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /label - should return 404 when project does not exist")
   void createLabel() throws Exception {
      // data - setup
      LabelsDTO labelDTO = new LabelsDTO(UUID.randomUUID(), "Bug", "#FF0000");
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.labelService.create(labelDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/label")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(labelDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.labelService).create(labelDTO);
   }
 
 
   @Test
   @DisplayName("GET - /labels/project/{projectId} - should return 404 when project has no labels")
   void getAllByProjectId() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiquetas do projeto não existem";
 
      // stubs
      when(this.labelService.getAllByProjectId(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/labels/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.labelService).getAllByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /label/{id} - should return 404 when label does not exist")
   void getLabelById() throws Exception {
      // label id
      UUID labelId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiqueta de projeto não existe";
 
      // stubs
      when(this.labelService.getById(labelId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.labelService).getById(labelId);
   }
 
 
   @Test
   @DisplayName("PUT - /label/{id} - should return 404 when label does not exist")
   void updateLabel() throws Exception {
      // data - setup
      UUID labelId = UUID.randomUUID();
      LabelsUpdateDTO updateDTO = new LabelsUpdateDTO("Bug - Updated", "#0000FF");
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiqueta de projeto não existe";
 
      // stubs
      when(this.labelService.update(labelId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.labelService).update(labelId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /label/{id} - should return 404 when label does not exist")
   void deleteLabel() throws Exception {
      // label id
      UUID labelId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiqueta de projeto não existe";
 
      // stubs
      when(this.labelService.delete(labelId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.labelService).delete(labelId);
   }

}