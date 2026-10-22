
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectService;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectUpdateDTO;

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

// jakarta imports
import jakarta.persistence.NoResultException;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - ProjectService")
public class ProjectBadTests {
 
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
   @DisplayName("getAll() - must throw when project list is empty")
   void getAll() {
      // list - setup
      List<ProjectEntity> projectList = List.of();

      // stubs
      when(this.projectRepository.getAll()).thenReturn(projectList);

      // act and assert
      assertThatThrownBy(() -> this.projectService.getAll())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projetos não existem");
      
      // checks
      verify(this.projectRepository, times(1)).getAll();
      
      // no checks
      verifyNoInteractions(this.projectMapper);
   }


   @Test
   @DisplayName("getById() - must throw project not found")
   void getById() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // stubs
      when(this.projectRepository.getById(projectId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectService.getById(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projeto não existe");
      
      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).getById(projectId);
      
      // no checks
      verifyNoInteractions(this.projectMapper);
   }

    
   @Test
   @DisplayName("getEntityById() - must throw project not found")
   void getEntityById() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // stubs
      when(this.projectRepository.getEntityById(projectId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectService.getEntityById(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projeto não existe");
      
      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).getEntityById(projectId);
   }


   @Test
   @DisplayName("existsById() - must throw when project does not exist")
   void existsById() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // stubs
      when(this.projectRepository.existsById(projectId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.projectService.existsById(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projeto não existe");
      
      // checks
      verify(this.projectValidator, times(1)).idValidate(projectId);
      verify(this.projectRepository, times(1)).existsById(projectId);
   }


   @Test
   @DisplayName("getByOrganizationId() - must throw when project list is empty")
   void getByOrganizationId() {
      // id - setup
      UUID organizationId = UUID.randomUUID();

      // list - setup
      List<ProjectEntity> projectList = List.of();

      // stubs
      when(this.projectRepository.getByOrganizationId(organizationId)).thenReturn(projectList);

      // act and assert
      assertThatThrownBy(() -> this.projectService.getByOrganizationId(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projetos não existem");
      
      // checks
      verify(this.projectValidator, times(1)).organizationIdValidate(organizationId);
      verify(this.projectRepository, times(1)).getByOrganizationId(organizationId);
      
      // no checks
      verifyNoInteractions(this.projectMapper);
   }


   @Test
   @DisplayName("getByOwnerId() - must throw when project list is empty")
   void getByOwnerId() {
      // id - setup
      UUID ownerId = UUID.randomUUID();

      // list - setup
      List<ProjectEntity> projectList = List.of();

      // stubs
      when(this.projectRepository.getByOwnerId(ownerId)).thenReturn(projectList);

      // act and assert
      assertThatThrownBy(() -> this.projectService.getByOwnerId(ownerId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projetos não existem");
      
      // checks
      verify(this.projectValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.projectRepository, times(1)).getByOwnerId(ownerId);
      
      // no checks
      verifyNoInteractions(this.projectMapper);
   }


   @Test
   @DisplayName("update() - must throw when project does not exist")
   void update() {
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

      // DTO - setup
      ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO(
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus().toString()
      );

      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // stubs
      when(this.projectRepository.getById(projectId)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);
      when(this.projectRepository.update(projectId, projectUpdateDTO)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectService.update(projectId, userId, projectUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projeto não existe");
      
      // checks
      verify(this.projectValidator, times(2)).idValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.projectValidator, times(1)).updateValidations(projectUpdateDTO);
      
      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.projectRepository, times(1)).getById(projectId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);

      verify(this.projectRepository, times(1)).update(projectId, projectUpdateDTO);

      // no checks
      verifyNoInteractions(this.activityLogService);      
   }


   @Test
   @DisplayName("delete() - must throw when project does not exist")
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

      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId,
         projectEntity.getName(), projectEntity.getDescription(), projectEntity.getStatus()
      );

      // stubs
      when(this.projectRepository.getById(projectId)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);
      when(this.projectRepository.delete(projectId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectService.delete(projectId, userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Projeto não existe");
      
      // checks
      verify(this.projectValidator, times(2)).idValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);
      
      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.projectRepository, times(1)).getById(projectId);
      verify(this.projectMapper, times(1)).toProjectResponseDTO(projectEntity);

      verify(this.projectRepository, times(1)).delete(projectId);

      // no checks
      verifyNoInteractions(this.activityLogService);  
      verify(this.projectMapper, never()).toProjectDeletedDTO(projectEntity);
   }

}