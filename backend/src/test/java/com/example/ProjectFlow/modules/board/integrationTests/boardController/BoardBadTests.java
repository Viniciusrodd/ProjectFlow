
// packages
package com.example.ProjectFlow.modules.board.integrationTests.boardController;

// imports
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
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(BoardController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - BoardController")
public class BoardBadTests {

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
   @DisplayName("POST - /board - should return 404 when project does not exist")
   void createBoard() throws Exception {
      // data - setup
      BoardDTO boardDTO = new BoardDTO(UUID.randomUUID(), "Sprint 1 Board");
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.boardService.create(boardDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/board")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(boardDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).create(boardDTO);
   }
 
 
   @Test
   @DisplayName("GET - /boards - should return 404 when there are no boards")
   void getAllBoards() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Quadros Kanban não existem";
 
      // stubs
      when(this.boardService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/boards")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /board/{id} - should return 404 when board does not exist")
   void getBoardById() throws Exception {
      // board id
      UUID boardId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Quadro Kanban não existe";
 
      // stubs
      when(this.boardService.getById(boardId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/board/" + boardId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).getById(boardId);
   }
 
 
   @Test
   @DisplayName("GET - /board/project/{projectId} - should return 404 when project has no board")
   void getByProjectId() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Quadro Kanban não existe";
 
      // stubs
      when(this.boardService.getByProjectId(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/board/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).getByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("PUT - /board/{id}/name - should return 404 when board does not exist")
   void updateBoardName() throws Exception {
      // data - setup
      UUID boardId = UUID.randomUUID();
      String newName = "Sprint 1 Board - Updated";
 
      // error message
      String errorMessage = "Recurso não encontrado: Quadro Kanban não existe";
 
      // stubs
      when(this.boardService.updateName(boardId, newName)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/board/" + boardId + "/name")
         .param("n", newName)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).updateName(boardId, newName);
   }
 
 
   @Test
   @DisplayName("DELETE - /board/{id} - should return 404 when board does not exist")
   void deleteBoard() throws Exception {
      // data - setup
      UUID boardId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Quadro Kanban não existe";
 
      // stubs
      when(this.boardService.delete(boardId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/board/" + boardId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardService).delete(boardId);
   }

}