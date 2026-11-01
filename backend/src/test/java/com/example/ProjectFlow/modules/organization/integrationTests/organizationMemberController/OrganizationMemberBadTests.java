
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationMemberController;

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
import com.example.ProjectFlow.modules.organization.controller.OrganizationMembersController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;
 
// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(OrganizationMembersController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - OrganizationMembersController")
public class OrganizationMemberBadTests {

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
   private OrganizationMemberService organizationMemberService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /organization/member - should return 404 when user does not exist")
   void createMemberParticipation() throws Exception {
      // data - setup
      OrganizationMembersDTO membersDTO = new OrganizationMembersDTO(
         UUID.randomUUID(), UUID.randomUUID(), "MEMBER"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Usuário não existe";
 
      // stubs
      when(this.organizationMemberService.createMemberParticipation(membersDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/organization/member")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(membersDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).createMemberParticipation(membersDTO);
   }
 
 
   @Test
   @DisplayName("GET - /organizations/members - should return 404 when there are no members")
   void getAllOrganizationMembers() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.organizationMemberService.getAllOrganizationMembers()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).getAllOrganizationMembers();
   }
 
 
   @Test
   @DisplayName("GET - /organization/member/{id} - should return 404 when member does not exist")
   void getOrganizationMember() throws Exception {
      // member id
      UUID memberId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Membro não existe";
 
      // stubs
      when(this.organizationMemberService.getOrganizationMemberById(memberId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/member/" + memberId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).getOrganizationMemberById(memberId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/members - should return 404 when organization does not exist")
   void getAllMembers() throws Exception {
      // organization id
      UUID organizationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.organizationMemberService.getAllMembersByOrganizationId(organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).getAllMembersByOrganizationId(organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/member/{userId} - should return 404 when member does not exist")
   void getMemberByOrganization() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Membro não existe";
 
      // stubs
      when(this.organizationMemberService.getMemberByOrganizationId(userId, organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/member/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).getMemberByOrganizationId(userId, organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/members/role - should return 404 when there are no members with the role")
   void getAllMembersByRoleNotFound() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      String role = "ADMIN";
 
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.organizationMemberService.getAllMembersByRole(organizationId, role)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/members/role")
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).getAllMembersByRole(organizationId, role);
   }
 
 
   @Test
   @DisplayName("PUT - /organization/member/{id}/{adminId} - should return 404 when member participation does not exist")
   void updateMemberRoleNotFound() throws Exception {
      // data - setup
      UUID adminId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();
      String role = "ADMIN";
 
      // error message
      String errorMessage = "Recurso não encontrado: Participação não existe";
 
      // stubs
      when(this.organizationMemberService.updateMemberRole(memberId, adminId, role)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/organization/member/" + memberId + "/" + adminId)
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).updateMemberRole(memberId, adminId, role);
   }

 
   @Test
   @DisplayName("DELETE - /organization/member/{id}/{adminId} - should return 404 when member participation does not exist")
   void removeMemberParticipationNotFound() throws Exception {
      // id's - setup
      UUID adminId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Participação não existe";
 
      // stubs
      when(this.organizationMemberService.removeParticipation(memberId, adminId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/organization/member/" + memberId + "/" + adminId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationMemberService).removeParticipation(memberId, adminId);
   }

}