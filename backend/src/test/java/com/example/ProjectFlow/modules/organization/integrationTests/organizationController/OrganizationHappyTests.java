
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationController;

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
import com.example.ProjectFlow.modules.organization.controller.OrganizationController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.organization.service.OrganizationCreationService;
 
// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(OrganizationController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - OrganizationController")
public class OrganizationHappyTests {
 
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
   @DisplayName("POST - /organization - should return created organization and status 201")
   void createOrganization() throws Exception {
      // organization data
      UUID ownerId = UUID.randomUUID();
      OrganizationDTO organizationDTO = new OrganizationDTO(
         ownerId, "ProjectFlow Team", "Organization for the ProjectFlow team"
      );
 
      // organization response data
      OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO(
         UUID.randomUUID(), ownerId, organizationDTO.name(), organizationDTO.description()
      );
 
      // stubs
      when(this.organizationCreationService.create(organizationDTO)).thenReturn(organizationResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/organization")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(organizationDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(organizationResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data.name").value(organizationResponseDTO.name()))
         .andExpect(jsonPath("$.data.description").value(organizationResponseDTO.description()));
 
      // checks
      verify(this.organizationCreationService).create(organizationDTO);
   }
 
 
   @Test
   @DisplayName("GET - /organizations - should return all organizations and status 200")
   void getAllOrganizations() throws Exception {
      // organizations data
      List<OrganizationResponseDTO> organizations = List.of(
         new OrganizationResponseDTO(UUID.randomUUID(), UUID.randomUUID(), "ProjectFlow Team", "First organization"),
         new OrganizationResponseDTO(UUID.randomUUID(), UUID.randomUUID(), "Acme Inc", "Second organization")
      );
 
      // stubs
      when(this.organizationService.getAll()).thenReturn(organizations);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(organizations.size()))
         .andExpect(jsonPath("$.data[0].id").value(organizations.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].ownerId").value(organizations.get(0).ownerId().toString()))
         .andExpect(jsonPath("$.data[0].name").value(organizations.get(0).name()))
         .andExpect(jsonPath("$.data[0].description").value(organizations.get(0).description()))
         .andExpect(jsonPath("$.data[1].id").value(organizations.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].ownerId").value(organizations.get(1).ownerId().toString()))
         .andExpect(jsonPath("$.data[1].name").value(organizations.get(1).name()))
         .andExpect(jsonPath("$.data[1].description").value(organizations.get(1).description()));
 
      // checks
      verify(this.organizationService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /organization/{id} - should return the organization and status 200")
   void getById() throws Exception {
      // organization data
      UUID organizationId = UUID.randomUUID();
      OrganizationResponseDTO organization = new OrganizationResponseDTO(
         organizationId, UUID.randomUUID(), "ProjectFlow Team", "Organization for the ProjectFlow team"
      );
 
      // stubs
      when(this.organizationService.getById(organizationId)).thenReturn(organization);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(organization.id().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(organization.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(organization.name()))
         .andExpect(jsonPath("$.data.description").value(organization.description()));
 
      // checks
      verify(this.organizationService).getById(organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /organizations/{ownerId} - should return the owner organizations and status 200")
   void getByOwnerId() throws Exception {
      // organizations data
      UUID ownerId = UUID.randomUUID();
      List<OrganizationResponseDTO> organizations = List.of(
         new OrganizationResponseDTO(UUID.randomUUID(), ownerId, "ProjectFlow Team", "First organization"),
         new OrganizationResponseDTO(UUID.randomUUID(), ownerId, "Acme Inc", "Second organization")
      );
 
      // stubs
      when(this.organizationService.getByOwnerId(ownerId)).thenReturn(organizations);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(organizations.size()))
         .andExpect(jsonPath("$.data[0].id").value(organizations.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data[0].name").value(organizations.get(0).name()))
         .andExpect(jsonPath("$.data[1].id").value(organizations.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data[1].name").value(organizations.get(1).name()));
 
      // checks
      verify(this.organizationService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /organization/{id}/user/{userId} - should return status 200 when updating (only the name)")
   void updateOrganization() throws Exception {
      // update data
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      OrganizationUpdateDTO updateDTO = new OrganizationUpdateDTO("ProjectFlow Team updated", null);
 
      // update response data
      OrganizationResponseDTO updatedOrganization = new OrganizationResponseDTO(
         organizationId, userId, updateDTO.name(), "Organization for the ProjectFlow team"
      );
 
      // stubs
      when(this.organizationService.update(organizationId, userId, updateDTO)).thenReturn(updatedOrganization);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/organization/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedOrganization.id().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(updatedOrganization.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedOrganization.name()))
         .andExpect(jsonPath("$.data.description").value(updatedOrganization.description()));
 
      // checks
      verify(this.organizationService).update(organizationId, userId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /organization/{id}/user/{userId} - should return the deleted organization and status 200")
   void deleteOrganization() throws Exception {
      // delete data
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      OrganizationDeletedDTO deletedOrganization = new OrganizationDeletedDTO(
         organizationId, userId, "ProjectFlow Team", 
         "Organization for the ProjectFlow team", LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationService.delete(organizationId, userId)).thenReturn(deletedOrganization);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/organization/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedOrganization.id().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(deletedOrganization.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedOrganization.name()))
         .andExpect(jsonPath("$.data.description").value(deletedOrganization.description()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.organizationService).delete(organizationId, userId);
   }

}