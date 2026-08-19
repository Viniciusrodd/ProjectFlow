
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


   // get board by id
   @GetMapping(value = "/board/{id}")
   @Operation(summary = "Get board by id")
   public ResponseEntity<ApiResponse<BoardResponseDTO>> getBoardById(@PathVariable UUID id) {
      BoardResponseDTO board = this.boardService.getById(id);

      ApiResponse<BoardResponseDTO> response = new ApiResponse.Builder<BoardResponseDTO>()
         .success(true)
         .statusCode(HttpStatus.OK.value())
         .message(ResponseMessages.FOUND)
         .data(board)
         .build();

      return ResponseEntity.status(HttpStatus.OK).body(response);
   }

}