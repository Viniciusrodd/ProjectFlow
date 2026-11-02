
// packages
package com.example.ProjectFlow.modules.labels.integrationTests.labelController;

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
import com.example.ProjectFlow.modules.labels.controller.LabelController;

// import services
import com.example.ProjectFlow.modules.labels.service.LabelService;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDeletedDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(LabelController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - LabelController")
public class LabelHappyTests {

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
   @DisplayName("POST - /label - should return created label and status 201")
   void createLabel() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      LabelsDTO labelDTO = new LabelsDTO(projectId, "Bug", "#FF0000");
 
      // label response data
      LabelsResponseDTO labelResponseDTO = new LabelsResponseDTO(
         UUID.randomUUID(), projectId, labelDTO.name(), labelDTO.color()
      );
 
      // stubs
      when(this.labelService.create(labelDTO)).thenReturn(labelResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/label")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(labelDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(labelResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.name").value(labelResponseDTO.name()))
         .andExpect(jsonPath("$.data.color").value(labelResponseDTO.color()));
 
      // checks
      verify(this.labelService).create(labelDTO);
   }
 
 
   @Test
   @DisplayName("GET - /labels/project/{projectId} - should return all labels of the project and status 200")
   void getAllByProjectId() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // labels data
      List<LabelsResponseDTO> labels = List.of(
         new LabelsResponseDTO(UUID.randomUUID(), projectId, "Bug", "#FF0000"),
         new LabelsResponseDTO(UUID.randomUUID(), projectId, "Feature", "#00FF00")
      );
 
      // stubs
      when(this.labelService.getAllByProjectId(projectId)).thenReturn(labels);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/labels/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(labels.size()))
         .andExpect(jsonPath("$.data[0].id").value(labels.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data[0].name").value(labels.get(0).name()))
         .andExpect(jsonPath("$.data[0].color").value(labels.get(0).color()))
         .andExpect(jsonPath("$.data[1].id").value(labels.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data[1].name").value(labels.get(1).name()))
         .andExpect(jsonPath("$.data[1].color").value(labels.get(1).color()));
 
      // checks
      verify(this.labelService).getAllByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /label/{id} - should return the label and status 200")
   void getLabelById() throws Exception {
      // label data
      UUID labelId = UUID.randomUUID();
      LabelsResponseDTO label = new LabelsResponseDTO(
         labelId, UUID.randomUUID(), "Bug", "#FF0000"
      );
 
      // stubs
      when(this.labelService.getById(labelId)).thenReturn(label);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(label.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(label.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(label.name()))
         .andExpect(jsonPath("$.data.color").value(label.color()));
 
      // checks
      verify(this.labelService).getById(labelId);
   }
 
 
   @Test
   @DisplayName("PUT - /label/{id} - should return status 200 when updating the label")
   void updateLabel() throws Exception {
      // data - setup
      UUID labelId = UUID.randomUUID();
      LabelsUpdateDTO updateDTO = new LabelsUpdateDTO("Bug - Updated", "#0000FF");
 
      // updated label data
      LabelsResponseDTO updatedLabel = new LabelsResponseDTO(
         labelId, UUID.randomUUID(), updateDTO.name(), updateDTO.color()
      );
 
      // stubs
      when(this.labelService.update(labelId, updateDTO)).thenReturn(updatedLabel);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedLabel.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(updatedLabel.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedLabel.name()))
         .andExpect(jsonPath("$.data.color").value(updatedLabel.color()));
 
      // checks
      verify(this.labelService).update(labelId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /label/{id} - should return the deleted label and status 200")
   void deleteLabel() throws Exception {
      // data - setup
      UUID labelId = UUID.randomUUID();
      LabelsDeletedDTO deletedLabel = new LabelsDeletedDTO(
         labelId, UUID.randomUUID(), "Bug", "#FF0000", LocalDateTime.now()
      );
 
      // stubs
      when(this.labelService.delete(labelId)).thenReturn(deletedLabel);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedLabel.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(deletedLabel.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedLabel.name()))
         .andExpect(jsonPath("$.data.color").value(deletedLabel.color()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.labelService).delete(labelId);
   }

}