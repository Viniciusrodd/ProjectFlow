
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectMemberController;

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
import com.example.ProjectFlow.modules.project.controller.ProjectMembersController;
 
// import services
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;
 
// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(ProjectMembersController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - ProjectMembersController")
public class ProjectMemberBadTests {

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
   private ProjectMemberService projectMemberService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;
 
 
   @Test
   @DisplayName("POST - /project/member - should return 404 when user does not exist")
   void createMemberParticipation() throws Exception {
      // data - setup
      ProjectMembersDTO membersDTO = new ProjectMembersDTO(
         UUID.randomUUID(), UUID.randomUUID(), "MEMBER"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Usuário não existe";
 
      // stubs
      when(this.projectMemberService.createMemberParticipation(membersDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/project/member")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(membersDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).createMemberParticipation(membersDTO);
   }
 
 
   @Test
   @DisplayName("GET - /projects/members - should return 404 when there are no members")
   void getAllProjectMembers() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.projectMemberService.getAllProjectMembers()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).getAllProjectMembers();
   }
 
 
   @Test
   @DisplayName("GET - /project/member/{id} - should return 404 when member does not exist")
   void getProjectMember() throws Exception {
      // member id
      UUID memberId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Membro não existe";
 
      // stubs
      when(this.projectMemberService.getProjectMemberById(memberId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/member/" + memberId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).getProjectMemberById(memberId);
   }
 
 
   @Test
   @DisplayName("GET - /project/{projectId}/members - should return 404 when project members does not exist")
   void getAllMembers() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.projectMemberService.getAllMembersByProjectId(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId + "/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).getAllMembersByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /project/{projectId}/members/role - should return 404 when there are no members with the role")
   void getAllMembersByRole() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      String role = "ADMIN";
 
      // error message
      String errorMessage = "Recurso não encontrado: Membros não existem";
 
      // stubs
      when(this.projectMemberService.getAllMembersByRole(projectId, role)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId + "/members/role")
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).getAllMembersByRole(projectId, role);
   }
 
 
   @Test
   @DisplayName("PUT - /project/member/{id}/{adminId} - should return 404 when member participation does not exist")
   void updateMemberRole() throws Exception {
      // data - setup
      UUID memberId = UUID.randomUUID();
      UUID adminId = UUID.randomUUID();
      String role = "ADMIN";
 
      // error message
      String errorMessage = "Recurso não encontrado: Participação não existe";
 
      // stubs
      when(this.projectMemberService.updateMemberRole(memberId, adminId, role)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/project/member/" + memberId + "/" + adminId)
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).updateMemberRole(memberId, adminId, role);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/member/{id}/{adminId} - should return 404 when member participation does not exist")
   void removeMemberParticipation() throws Exception {
      // data - setup
      UUID memberId = UUID.randomUUID();
      UUID adminId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Participação não existe";
 
      // stubs
      when(this.projectMemberService.removeParticipation(memberId, adminId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/project/member/" + memberId + "/" + adminId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectMemberService).removeParticipation(memberId, adminId);
   }

}