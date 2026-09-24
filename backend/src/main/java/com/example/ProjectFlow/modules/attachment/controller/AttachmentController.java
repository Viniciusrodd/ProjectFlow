
// packages
package com.example.ProjectFlow.modules.attachment.controller;

// imports
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import document

// import DTOs
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/task/{taskId}/attachment/{uploadedBy}")
public class AttachmentController {
 
   // properties
   private final AttachmentService attachmentService;

   // constructor - dependency injection
   public AttachmentController(AttachmentService attachmentService) {
      this.attachmentService = attachmentService;
   }


   // task attachment upload
   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @Operation(summary = "Upload task attachment")
   public ResponseEntity<ApiResponse<AttachmentResponseDTO>> uploadTaskAttachment(
      @PathVariable UUID taskId,
      @PathVariable UUID uploadedBy,
      @RequestParam MultipartFile file
   ) {
      AttachmentResponseDTO attachmentData = this.attachmentService.uploadAttachment(taskId, uploadedBy, file);

      ApiResponse<AttachmentResponseDTO> response = new ApiResponse.Builder<AttachmentResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(attachmentData)
         .build();
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}