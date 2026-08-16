
// packages
package com.example.ProjectFlow.modules.project.controller;

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
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class ProjectMembersController {
   
   // properties
   private final ProjectMemberService projectMemberService;

   // constructor - dependency injection
   public ProjectMembersController(ProjectMemberService projectMemberService) {
      this.projectMemberService = projectMemberService;
   }


   // create member participation
   @PostMapping("/project/member")
   @Operation(summary = "Create a project member participation")
   public ResponseEntity<ApiResponse<ProjectMembersResponseDTO>> createMemberParticipation(
      @RequestBody ProjectMembersDTO data
   ) {
      ProjectMembersResponseDTO member = this.projectMemberService.createMemberParticipation(data);

      ApiResponse<ProjectMembersResponseDTO> response = new ApiResponse.Builder<ProjectMembersResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(member)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}