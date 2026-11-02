
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectMemberController;

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
import com.example.ProjectFlow.modules.project.controller.ProjectMembersController;
 
// import services
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;
 
// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.MemberByProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;
 
// import enums
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(ProjectMembersController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - ProjectMembersController")
public class ProjectMemberHappyTests {
   
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
   @DisplayName("POST - /project/member - should return created member participation and status 201")
   void createMemberParticipation() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      ProjectMembersDTO membersDTO = new ProjectMembersDTO(projectId, userId, "MEMBER");
 
      // member response data
      ProjectMembersResponseDTO memberResponseDTO = new ProjectMembersResponseDTO(
         projectId, userId, RoleEnum.MEMBER, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectMemberService.createMemberParticipation(membersDTO)).thenReturn(memberResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/project/member")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(membersDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.role").value(memberResponseDTO.role().name()))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty());
 
      // checks
      verify(this.projectMemberService).createMemberParticipation(membersDTO);
   }
 
 
   @Test
   @DisplayName("GET - /projects/members - should return all projects members and status 200")
   void getAllProjectMembers() throws Exception {
      // members data
      List<MemberByProjectResponseDTO> members = List.of(
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         ),
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.MEMBER, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.projectMemberService.getAllProjectMembers()).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(members.size()))
         .andExpect(jsonPath("$.data[0].id").value(members.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].user.id").value(members.get(0).user().id().toString()))
         .andExpect(jsonPath("$.data[0].user.name").value(members.get(0).user().name()))
         .andExpect(jsonPath("$.data[0].user.email").value(members.get(0).user().email()))
         .andExpect(jsonPath("$.data[0].role").value(members.get(0).role().name()))
         .andExpect(jsonPath("$.data[1].id").value(members.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].user.id").value(members.get(1).user().id().toString()))
         .andExpect(jsonPath("$.data[1].user.name").value(members.get(1).user().name()))
         .andExpect(jsonPath("$.data[1].user.email").value(members.get(1).user().email()))
         .andExpect(jsonPath("$.data[1].role").value(members.get(1).role().name()));
 
      // checks
      verify(this.projectMemberService).getAllProjectMembers();
   }
 
 
   @Test
   @DisplayName("GET - /project/member/{id} - should return the member and status 200")
   void getProjectMember() throws Exception {
      // member data
      UUID memberId = UUID.randomUUID();
      MemberByProjectResponseDTO member = new MemberByProjectResponseDTO(
         memberId, 
         new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", "profileImage123"), 
         RoleEnum.ADMIN, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectMemberService.getProjectMemberById(memberId)).thenReturn(member);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/member/" + memberId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(member.id().toString()))
         .andExpect(jsonPath("$.data.user.id").value(member.user().id().toString()))
         .andExpect(jsonPath("$.data.user.name").value(member.user().name()))
         .andExpect(jsonPath("$.data.user.email").value(member.user().email()))
         .andExpect(jsonPath("$.data.user.profileImageId").value(member.user().profileImageId()))
         .andExpect(jsonPath("$.data.role").value(member.role().name()))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty());
 
      // checks
      verify(this.projectMemberService).getProjectMemberById(memberId);
   }
 
 
   @Test
   @DisplayName("GET - /project/{projectId}/members - should return all project members and status 200")
   void getAllMembers() throws Exception {
      // members data
      UUID projectId = UUID.randomUUID();
      List<MemberByProjectResponseDTO> members = List.of(
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         ),
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.MEMBER, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.projectMemberService.getAllMembersByProjectId(projectId)).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId + "/members")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(members.size()))
         .andExpect(jsonPath("$.data[0].id").value(members.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].user.name").value(members.get(0).user().name()))
         .andExpect(jsonPath("$.data[0].role").value(members.get(0).role().name()))
         .andExpect(jsonPath("$.data[1].id").value(members.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].user.name").value(members.get(1).user().name()))
         .andExpect(jsonPath("$.data[1].role").value(members.get(1).role().name()));
 
      // checks
      verify(this.projectMemberService).getAllMembersByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /project/{projectId}/members/role - should return the members of the role and status 200")
   void getAllMembersByRole() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      String role = "ADMIN";
      List<MemberByProjectResponseDTO> members = List.of(
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         ),
         new MemberByProjectResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.projectMemberService.getAllMembersByRole(projectId, role)).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId + "/members/role")
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(members.size()))
         .andExpect(jsonPath("$.data[0].id").value(members.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].user.name").value(members.get(0).user().name()))
         .andExpect(jsonPath("$.data[0].role").value(role))
         .andExpect(jsonPath("$.data[1].id").value(members.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].user.name").value(members.get(1).user().name()))
         .andExpect(jsonPath("$.data[1].role").value(role));
 
      // checks
      verify(this.projectMemberService).getAllMembersByRole(projectId, role);
   }
 
 
   @Test
   @DisplayName("PUT - /project/member/{id}/{adminId} - should return the updated member and status 200")
   void updateMemberRole() throws Exception {
      // data - setup
      UUID memberId = UUID.randomUUID();
      UUID adminId = UUID.randomUUID();
      String role = "ADMIN";
 
      // update response data
      ProjectMembersResponseDTO updatedMember = new ProjectMembersResponseDTO(
         UUID.randomUUID(), UUID.randomUUID(), RoleEnum.ADMIN, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectMemberService.updateMemberRole(memberId, adminId, role)).thenReturn(updatedMember);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/project/member/" + memberId + "/" + adminId)
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.projectId").value(updatedMember.projectId().toString()))
         .andExpect(jsonPath("$.data.userId").value(updatedMember.userId().toString()))
         .andExpect(jsonPath("$.data.role").value(role))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty());
 
      // checks
      verify(this.projectMemberService).updateMemberRole(memberId, adminId, role);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/member/{id}/{adminId} - should return the removed participation and status 200")
   void removeMemberParticipation() throws Exception {
      // data - setup
      UUID memberId = UUID.randomUUID();
      UUID adminId = UUID.randomUUID();
      ProjectMembersDeletedDTO removedMember = new ProjectMembersDeletedDTO(
         UUID.randomUUID(), UUID.randomUUID(), RoleEnum.MEMBER, 
         LocalDateTime.now().minusDays(10), LocalDateTime.now()
      );
 
      // stubs
      when(this.projectMemberService.removeParticipation(memberId, adminId)).thenReturn(removedMember);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/project/member/" + memberId + "/" + adminId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.projectId").value(removedMember.projectId().toString()))
         .andExpect(jsonPath("$.data.userId").value(removedMember.userId().toString()))
         .andExpect(jsonPath("$.data.role").value(removedMember.role().name()))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty())
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.projectMemberService).removeParticipation(memberId, adminId);
   }

}