
// packages
package com.example.ProjectFlow.modules.project.controller;

// imports
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.project.service.ProjectImageService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;
import com.example.ProjectFlow.modules.project.dto.projectImageDTO.ProjectImageResponseDTO;
// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/project/logo-image")
public class ProjectImageController {
 
   // properties
   private final ProjectImageService projectImageService;
   
   // constructor - dependency injection
   public ProjectImageController(ProjectImageService projectImageService) {
      this.projectImageService = projectImageService;
   }


   // upload project image
   @PostMapping(value = "/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @Operation(summary = "Upload project image")
   public ResponseEntity<ApiResponse<ProjectImageResponseDTO>> uploadProjectImage(
      @PathVariable UUID projectId,
      @RequestParam MultipartFile file
   ) {
      ProjectImageResponseDTO projectImageData = this.projectImageService.uploadProjectImage(projectId, file);

      ApiResponse<ProjectImageResponseDTO> response = new ApiResponse.Builder<ProjectImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(projectImageData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get project image - infos
   @GetMapping(value = "/{projectId}")
   @Operation(summary = "Get project image data information")
   public ResponseEntity<ApiResponse<ProjectImageResponseDTO>> getProjectImage(
      @PathVariable UUID projectId
   ) {
      ProjectImageDocument projectImageDocument = this.projectImageService.getProjectImage(projectId);
      ProjectImageResponseDTO projectImage = ProjectImageResponseDTO.get(projectImageDocument);

      ApiResponse<ProjectImageResponseDTO> response = new ApiResponse.Builder<ProjectImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(projectImage)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }   


   // get project image - download
   @GetMapping(
      value = "/{projectId}/download",
      produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp", "image/jpg" }
   )
   @Operation(summary = "Download project image")
   public ResponseEntity<byte[]> getProjectImageData(@PathVariable UUID projectId) {
      ProjectImageDocument document = this.projectImageService.getProjectImage(projectId);

      return ResponseEntity
         .ok()
         .contentType(MediaType.parseMediaType(document.getMimeType()))
         .header("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"")
         .body(document.getBinary());
   }


   // delete project image
   @DeleteMapping(value = "/{projectId}")
   @Operation(summary = "Delete project image")
   public ResponseEntity<ApiResponse<Void>> deleteProjectImage(@PathVariable UUID projectId) {
      this.projectImageService.deleteProjectImage(projectId);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }   

}