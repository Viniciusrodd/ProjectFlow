
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectCreationService;

// imports
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

// jakarta imports

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import service
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.project.service.ProjectCreationService;
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.project.enums.StatusEnum;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMapper;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ProjectCreationService")
public class ProjectCreationTests {

   // mock dependencies
   @Mock
   private ProjectService projectService;
   @Mock
   private ProjectMemberService projectMemberService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private ProjectMapper projectMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProjectCreationService projectCreationService;


   @Test
   @DisplayName("create() - should create a project")
   void create() {
      // data - setup
      RoleEnum role = RoleEnum.ADMIN;
      UUID organizationId = UUID.randomUUID();
      StatusEnum status = StatusEnum.ACTIVE;

      // owner - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("vini")
         .build();
      
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(UUID.randomUUID())
         .name("project x")
         .description("project description")
         .owner(userEntity)
         .build();

      // DTO - setup
      ProjectDTO projectDTO = new ProjectDTO(
         organizationId, userEntity.getId(), projectEntity.getName(), projectEntity.getDescription()
      );

      ProjectMembersDTO projectMembersDTO = new ProjectMembersDTO(
         projectEntity.getId(), userEntity.getId(), role.toString()
      );

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectEntity.getId())
         .userId(projectEntity.getOwnerId())
         .action(ActivityActionEnum.PROJETO_CRIADO)
         .description(
            "Projeto: " + projectEntity.getName() + 
            ", criado por: " + projectEntity.getOwner().getName()
         )
         .build();

      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectEntity.getId(), organizationId, userEntity.getId(),
         projectEntity.getName(), projectEntity.getDescription(), status
      );

      // stubs
      when(this.projectService.create(projectDTO)).thenReturn(projectEntity);
      when(this.projectMapper.toProjectResponseDTO(projectEntity)).thenReturn(projectResponseDTO);

      // act and assert
      ProjectResponseDTO result = this.projectCreationService.create(projectDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectResponseDTO);

      // checks
      verify(projectService, times(1)).create(projectDTO);
      verify(projectMemberService, times(1)).createMemberParticipation(projectMembersDTO);
      verify(activityLogService, times(1)).create(activityLogDTO);
      verify(projectMapper, times(1)).toProjectResponseDTO(projectEntity);
   }

}