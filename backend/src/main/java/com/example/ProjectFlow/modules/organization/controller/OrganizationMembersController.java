
// packages
package com.example.ProjectFlow.modules.organization.controller;

// imports

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
   @PostMapping("/organization/members")
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

}