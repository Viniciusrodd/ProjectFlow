
// packages
package com.example.ProjectFlow.modules.organization.controller;

// imports
import java.util.List;
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;

// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class OrganizationMembersController {
   
   // properties
   private final OrganizationMemberService organizationMemberService;

   // constructor - dependency injection
   public OrganizationMembersController(OrganizationMemberService organizationMemberService) {
      this.organizationMemberService = organizationMemberService;
   }


   // create member participation
   @PostMapping("/organization/member")
   @Operation(summary = "Create a organization member participation")
   public ResponseEntity<ApiResponse<OrganizationMembersResponseDTO>> createMemberParticipation(
      @RequestBody OrganizationMembersDTO data
   ) {
      OrganizationMembersResponseDTO member = this.organizationMemberService.createMemberParticipation(data);

      ApiResponse<OrganizationMembersResponseDTO> response = new ApiResponse.Builder<OrganizationMembersResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(member)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all members
   @GetMapping(value = "/organizations/members")
   @Operation(summary = "Get all organizations members data")
   public ResponseEntity<ApiResponse<List<MemberByOrganizationResponseDTO>>> getAllOrganizationMembers() {
      List<MemberByOrganizationResponseDTO> members = this.organizationMemberService.getAllOrganizationMembers();

      ApiResponse<List<MemberByOrganizationResponseDTO>> response = new ApiResponse.Builder<List<MemberByOrganizationResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get member by relation id
   @GetMapping(value = "/organization/member/{id}")
   @Operation(summary = "Get organization member data by id")
   public ResponseEntity<ApiResponse<MemberByOrganizationResponseDTO>> getOrganizationMember(
      @PathVariable UUID id
   ) {
      MemberByOrganizationResponseDTO member = this.organizationMemberService.getOrganizationMemberById(id);

      ApiResponse<MemberByOrganizationResponseDTO> response = new ApiResponse.Builder<MemberByOrganizationResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(member)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all members by organization
   @GetMapping(value = "/organization/{organizationId}/members")
   @Operation(summary = "Get all members of an organization")
   public ResponseEntity<ApiResponse<List<MemberByOrganizationResponseDTO>>> getAllMembers(
      @PathVariable UUID organizationId
   ) {
      List<MemberByOrganizationResponseDTO> members = this.organizationMemberService.getAllMembersByOrganizationId(organizationId);

      ApiResponse<List<MemberByOrganizationResponseDTO>> response = new ApiResponse.Builder<List<MemberByOrganizationResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all members by role
   @GetMapping(value = "/organization/{organizationId}/members/role")
   @Operation(summary = "Get all members of an organization by role")
   public ResponseEntity<ApiResponse<List<MemberByOrganizationResponseDTO>>> getAllMembersByRole(
      @PathVariable UUID organizationId,
      @RequestParam String r
   ) {
      List<MemberByOrganizationResponseDTO> members = this.organizationMemberService.getAllMembersByRole(organizationId, r);

      ApiResponse<List<MemberByOrganizationResponseDTO>> response = new ApiResponse.Builder<List<MemberByOrganizationResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update member role
   @PutMapping(value = "/organization/member/{id}")
   @Operation(summary = "Update a member role")
   public ResponseEntity<ApiResponse<OrganizationMembersResponseDTO>> updateMemberRole(
      @PathVariable UUID id,
      @RequestParam String r
   ) {
      OrganizationMembersResponseDTO updatedMember = this.organizationMemberService.updateMemberRole(id, r);

      ApiResponse<OrganizationMembersResponseDTO> response = new ApiResponse.Builder<OrganizationMembersResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedMember)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // remove member participation
   @DeleteMapping(value = "/organization/member/{id}")
   @Operation(summary = "Remove a member's participation")
   public ResponseEntity<ApiResponse<OrganizationMembersDeletedDTO>> removeMemberParticipation(
      @PathVariable UUID id
   ) {
      OrganizationMembersDeletedDTO participationRemoved = this.organizationMemberService.removeParticipation(id);

      ApiResponse<OrganizationMembersDeletedDTO> response = new ApiResponse.Builder<OrganizationMembersDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(participationRemoved)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}