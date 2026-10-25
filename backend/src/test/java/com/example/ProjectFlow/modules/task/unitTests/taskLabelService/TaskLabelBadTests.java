
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskLabelService;

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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.task.repository.TaskLabelsRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TaskLabelsValidator;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.labels.service.LabelService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;
 
// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
 
// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskLabelsMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// jakarta imports
import jakarta.persistence.NoResultException;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - TaskLabelService")
public class TaskLabelBadTests {
 
   // mock dependencies
   @Mock
   private TaskLabelsRepository taskLabelsRepository;
   @Mock
   private TaskLabelsValidator taskLabelsValidator;
   @Mock
   private TaskService taskService;
   @Mock
   private LabelService labelService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private TaskLabelsMapper taskLabelsMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private TaskLabelService taskLabelService;


   @Test
   @DisplayName("create() - must throw when task does not exist")
   void create() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // error message - setup
      String errorMessage = "Recurso não encontrado: Tarefa não existe";

      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.taskService).getEntityById(taskId);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.create(taskId, labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.taskService, times(1)).getEntityById(taskId);
 
      // no checks
      verify(this.taskLabelsValidator, never()).projectValidation(any(), any());
      verifyNoInteractions(this.labelService);
      verifyNoInteractions(this.taskLabelsRepository);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskLabelsMapper);
   }


   @Test
   @DisplayName("getAllByTaskId() - must throw when task label list is empty")
   void getAllByTaskIdEmptyList() {
      // id - setup
      UUID taskId = UUID.randomUUID();
 
      // list - setup
      List<TaskLabelsEntity> taskLabelsList = List.of();
 
      // stubs
      when(this.taskService.existsById(taskId)).thenReturn(true);
      when(this.taskLabelsRepository.getAllByTaskId(taskId)).thenReturn(taskLabelsList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.getAllByTaskId(taskId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiquetas de tarefa não existem");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.taskLabelsRepository, times(1)).getAllByTaskId(taskId);
 
      // no checks
      verifyNoInteractions(this.taskLabelsMapper);
   }


   @Test
   @DisplayName("getAllByLabelId() - must throw when task label list is empty")
   void getAllByLabelIdEmptyList() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // list - setup
      List<TaskLabelsEntity> taskLabelsList = List.of();
 
      // stubs
      when(this.labelService.existsById(labelId)).thenReturn(true);
      when(this.taskLabelsRepository.getAllByLabelId(labelId)).thenReturn(taskLabelsList);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.getAllByLabelId(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Tarefas da etiqueta não existem");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.labelService, times(1)).existsById(labelId);
      verify(this.taskLabelsRepository, times(1)).getAllByLabelId(labelId);
 
      // no checks
      verifyNoInteractions(this.taskLabelsMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw task label not found")
   void getEntityById() {
      // id - setup
      UUID taskLabelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.getEntityById(taskLabelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.getEntityById(taskLabelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de tarefa não existe");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).getEntityById(taskLabelId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when task label does not exist")
   void existsById() {
      // id - setup
      UUID taskLabelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.existsById(taskLabelId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.existsById(taskLabelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de tarefa não existe");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).existsById(taskLabelId);
   }
 
 
   @Test
   @DisplayName("existsByTaskIdAndLabelId() - must throw when task label does not exist")
   void existsByTaskIdAndLabelId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.existsByTaskIdAndLabelId(taskId, labelId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.existsByTaskIdAndLabelId(taskId, labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de tarefa não existe");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.taskLabelsRepository, times(1)).existsByTaskIdAndLabelId(taskId, labelId);
   }
 
 
   @Test
   @DisplayName("removeRelation() - must throw when task label does not exist")
   void removeRelation() {
      // id - setup
      UUID taskLabelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.removeRelation(taskLabelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.taskLabelService.removeRelation(taskLabelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de tarefa não existe");
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).removeRelation(taskLabelId);
 
      // no checks
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.taskLabelsMapper);
   }

}