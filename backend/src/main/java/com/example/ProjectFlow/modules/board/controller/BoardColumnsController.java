
// packages
package com.example.ProjectFlow.modules.board.controller;

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
import com.example.ProjectFlow.modules.board.service.BoardColumnService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class BoardColumnsController {
 
   // properties
   private final BoardColumnService boardColumnService;

   // constructor - dependency injection
   public BoardColumnsController(BoardColumnService boardColumnService) {
      this.boardColumnService = boardColumnService;
   }


   // create board column
   @PostMapping("/board/column")
   @Operation(summary = "Create a board column")
   public ResponseEntity<ApiResponse<BoardColumnsResponseDTO>> createBoardColumn(
      @RequestBody BoardColumnsDTO data
   ) {
      BoardColumnsResponseDTO boardColumns = this.boardColumnService.create(data);

      ApiResponse<BoardColumnsResponseDTO> response = new ApiResponse.Builder<BoardColumnsResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(boardColumns)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all board columns
   @GetMapping(value = "/board/{boardId}/columns")
   @Operation(summary = "Get all board columns")
   public ResponseEntity<ApiResponse<List<BoardColumnsResponseDTO>>> getAllBoardColumns(
      @PathVariable UUID boardId
   ) {
      List<BoardColumnsResponseDTO> boardColumns = this.boardColumnService.getAllColumnsByBoardId(boardId);

      ApiResponse<List<BoardColumnsResponseDTO>> response = new ApiResponse.Builder<List<BoardColumnsResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(boardColumns)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}