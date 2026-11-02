
// packages
package com.example.ProjectFlow.modules.board.integrationTests.boardColumnController;

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
import com.example.ProjectFlow.modules.board.controller.BoardColumnsController;

// import services
import com.example.ProjectFlow.modules.board.service.BoardColumnService;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsResponseDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsUpdateDTO;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(BoardColumnsController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - BoardColumnsController")
public class BoardColumnHappyTests {

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
   @DisplayName("POST - /board/column - should return created column and status 201")
   void createBoardColumn() throws Exception {
      // data - setup
      UUID boardId = UUID.randomUUID();
      BoardColumnsDTO columnDTO = new BoardColumnsDTO(
         boardId, "TO DO", 1, "#FFFFFF"
      );
 
      // column response data
      BoardColumnsResponseDTO columnResponseDTO = new BoardColumnsResponseDTO(
         UUID.randomUUID(), boardId, BoardEnum.TODO, columnDTO.position(), columnDTO.color()
      );
 
      // stubs
      when(this.boardColumnService.create(columnDTO)).thenReturn(columnResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/column")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(columnDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(columnResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.boardId").value(boardId.toString()))
         .andExpect(jsonPath("$.data.name").value(columnResponseDTO.name().name()))
         .andExpect(jsonPath("$.data.position").value(columnResponseDTO.position()))
         .andExpect(jsonPath("$.data.color").value(columnResponseDTO.color()));
 
      // checks
      verify(this.boardColumnService).create(columnDTO);
   }
 
 
   @Test
   @DisplayName("GET - /board/{boardId}/columns - should return all columns and status 200")
   void getAllBoardColumns() throws Exception {
      // board id
      UUID boardId = UUID.randomUUID();
      
      // columns data
      List<BoardColumnsResponseDTO> columns = List.of(
         new BoardColumnsResponseDTO(
            UUID.randomUUID(), boardId, BoardEnum.TODO, 1, "#FFFFFF"
         ),
         new BoardColumnsResponseDTO(
            UUID.randomUUID(), boardId, BoardEnum.PROGRESS, 2, "#000000"
         )
      );
 
      // stubs
      when(this.boardColumnService.getAllColumnsByBoardId(boardId)).thenReturn(columns);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + boardId + "/columns")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(columns.size()))
         .andExpect(jsonPath("$.data[0].id").value(columns.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].boardId").value(boardId.toString()))
         .andExpect(jsonPath("$.data[0].name").value(columns.get(0).name().name()))
         .andExpect(jsonPath("$.data[0].position").value(columns.get(0).position()))
         .andExpect(jsonPath("$.data[0].color").value(columns.get(0).color()))
         .andExpect(jsonPath("$.data[1].id").value(columns.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].boardId").value(boardId.toString()))
         .andExpect(jsonPath("$.data[1].name").value(columns.get(1).name().name()))
         .andExpect(jsonPath("$.data[1].position").value(columns.get(1).position()))
         .andExpect(jsonPath("$.data[1].color").value(columns.get(1).color()));
 
      // checks
      verify(this.boardColumnService).getAllColumnsByBoardId(boardId);
   }
 
 
   @Test
   @DisplayName("GET - /board/column/{id} - should return the column and status 200")
   void getBoardColumnById() throws Exception {
      // column data
      UUID columnId = UUID.randomUUID();
      BoardColumnsResponseDTO column = new BoardColumnsResponseDTO(
         columnId, UUID.randomUUID(), BoardEnum.TODO, 1, "#FFFFFF"
      );
 
      // stubs
      when(this.boardColumnService.getColumnById(columnId)).thenReturn(column);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(column.id().toString()))
         .andExpect(jsonPath("$.data.boardId").value(column.boardId().toString()))
         .andExpect(jsonPath("$.data.name").value(column.name().name()))
         .andExpect(jsonPath("$.data.position").value(column.position()))
         .andExpect(jsonPath("$.data.color").value(column.color()));
 
      // checks
      verify(this.boardColumnService).getColumnById(columnId);
   }
 
 
   @Test
   @DisplayName("PUT - /board/column/{id} - should return status 200 when updating the column")
   void updateColumn() throws Exception {
      // data - setup
      UUID columnId = UUID.randomUUID();
      BoardColumnsUpdateDTO updateDTO = new BoardColumnsUpdateDTO(
         "IN_PROGRESS", 2, "#CCCCCC"
      );
 
      // update response data
      BoardColumnsResponseDTO updatedColumn = new BoardColumnsResponseDTO(
         columnId, UUID.randomUUID(), BoardEnum.PROGRESS, updateDTO.position(), updateDTO.color()
      );
 
      // stubs
      when(this.boardColumnService.update(columnId, updateDTO)).thenReturn(updatedColumn);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedColumn.id().toString()))
         .andExpect(jsonPath("$.data.boardId").value(updatedColumn.boardId().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedColumn.name().name()))
         .andExpect(jsonPath("$.data.position").value(updatedColumn.position()))
         .andExpect(jsonPath("$.data.color").value(updatedColumn.color()));
 
      // checks
      verify(this.boardColumnService).update(columnId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /board/column/{id} - should return the deleted column and status 200")
   void deleteColumn() throws Exception {
      // data - setup
      UUID columnId = UUID.randomUUID();
      BoardColumnsDeletedDTO deletedColumn = new BoardColumnsDeletedDTO(
         columnId, UUID.randomUUID(), BoardEnum.TODO, 1, "#FFFFFF", LocalDateTime.now()
      );
 
      // stubs
      when(this.boardColumnService.delete(columnId)).thenReturn(deletedColumn);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedColumn.id().toString()))
         .andExpect(jsonPath("$.data.boardId").value(deletedColumn.boardId().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedColumn.name().name()))
         .andExpect(jsonPath("$.data.position").value(deletedColumn.position()))
         .andExpect(jsonPath("$.data.color").value(deletedColumn.color()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.boardColumnService).delete(columnId);
   }

}