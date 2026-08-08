
// packages
package com.example.ProjectFlow.modules.organization.controller;

// imports
import java.util.List;
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class OrganizationController {
 
   // properties
   private OrganizationService organizationService;

   // constructor - dependency injection
   public OrganizationController(OrganizationService organizationService) {
      this.organizationService = organizationService;
   }


   // create organization
   @PostMapping("/organization")
   @Operation(summary = "Create a organization")
   public ResponseEntity<ApiResponse<OrganizationResponseDTO>> createOrganization(
      @RequestBody OrganizationDTO data
   ) {
      OrganizationResponseDTO organization = this.organizationService.create(data);

      ApiResponse<OrganizationResponseDTO> response = new ApiResponse.Builder<OrganizationResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(organization)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all organizations
   @GetMapping("/organizations")
   @Operation(summary = "Get all organizations")
   public ResponseEntity<ApiResponse<List<OrganizationResponseDTO>>> getAllOrganizations() {
      List<OrganizationResponseDTO> organizations = this.organizationService.getAll();

      ApiResponse<List<OrganizationResponseDTO>> response = new ApiResponse.Builder<List<OrganizationResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(organizations)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get organization by id
   @GetMapping("/organization/{id}")
   @Operation(summary = "Get organization by id")
   public ResponseEntity<ApiResponse<OrganizationResponseDTO>> getById(@PathVariable UUID id) {
      OrganizationResponseDTO organization = this.organizationService.getById(id);

      ApiResponse<OrganizationResponseDTO> response = new ApiResponse.Builder<OrganizationResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(organization)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get organization by owner id
   @GetMapping("/organizations/{ownerId}")
   @Operation(summary = "Get organizations by owner id")
   public ResponseEntity<ApiResponse<List<OrganizationResponseDTO>>> getByOwnerId(@PathVariable UUID ownerId) {
      List<OrganizationResponseDTO> organizations = this.organizationService.getByOwnerId(ownerId);

      ApiResponse<List<OrganizationResponseDTO>> response = new ApiResponse.Builder<List<OrganizationResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(organizations)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}