
// packages
package com.example.ProjectFlow.modules.task.unitTests.taskService;

// imports
import java.time.LocalDate;
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
import com.example.ProjectFlow.modules.task.repository.TaskRepository;
 
// import validator
import com.example.ProjectFlow.modules.task.validator.TasksValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksCompleteResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksUpdateDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.board.service.BoardColumnService;
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.task.service.TaskService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import mapper
import com.example.ProjectFlow.modules.task.mapper.TaskMapper;

// import enums
import com.example.ProjectFlow.modules.board.enums.BoardEnum;
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - TaskService")
public class TaskHappyTests {
 
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
   @DisplayName("create() - must create a task")
   void create() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // users - setup
      UserEntity ownerEntity = new UserEntity.Builder()
         .id(ownerId)
         .name("user x")
         .build();
 
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
 
      // column - setup
      BoardColumnsEntity columnEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.TODO)
         .position(1)
         .build();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .boardColumn(columnEntity)
         .owner(ownerEntity)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // DTO's - setup
      TasksDTO tasksDTO = new TasksDTO(
         projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(),
         taskEntity.getPriority().toString(), taskEntity.getDueDate()
      );
 
      TasksResponseDTO tasksResponseDTO = new TasksResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(),
         taskEntity.getPriority(), taskEntity.getDueDate()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .userId(taskEntity.getOwner().getId())
         .action(ActivityActionEnum.TAREFA_CRIADA)
         .description(
            "Tarefa: " + taskEntity.getTitle() +
            ", do projeto: " + taskEntity.getProject().getName() +
            ", criada por: " + ownerEntity.getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.TAREFA_ATRIBUIDA)
         .message(
            "Tarefa: " + taskEntity.getTitle() +
            ", atribuida ao projeto: " + taskEntity.getProject().getName() +
            ", por: " + ownerEntity.getName()
         )
         .build();
 
      // stubs
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);
      when(this.boardColumnService.getEntityById(columnId)).thenReturn(columnEntity);
      when(this.userService.getEntityById(ownerId)).thenReturn(ownerEntity);
      when(this.taskRepository.create(tasksDTO, projectEntity, columnEntity, ownerEntity)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksResponseDTO(taskEntity)).thenReturn(tasksResponseDTO);
 
      // act and assert
      TasksResponseDTO result = this.taskService.create(tasksDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).projectIdValidate(projectId);
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.tasksValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.tasksValidator, times(1)).titleValidate(tasksDTO.title());
      verify(this.tasksValidator, times(1)).descriptionValidate(tasksDTO.description());
      verify(this.tasksValidator, times(1)).priorityValidate(tasksDTO.priority());
      verify(this.tasksValidator, times(1)).dueDateValidate(tasksDTO.dueDate());
 
      verify(this.projectService, times(1)).getEntityById(projectId);
      verify(this.boardColumnService, times(1)).getEntityById(columnId);
      verify(this.userService, times(1)).getEntityById(ownerId);
 
      verify(this.taskRepository, times(1)).create(tasksDTO, projectEntity, columnEntity, ownerEntity);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskMapper, times(1)).toTasksResponseDTO(taskEntity);
   }


   @Test
   @DisplayName("getAll() - must return all tasks")
   void getAll() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // list - setup
      List<TasksEntity> taskList = List.of(taskEntity);
 
      // DTO - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      // stubs
      when(this.taskRepository.getAll()).thenReturn(taskList);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      List<TasksCompleteResponseDTO> result = this.taskService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(tasksCompleteResponseDTO);
 
      // checks
      verify(this.taskRepository, times(1)).getAll();
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("getById() - must return task by id")
   void getById() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // DTO - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      // stubs
      when(this.taskRepository.getById(taskId)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      TasksCompleteResponseDTO result = this.taskService.getById(taskId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).getById(taskId);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must return task entity by id")
   void getEntityById() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .build();
 
      // stubs
      when(this.taskRepository.getEntityById(taskId)).thenReturn(taskEntity);
 
      // act and assert
      TasksEntity result = this.taskService.getEntityById(taskId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(taskEntity);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).getEntityById(taskId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if task exist by id")
   void existsById() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.existsById(taskId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskService.existsById(taskId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).existsById(taskId);
   }
 
 
   @Test
   @DisplayName("getByProjectId() - must return all tasks by project id")
   void getByProjectId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // list - setup
      List<TasksEntity> taskList = List.of(taskEntity);
 
      // DTO - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      // stubs
      when(this.taskRepository.getByProjectId(projectId)).thenReturn(taskList);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      List<TasksCompleteResponseDTO> result = this.taskService.getByProjectId(projectId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).projectIdValidate(projectId);
      verify(this.taskRepository, times(1)).getByProjectId(projectId);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("getByColumnId() - must return all tasks by column id")
   void getByColumnId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // list - setup
      List<TasksEntity> taskList = List.of(taskEntity);
 
      // DTO - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      // stubs
      when(this.taskRepository.getByColumnId(columnId)).thenReturn(taskList);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      List<TasksCompleteResponseDTO> result = this.taskService.getByColumnId(columnId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.taskRepository, times(1)).getByColumnId(columnId);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("getByOwnerId() - must return all tasks by owner id")
   void getByOwnerId() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title("task x")
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // list - setup
      List<TasksEntity> taskList = List.of(taskEntity);
 
      // DTO - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      // stubs
      when(this.taskRepository.getByOwnerId(ownerId)).thenReturn(taskList);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      List<TasksCompleteResponseDTO> result = this.taskService.getByOwnerId(ownerId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.taskRepository, times(1)).getByOwnerId(ownerId);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }


   @Test
   @DisplayName("updateColumn() - must update task column (task position)")
   void updateColumn() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
 
      // users - setup
      UserEntity ownerEntity = new UserEntity.Builder()
         .id(ownerId)
         .name("user x")
         .build();
 
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
 
      // column - setup (new column)
      BoardColumnsEntity columnEntity = new BoardColumnsEntity.Builder()
         .id(columnId)
         .name(BoardEnum.PROGRESS)
         .position(2)
         .build();
 
      // task - setup (already updated)
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .project(projectEntity)
         .boardColumn(columnEntity)
         .owner(ownerEntity)
         .title("task x")
         .build();
 
      // DTO's - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .userId(taskEntity.getOwner().getId())
         .action(ActivityActionEnum.POSICAO_DA_TAREFA_ATUALIZADA)
         .description(
            "Posição da tarefa: " + taskEntity.getTitle() +
            ", atualizada para: " + taskEntity.getBoardColumn().getPosition() +
            ", por: " + taskEntity.getOwner().getName() +
            ", no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.POSICAO_DA_TAREFA_ATUALIZADO)
         .message(
            "Posição da tarefa: " + taskEntity.getTitle() +
            ", atualizada para: " + taskEntity.getBoardColumn().getPosition() +
            ", por: " + taskEntity.getOwner().getName() +
            ", no projeto: " + taskEntity.getProject().getName()
         )
         .build();
 
      // stubs
      when(this.boardColumnService.getEntityById(columnId)).thenReturn(columnEntity);
      when(this.taskRepository.updateColumn(taskId, columnEntity)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      TasksCompleteResponseDTO result = this.taskService.updateColumn(taskId, columnId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.tasksValidator, times(1)).columnIdValidate(columnId);
      verify(this.boardColumnService, times(1)).getEntityById(columnId);
 
      verify(this.taskRepository, times(1)).updateColumn(taskId, columnEntity);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("update() - must update task (only the title)")
   void update() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
      // data to update
      String newTitle = "task atualizada";
 
      // task - setup
      TasksEntity taskEntity = new TasksEntity.Builder()
         .id(taskId)
         .title(newTitle)
         .description("task description")
         .priority(PriorityEnum.HIGH)
         .dueDate(LocalDate.now().plusDays(7))
         .build();
 
      // DTO's - setup
      TasksUpdateDTO tasksUpdateDTO = new TasksUpdateDTO(
         newTitle, taskEntity.getDescription(),
         taskEntity.getPriority().toString(), taskEntity.getDueDate()
      );
 
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .description("Tarefa: " + taskEntity.getTitle() + ", atualizada")
         .build();
 
      // stubs
      when(this.taskRepository.update(taskId, tasksUpdateDTO)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      TasksCompleteResponseDTO result = this.taskService.update(taskId, tasksUpdateDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.tasksValidator, times(1)).updateValidations(tasksUpdateDTO);
      verify(this.taskRepository, times(1)).update(taskId, tasksUpdateDTO);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("taskComplete() - must set a task as complete")
   void taskComplete() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
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
 
      // DTO's - setup
      TasksCompleteResponseDTO tasksCompleteResponseDTO = new TasksCompleteResponseDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .action(ActivityActionEnum.TAREFA_CONCLUIDA)
         .description(
            "Tarefa: " + taskEntity.getTitle() +
            ", do projeto: " + taskEntity.getProject().getName() +
            ", completada"
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.TAREFA_CONCLUIDA)
         .message(
            "Tarefa: " + taskEntity.getTitle() +
            ", do projeto: " + taskEntity.getProject().getName() +
            ", completada"
         )
         .build();
 
      // stubs
      when(this.taskRepository.taskComplete(taskId)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksCompleteResponseDTO(taskEntity)).thenReturn(tasksCompleteResponseDTO);
 
      // act and assert
      TasksCompleteResponseDTO result = this.taskService.taskComplete(taskId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksCompleteResponseDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).taskComplete(taskId);

      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      
      verify(this.taskMapper, times(1)).toTasksCompleteResponseDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("delete() - must delete a task")
   void delete() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID memberUserId = UUID.randomUUID();
      UUID columnId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
 
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
 
      // DTO's - setup
      TasksDeletedDTO tasksDeletedDTO = new TasksDeletedDTO(
         taskId, projectId, columnId, ownerId,
         taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getPriority(),
         taskEntity.getDueDate(), taskEntity.getCompletedAt(), LocalDateTime.now()
      );
 
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .taskId(taskEntity.getId())
         .action(ActivityActionEnum.TAREFA_DELETADA)
         .description(
            "Tarefa: " + taskEntity.getTitle() +
            ", do projeto: " + taskEntity.getProject().getName() +
            ", deletada"
         )
         .build();
 
      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(memberUserEntity.getId().toString())
         .title(NotificationTitleEnum.TAREFA_EXCLUIDA)
         .message(
            "Tarefa: " + taskEntity.getTitle() +
            ", do projeto: " + taskEntity.getProject().getName() +
            ", deletada"
         )
         .build();
 
      // stubs
      when(this.taskRepository.delete(taskId)).thenReturn(taskEntity);
      when(this.taskMapper.toTasksDeletedDTO(taskEntity)).thenReturn(tasksDeletedDTO);
 
      // act and assert
      TasksDeletedDTO result = this.taskService.delete(taskId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(tasksDeletedDTO);
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).delete(taskId);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.taskMapper, times(1)).toTasksDeletedDTO(taskEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if task is deleted")
   void isDeleted() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.taskRepository.isDeleted(taskId)).thenReturn(true);
 
      // act and assert
      boolean result = this.taskService.isDeleted(taskId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.tasksValidator, times(1)).idValidate(taskId);
      verify(this.taskRepository, times(1)).isDeleted(taskId);
   }

}