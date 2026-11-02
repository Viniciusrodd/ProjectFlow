
// packages
package com.example.ProjectFlow.modules.task.integrationTests.taskController;

// imports
import java.time.LocalDate;
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
import com.example.ProjectFlow.modules.task.controller.TaskController;

// import services
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;
import com.example.ProjectFlow.modules.task.service.TaskChecklistService;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksCompleteResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(TaskController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - TaskController")
public class TaskHappyTests {

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
   @DisplayName("POST - /task - should return created task and status 201")
   void createTask() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
      TasksDTO taskDTO = new TasksDTO(
         projectId, columnId, ownerId, "Implement auth", "JWT auth", "HIGH", LocalDate.now().plusDays(7)
      );
 
      // task response data
      TasksResponseDTO taskResponseDTO = new TasksResponseDTO(
         UUID.randomUUID(), projectId, columnId, ownerId,
         taskDTO.title(), taskDTO.description(), PriorityEnum.HIGH, taskDTO.dueDate()
      );
 
      // stubs
      when(this.taskService.create(taskDTO)).thenReturn(taskResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(taskDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(taskResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.columnId").value(columnId.toString()))
         .andExpect(jsonPath("$.data.ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data.title").value(taskResponseDTO.title()))
         .andExpect(jsonPath("$.data.description").value(taskResponseDTO.description()))
         .andExpect(jsonPath("$.data.priority").value(taskResponseDTO.priority().name()))
         .andExpect(jsonPath("$.data.due_date").value(taskResponseDTO.due_date().toString()));
 
      // checks
      verify(this.taskService).create(taskDTO);
   }
 
 
   @Test
   @DisplayName("GET - /tasks - should return all tasks and status 200")
   void getAllTasks() throws Exception {
      // tasks data
      List<TasksCompleteResponseDTO> tasks = List.of(
         new TasksCompleteResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(), null
         ),
         new TasksCompleteResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "Task B", "Description B", PriorityEnum.LOW, LocalDate.now(), null
         )
      );
 
      // stubs
      when(this.taskService.getAll()).thenReturn(tasks);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(tasks.size()))
         .andExpect(jsonPath("$.data[0].id").value(tasks.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].title").value(tasks.get(0).title()))
         .andExpect(jsonPath("$.data[0].priority").value(tasks.get(0).priority().name()))
         .andExpect(jsonPath("$.data[1].id").value(tasks.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].title").value(tasks.get(1).title()))
         .andExpect(jsonPath("$.data[1].priority").value(tasks.get(1).priority().name()));
 
      // checks
      verify(this.taskService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /task/{id} - should return the task and status 200")
   void getTaskById() throws Exception {
      // task data
      UUID taskId = UUID.randomUUID();
      TasksCompleteResponseDTO task = new TasksCompleteResponseDTO(
         taskId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         "Task A", "Description A", PriorityEnum.MEDIUM, LocalDate.now(), null
      );
 
      // stubs
      when(this.taskService.getById(taskId)).thenReturn(task);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(task.id().toString()))
         .andExpect(jsonPath("$.data.title").value(task.title()))
         .andExpect(jsonPath("$.data.priority").value(task.priority().name()));
 
      // checks
      verify(this.taskService).getById(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/project/{projectId} - should return tasks of the project and status 200")
   void getByProjectId() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // tasks data
      List<TasksCompleteResponseDTO> tasks = List.of(
         new TasksCompleteResponseDTO(
            UUID.randomUUID(), projectId, UUID.randomUUID(), UUID.randomUUID(),
            "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(), null
         )
      );
 
      // stubs
      when(this.taskService.getByProjectId(projectId)).thenReturn(tasks);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(tasks.size()))
         .andExpect(jsonPath("$.data[0].id").value(tasks.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data[0].title").value(tasks.get(0).title()));
 
      // checks
      verify(this.taskService).getByProjectId(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/column/{columnId} - should return tasks of the column and status 200")
   void getByColumnId() throws Exception {
      // column id
      UUID columnId = UUID.randomUUID();
 
      // tasks data
      List<TasksCompleteResponseDTO> tasks = List.of(
         new TasksCompleteResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), columnId, UUID.randomUUID(),
            "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(), null
         )
      );
 
      // stubs
      when(this.taskService.getByColumnId(columnId)).thenReturn(tasks);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(tasks.size()))
         .andExpect(jsonPath("$.data[0].id").value(tasks.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].columnId").value(columnId.toString()))
         .andExpect(jsonPath("$.data[0].title").value(tasks.get(0).title()));
 
      // checks
      verify(this.taskService).getByColumnId(columnId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/owner/{ownerId} - should return tasks of the owner and status 200")
   void getByOwnerId() throws Exception {
      // owner id
      UUID ownerId = UUID.randomUUID();
 
      // tasks data
      List<TasksCompleteResponseDTO> tasks = List.of(
         new TasksCompleteResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), ownerId,
            "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(), null
         )
      );
 
      // stubs
      when(this.taskService.getByOwnerId(ownerId)).thenReturn(tasks);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/owner/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(tasks.size()))
         .andExpect(jsonPath("$.data[0].id").value(tasks.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data[0].title").value(tasks.get(0).title()));
 
      // checks
      verify(this.taskService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id}/column/{columnId} - should return updated task and status 200")
   void updateColumn() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
 
      // updated task data
      TasksCompleteResponseDTO updatedTask = new TasksCompleteResponseDTO(
         taskId, UUID.randomUUID(), columnId, UUID.randomUUID(),
         "Task A", "Description A", PriorityEnum.MEDIUM, LocalDate.now(), null
      );
 
      // stubs
      when(this.taskService.updateColumn(taskId, columnId)).thenReturn(updatedTask);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId + "/column/" + columnId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedTask.id().toString()))
         .andExpect(jsonPath("$.data.columnId").value(columnId.toString()));
 
      // checks
      verify(this.taskService).updateColumn(taskId, columnId);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id} - should return status 200 when updating the task")
   void updateTask() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      TasksUpdateDTO updateDTO = new TasksUpdateDTO(
         "Task A - Updated", "New description", "LOW", LocalDate.now().plusDays(3)
      );
 
      // updated task data
      TasksCompleteResponseDTO updatedTask = new TasksCompleteResponseDTO(
         taskId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         updateDTO.title(), updateDTO.description(), PriorityEnum.LOW, updateDTO.dueDate(), null
      );
 
      // stubs
      when(this.taskService.update(taskId, updateDTO)).thenReturn(updatedTask);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedTask.id().toString()))
         .andExpect(jsonPath("$.data.title").value(updatedTask.title()))
         .andExpect(jsonPath("$.data.priority").value(updatedTask.priority().name()));
 
      // checks
      verify(this.taskService).update(taskId, updateDTO);
   }
 
 
   @Test
   @DisplayName("PUT - /task/{id}/complete - should return completed task and status 200")
   void taskComplete() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
 
      // completed task data
      TasksCompleteResponseDTO completedTask = new TasksCompleteResponseDTO(
         taskId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(), LocalDateTime.now()
      );
 
      // stubs
      when(this.taskService.taskComplete(taskId)).thenReturn(completedTask);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/" + taskId + "/complete")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(completedTask.id().toString()))
         .andExpect(jsonPath("$.data.completedAt").isNotEmpty());
 
      // checks
      verify(this.taskService).taskComplete(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/{id} - should return the deleted task and status 200")
   void deleteTask() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      TasksDeletedDTO deletedTask = new TasksDeletedDTO(
         taskId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now(),
         null, LocalDateTime.now()
      );
 
      // stubs
      when(this.taskService.delete(taskId)).thenReturn(deletedTask);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedTask.id().toString()))
         .andExpect(jsonPath("$.data.title").value(deletedTask.title()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.taskService).delete(taskId);
   }


   //// task labels


   @Test
   @DisplayName("POST - /task/{taskId}/label/{labelId} - should return created task label and status 201")
   void createTaskLabel() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // task label response data
      TaskLabelsResponseDTO taskLabel = new TaskLabelsResponseDTO(
         UUID.randomUUID(), taskId, labelId, LocalDateTime.now()
      );
 
      // stubs
      when(this.taskLabelService.create(taskId, labelId)).thenReturn(taskLabel);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task/" + taskId + "/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(taskLabel.id().toString()))
         .andExpect(jsonPath("$.data.taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data.labelId").value(labelId.toString()));
 
      // checks
      verify(this.taskLabelService).create(taskId, labelId);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/labels - should return labels of the task and status 200")
   void getByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // labels data
      List<LabelsByTaskResponseDTO> labels = List.of(
         new LabelsByTaskResponseDTO(UUID.randomUUID(), null)
      );
 
      // stubs
      when(this.taskLabelService.getAllByTaskId(taskId)).thenReturn(labels);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId + "/labels")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(labels.size()))
         .andExpect(jsonPath("$.data[0].id").value(labels.get(0).id().toString()));
 
      // checks
      verify(this.taskLabelService).getAllByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /tasks/label/{labelId} - should return tasks of the label and status 200")
   void getByLabelId() throws Exception {
      // label id
      UUID labelId = UUID.randomUUID();
 
      // tasks data
      TasksResponseDTO taskInside = new TasksResponseDTO(
         UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
         "Task A", "Description A", PriorityEnum.HIGH, LocalDate.now()
      );
      List<TasksByLabelResponseDTO> tasks = List.of(
         new TasksByLabelResponseDTO(UUID.randomUUID(), taskInside)
      );
 
      // stubs
      when(this.taskLabelService.getAllByLabelId(labelId)).thenReturn(tasks);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/tasks/label/" + labelId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(tasks.size()))
         .andExpect(jsonPath("$.data[0].id").value(tasks.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].task.id").value(taskInside.id().toString()));
 
      // checks
      verify(this.taskLabelService).getAllByLabelId(labelId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/label/{id} - should return the removed task label and status 200")
   void removeTaskLabel() throws Exception {
      // data - setup
      UUID relationId = UUID.randomUUID();
      TaskLabelsDeletedDTO relationRemoved = new TaskLabelsDeletedDTO(
         relationId, UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now()
      );
 
      // stubs
      when(this.taskLabelService.removeRelation(relationId)).thenReturn(relationRemoved);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/label/" + relationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(relationRemoved.id().toString()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.taskLabelService).removeRelation(relationId);
   }


   //// task checklist


   @Test
   @DisplayName("POST - /task/{taskId}/checklist - should return created checklist item and status 201")
   void createTaskChecklist() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      TaskChecklistDTO checklistDTO = new TaskChecklistDTO("First step", false, 1);
 
      // checklist response data
      TaskChecklistResponseDTO checklistResponse = new TaskChecklistResponseDTO(
         UUID.randomUUID(), taskId, checklistDTO.description(), checklistDTO.completed(), checklistDTO.position()
      );
 
      // stubs
      when(this.taskChecklistService.create(taskId, checklistDTO)).thenReturn(checklistResponse);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/task/" + taskId + "/checklist")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(checklistDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(checklistResponse.id().toString()))
         .andExpect(jsonPath("$.data.taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data.description").value(checklistResponse.description()))
         .andExpect(jsonPath("$.data.completed").value(checklistResponse.completed()))
         .andExpect(jsonPath("$.data.position").value(checklistResponse.position()));
 
      // checks
      verify(this.taskChecklistService).create(taskId, checklistDTO);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/checklist/items - should return checklist items of the task and status 200")
   void getChecklistByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // checklist data
      List<TaskChecklistResponseDTO> items = List.of(
         new TaskChecklistResponseDTO(UUID.randomUUID(), taskId, "First step", false, 1)
      );
 
      // stubs
      when(this.taskChecklistService.getAllByTaskId(taskId)).thenReturn(items);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/" + taskId + "/checklist/items")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(items.size()))
         .andExpect(jsonPath("$.data[0].id").value(items.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].taskId").value(taskId.toString()));
 
      // checks
      verify(this.taskChecklistService).getAllByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /task/checklist/item/{id} - should return checklist item and status 200")
   void getChecklistItemById() throws Exception {
      // item data
      UUID itemId = UUID.randomUUID();
      TaskChecklistResponseDTO item = new TaskChecklistResponseDTO(
         itemId, UUID.randomUUID(), "First step", false, 1
      );
 
      // stubs
      when(this.taskChecklistService.getById(itemId)).thenReturn(item);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(item.id().toString()))
         .andExpect(jsonPath("$.data.description").value(item.description()));
 
      // checks
      verify(this.taskChecklistService).getById(itemId);
   }
 
 
   @Test
   @DisplayName("GET - /task/checklist/items - should return all checklist items and status 200")
   void getAllChecklistItems() throws Exception {
      // items data
      List<TaskChecklistResponseDTO> items = List.of(
         new TaskChecklistResponseDTO(UUID.randomUUID(), UUID.randomUUID(), "First step", false, 1),
         new TaskChecklistResponseDTO(UUID.randomUUID(), UUID.randomUUID(), "Second step", false, 2)
      );
 
      // stubs
      when(this.taskChecklistService.getAll()).thenReturn(items);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/task/checklist/items")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(items.size()))
         .andExpect(jsonPath("$.data[0].id").value(items.get(0).id().toString()))
         .andExpect(jsonPath("$.data[1].id").value(items.get(1).id().toString()));
 
      // checks
      verify(this.taskChecklistService).getAll();
   }
 
 
   @Test
   @DisplayName("PUT - /task/checklist/item/{id} - should return updated checklist item and status 200")
   void updateChecklistItem() throws Exception {
      // data - setup
      UUID itemId = UUID.randomUUID();
      TaskChecklistUpdateDTO updateDTO = new TaskChecklistUpdateDTO("Updated step", 2);
 
      // updated item data
      TaskChecklistResponseDTO updatedItem = new TaskChecklistResponseDTO(
         itemId, UUID.randomUUID(), updateDTO.description(), false, updateDTO.position()
      );
 
      // stubs
      when(this.taskChecklistService.update(itemId, updateDTO)).thenReturn(updatedItem);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedItem.id().toString()))
         .andExpect(jsonPath("$.data.description").value(updatedItem.description()))
         .andExpect(jsonPath("$.data.position").value(updatedItem.position()));
 
      // checks
      verify(this.taskChecklistService).update(itemId, updateDTO);
   }
 
 
   @Test
   @DisplayName("PUT - /task/checklist/item/{id}/completed - should return updated completed field and status 200")
   void setChecklistItemCompleted() throws Exception {
      // data - setup
      UUID itemId = UUID.randomUUID();
      boolean completed = true;
 
      // updated item data
      TaskChecklistResponseDTO updatedItem = new TaskChecklistResponseDTO(
         itemId, UUID.randomUUID(), "First step", completed, 1
      );
 
      // stubs
      when(this.taskChecklistService.setCompleted(itemId, completed)).thenReturn(updatedItem);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/task/checklist/item/" + itemId + "/completed")
         .param("c", String.valueOf(completed))
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedItem.id().toString()))
         .andExpect(jsonPath("$.data.completed").value(updatedItem.completed()));
 
      // checks
      verify(this.taskChecklistService).setCompleted(itemId, completed);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/checklist/item/{id} - should return the deleted checklist item and status 200")
   void deleteChecklistItem() throws Exception {
      // data - setup
      UUID itemId = UUID.randomUUID();
      TaskChecklistDeletedDTO deletedItem = new TaskChecklistDeletedDTO(
         itemId, UUID.randomUUID(), "First step", false, 1, LocalDateTime.now()
      );
 
      // stubs
      when(this.taskChecklistService.delete(itemId)).thenReturn(deletedItem);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/task/checklist/item/" + itemId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedItem.id().toString()))
         .andExpect(jsonPath("$.data.description").value(deletedItem.description()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.taskChecklistService).delete(itemId);
   }

}