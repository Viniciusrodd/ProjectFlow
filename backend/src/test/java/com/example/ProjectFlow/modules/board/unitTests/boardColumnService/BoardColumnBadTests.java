
// packages
package com.example.ProjectFlow.modules.board.unitTests.boardColumnService;

// imports
import java.util.List;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.board.repository.BoardColumnsRepository;
 
// import validator
import com.example.ProjectFlow.modules.board.validator.BoardColumnsValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.board.service.BoardService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
 
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
 
// import mapper
import com.example.ProjectFlow.modules.board.mapper.BoardColumnsMapper;
 
// jakarta imports
import jakarta.persistence.NoResultException;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - BoardColumnService")
public class BoardColumnBadTests {
 
   // mock dependencies
   @Mock
   private BoardColumnsRepository boardColumnsRepository;
   @Mock
   private BoardColumnsValidator boardColumnsValidator;
   @Mock
   private BoardService boardService;
   @Mock
   private BoardColumnsMapper boardColumnsMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private BoardColumnService boardColumnService;
 
 
   @Test
   @DisplayName("create() - must throw when column name already exists")
   void create() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
 
      // DTO - setup
      BoardColumnsDTO boardColumnsDTO = new BoardColumnsDTO(
         boardId, "to do", 1, "#ff0000"
      );
 
      // stubs
      when(this.boardColumnsRepository.checkColumnNameExistence(boardColumnsDTO.name())).thenReturn(true);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.create(boardColumnsDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Dados inválidos: A coluna 'to do' já existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).boardIdValidate(boardId);
      verify(this.boardColumnsValidator, times(1)).nameValidate(boardColumnsDTO.name());
      verify(this.boardColumnsValidator, times(1)).positionValidate(boardColumnsDTO.position());
      verify(this.boardColumnsValidator, times(1)).colorValidate(boardColumnsDTO.color());
      verify(this.boardColumnsRepository, times(1)).checkColumnNameExistence(boardColumnsDTO.name());
 
      // no checks
      verify(this.boardColumnsRepository, never()).checkColumnPositionExistence(anyInt());
      verify(this.boardColumnsRepository, never()).create(any(), any());
      verifyNoInteractions(this.boardService);
      verifyNoInteractions(this.boardColumnsMapper);
   }


   @Test
   @DisplayName("getAllColumnsByBoardId() - must throw when column list is empty")
   void getAllColumnsByBoardId() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // list - setup
      List<BoardColumnsEntity> columnsList = List.of();
 
      // stubs
      when(this.boardService.existsById(boardId)).thenReturn(true);
      when(this.boardColumnsRepository.getAllColumnsByBoardId(boardId)).thenReturn(columnsList);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.getAllColumnsByBoardId(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Colunas não existem");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).boardIdValidate(boardId);
      verify(this.boardService, times(1)).existsById(boardId);
      verify(this.boardColumnsRepository, times(1)).getAllColumnsByBoardId(boardId);
 
      // no checks
      verifyNoInteractions(this.boardColumnsMapper);
   }


   @Test
   @DisplayName("getColumnById() - must throw column not found")
   void getColumnById() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.getColumnById(columnId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.getColumnById(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).getColumnById(columnId);
 
      // no checks
      verifyNoInteractions(this.boardColumnsMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw column not found")
   void getEntityById() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.getEntityById(columnId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.getEntityById(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).getEntityById(columnId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when column does not exist")
   void existsById() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.existsById(columnId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.existsById(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).existsById(columnId);
   }


   @Test
   @DisplayName("update() - must throw when column does not exist")
   void update() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // DTO - setup
      BoardColumnsUpdateDTO boardColumnsUpdateDTO = new BoardColumnsUpdateDTO(
         "progress", 2, "#00ff00"
      );
 
      // stubs
      when(this.boardColumnsRepository.checkColumnNameExistence(boardColumnsUpdateDTO.name())).thenReturn(false);
      when(this.boardColumnsRepository.checkColumnPositionExistence(boardColumnsUpdateDTO.position())).thenReturn(false);
      when(this.boardColumnsRepository.update(columnId, boardColumnsUpdateDTO)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.update(columnId, boardColumnsUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsValidator, times(1)).updateValidations(boardColumnsUpdateDTO);
      verify(this.boardColumnsRepository, times(1)).checkColumnNameExistence(boardColumnsUpdateDTO.name());
      verify(this.boardColumnsRepository, times(1)).checkColumnPositionExistence(boardColumnsUpdateDTO.position());
      verify(this.boardColumnsRepository, times(1)).update(columnId, boardColumnsUpdateDTO);
 
      // no checks
      verifyNoInteractions(this.boardColumnsMapper);
   }


   @Test
   @DisplayName("delete() - must throw when column does not exist")
   void delete() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.delete(columnId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.delete(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).delete(columnId);
 
      // no checks
      verifyNoInteractions(this.boardColumnsMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when column does not exist")
   void isDeleted() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.isDeleted(columnId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardColumnService.isDeleted(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Coluna não existe");
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).isDeleted(columnId);
   }

}