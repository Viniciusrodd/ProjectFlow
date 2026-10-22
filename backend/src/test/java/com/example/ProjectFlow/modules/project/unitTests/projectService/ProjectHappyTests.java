
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectService;

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
import com.example.ProjectFlow.modules.project.repository.ProjectRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectUpdateDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDeletedDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMapper;

// import enum
import com.example.ProjectFlow.modules.project.enums.StatusEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ProjectService")
public class ProjectHappyTests {
 
   // mock dependencies
   @Mock
   private ProjectRepository projectRepository;
   @Mock
   private ProjectValidator projectValidator;
   @Mock
   private UserService userService;
   @Mock
   private OrganizationService organizationService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private ProjectMapper projectMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProjectService projectService;


   @Test
   @DisplayName("getAll() - must return all projects")
   void getAll() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // list - setup
      List<ProjectEntity> projectList = List.of(projectEntity);

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, ownerId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // mocks config
      when(this.projectRepository.getAll()).thenReturn(projectList);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      List<ProjectResponseDTO> result = this.projectService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(projectResponseDTO);

      // checks
      verify(this.projectRepository, times(1)).getAll();
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);
   }


   @Test
   @DisplayName("getById() - must return project by id")
   void getById() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, ownerId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // mocks config
      when(this.projectRepository.getById(projectId)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      ProjectResponseDTO result = this.projectService.getById(projectId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectResponseDTO);

      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).getById(projectId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);
   }


   @Test
   @DisplayName("getEntityById() - must return project entity by id")
   void getEntityById() {
      // id's - setup
      UUID projectId = UUID.randomUUID();

      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();

      // mocks config
      when(this.projectRepository.getEntityById(projectId)).thenReturn(projectEntity);

      // act and assert
      ProjectEntity result = this.projectService.getEntityById(projectId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectEntity);

      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).getEntityById(projectId);
   }


   @Test
   @DisplayName("existsById() - must check if project exist by id")
   void existsById() {
      // id's - setup
      UUID projectId = UUID.randomUUID();

      // mocks config
      when(this.projectRepository.existsById(projectId)).thenReturn(true);

      // act and assert
      boolean result = this.projectService.existsById(projectId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).existsById(projectId);
   }


   @Test
   @DisplayName("getByOrganizationId() - must return all project by organization id")
   void getByOrganizationId() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // list - setup
      List<ProjectEntity> projectList = List.of(projectEntity);

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, ownerId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // mocks config
      when(this.projectRepository.getByOrganizationId(organizationId)).thenReturn(projectList);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      List<ProjectResponseDTO> result = this.projectService.getByOrganizationId(organizationId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(projectResponseDTO);

      // checks
      verify(this.projectValidator, times(1)).organizationIdValidate(organizationId);
      verify(this.projectRepository, times(1)).getByOrganizationId(organizationId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);
   }


   @Test
   @DisplayName("getByOwnerId() - must return all project by owner id")
   void getByOwnerId() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();

      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // list - setup
      List<ProjectEntity> projectList = List.of(projectEntity);

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, ownerId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // mocks config
      when(this.projectRepository.getByOwnerId(ownerId)).thenReturn(projectList);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      List<ProjectResponseDTO> result = this.projectService.getByOwnerId(ownerId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(projectResponseDTO);

      // checks
      verify(this.projectValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.projectRepository, times(1)).getByOwnerId(ownerId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);
   }


   @Test
   @DisplayName("update() - must update project (only the name)")
   void update() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // data to update
      String newName = "project atualizado";
      
      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name(newName)
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // DTO - setup
      ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO(
         newName, projectEntity.getDescription(), projectEntity.getStatus().toString()
      );

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectEntity.getId())
         .action(ActivityActionEnum.PROJETO_ATUALIZADO)
         .description("Projeto: " + projectEntity.getName() + " atualizado")
         .build();

      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // mocks config
      when(this.projectRepository.getById(projectId)).thenReturn(projectEntity);
      when(this.projectRepository.update(projectId, projectUpdateDTO)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      ProjectResponseDTO result = this.projectService.update(projectId, userId, projectUpdateDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectResponseDTO);

      // checks
      verify(this.projectValidator, times(2)).idValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.projectValidator, times(1)).updateValidations(projectUpdateDTO);

      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.projectRepository, times(1)).getById(projectId);

      verify(this.projectRepository, times(1)).update(projectId, projectUpdateDTO);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.projectMapper, times(2)).toProjectResponseDTO(projectEntity);
   }


   @Test
   @DisplayName("delete() - must delete a project")
   void delete() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      
      // data - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .description("project description")
         .status(StatusEnum.ACTIVE)
         .build();

      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectEntity.getId())
         .action(ActivityActionEnum.PROJETO_DELETADO)
         .description("Projeto: " + projectEntity.getName() + " deletado")
         .build();

      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );
      
      ProjectDeletedDTO projectDeletedDTO = new ProjectDeletedDTO(
         projectId, organizationId, userId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus(),
         LocalDateTime.now()
      );

      // mocks config
      when(this.projectRepository.getById(projectId)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);
      when(this.projectRepository.delete(projectId)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectDeletedDTO(projectEntity)).thenReturn(projectDeletedDTO);

      // act and assert
      ProjectDeletedDTO result = this.projectService.delete(projectId, userId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectDeletedDTO);

      // checks
      verify(this.projectValidator, times(2)).idValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);

      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.projectRepository, times(1)).getById(projectId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);

      verify(this.projectRepository, times(1)).delete(projectId);
      verify(this.activityLogService, times(1)).create(activityLogDTO);      
      verify(this.projectMapper, times(1)).toProjectDeletedDTO(projectEntity);
   }


   @Test
   @DisplayName("isDeleted() - must check if project is deleted")
   void isDeleted() {
      // id's - setup
      UUID projectId = UUID.randomUUID();

      // mocks config
      when(this.projectRepository.isDeleted(projectId)).thenReturn(true);

      // act and assert
      boolean result = this.projectService.isDeleted(projectId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).isDeleted(projectId);
   }

}