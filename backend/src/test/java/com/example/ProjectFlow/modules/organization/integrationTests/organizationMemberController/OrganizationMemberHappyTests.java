
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationMemberController;

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
import com.example.ProjectFlow.modules.organization.controller.OrganizationMembersController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(OrganizationMembersController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - OrganizationMembersController")
public class OrganizationMemberHappyTests {
   
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
   @DisplayName("POST - /organization/member - should return created member participation and status 201")
   void createMemberParticipation() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      OrganizationMembersDTO membersDTO = new OrganizationMembersDTO(
         organizationId, userId, "MEMBER"
      );
 
      // member response data
      OrganizationMembersResponseDTO memberResponseDTO = new OrganizationMembersResponseDTO(
         UUID.randomUUID(), organizationId, userId, RoleEnum.MEMBER, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationMemberService.createMemberParticipation(membersDTO)).thenReturn(memberResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/organization/member")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(membersDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(memberResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.role").value(memberResponseDTO.role().name()))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty());
 
      // checks
      verify(this.organizationMemberService).createMemberParticipation(membersDTO);
   }
 
 
   @Test
   @DisplayName("GET - /organizations/members - should return all organizations members and status 200")
   void getAllOrganizationMembers() throws Exception {
      // members data
      List<MemberByOrganizationResponseDTO> members = List.of(
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         ),
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.MEMBER, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.organizationMemberService.getAllOrganizationMembers()).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organizations/members")
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
      verify(this.organizationMemberService).getAllOrganizationMembers();
   }
 
 
   @Test
   @DisplayName("GET - /organization/member/{id} - should return the member and status 200")
   void getOrganizationMember() throws Exception {
      // member data
      UUID memberId = UUID.randomUUID();
      MemberByOrganizationResponseDTO member = new MemberByOrganizationResponseDTO(
         memberId, 
         new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", "profileImage123"), 
         RoleEnum.ADMIN, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationMemberService.getOrganizationMemberById(memberId)).thenReturn(member);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/member/" + memberId)
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
      verify(this.organizationMemberService).getOrganizationMemberById(memberId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/members - should return all organization members and status 200")
   void getAllMembers() throws Exception {
      // members data
      UUID organizationId = UUID.randomUUID();
      List<MemberByOrganizationResponseDTO> members = List.of(
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.OWNER, LocalDateTime.now()
         ),
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.MEMBER, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.organizationMemberService.getAllMembersByOrganizationId(organizationId)).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/members")
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
      verify(this.organizationMemberService).getAllMembersByOrganizationId(organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/member/{userId} - should return the organization member and status 200")
   void getMemberByOrganization() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      MemberByOrganizationResponseDTO member = new MemberByOrganizationResponseDTO(
         UUID.randomUUID(), 
         new UserProfileDTO(userId, "vinicius@gmail.com", "vinicius", null), 
         RoleEnum.MEMBER, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationMemberService.getMemberByOrganizationId(userId, organizationId)).thenReturn(member);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/member/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(member.id().toString()))
         .andExpect(jsonPath("$.data.user.id").value(userId.toString()))
         .andExpect(jsonPath("$.data.user.name").value(member.user().name()))
         .andExpect(jsonPath("$.data.user.email").value(member.user().email()))
         .andExpect(jsonPath("$.data.role").value(member.role().name()));
 
      // checks
      verify(this.organizationMemberService).getMemberByOrganizationId(userId, organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /organization/{organizationId}/members/role - should return the members of the role and status 200")
   void getAllMembersByRole() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      String role = "ADMIN";
      List<MemberByOrganizationResponseDTO> members = List.of(
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "vinicius@gmail.com", "vinicius", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         ),
         new MemberByOrganizationResponseDTO(
            UUID.randomUUID(), 
            new UserProfileDTO(UUID.randomUUID(), "maria@gmail.com", "maria", null), 
            RoleEnum.ADMIN, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.organizationMemberService.getAllMembersByRole(organizationId, role)).thenReturn(members);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/organization/" + organizationId + "/members/role")
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
      verify(this.organizationMemberService).getAllMembersByRole(organizationId, role);
   }
 
 
   @Test
   @DisplayName("PUT - /organization/member/{id}/{adminId} - should return the updated member and status 200")
   void updateMemberRole() throws Exception {
      // data - setup
      UUID adminId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();
      String role = "ADMIN";
 
      // update response data
      OrganizationMembersResponseDTO updatedMember = new OrganizationMembersResponseDTO(
         memberId, UUID.randomUUID(), UUID.randomUUID(), RoleEnum.ADMIN, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationMemberService.updateMemberRole(memberId, adminId, role)).thenReturn(updatedMember);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/organization/member/" + memberId + "/" + adminId)
         .param("r", role)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedMember.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(updatedMember.organizationId().toString()))
         .andExpect(jsonPath("$.data.userId").value(updatedMember.userId().toString()))
         .andExpect(jsonPath("$.data.role").value(role))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty());
 
      // checks
      verify(this.organizationMemberService).updateMemberRole(memberId, adminId, role);
   }
 
 
   @Test
   @DisplayName("DELETE - /organization/member/{id}/{adminId} - should return the removed participation and status 200")
   void removeMemberParticipation() throws Exception {
      // data - setup
      UUID adminId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();
      OrganizationMembersDeletedDTO removedMember = new OrganizationMembersDeletedDTO(
         UUID.randomUUID(), UUID.randomUUID(), RoleEnum.MEMBER, 
         LocalDateTime.now().minusDays(10), LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationMemberService.removeParticipation(memberId, adminId)).thenReturn(removedMember);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/organization/member/" + memberId + "/" + adminId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.organizationId").value(removedMember.organizationId().toString()))
         .andExpect(jsonPath("$.data.userId").value(removedMember.userId().toString()))
         .andExpect(jsonPath("$.data.role").value(removedMember.role().name()))
         .andExpect(jsonPath("$.data.joinedAt").isNotEmpty())
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.organizationMemberService).removeParticipation(memberId, adminId);
   }

}