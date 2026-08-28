
// packages
package com.example.ProjectFlow.modules.comment.controller;

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

// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;

// import services
import com.example.ProjectFlow.modules.comment.service.CommentService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class CommentController {
 
   // properties
   private final CommentService commentService;

   // constructor - dependency injection
   public CommentController(CommentService commentService) {
      this.commentService = commentService;
   }


   // comment creation
   @PostMapping("/comment")
   @Operation(summary = "Create a task comment")
   public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(@RequestBody CommentDTO data) {
      CommentResponseDTO comment = this.commentService.create(data);

      ApiResponse<CommentResponseDTO> response = new ApiResponse.Builder<CommentResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(comment)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}