
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskLabelService;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.task.repository.TaskLabelsRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TaskLabelsValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsDeletedDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.task.service.TaskService;
import com.example.ProjectFlow.modules.labels.service.LabelService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskLabelService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;
 
// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskLabelsMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - TaskLabelService")
public class TaskLabelHappyTests {
 
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
   @DisplayName("create() - must create a task label relation")
   void create() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
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
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .project(projectEntity)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // task label relation - setup
      TaskLabelsEntity taskLabelsEntity = new TaskLabelsEntity.Builder()
         .id(taskLabelId)
         .task(taskEntity)
         .label(labelEntity)
         .build();
 
      // DTO's - setup
      TaskLabelsResponseDTO taskLabelsResponseDTO = new TaskLabelsResponseDTO(
         taskLabelId, taskId, labelId, LocalDateTime.now()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskLabelsEntity.getTask().getId())
         .action(ActivityActionEnum.ETIQUETA_DA_TAREFA_ADICIONADA)
         .description(
            "Etiqueta: " + taskLabelsEntity.getLabel().getName() +
            ", da tarefa: " + taskLabelsEntity.getTask().getTitle() +
            ", criada no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.ETIQUETA_DA_TAREFA_ADICIONADA)
         .message(
            "Etiqueta: " + taskLabelsEntity.getLabel().getName() +
            ", da tarefa: " + taskLabelsEntity.getTask().getTitle() +
            ", criada no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskService.getEntityById(taskId)).thenReturn(taskEntity);
      when(this.labelService.getEntityById(labelId)).thenReturn(labelEntity);
      when(this.taskLabelsRepository.create(taskEntity, labelEntity)).thenReturn(taskLabelsEntity);
      when(this.taskLabelsMapper.toTaskLabelsResponseDTO(taskLabelsEntity)).thenReturn(taskLabelsResponseDTO);
 
      // act and assert
      TaskLabelsResponseDTO result = this.taskLabelService.create(taskId, labelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskLabelsResponseDTO);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.taskService, times(1)).getEntityById(taskId);
      verify(this.labelService, times(1)).getEntityById(labelId);
      verify(this.taskLabelsValidator, times(1)).projectValidation(taskEntity.getProjectId(), labelEntity.getProjectId());
 
      verify(this.taskLabelsRepository, times(1)).create(taskEntity, labelEntity);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskLabelsMapper, times(1)).toTaskLabelsResponseDTO(taskLabelsEntity);
   }


   @Test
   @DisplayName("getAllByTaskId() - must return all labels by task id")
   void getAllByTaskId() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
 
      // task label relation - setup
      TaskLabelsEntity taskLabelsEntity = new TaskLabelsEntity.Builder()
         .id(taskLabelId)
         .build();
 
      // list - setup
      List<TaskLabelsEntity> taskLabelsList = List.of(taskLabelsEntity);
 
      // DTO - setup
      LabelsByTaskResponseDTO labelsByTaskResponseDTO = mock(LabelsByTaskResponseDTO.class);
 
      // stubs
      when(this.taskLabelsRepository.getAllByTaskId(taskId)).thenReturn(taskLabelsList);
      when(this.taskLabelsMapper.toLabelsByTaskResponseDTO(taskLabelsEntity)).thenReturn(labelsByTaskResponseDTO);
 
      // act and assert
      List<LabelsByTaskResponseDTO> result = this.taskLabelService.getAllByTaskId(taskId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(labelsByTaskResponseDTO);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskService, times(1)).existsById(taskId);
      verify(this.taskLabelsRepository, times(1)).getAllByTaskId(taskId);
      verify(this.taskLabelsMapper, times(1)).toLabelsByTaskResponseDTO(taskLabelsEntity);
   }
 
 
   @Test
   @DisplayName("getAllByLabelId() - must return all tasks by label id")
   void getAllByLabelId() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // task label relation - setup
      TaskLabelsEntity taskLabelsEntity = new TaskLabelsEntity.Builder()
         .id(taskLabelId)
         .build();
 
      // list - setup
      List<TaskLabelsEntity> taskLabelsList = List.of(taskLabelsEntity);
 
      // DTO - setup
      TasksByLabelResponseDTO tasksByLabelResponseDTO = mock(TasksByLabelResponseDTO.class);
 
      // stubs
      when(this.taskLabelsRepository.getAllByLabelId(labelId)).thenReturn(taskLabelsList);
      when(this.taskLabelsMapper.toTasksByLabelResponseDTO(taskLabelsEntity)).thenReturn(tasksByLabelResponseDTO);
 
      // act and assert
      List<TasksByLabelResponseDTO> result = this.taskLabelService.getAllByLabelId(labelId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(tasksByLabelResponseDTO);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.labelService, times(1)).existsById(labelId);
      verify(this.taskLabelsRepository, times(1)).getAllByLabelId(labelId);
      verify(this.taskLabelsMapper, times(1)).toTasksByLabelResponseDTO(taskLabelsEntity);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must return task label entity by id")
   void getEntityById() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
 
      // task label relation - setup
      TaskLabelsEntity taskLabelsEntity = new TaskLabelsEntity.Builder()
         .id(taskLabelId)
         .build();
 
      // stubs
      when(this.taskLabelsRepository.getEntityById(taskLabelId)).thenReturn(taskLabelsEntity);
 
      // act and assert
      TaskLabelsEntity result = this.taskLabelService.getEntityById(taskLabelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskLabelsEntity);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).getEntityById(taskLabelId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if task label exist by id")
   void existsById() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.existsById(taskLabelId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskLabelService.existsById(taskLabelId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).existsById(taskLabelId);
   }
 
 
   @Test
   @DisplayName("existsByTaskIdAndLabelId() - must check if task label exist by task id and label id")
   void existsByTaskIdAndLabelId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.taskLabelsRepository.existsByTaskIdAndLabelId(taskId, labelId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskLabelService.existsByTaskIdAndLabelId(taskId, labelId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.taskLabelsValidator, times(1)).taskIdValidate(taskId);
      verify(this.taskLabelsValidator, times(1)).labelIdValidate(labelId);
      verify(this.taskLabelsRepository, times(1)).existsByTaskIdAndLabelId(taskId, labelId);
   }
 
 
   @Test
   @DisplayName("removeRelation() - must remove a task label relation")
   void removeRelation() {
      // id's - setup
      UUID taskLabelId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID labelId = UUID.randomUUID();
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
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .project(projectEntity)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // task label relation - setup
      TaskLabelsEntity taskLabelsEntity = new TaskLabelsEntity.Builder()
         .id(taskLabelId)
         .task(taskEntity)
         .label(labelEntity)
         .build();
 
      // DTO's - setup
      TaskLabelsDeletedDTO taskLabelsDeletedDTO = new TaskLabelsDeletedDTO(
         taskLabelId, taskId, labelId,
         LocalDateTime.now(), LocalDateTime.now()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskLabelsEntity.getTask().getId())
         .action(ActivityActionEnum.ETIQUETA_DA_TAREFA_REMOVIDA)
         .description(
            "Etiqueta: " + taskLabelsEntity.getLabel().getName() +
            ", da tarefa: " + taskLabelsEntity.getTask().getTitle() +
            ", removida no projeto: " + taskLabelsEntity.getTask().getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.ETIQUETA_DA_TAREFA_REMOVIDA)
         .message(
            "Etiqueta: " + taskLabelsEntity.getLabel().getName() +
            ", da tarefa: " + taskLabelsEntity.getTask().getTitle() +
            ", removida no projeto: " + taskLabelsEntity.getTask().getProject().getName()
         )
         .build();
 
      // stubs
      when(this.taskLabelsRepository.removeRelation(taskLabelId)).thenReturn(taskLabelsEntity);
      when(this.taskLabelsMapper.toTaskLabelsDeletedDTO(taskLabelsEntity)).thenReturn(taskLabelsDeletedDTO);
 
      // act and assert
      TaskLabelsDeletedDTO result = this.taskLabelService.removeRelation(taskLabelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskLabelsDeletedDTO);
 
      // checks
      verify(this.taskLabelsValidator, times(1)).idValidate(taskLabelId);
      verify(this.taskLabelsRepository, times(1)).removeRelation(taskLabelId);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskLabelsMapper, times(1)).toTaskLabelsDeletedDTO(taskLabelsEntity);
   }

}