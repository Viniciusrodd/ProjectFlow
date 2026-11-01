
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationController;

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
import com.example.ProjectFlow.modules.organization.controller.OrganizationController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.organization.service.OrganizationCreationService;
 
// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(OrganizationController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - OrganizationController")
public class OrganizationBadTests {
   
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
   private OrganizationService organizationService;
   @MockitoBean 
   private OrganizationCreationService organizationCreationService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;
 
 
   @Test
   @DisplayName("POST - /organization - should return 404 when owner does not exist")
   void create() throws Exception {
      // organization data
      OrganizationDTO organizationDTO = new OrganizationDTO(
         UUID.randomUUID(), "ProjectFlow Team", "Organization for the ProjectFlow team"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Usuário não existe";
 
      // stubs
      when(this.organizationCreationService.create(organizationDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/organization")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(organizationDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationCreationService).create(organizationDTO);
   }
 
 
   @Test
   @DisplayName("GET - /organizations - should return 404 when there are no organizations")
   void getAllOrganizations() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Organizações não existem";
 
      // stubs
      when(this.organizationService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /organization/{id} - should return 404 when organization does not exist")
   void getById() throws Exception {
      // organization id
      UUID organizationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Organização não existe";
 
      // stubs
      when(this.organizationService.getById(organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationService).getById(organizationId);
   }
 
  
   @Test
   @DisplayName("GET - /organizations/{ownerId} - should return 404 when owner has no organizations")
   void getByOwnerId() throws Exception {
      // owner id
      UUID ownerId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Organizações não existem";
 
      // stubs
      when(this.organizationService.getByOwnerId(ownerId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /organization/{id}/user/{userId} - should return 404 when organization does not exist")
   void updateOrganization() throws Exception {
      // update data
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      OrganizationUpdateDTO updateDTO = new OrganizationUpdateDTO("ProjectFlow Team updated", "New description");
 
      // error message
      String errorMessage = "Recurso não encontrado: Organização não existe";
 
      // stubs
      when(this.organizationService.update(organizationId, userId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/organization/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationService).update(organizationId, userId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /organization/{id}/user/{userId} - should return 404 when organization does not exist")
   void deleteOrganizationNotFound() throws Exception {
      // delete data
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Organização não existe";
 
      // stubs
      when(this.organizationService.delete(organizationId, userId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/organization/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationService).delete(organizationId, userId);
   }

}