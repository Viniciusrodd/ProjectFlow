
// packages
package com.example.ProjectFlow.modules.attachment.controller;

// imports
import java.util.UUID;

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;

// import DTOs
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH + "/task/{taskId}/attachment")
public class AttachmentController {
 
   // properties
   private final AttachmentService attachmentService;
   private final AttachmentMapper attachmentMapper;

   // constructor - dependency injection
   public AttachmentController(
      AttachmentService attachmentService,
      AttachmentMapper attachmentMapper
   ) {
      this.attachmentService = attachmentService;
      this.attachmentMapper = attachmentMapper;
   }


   // task attachment upload
   @PostMapping(value = "/{uploadedBy}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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


   // get task attachment - infos
   @GetMapping()
   @Operation(summary = "Get task attachment data")
   public ResponseEntity<ApiResponse<AttachmentResponseDTO>> getTaskAttachment(
      @PathVariable UUID taskId
   ) {
      AttachmentDocument attachmentDocument = this.attachmentService.getTaskAttachment(taskId);
      AttachmentResponseDTO attachmentData = this.attachmentMapper.toAttachmentResponseDTO(attachmentDocument);

      ApiResponse<AttachmentResponseDTO> response = new ApiResponse.Builder<AttachmentResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(attachmentData)
         .build();
      
      return ResponseEntity.status(HttpStatus.OK).body(response);
   }


   // get task attachment - download
   @GetMapping(value = "/download")
   @Operation(summary = "Download task attachment")
   public ResponseEntity<byte[]> getTaskAttachmentData(@PathVariable UUID taskId) {
      AttachmentDocument document = this.attachmentService.getTaskAttachment(taskId);

      return ResponseEntity
         .ok()
         .contentType(MediaType.parseMediaType(document.getMimeType()))
         .header("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"")
         .body(document.getBinary());
   }

}