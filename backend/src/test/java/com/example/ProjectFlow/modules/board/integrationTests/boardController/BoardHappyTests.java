
// packages
package com.example.ProjectFlow.modules.board.integrationTests.boardController;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// jackson imports
import tools.jackson.databind.ObjectMapper;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// mockito imports
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;

// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import controller
import com.example.ProjectFlow.modules.board.controller.BoardController;

// import services
import com.example.ProjectFlow.modules.board.service.BoardService;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;

 
@WebMvcTest(BoardController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - BoardController")
public class BoardHappyTests {

   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // to convert java objects <-> json
   @Autowired
   private ObjectMapper objectMapper;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;
 
   // replaces real services for spring context mock
   @MockitoBean 
   private BoardService boardService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /board - should return created board and status 201")
   void createBoard() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      BoardDTO boardDTO = new BoardDTO(projectId, "Sprint 1 Board");
 
      // board response data
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         UUID.randomUUID(), projectId, boardDTO.name()
      );
 
      // stubs
      when(this.boardService.create(boardDTO)).thenReturn(boardResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/board")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(boardDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(boardResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.name").value(boardResponseDTO.name()));
 
      // checks
      verify(this.boardService).create(boardDTO);
   }
 
 
   @Test
   @DisplayName("GET - /boards - should return all boards and status 200")
   void getAllBoards() throws Exception {
      // boards data
      List<BoardResponseDTO> boards = List.of(
         new BoardResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), "Sprint 1 Board"
         ),
         new BoardResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), "Sprint 2 Board"
         )
      );
 
      // stubs
      when(this.boardService.getAll()).thenReturn(boards);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/boards")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(boards.size()))
         .andExpect(jsonPath("$.data[0].id").value(boards.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].projectId").value(boards.get(0).projectId().toString()))
         .andExpect(jsonPath("$.data[0].name").value(boards.get(0).name()))
         .andExpect(jsonPath("$.data[1].id").value(boards.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].projectId").value(boards.get(1).projectId().toString()))
         .andExpect(jsonPath("$.data[1].name").value(boards.get(1).name()));
 
      // checks
      verify(this.boardService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /board/{id} - should return the board and status 200")
   void getBoardById() throws Exception {
      // board data
      UUID boardId = UUID.randomUUID();
      BoardResponseDTO board = new BoardResponseDTO(
         boardId, UUID.randomUUID(), "Sprint 1 Board"
      );
 
      // stubs
      when(this.boardService.getById(boardId)).thenReturn(board);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/board/" + boardId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(board.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(board.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(board.name()));
 
      // checks
      verify(this.boardService).getById(boardId);
   }
 
 
   @Test
   @DisplayName("GET - /board/project/{projectId} - should return the project board and status 200")
   void getByProjectId() throws Exception {
      // board data
      UUID projectId = UUID.randomUUID();
      BoardResponseDTO board = new BoardResponseDTO(
         UUID.randomUUID(), projectId, "Sprint 1 Board"
      );
 
      // stubs
      when(this.boardService.getByProjectId(projectId)).thenReturn(board);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/board/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(board.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.name").value(board.name()));
 
      // checks
      verify(this.boardService).getByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("PUT - /board/{id}/name - should return status 200 when updating the name")
   void updateBoardName() throws Exception {
      // data - setup
      UUID boardId = UUID.randomUUID();
      String newName = "Sprint 1 Board - Updated";
 
      // update response data
      BoardResponseDTO updatedBoard = new BoardResponseDTO(
         boardId, UUID.randomUUID(), newName
      );
 
      // stubs
      when(this.boardService.updateName(boardId, newName)).thenReturn(updatedBoard);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/board/" + boardId + "/name")
         .param("n", newName)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedBoard.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(updatedBoard.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedBoard.name()));
 
      // checks
      verify(this.boardService).updateName(boardId, newName);
   }
 
 
   @Test
   @DisplayName("DELETE - /board/{id} - should return the deleted board and status 200")
   void deleteBoard() throws Exception {
      // data - setup
      UUID boardId = UUID.randomUUID();
      BoardDeletedDTO deletedBoard = new BoardDeletedDTO(
         boardId, UUID.randomUUID(), "Sprint 1 Board", LocalDateTime.now()
      );
 
      // stubs
      when(this.boardService.delete(boardId)).thenReturn(deletedBoard);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/board/" + boardId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedBoard.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(deletedBoard.projectId().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedBoard.name()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.boardService).delete(boardId);
   }

}