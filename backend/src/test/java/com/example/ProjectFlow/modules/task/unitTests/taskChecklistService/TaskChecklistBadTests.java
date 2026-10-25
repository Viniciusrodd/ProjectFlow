
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskChecklistService;

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
import com.example.ProjectFlow.modules.task.repository.TaskChecklistRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TaskChecklistValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskChecklistService;
 
// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;
 
// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskChecklistMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// jakarta imports
import jakarta.persistence.NoResultException;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - TaskChecklistService")
public class TaskChecklistBadTests {

   // mock dependencies
   @Mock
   private TaskChecklistRepository taskChecklistRepository;
   @Mock
   private TaskChecklistValidator taskChecklistValidator;
   @Mock
   private TaskService taskService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private TaskChecklistMapper taskChecklistMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private TaskChecklistService taskChecklistService;
   
   
   @Test
   @DisplayName("create() - must throw when task does not exist")
   void create() {
      // id's - setup
      UUID taskId = UUID.randomUUID();

      // error message - setup
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // DTO - setup
      TaskChecklistDTO taskChecklistDTO = new TaskChecklistDTO(
         "item description", false, 1
      );
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.taskService).getEntityById(taskId);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.create(taskId, taskChecklistDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskChecklistValidator, times(1)).descriptionValidate(taskChecklistDTO.description());
      verify(this.taskChecklistValidator, times(1)).positionValidate(taskChecklistDTO.position());
      verify(this.taskService, times(1)).getEntityById(taskId);
 
      // no checks
      verifyNoInteractions(this.taskChecklistRepository);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskChecklistMapper);
   }


   @Test
   @DisplayName("getAllByTaskId() - must throw when checklist item list is empty")
   void getAllByTaskIdEmptyList() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // list - setup
      List<TaskChecklistEntity> itemList = List.of();
 
      // stubs
      when(this.taskChecklistRepository.getAllByTaskId(taskId)).thenReturn(itemList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.getAllByTaskId(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Items de checklist da tarefa não existem");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.taskChecklistRepository, times(1)).getAllByTaskId(taskId);
 
      // no checks
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("getById() - must throw checklist item not found")
   void getById() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.getById(itemId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.getById(itemId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).getById(itemId);
 
      // no checks
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("getAll() - must throw when checklist item list is empty")
   void getAll() {
      // list - setup
      List<TaskChecklistEntity> itemList = List.of();
 
      // stubs
      when(this.taskChecklistRepository.getAll()).thenReturn(itemList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.getAll())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Items de checklist da tarefa não existem");
 
      // checks
      verify(this.taskChecklistRepository, times(1)).getAll();
 
      // no checks
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw checklist item not found")
   void getEntityById() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.getEntityById(itemId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.getEntityById(itemId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).getEntityById(itemId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when checklist item does not exist")
   void existsById() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.existsById(itemId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.existsById(itemId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).existsById(itemId);
   }


   @Test
   @DisplayName("update() - must throw when checklist item does not exist")
   void update() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // DTO - setup
      TaskChecklistUpdateDTO taskChecklistUpdateDTO = new TaskChecklistUpdateDTO(
         "item atualizado", 1
      );
 
      // stubs
      when(this.taskChecklistRepository.update(itemId, taskChecklistUpdateDTO)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.update(itemId, taskChecklistUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistValidator, times(1)).updateValidations(taskChecklistUpdateDTO);
      verify(this.taskChecklistRepository, times(1)).update(itemId, taskChecklistUpdateDTO);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("setCompleted() - must throw when checklist item does not exist")
   void setCompleted() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.setCompleted(itemId, true)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.setCompleted(itemId, true))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).setCompleted(itemId, true);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("delete() - must throw when checklist item does not exist")
   void delete() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.delete(itemId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.delete(itemId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).delete(itemId);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskChecklistMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when checklist item does not exist")
   void isDeleted() {
      // id - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.isDeleted(itemId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskChecklistService.isDeleted(itemId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Item de checklist da tarefa não existe");
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).isDeleted(itemId);
   }

}