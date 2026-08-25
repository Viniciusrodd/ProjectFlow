
// packages
package com.example.ProjectFlow.modules.organization.controller;

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
import com.example.ProjectFlow.modules.organization.service.OrganizationImageService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;
// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/organization/logo-image")
public class OrganizationImageController {
 
   // properties
   private final OrganizationImageService organizationImageService;

   // constructor - dependency injection
   public OrganizationImageController(OrganizationImageService organizationImageService) {
      this.organizationImageService = organizationImageService;
   }


   // upload organization image
   @PostMapping(value = "/{organizationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @Operation(summary = "Upload organization image")
   public ResponseEntity<ApiResponse<OrganizationImageResponseDTO>> uploadOrganizationImage(
      @PathVariable UUID organizationId, 
      @RequestParam MultipartFile file
   ) {
      OrganizationImageResponseDTO organizationImageData = this.organizationImageService.uploadOrganizationImage(organizationId, file);

      ApiResponse<OrganizationImageResponseDTO> response = new ApiResponse.Builder<OrganizationImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(organizationImageData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   
   // get organization image - infos
   @GetMapping(value = "/{organizationId}")
   @Operation(summary = "Get organization image data informations")
   public ResponseEntity<ApiResponse<OrganizationImageResponseDTO>> getOrganizationImage(
      @PathVariable UUID organizationId
   ) {
      OrganizationImageDocument organizationImageDocument = this.organizationImageService.getOrganizationImage(organizationId);
      OrganizationImageResponseDTO organizationImage = OrganizationImageResponseDTO.get(organizationImageDocument);

      ApiResponse<OrganizationImageResponseDTO> response = new ApiResponse.Builder<OrganizationImageResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(organizationImage)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get organization image - download
   @GetMapping(
      value = "/{organizationId}/download",
      produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp", "image/jpg" }
   )
   @Operation(summary = "Download organization image")
   public ResponseEntity<byte[]> getOrganizationImageData(@PathVariable UUID organizationId) {
      OrganizationImageDocument document = this.organizationImageService.getOrganizationImage(organizationId);

      return ResponseEntity
         .ok()
         .contentType(MediaType.parseMediaType(document.getMimeType()))
         .header("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"")
         .body(document.getBinary());
   }


   // delete organization image
   @DeleteMapping(value = "{organizationId}")
   @Operation(summary = "Organization image delete")
   public ResponseEntity<ApiResponse<Void>> deleteOrganizationImage(@PathVariable UUID organizationId) {
      this.organizationImageService.deleteOrganizationImage(organizationId);

      ApiResponse<Void> response = new ApiResponse.Builder<Void>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.DELETED)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }
}