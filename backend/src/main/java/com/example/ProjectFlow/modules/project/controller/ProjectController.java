
// packages
package com.example.ProjectFlow.modules.project.controller;

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
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;

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
   public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(
      @RequestBody ProjectDTO data
   ) {
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
}