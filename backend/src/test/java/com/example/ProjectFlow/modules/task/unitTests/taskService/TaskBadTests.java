
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskService;

// imports
import java.time.LocalDate;
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
import com.example.ProjectFlow.modules.task.repository.TaskRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TasksValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskService;
 
// import entity
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.board.enums.BoardEnum;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
 
// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// jakarta imports
import jakarta.persistence.NoResultException;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - TaskService")
public class TaskBadTests {
 
   // mock dependencies
   @Mock
   private TaskRepository taskRepository;
   @Mock
   private TasksValidator tasksValidator;
   @Mock
   private ProjectService projectService;
   @Mock
   private BoardColumnService boardColumnService;
   @Mock
   private UserService userService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private TaskMapper taskMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private TaskService taskService;
 
 
   @Test
   @DisplayName("create() - must throw when project does not exist")
   void create() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // error message - setup
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // DTO - setup
      TasksDTO tasksDTO = new TasksDTO(
         projectId, columnId, ownerId,
         "task x", "task description",
         "HIGH", LocalDate.now().plusDays(7)
      );
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.projectService).getEntityById(projectId);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.create(tasksDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.tasksValidator, times(1)).projectIdValidate(projectId);
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.tasksValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.tasksValidator, times(1)).titleValidate(tasksDTO.title());
      verify(this.tasksValidator, times(1)).descriptionValidate(tasksDTO.description());
      verify(this.tasksValidator, times(1)).priorityValidate(tasksDTO.priority());
      verify(this.tasksValidator, times(1)).dueDateValidate(tasksDTO.dueDate());
      verify(this.projectService, times(1)).getEntityById(projectId);
 
      // no checks
      verifyNoInteractions(this.boardColumnService);
      verifyNoInteractions(this.userService);
      verifyNoInteractions(this.taskRepository);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskMapper);
   }


   @Test
   @DisplayName("getAll() - must throw when task list is empty")
   void getAll() {
      // list - setup
      List<TasksEntity> taskList = List.of();
 
      // stubs
      when(this.taskRepository.getAll()).thenReturn(taskList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getAll())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefas não existem");
 
      // checks
      verify(this.taskRepository, times(1)).getAll();
 
      // no checks
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("getById() - must throw task not found")
   void getById() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.getById(taskId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getById(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).getById(taskId);
 
      // no checks
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw task not found")
   void getEntityById() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.getEntityById(taskId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getEntityById(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).getEntityById(taskId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when task does not exist")
   void existsById() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.existsById(taskId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.existsById(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).existsById(taskId);
   }
 
 
   @Test
   @DisplayName("getByProjectId() - must throw when task list is empty")
   void getByProjectId() {
      // id - setup
      UUID projectId = UUID.randomUUID();
 
      // list - setup
      List<TasksEntity> taskList = List.of();
 
      // stubs
      when(this.taskRepository.getByProjectId(projectId)).thenReturn(taskList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getByProjectId(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefas não existem");
 
      // checks
      verify(this.tasksValidator, times(1)).projectIdValidate(projectId);
      verify(this.taskRepository, times(1)).getByProjectId(projectId);
 
      // no checks
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("getByColumnId() - must throw when task list is empty")
   void getByColumnId() {
      // id - setup
      UUID columnId = UUID.randomUUID();
 
      // list - setup
      List<TasksEntity> taskList = List.of();
 
      // stubs
      when(this.taskRepository.getByColumnId(columnId)).thenReturn(taskList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getByColumnId(columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefas não existem");
 
      // checks
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.taskRepository, times(1)).getByColumnId(columnId);
 
      // no checks
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("getByOwnerId() - must throw when task list is empty")
   void getByOwnerId() {
      // id - setup
      UUID ownerId = UUID.randomUUID();
 
      // list - setup
      List<TasksEntity> taskList = List.of();
 
      // stubs
      when(this.taskRepository.getByOwnerId(ownerId)).thenReturn(taskList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.getByOwnerId(ownerId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefas não existem");
 
      // checks
      verify(this.tasksValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.taskRepository, times(1)).getByOwnerId(ownerId);
 
      // no checks
      verifyNoInteractions(this.taskMapper);
   }


   @Test
   @DisplayName("updateColumn() - must throw when task does not exist")
   void updateColumn() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
 
      // column - setup
      BoardColumnsEntity columnEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.PROGRESS)
         .position(2)
         .build();
 
      // stubs
      when(this.boardColumnService.getEntityById(columnId)).thenReturn(columnEntity);
      when(this.taskRepository.updateColumn(taskId, columnEntity)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.updateColumn(taskId, columnId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.boardColumnService, times(1)).getEntityById(columnId);
      verify(this.taskRepository, times(1)).updateColumn(taskId, columnEntity);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskMapper);
   }


   @Test
   @DisplayName("update() - must throw when task does not exist")
   void update() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // DTO - setup
      TasksUpdateDTO tasksUpdateDTO = new TasksUpdateDTO(
         "task atualizada", "task description",
         "HIGH", LocalDate.now().plusDays(7)
      );
 
      // stubs
      when(this.taskRepository.update(taskId, tasksUpdateDTO)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.update(taskId, tasksUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.tasksValidator, times(1)).updateValidations(tasksUpdateDTO);
      verify(this.taskRepository, times(1)).update(taskId, tasksUpdateDTO);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("taskComplete() - must throw when task does not exist")
   void taskComplete() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.taskComplete(taskId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.taskComplete(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).taskComplete(taskId);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("delete() - must throw when task does not exist")
   void delete() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.delete(taskId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.delete(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).delete(taskId);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when task does not exist")
   void isDeleted() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.isDeleted(taskId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskService.isDeleted(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefa não existe");
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).isDeleted(taskId);
   }

}