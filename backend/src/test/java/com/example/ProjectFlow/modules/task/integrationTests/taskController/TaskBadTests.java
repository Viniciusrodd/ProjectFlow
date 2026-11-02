
// packages
package com.example.ProjectFlow.modules.task.integrationTests.taskController;

// imports
import java.time.LocalDate;
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
import com.example.ProjectFlow.modules.task.controller.TaskController;
 
// import services
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;
import com.example.ProjectFlow.modules.task.service.TaskChecklistService;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(TaskController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - TaskController")
public class TaskBadTests {

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
   private TaskService taskService;
   @MockitoBean
   private TaskLabelService taskLabelService;
   @MockitoBean
   private TaskChecklistService taskChecklistService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   //// tasks


   @Test
   @DisplayName("POST - /task - should return 404 when project does not exist")
   void createTask() throws Exception {
      // data - setup
      TasksDTO taskDTO = new TasksDTO(
         UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         "Task A", "Description A", "HIGH", LocalDate.now()
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.taskService.create(taskDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(taskDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).create(taskDTO);
   }
 
 
   @Test
   @DisplayName("GET - /tasks - should return 404 when there are no tasks")
   void getAllTasks() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Tarefas não existem";
 
      // stubs
      when(this.taskService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /task/{id} - should return 404 when task does not exist")
   void getTaskById() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskService.getById(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).getById(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/project/{projectId} - should return 404 when project has no tasks")
   void getByProjectId() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefas não existem";
 
      // stubs
      when(this.taskService.getByProjectId(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).getByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/column/{columnId} - should return 404 when column has no tasks")
   void getByColumnId() throws Exception {
      // column id
      UUID columnId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefas não existem";
 
      // stubs
      when(this.taskService.getByColumnId(columnId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).getByColumnId(columnId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/owner/{ownerId} - should return 404 when owner has no tasks")
   void getByOwnerId() throws Exception {
      // owner id
      UUID ownerId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefas não existem";
 
      // stubs
      when(this.taskService.getByOwnerId(ownerId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/owner/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id}/column/{columnId} - should return 404 when task does not exist")
   void updateColumn() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskService.updateColumn(taskId, columnId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).updateColumn(taskId, columnId);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id} - should return 404 when task does not exist")
   void updateTask() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      TasksUpdateDTO updateDTO = new TasksUpdateDTO("Task A", "Description", "HIGH", LocalDate.now());
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskService.update(taskId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).update(taskId, updateDTO);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id}/complete - should return 404 when task does not exist")
   void taskComplete() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskService.taskComplete(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId + "/complete")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).taskComplete(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/{id} - should return 404 when task does not exist")
   void deleteTask() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskService.delete(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskService).delete(taskId);
   }


   //// task labels


   @Test
   @DisplayName("POST - /task/{taskId}/label/{labelId} - should return 404 when task does not exist")
   void createTaskLabel() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskLabelService.create(taskId, labelId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task/" + taskId + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskLabelService).create(taskId, labelId);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/labels - should return 404 when task has no labels")
   void getByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiquetas de tarefa não existem";
 
      // stubs
      when(this.taskLabelService.getAllByTaskId(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId + "/labels")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskLabelService).getAllByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/label/{labelId} - should return 404 when label has no tasks")
   void getByLabelId() throws Exception {
      // label id
      UUID labelId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefas da etiqueta não existem";
 
      // stubs
      when(this.taskLabelService.getAllByLabelId(labelId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskLabelService).getAllByLabelId(labelId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/label/{id} - should return 404 when task label relation does not exist")
   void removeTaskLabel() throws Exception {
      // relation id
      UUID relationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Etiqueta de tarefa não existe";
 
      // stubs
      when(this.taskLabelService.removeRelation(relationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/label/" + relationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskLabelService).removeRelation(relationId);
   }


   //// task checklist


   @Test
   @DisplayName("POST - /task/{taskId}/checklist - should return 404 when task does not exist")
   void createTaskChecklist() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      TaskChecklistDTO checklistDTO = new TaskChecklistDTO("First step", false, 1);
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.taskChecklistService.create(taskId, checklistDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task/" + taskId + "/checklist")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(checklistDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).create(taskId, checklistDTO);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/checklist/items - should return 404 when task has no checklist items")
   void getChecklistByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Items de checklist da tarefa não existem";
 
      // stubs
      when(this.taskChecklistService.getAllByTaskId(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId + "/checklist/items")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).getAllByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /task/checklist/item/{id} - should return 404 when checklist item does not exist")
   void getChecklistItemById() throws Exception {
      // item id
      UUID itemId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Item de checklist da tarefa não existe";
 
      // stubs
      when(this.taskChecklistService.getById(itemId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).getById(itemId);
   }
 
 
   @Test
   @DisplayName("GET - /task/checklist/items - should return 404 when there are no checklist items")
   void getAllChecklistItems() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Items de checklist da tarefa não existem";
 
      // stubs
      when(this.taskChecklistService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/checklist/items")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).getAll();
   }
 
 
   @Test
   @DisplayName("PUT - /task/checklist/item/{id} - should return 404 when checklist item does not exist")
   void updateChecklistItem() throws Exception {
      // data - setup
      UUID itemId = UUID.randomUUID();
      TaskChecklistUpdateDTO updateDTO = new TaskChecklistUpdateDTO("Updated step", 2);
 
      // error message
      String errorMessage = "Recurso não encontrado: Item de checklist da tarefa não existe";
 
      // stubs
      when(this.taskChecklistService.update(itemId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).update(itemId, updateDTO);
   }
 
 
   @Test
   @DisplayName("PUT - /task/checklist/item/{id}/completed - should return 404 when checklist item does not exist")
   void setChecklistItemCompleted() throws Exception {
      // item id
      UUID itemId = UUID.randomUUID();
      boolean completed = true;
 
      // error message
      String errorMessage = "Recurso não encontrado: Item de checklist da tarefa não existe";
 
      // stubs
      when(this.taskChecklistService.setCompleted(itemId, completed)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/checklist/item/" + itemId + "/completed")
         .param("c", String.valueOf(completed))
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).setCompleted(itemId, completed);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/checklist/item/{id} - should return 404 when checklist item does not exist")
   void deleteChecklistItem() throws Exception {
      // item id
      UUID itemId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Item de checklist da tarefa não existe";
 
      // stubs
      when(this.taskChecklistService.delete(itemId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.taskChecklistService).delete(itemId);
   }

}