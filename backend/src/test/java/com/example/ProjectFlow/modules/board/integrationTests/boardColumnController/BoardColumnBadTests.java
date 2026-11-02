
// packages
package com.example.ProjectFlow.modules.board.integrationTests.boardColumnController;

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
import com.example.ProjectFlow.modules.board.controller.BoardColumnsController;
 
// import services
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
 
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(BoardColumnsController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - BoardColumnsController")
public class BoardColumnBadTests {
   
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
   private BoardColumnService boardColumnService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/board";


   @Test
   @DisplayName("POST - /board/column - should return 404 when board does not exist")
   void createBoardColumn() throws Exception {
      // data - setup
      BoardColumnsDTO columnDTO = new BoardColumnsDTO(
         UUID.randomUUID(), "TO DO", 1, "#FFFFFF"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Quadro Kanban não existe";
 
      // stubs
      when(this.boardColumnService.create(columnDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/column")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(columnDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardColumnService).create(columnDTO);
   }
 
 
   @Test
   @DisplayName("GET - /board/{boardId}/columns - should return 404 when board has no columns")
   void getAllBoardColumns() throws Exception {
      // board id
      UUID boardId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Colunas não existem";
 
      // stubs
      when(this.boardColumnService.getAllColumnsByBoardId(boardId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + boardId + "/columns")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardColumnService).getAllColumnsByBoardId(boardId);
   }
 
 
   @Test
   @DisplayName("GET - /board/column/{id} - should return 404 when column does not exist")
   void getBoardColumnById() throws Exception {
      // column id
      UUID columnId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Coluna não existe";
 
      // stubs
      when(this.boardColumnService.getColumnById(columnId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardColumnService).getColumnById(columnId);
   }
 
 
   @Test
   @DisplayName("PUT - /board/column/{id} - should return 404 when column does not exist")
   void updateColumn() throws Exception {
      // data - setup
      UUID columnId = UUID.randomUUID();
      BoardColumnsUpdateDTO updateDTO = new BoardColumnsUpdateDTO("IN_PROGRESS", 2, "#CCCCCC");
 
      // error message
      String errorMessage = "Recurso não encontrado: Coluna não existe";
 
      // stubs
      when(this.boardColumnService.update(columnId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardColumnService).update(columnId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /board/column/{id} - should return 404 when column does not exist")
   void deleteBoardColumnNotFound() throws Exception {
      // data - setup
      UUID columnId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Coluna não existe";
 
      // stubs
      when(this.boardColumnService.delete(columnId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.boardColumnService).delete(columnId);
   }

}