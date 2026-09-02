
// packages
package com.example.ProjectFlow.modules.project.controller;

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
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.MemberByProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersResponseDTO;

// import services
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

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


   // get member by relation id
   @GetMapping(value = "/project/member/{id}")
   @Operation(summary = "Get project member data by id")
   public ResponseEntity<ApiResponse<MemberByProjectResponseDTO>> getProjectMember(
      @PathVariable UUID id
   ) {
      MemberByProjectResponseDTO member = this.projectMemberService.getProjectMemberById(id);

      ApiResponse<MemberByProjectResponseDTO> response = new ApiResponse.Builder<MemberByProjectResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(member)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all members by project
   @GetMapping(value = "/project/{projectId}/members")
   @Operation(summary = "Get all members of an project")
   public ResponseEntity<ApiResponse<List<MemberByProjectResponseDTO>>> getAllMembers(
      @PathVariable UUID projectId
   ) {
      List<MemberByProjectResponseDTO> members = this.projectMemberService.getAllMembersByProjectId(projectId);

      ApiResponse<List<MemberByProjectResponseDTO>> response = new ApiResponse.Builder<List<MemberByProjectResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get project members by role
   @GetMapping(value = "/project/{projectId}/members/role")
   @Operation(summary = "Get all members of an project by role")
   public ResponseEntity<ApiResponse<List<MemberByProjectResponseDTO>>> getAllMembersByRole(
      @PathVariable UUID projectId,
      @RequestParam String r
   ) {
      List<MemberByProjectResponseDTO> members = this.projectMemberService.getAllMembersByRole(projectId, r);

      ApiResponse<List<MemberByProjectResponseDTO>> response = new ApiResponse.Builder<List<MemberByProjectResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(members)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // update member role
   @PutMapping(value = "/project/member/{id}")
   @Operation(summary = "Update a member role")
   public ResponseEntity<ApiResponse<ProjectMembersResponseDTO>> updateMemberRole(
      @PathVariable UUID id,
      @RequestParam String r
   ) {
      ProjectMembersResponseDTO updatedMember = this.projectMemberService.updateMemberRole(id, r);

      ApiResponse<ProjectMembersResponseDTO> response = new ApiResponse.Builder<ProjectMembersResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedMember)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // remove member participation
   @DeleteMapping(value = "/project/member/{id}")
   @Operation(summary = "Remove a member's participation")
   public ResponseEntity<ApiResponse<ProjectMembersDeletedDTO>> removeMemberParticipation(
      @PathVariable UUID id
   ) {
      ProjectMembersDeletedDTO participationRemoved = this.projectMemberService.removeParticipation(id);

      ApiResponse<ProjectMembersDeletedDTO> response = new ApiResponse.Builder<ProjectMembersDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(participationRemoved)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}