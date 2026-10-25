
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskChecklistService;

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
import com.example.ProjectFlow.modules.task.repository.TaskChecklistRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TaskChecklistValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskChecklistService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;
 
// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskChecklistMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - TaskChecklistService")
public class TaskChecklistHappyTests {
 
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
   @DisplayName("create() - must create a task checklist item")
   void create() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // user - setup
      UserEntity memberUserEntity = new UserEntity.Builder()
         .id(memberUserId)
         .name("member x")
         .build();
 
      // project member - setup
      ProjectMembersEntity projectMemberEntity = new ProjectMembersEntity.Builder()
         .user(memberUserEntity)
         .build();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();
      projectEntity.setMembers(List.of(projectMemberEntity));
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .title("task x")
         .build();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .task(taskEntity)
         .description("item description")
         .completed(false)
         .position(1)
         .build();
 
      // DTO's - setup
      TaskChecklistDTO taskChecklistDTO = new TaskChecklistDTO(
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskChecklistEntity.getTask().getId())
         .action(ActivityActionEnum.CHECKLIST_DA_TAREFA_CRIADO)
         .description(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", criado na posição: " + taskChecklistEntity.getPosition() +
            ", do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.CHECKLIST_DA_TAREFA_CRIADO)
         .message(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", criado na posição: " + taskChecklistEntity.getPosition() +
            ", do projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
      when(this.taskChecklistRepository.create(taskEntity, taskChecklistDTO)).thenReturn(taskChecklistEntity);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      TaskChecklistResponseDTO result = this.taskChecklistService.create(taskId, taskChecklistDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskChecklistValidator, times(1)).descriptionValidate(taskChecklistDTO.description());
      verify(this.taskChecklistValidator, times(1)).positionValidate(taskChecklistDTO.position());
 
      verify(this.taskService, times(1)).getEntityById(taskId);
      verify(this.taskChecklistRepository, times(1)).checkPositionExistence(taskChecklistDTO.position(), taskId);
      verify(this.taskChecklistRepository, times(1)).create(taskEntity, taskChecklistDTO);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }


   @Test
   @DisplayName("getAllByTaskId() - must return all checklist items by task id")
   void getAllByTaskId() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .description("item description")
         .completed(false)
         .position(1)
         .build();
 
      // list - setup
      List<TaskChecklistEntity> itemList = List.of(taskChecklistEntity);
 
      // DTO - setup
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      // stubs
      when(this.taskChecklistRepository.getAllByTaskId(taskId)).thenReturn(itemList);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      List<TaskChecklistResponseDTO> result = this.taskChecklistService.getAllByTaskId(taskId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.taskChecklistRepository, times(1)).getAllByTaskId(taskId);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("getById() - must return checklist item by id")
   void getById() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .description("item description")
         .completed(false)
         .position(1)
         .build();
 
      // DTO - setup
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      // stubs
      when(this.taskChecklistRepository.getById(itemId)).thenReturn(taskChecklistEntity);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      TaskChecklistResponseDTO result = this.taskChecklistService.getById(itemId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).getById(itemId);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("getAll() - must return all checklist items")
   void getAll() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .description("item description")
         .completed(false)
         .position(1)
         .build();
 
      // list - setup
      List<TaskChecklistEntity> itemList = List.of(taskChecklistEntity);
 
      // DTO - setup
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      // stubs
      when(this.taskChecklistRepository.getAll()).thenReturn(itemList);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      List<TaskChecklistResponseDTO> result = this.taskChecklistService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistRepository, times(1)).getAll();
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must return checklist item entity by id")
   void getEntityById() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .build();
 
      // stubs
      when(this.taskChecklistRepository.getEntityById(itemId)).thenReturn(taskChecklistEntity);
 
      // act and assert
      TaskChecklistEntity result = this.taskChecklistService.getEntityById(itemId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistEntity);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).getEntityById(itemId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if checklist item exist by id")
   void existsById() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.existsById(itemId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskChecklistService.existsById(itemId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).existsById(itemId);
   }


   @Test
   @DisplayName("update() - must update checklist item (only the description)")
   void update() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // data to update
      String newDescription = "item atualizado";
 
      // user - setup
      UserEntity memberUserEntity = new UserEntity.Builder()
         .id(memberUserId)
         .name("member x")
         .build();
 
      // project member - setup
      ProjectMembersEntity projectMemberEntity = new ProjectMembersEntity.Builder()
         .user(memberUserEntity)
         .build();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();
      projectEntity.setMembers(List.of(projectMemberEntity));
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .title("task x")
         .build();
 
      // checklist item - setup (already updated)
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .task(taskEntity)
         .description(newDescription)
         .completed(false)
         .position(1)
         .build();
 
      // DTO's - setup
      TaskChecklistUpdateDTO taskChecklistUpdateDTO = new TaskChecklistUpdateDTO(
         newDescription, taskChecklistEntity.getPosition()
      );
 
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskChecklistEntity.getTask().getId())
         .action(ActivityActionEnum.CHECKLIST_DA_TAREFA_ATUALIZADO)
         .description(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", atualizado no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.CHECKLIST_DA_TAREFA_ATUALIZADO)
         .message(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", atualizado no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskChecklistRepository.update(itemId, taskChecklistUpdateDTO)).thenReturn(taskChecklistEntity);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      TaskChecklistResponseDTO result = this.taskChecklistService.update(itemId, taskChecklistUpdateDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistValidator, times(1)).updateValidations(taskChecklistUpdateDTO);
      verify(this.taskChecklistRepository, times(1)).update(itemId, taskChecklistUpdateDTO);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("setCompleted() - must set checklist item as completed")
   void setCompleted() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // user - setup
      UserEntity memberUserEntity = new UserEntity.Builder()
         .id(memberUserId)
         .name("member x")
         .build();
 
      // project member - setup
      ProjectMembersEntity projectMemberEntity = new ProjectMembersEntity.Builder()
         .user(memberUserEntity)
         .build();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();
      projectEntity.setMembers(List.of(projectMemberEntity));
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .title("task x")
         .build();
 
      // checklist item - setup (already completed)
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .task(taskEntity)
         .description("item description")
         .completed(true)
         .position(1)
         .build();
 
      // DTO's - setup
      TaskChecklistResponseDTO taskChecklistResponseDTO = new TaskChecklistResponseDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskChecklistEntity.getTask().getId())
         .action(ActivityActionEnum.CHECKLIST_DA_TAREFA_ATUALIZADO)
         .description(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", completado no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.CHECKLIST_DA_TAREFA_COMPLETADO)
         .message(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", completado no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskChecklistRepository.setCompleted(itemId, true)).thenReturn(taskChecklistEntity);
      when(this.taskChecklistMapper.toTaskChecklistResponseDTO(taskChecklistEntity)).thenReturn(taskChecklistResponseDTO);
 
      // act and assert
      TaskChecklistResponseDTO result = this.taskChecklistService.setCompleted(itemId, true);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistResponseDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).setCompleted(itemId, true);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistResponseDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("delete() - must delete a checklist item")
   void delete() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // user - setup
      UserEntity memberUserEntity = new UserEntity.Builder()
         .id(memberUserId)
         .name("member x")
         .build();
 
      // project member - setup
      ProjectMembersEntity projectMemberEntity = new ProjectMembersEntity.Builder()
         .user(memberUserEntity)
         .build();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();
      projectEntity.setMembers(List.of(projectMemberEntity));
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .title("task x")
         .build();
 
      // checklist item - setup
      TaskChecklistEntity taskChecklistEntity = new TaskChecklistEntity.Builder()
         .id(itemId)
         .task(taskEntity)
         .description("item description")
         .completed(false)
         .position(1)
         .build();
 
      // DTO's - setup
      TaskChecklistDeletedDTO taskChecklistDeletedDTO = new TaskChecklistDeletedDTO(
         itemId, taskId,
         taskChecklistEntity.getDescription(),
         taskChecklistEntity.getCompleted(),
         taskChecklistEntity.getPosition(),
         LocalDateTime.now()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskChecklistEntity.getTask().getId())
         .action(ActivityActionEnum.CHECKLIST_DA_TAREFA_REMOVIDO)
         .description(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", removido no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.CHECKLIST_DA_TAREFA_REMOVIDO)
         .message(
            "Item de checklist da tarefa: " + taskChecklistEntity.getTask().getTitle() +
            ", removido no projeto: " + taskChecklistEntity.getTask().getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskChecklistRepository.delete(itemId)).thenReturn(taskChecklistEntity);
      when(this.taskChecklistMapper.toTaskChecklistDeletedDTO(taskChecklistEntity)).thenReturn(taskChecklistDeletedDTO);
 
      // act and assert
      TaskChecklistDeletedDTO result = this.taskChecklistService.delete(itemId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskChecklistDeletedDTO);
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).delete(itemId);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskChecklistMapper, times(1)).toTaskChecklistDeletedDTO(taskChecklistEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if checklist item is deleted")
   void isDeleted() {
      // id's - setup
      UUID itemId = UUID.randomUUID();
 
      // stubs
      when(this.taskChecklistRepository.isDeleted(itemId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskChecklistService.isDeleted(itemId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.taskChecklistValidator, times(1)).idValidate(itemId);
      verify(this.taskChecklistRepository, times(1)).isDeleted(itemId);
   }

}