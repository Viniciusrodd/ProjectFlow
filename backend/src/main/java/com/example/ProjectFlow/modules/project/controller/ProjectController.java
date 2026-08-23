
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

// import services
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectUpdateDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class ProjectController {
   
   // properties
   private final ProjectService projectService;

   // constructor - dependency injection
   public ProjectController(ProjectService projectService) {
      this.projectService = projectService;
   }


   // project creation
   @PostMapping("/project")
   @Operation(summary = "Create a project")
   public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(@RequestBody ProjectDTO data) {
      ProjectResponseDTO project = this.projectService.create(data);

      ApiResponse<ProjectResponseDTO> response = new ApiResponse.Builder<ProjectResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(project)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all projects
   @GetMapping("/projects")
   @Operation(summary = "Get all projects")
   public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getAllProjects() {
      List<ProjectResponseDTO> projects = this.projectService.getAll();

      ApiResponse<List<ProjectResponseDTO>> response = new ApiResponse.Builder<List<ProjectResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(projects)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get project by id
   @GetMapping(value = "/project/{id}")
   @Operation(summary = "Get project by id")
   public ResponseEntity<ApiResponse<ProjectResponseDTO>> getProjectById(@PathVariable UUID id) {
      ProjectResponseDTO project = this.projectService.getById(id);

      ApiResponse<ProjectResponseDTO> response = new ApiResponse.Builder<ProjectResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(project)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all projects by organization id
   @GetMapping(value = "/projects/organization/{organizationId}")
   @Operation(summary = "Get all projects by organization id")
   public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getByOrganizationId(@PathVariable UUID organizationId) {
      List<ProjectResponseDTO> projects = this.projectService.getByOrganizationId(organizationId);

      ApiResponse<List<ProjectResponseDTO>> response = new ApiResponse.Builder<List<ProjectResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(projects)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get all projects by owner id
   @GetMapping(value = "/projects/owner/{ownerId}")
   @Operation(summary = "Get all projects by owner id")
   public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getByOwnerId(@PathVariable UUID ownerId) {
      List<ProjectResponseDTO> projects = this.projectService.getByOwnerId(ownerId);

      ApiResponse<List<ProjectResponseDTO>> response = new ApiResponse.Builder<List<ProjectResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(projects)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // project update
   @PutMapping(value = "/project/{id}")
   @Operation(summary = "Update project")
   public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProject(
      @PathVariable UUID id,
      @RequestBody ProjectUpdateDTO data
   ) {
      ProjectResponseDTO updatedProject = this.projectService.update(id, data);

      ApiResponse<ProjectResponseDTO> response = new ApiResponse.Builder<ProjectResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedProject)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // project status update
   @PutMapping(value = "/project/{id}/status")
   @Operation(summary = "Update status of project")
   public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProjectStatus(
      @PathVariable UUID id,
      @RequestParam String s
   ) {
      ProjectResponseDTO updatedProject = this.projectService.updateStatus(id, s);

      ApiResponse<ProjectResponseDTO> response = new ApiResponse.Builder<ProjectResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.UPDATED)
         .data(updatedProject)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // delete project
   @DeleteMapping(value = "/project/{id}")
   @Operation(summary = "Delete project")
   public ResponseEntity<ApiResponse<ProjectDeletedDTO>> deleteProject(@PathVariable UUID id) {
      ProjectDeletedDTO deletedProject = this.projectService.delete(id);

      ApiResponse<ProjectDeletedDTO> response = new ApiResponse.Builder<ProjectDeletedDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .data(deletedProject)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


}