
// packages
package com.example.ProjectFlow.modules.board.unitTests.boardService;

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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.board.repository.BoardRepository;
 
// import validator
import com.example.ProjectFlow.modules.board.validator.BoardValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.board.dto.boardDTO.BoardDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardService;
 
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
 
// import mapper
import com.example.ProjectFlow.modules.board.mapper.BoardMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// jakarta imports
import jakarta.persistence.NoResultException;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - BoardService")
public class BoardBadTests {

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
   @DisplayName("create() - must throw when project does not exist")
   void create() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
 
      // error message - setup
      String errorMessage = "Recurso não encontrado: Projeto não existe";

      // DTO - setup
      BoardDTO boardDTO = new BoardDTO(
         projectId, "board x"
      );
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.projectService).getEntityById(projectId);   

      // act and assert
      assertThatThrownBy(() -> this.boardService.create(boardDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.boardValidator, times(1)).projectIdValidate(projectId);
      verify(this.boardValidator, times(1)).nameValidate(boardDTO.name());
      verify(this.projectService, times(1)).getEntityById(projectId);
 
      // no checks
      verifyNoInteractions(this.boardRepository);
      verifyNoInteractions(this.boardMapper);
   }


   @Test
   @DisplayName("getAll() - must throw when board list is empty")
   void getAll() {
      // list - setup
      List<BoardEntity> boardList = List.of();
 
      // stubs
      when(this.boardRepository.getAll()).thenReturn(boardList);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.getAll())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadros Kanban não existem");
 
      // checks
      verify(this.boardRepository, times(1)).getAll();
 
      // no checks
      verifyNoInteractions(this.boardMapper);
   }


   @Test
   @DisplayName("getById() - must throw board not found")
   void getById() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.getById(boardId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.getById(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).getById(boardId);
 
      // no checks
      verifyNoInteractions(this.boardMapper);
   }


   @Test
   @DisplayName("getEntityById() - must throw board not found")
   void getEntityById() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.getEntityById(boardId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.getEntityById(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).getEntityById(boardId);
   }


   @Test
   @DisplayName("existsById() - must throw when board does not exist")
   void existsById() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.existsById(boardId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.existsById(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).existsById(boardId);
   }


   @Test
   @DisplayName("getByProjectId() - must throw board not found")
   void getByProjectId() {
      // id - setup
      UUID projectId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.getByProjectId(projectId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.getByProjectId(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).projectIdValidate(projectId);
      verify(this.boardRepository, times(1)).getByProjectId(projectId);
 
      // no checks
      verifyNoInteractions(this.boardMapper);
   }


   @Test
   @DisplayName("updateName() - must throw when board does not exist")
   void updateName() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // data to update
      String newName = "board atualizado";
 
      // stubs
      when(this.boardRepository.updateName(boardId, newName)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.updateName(boardId, newName))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardValidator, times(1)).nameValidate(newName);
      verify(this.boardRepository, times(1)).updateName(boardId, newName);
 
      // no checks
      verifyNoInteractions(this.boardMapper);
   }


   @Test
   @DisplayName("delete() - must throw when board does not exist")
   void delete() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.delete(boardId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.delete(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).delete(boardId);
 
      // no checks
      verifyNoInteractions(this.boardMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when board does not exist")
   void isDeleted() {
      // id - setup
      UUID boardId = UUID.randomUUID();
 
      // stubs
      when(this.boardRepository.isDeleted(boardId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.boardService.isDeleted(boardId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Quadro Kanban não existe");
 
      // checks
      verify(this.boardValidator, times(1)).idValidate(boardId);
      verify(this.boardRepository, times(1)).isDeleted(boardId);
   }

}