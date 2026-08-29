
// packages
package com.example.ProjectFlow.modules.labels.controller;

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

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;

// import services
import com.example.ProjectFlow.modules.labels.service.LabelService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class LabelController {
 
   // properties
   private final LabelService labelService;

   // constructor - dependency injection
   public LabelController(LabelService labelService) {
      this.labelService = labelService;
   }


   // label creation
   @PostMapping("/label")
   @Operation(summary = "Create a task label")
   public ResponseEntity<ApiResponse<LabelsResponseDTO>> createLabel(@RequestBody LabelsDTO data) {
      LabelsResponseDTO label = this.labelService.create(data);

      ApiResponse<LabelsResponseDTO> response = new ApiResponse.Builder<LabelsResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(label)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all task labels by project id
   @GetMapping(value = "/labels/project/{projectId}")
   @Operation(summary = "Get all project task labels")
   public ResponseEntity<ApiResponse<List<LabelsResponseDTO>>> getAllByProjectId(@PathVariable UUID projectId) {
      List<LabelsResponseDTO> labels = this.labelService.getAllByProjectId(projectId);

      ApiResponse<List<LabelsResponseDTO>> response = new ApiResponse.Builder<List<LabelsResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(labels)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}