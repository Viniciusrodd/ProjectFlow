
// packages
package com.example.ProjectFlow.modules.board.unitTests.boardColumnService;

// imports
import java.time.LocalDateTime;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.board.repository.BoardColumnsRepository;
 
// import validator
import com.example.ProjectFlow.modules.board.validator.BoardColumnsValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsResponseDTO;
import com.example.ProjectFlow.modules.board.dto.boardColumnsDTO.BoardColumnsUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.board.service.BoardService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
 
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.board.entity.BoardEntity;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;

// import mapper
import com.example.ProjectFlow.modules.board.mapper.BoardColumnsMapper;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - BoardColumnService")
public class BoardColumnHappyTests {
 
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
   @DisplayName("create() - must create a board column")
   void create() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
      UUID boardId = UUID.randomUUID();
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .build();
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.TODO)
         .position(1)
         .color("#ff0000")
         .build();
 
      // DTO's - setup
      BoardColumnsDTO boardColumnsDTO = new BoardColumnsDTO(
         boardId, boardColumnsEntity.getName().toString(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor()
      );
 
      BoardColumnsResponseDTO boardColumnsResponseDTO = new BoardColumnsResponseDTO(
         columnId, boardId, boardColumnsEntity.getName(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor()
      );
 
      // stubs
      when(this.boardColumnsRepository.checkColumnNameExistence(boardColumnsDTO.name())).thenReturn(false);
      when(this.boardColumnsRepository.checkColumnPositionExistence(boardColumnsDTO.position())).thenReturn(false);
      when(this.boardService.getEntityById(boardId)).thenReturn(boardEntity);
      when(this.boardColumnsRepository.create(boardColumnsDTO, boardEntity)).thenReturn(boardColumnsEntity);
      when(this.boardColumnsMapper.toBoardColumnsResponseDTO(boardColumnsEntity)).thenReturn(boardColumnsResponseDTO);
 
      // act and assert
      BoardColumnsResponseDTO result = this.boardColumnService.create(boardColumnsDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardColumnsResponseDTO);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).boardIdValidate(boardId);
      verify(this.boardColumnsValidator, times(1)).nameValidate(boardColumnsDTO.name());
      verify(this.boardColumnsValidator, times(1)).positionValidate(boardColumnsDTO.position());
      verify(this.boardColumnsValidator, times(1)).colorValidate(boardColumnsDTO.color());
 
      verify(this.boardColumnsRepository, times(1)).checkColumnNameExistence(boardColumnsDTO.name());
      verify(this.boardColumnsRepository, times(1)).checkColumnPositionExistence(boardColumnsDTO.position());
      verify(this.boardService, times(1)).getEntityById(boardId);
 
      verify(this.boardColumnsRepository, times(1)).create(boardColumnsDTO, boardEntity);
      verify(this.boardColumnsMapper, times(1)).toBoardColumnsResponseDTO(boardColumnsEntity);
   }


   @Test
   @DisplayName("getAllColumnsByBoardId() - must return all columns by board id")
   void getAllColumnsByBoardId() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
      UUID boardId = UUID.randomUUID();
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.TODO)
         .position(1)
         .color("#ff0000")
         .build();
 
      // list - setup
      List<BoardColumnsEntity> columnsList = List.of(boardColumnsEntity);
 
      // DTO - setup
      BoardColumnsResponseDTO boardColumnsResponseDTO = new BoardColumnsResponseDTO(
         columnId, boardId, boardColumnsEntity.getName(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor()
      );
 
      // stubs
      when(this.boardColumnsRepository.getAllColumnsByBoardId(boardId)).thenReturn(columnsList);
      when(this.boardColumnsMapper.toBoardColumnsResponseDTO(boardColumnsEntity)).thenReturn(boardColumnsResponseDTO);
 
      // act and assert
      List<BoardColumnsResponseDTO> result = this.boardColumnService.getAllColumnsByBoardId(boardId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(boardColumnsResponseDTO);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).boardIdValidate(boardId);
      verify(this.boardService, times(1)).existsById(boardId);
      verify(this.boardColumnsRepository, times(1)).getAllColumnsByBoardId(boardId);
      verify(this.boardColumnsMapper, times(1)).toBoardColumnsResponseDTO(boardColumnsEntity);
   }


   @Test
   @DisplayName("getColumnById() - must return column by id")
   void getColumnById() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
      UUID boardId = UUID.randomUUID();
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.TODO)
         .position(1)
         .color("#ff0000")
         .build();
 
      // DTO - setup
      BoardColumnsResponseDTO boardColumnsResponseDTO = new BoardColumnsResponseDTO(
         columnId, boardId, boardColumnsEntity.getName(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor()
      );
 
      // stubs
      when(this.boardColumnsRepository.getColumnById(columnId)).thenReturn(boardColumnsEntity);
      when(this.boardColumnsMapper.toBoardColumnsResponseDTO(boardColumnsEntity)).thenReturn(boardColumnsResponseDTO);
 
      // act and assert
      BoardColumnsResponseDTO result = this.boardColumnService.getColumnById(columnId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardColumnsResponseDTO);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).getColumnById(columnId);
      verify(this.boardColumnsMapper, times(1)).toBoardColumnsResponseDTO(boardColumnsEntity);
   }


   @Test
   @DisplayName("getEntityById() - must return column entity by id")
   void getEntityById() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .build();
 
      // stubs
      when(this.boardColumnsRepository.getEntityById(columnId)).thenReturn(boardColumnsEntity);
 
      // act and assert
      BoardColumnsEntity result = this.boardColumnService.getEntityById(columnId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardColumnsEntity);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).getEntityById(columnId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if column exist by id")
   void existsById() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.existsById(columnId)).thenReturn(true);
 
      // act and assert
      boolean result = this.boardColumnService.existsById(columnId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).existsById(columnId);
   }


   @Test
   @DisplayName("update() - must update column (name, position and color)")
   void update() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
      UUID boardId = UUID.randomUUID();
 
      // data to update
      BoardEnum newName = BoardEnum.PROGRESS;
      int newPosition = 2;
      String newColor = "#00ff00";
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(newName)
         .position(newPosition)
         .color(newColor)
         .build();
 
      // DTO's - setup
      BoardColumnsUpdateDTO boardColumnsUpdateDTO = new BoardColumnsUpdateDTO(
         newName.toString(), newPosition, newColor
      );
 
      BoardColumnsResponseDTO boardColumnsResponseDTO = new BoardColumnsResponseDTO(
         columnId, boardId, boardColumnsEntity.getName(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor()
      );
 
      // stubs
      when(this.boardColumnsRepository.checkColumnNameExistence(newName.toString())).thenReturn(false);
      when(this.boardColumnsRepository.checkColumnPositionExistence(newPosition)).thenReturn(false);
      when(this.boardColumnsRepository.update(columnId, boardColumnsUpdateDTO)).thenReturn(boardColumnsEntity);
      when(this.boardColumnsMapper.toBoardColumnsResponseDTO(boardColumnsEntity)).thenReturn(boardColumnsResponseDTO);
 
      // act and assert
      BoardColumnsResponseDTO result = this.boardColumnService.update(columnId, boardColumnsUpdateDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardColumnsResponseDTO);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsValidator, times(1)).updateValidations(boardColumnsUpdateDTO);
      verify(this.boardColumnsRepository, times(1)).checkColumnNameExistence(newName.toString());
      verify(this.boardColumnsRepository, times(1)).checkColumnPositionExistence(newPosition);
      verify(this.boardColumnsRepository, times(1)).update(columnId, boardColumnsUpdateDTO);
      verify(this.boardColumnsMapper, times(1)).toBoardColumnsResponseDTO(boardColumnsEntity);
   }


   @Test
   @DisplayName("delete() - must delete a board column")
   void delete() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
      UUID boardId = UUID.randomUUID();
 
      // column - setup
      BoardColumnsEntity boardColumnsEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.TODO)
         .position(1)
         .color("#ff0000")
         .build();
 
      // DTO - setup
      BoardColumnsDeletedDTO boardColumnsDeletedDTO = new BoardColumnsDeletedDTO(
         columnId, boardId, boardColumnsEntity.getName(),
         boardColumnsEntity.getPosition(), boardColumnsEntity.getColor(),
         LocalDateTime.now()
      );
 
      // stubs
      when(this.boardColumnsRepository.delete(columnId)).thenReturn(boardColumnsEntity);
      when(this.boardColumnsMapper.toBoardColumnsDeletedDTO(boardColumnsEntity)).thenReturn(boardColumnsDeletedDTO);
 
      // act and assert
      BoardColumnsDeletedDTO result = this.boardColumnService.delete(columnId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardColumnsDeletedDTO);
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).delete(columnId);
      verify(this.boardColumnsMapper, times(1)).toBoardColumnsDeletedDTO(boardColumnsEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if column is deleted")
   void isDeleted() {
      // id's - setup
      UUID columnId = UUID.randomUUID();
 
      // stubs
      when(this.boardColumnsRepository.isDeleted(columnId)).thenReturn(true);
 
      // act and assert
      boolean result = this.boardColumnService.isDeleted(columnId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.boardColumnsValidator, times(1)).idValidate(columnId);
      verify(this.boardColumnsRepository, times(1)).isDeleted(columnId);
   }

}