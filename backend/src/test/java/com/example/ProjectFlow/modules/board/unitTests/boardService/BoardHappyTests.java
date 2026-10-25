
// packages
package com.example.ProjectFlow.modules.board.unitTests.boardService;

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
import com.example.ProjectFlow.modules.board.repository.BoardRepository;
 
// import validator
import com.example.ProjectFlow.modules.board.validator.BoardValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDeletedDTO;
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardResponseDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardService;
 
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
 
// import mapper
import com.example.ProjectFlow.modules.board.mapper.BoardMapper;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - BoardService")
public class BoardHappyTests {
 
   // mock dependencies
   @Mock
   private BoardRepository boardRepository;
   @Mock
   private BoardValidator boardValidator;
   @Mock
   private ProjectService projectService;
   @Mock
   private BoardMapper boardMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private BoardService boardService;
 
 
   @Test
   @DisplayName("create() - must create a board")
   void create() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();

      // board - setup         
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name("board x")
         .build();
 
      // DTO's - setup
      BoardDTO boardDTO = new BoardDTO(
         projectId, boardEntity.getName()
      );
 
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         boardId, projectId, boardEntity.getName()
      );
 
      // stubs
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);
      when(this.boardRepository.create(boardDTO, projectEntity)).thenReturn(boardEntity);
      when(this.boardMapper.toBoardResponseDTO(boardEntity)).thenReturn(boardResponseDTO);
 
      // act and assert
      BoardResponseDTO result = this.boardService.create(boardDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardResponseDTO);
 
      // checks
      verify(this.boardValidator, times(1)).projectIdValidate(projectId);
      verify(this.boardValidator, times(1)).nameValidate(boardDTO.name());
      verify(this.projectService, times(1)).getEntityById(projectId);
      verify(this.boardRepository, times(1)).create(boardDTO, projectEntity);
      verify(this.boardMapper, times(1)).toBoardResponseDTO(boardEntity);
   }


   @Test
   @DisplayName("getAll() - must return all boards")
   void getAll() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name("board x")
         .build();
 
      // list - setup
      List<BoardEntity> boardList = List.of(boardEntity);
 
      // DTO - setup
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         boardId, projectId, boardEntity.getName()
      );
 
      // stubs
      when(this.boardRepository.getAll()).thenReturn(boardList);
      when(this.boardMapper.toBoardResponseDTO(boardEntity)).thenReturn(boardResponseDTO);
 
      // act and assert
      List<BoardResponseDTO> result = this.boardService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(boardResponseDTO);
 
      // checks
      verify(this.boardRepository, times(1)).getAll();
      verify(this.boardMapper, times(1)).toBoardResponseDTO(boardEntity);
   }


   @Test
   @DisplayName("getById() - must return board by id")
   void getById() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name("board x")
         .build();
 
      // DTO - setup
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         boardId, projectId, boardEntity.getName()
      );
 
      // stubs
      when(this.boardRepository.getById(boardId)).thenReturn(boardEntity);
      when(this.boardMapper.toBoardResponseDTO(boardEntity)).thenReturn(boardResponseDTO);
 
      // act and assert
      BoardResponseDTO result = this.boardService.getById(boardId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardResponseDTO);
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).getById(boardId);
      verify(this.boardMapper, times(1)).toBoardResponseDTO(boardEntity);
   }


   @Test
   @DisplayName("getEntityById() - must return board entity by id")
   void getEntityById() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .build();
 
      // stubs
      when(this.boardRepository.getEntityById(boardId)).thenReturn(boardEntity);
 
      // act and assert
      BoardEntity result = this.boardService.getEntityById(boardId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardEntity);
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).getEntityById(boardId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if board exist by id")
   void existsById() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.existsById(boardId)).thenReturn(true);
 
      // act and assert
      boolean result = this.boardService.existsById(boardId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).existsById(boardId);
   }


   @Test
   @DisplayName("getByProjectId() - must return board by project id")
   void getByProjectId() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // data - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name("board x")
         .build();
 
      // DTO - setup
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         boardId, projectId, boardEntity.getName()
      );
 
      // stubs
      when(this.boardRepository.getByProjectId(projectId)).thenReturn(boardEntity);
      when(this.boardMapper.toBoardResponseDTO(boardEntity)).thenReturn(boardResponseDTO);
 
      // act and assert
      BoardResponseDTO result = this.boardService.getByProjectId(projectId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardResponseDTO);
 
      // checks
      verify(this.boardValidator, times(1)).projectIdValidate(projectId);
      verify(this.boardRepository, times(1)).getByProjectId(projectId);
      verify(this.boardMapper, times(1)).toBoardResponseDTO(boardEntity);
   }


   @Test
   @DisplayName("updateName() - must update board name")
   void updateName() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // data to update
      String newName = "board atualizado";
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name(newName)
         .build();
 
      // DTO - setup
      BoardResponseDTO boardResponseDTO = new BoardResponseDTO(
         boardId, projectId, boardEntity.getName()
      );
 
      // stubs
      when(this.boardRepository.updateName(boardId, newName)).thenReturn(boardEntity);
      when(this.boardMapper.toBoardResponseDTO(boardEntity)).thenReturn(boardResponseDTO);
 
      // act and assert
      BoardResponseDTO result = this.boardService.updateName(boardId, newName);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardResponseDTO);
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardValidator, times(1)).nameValidate(newName);
      verify(this.boardRepository, times(1)).updateName(boardId, newName);
      verify(this.boardMapper, times(1)).toBoardResponseDTO(boardEntity);
   }


   @Test
   @DisplayName("delete() - must delete a board")
   void delete() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // board - setup
      BoardEntity boardEntity = new BoardEntity.Builder()
         .id(boardId)
         .name("board x")
         .build();
 
      // DTO - setup
      BoardDeletedDTO boardDeletedDTO = new BoardDeletedDTO(
         boardId, projectId, boardEntity.getName(),
         LocalDateTime.now()
      );
 
      // stubs
      when(this.boardRepository.delete(boardId)).thenReturn(boardEntity);
      when(this.boardMapper.toBoardDeletedDTO(boardEntity)).thenReturn(boardDeletedDTO);
 
      // act and assert
      BoardDeletedDTO result = this.boardService.delete(boardId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(boardDeletedDTO);
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).delete(boardId);
      verify(this.boardMapper, times(1)).toBoardDeletedDTO(boardEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if board is deleted")
   void isDeleted() {
      // id's - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.isDeleted(boardId)).thenReturn(true);
 
      // act and assert
      boolean result = this.boardService.isDeleted(boardId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).isDeleted(boardId);
   }

}