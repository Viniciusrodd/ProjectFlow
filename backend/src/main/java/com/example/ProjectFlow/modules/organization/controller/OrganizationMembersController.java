
// packages
package com.example.ProjectFlow.modules.organization.controller;

// imports

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersCompleteResponseDTO;
// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersResponseDTO;

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
   @PostMapping(value = "/organization/members")
   @Operation(summary = "Create a organization member participation")
   public ResponseEntity<ApiResponse<OrganizationMembersResponseDTO>> createMemberParticipation(
      @RequestBody OrganizationMembersDTO data
   ) {
      OrganizationMembersResponseDTO organizationMember = this.organizationMemberService.createMemberParticipation(data);

      ApiResponse<OrganizationMembersResponseDTO> response = new ApiResponse.Builder<OrganizationMembersResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(organizationMember)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all organization members
   @GetMapping(value = "/organization/{organizationId}/members")
   @Operation(summary = "Get all members of an organization")
   public ResponseEntity<ApiResponse<List<OrganizationMembersCompleteResponseDTO>>> getAllMembers(
      @PathVariable UUID organizationId
   ) {
      List<OrganizationMembersCompleteResponseDTO> members = this.organizationMemberService.getAllMembersByOrganizationId(organizationId);

      ApiResponse<List<OrganizationMembersCompleteResponseDTO>> response = new ApiResponse.Builder<List<OrganizationMembersCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all members by role
   @GetMapping(value = "/organization/{organizationId}/members/role/{role}")
   @Operation(summary = "Get all members of an organization by role")
   public ResponseEntity<ApiResponse<List<OrganizationMembersCompleteResponseDTO>>> getAllMembersByRole(
      @PathVariable UUID organizationId,
      @PathVariable String role
   ) {
      List<OrganizationMembersCompleteResponseDTO> members = this.organizationMemberService.getAllMembersByRole(organizationId, role);

      ApiResponse<List<OrganizationMembersCompleteResponseDTO>> response = new ApiResponse.Builder<List<OrganizationMembersCompleteResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}