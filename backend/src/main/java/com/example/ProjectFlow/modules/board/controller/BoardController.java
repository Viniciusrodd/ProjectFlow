
// packages
package com.example.ProjectFlow.modules.board.controller;

// imports

// web imports
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// http imports
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// swagger imports
import io.swagger.v3.oas.annotations.Operation;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import services
import com.example.ProjectFlow.modules.board.service.BoardService;

// import responses
import com.example.ProjectFlow.common.responses.ApiResponse;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.BoardResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@RestController
@RequestMapping(ApiConstants.BASE_API_PATH)
public class BoardController {
 
   // properties
   private final BoardService boardService;

   // constructor - dependency injection
   public BoardController(BoardService boardService) {
      this.boardService = boardService;
   }


   // board creation
   @PostMapping("/board")
   @Operation(summary = "Create a board")
   public ResponseEntity<ApiResponse<BoardResponseDTO>> createBoard(@RequestBody BoardDTO data) {
      BoardResponseDTO board = this.boardService.create(data);

      ApiResponse<BoardResponseDTO> response = new ApiResponse.Builder<BoardResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.CREATED.value())
         .message(ResponseMessages.CREATED)
         .data(board)
         .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


   // get all boards
   @GetMapping("/boards")
   @Operation(summary = "Get all boards")
   public ResponseEntity<ApiResponse<List<BoardResponseDTO>>> getAllBoards() {
      List<BoardResponseDTO> boards = this.boardService.getAll();

      ApiResponse<List<BoardResponseDTO>> response = new ApiResponse.Builder<List<BoardResponseDTO>>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(boards)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}