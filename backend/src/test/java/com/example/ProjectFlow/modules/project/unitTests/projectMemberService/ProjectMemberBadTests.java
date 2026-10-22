
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectMemberService;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectMembersRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectMembersValidator;

// import service
import com.example.ProjectFlow.modules.project.service.ProjectMemberService;
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;

// import enums
import com.example.ProjectFlow.modules.project.enums.RoleEnum;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMembersMapper;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import exceptions
import jakarta.persistence.NoResultException;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - ProjectMemberService")
public class ProjectMemberBadTests {
   
   // mock dependencies
   @Mock
   private ProjectMembersRepository projectMembersRepository;
   @Mock
   private ProjectMembersValidator projectMembersValidator;
   @Mock
   private ProjectService projectService;
   @Mock
   private UserService userService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock
   private ProjectMembersMapper projectMembersMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProjectMemberService projectMemberService;


   @Test
   @DisplayName("createMemberParticipation() - must throw that user does not exist")
   void createMemberParticipation() {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      RoleEnum role = RoleEnum.MEMBER;

      // message error - setup
      String errorMessage = "Recurso não encontrado: Usuário não existe";

      // dto - setup
      ProjectMembersDTO projectMembersDTO = new ProjectMembersDTO(
         projectId, userId, role.toString()
      );

      // stubs
      when(this.userService.getEntityById(userId))
         .thenThrow(MultiExceptions.notFound(errorMessage));

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.createMemberParticipation(projectMembersDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).userIdValidate(userId);
      verify(this.projectMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.userService, times(1)).getEntityById(userId);

      // no checks
      verifyNoInteractions(projectService);
      verifyNoInteractions(projectMembersRepository);
      verifyNoInteractions(activityLogService);
      verifyNoInteractions(notificationService);
      verifyNoInteractions(projectMembersMapper);
   }


   @Test
   @DisplayName("getAllProjectMembers() - must throw when members list is empty")
   void getAllProjectMembers() {
      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of();

      // stubs
      when(this.projectMembersRepository.getAllProjectMembers()).thenReturn(membersListEntity);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.getAllProjectMembers())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");

      // checks
      verify(this.projectMembersRepository, times(1)).getAllProjectMembers();

      // no checks
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("getProjectMemberById() - must throw when project member not found")
   void getProjectMemberById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.getProjectMemberById(relationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.getProjectMemberById(relationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).getProjectMemberById(relationId);

      // no checks
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("getAllMembersByProjectId() - must throw when members list is empty")
   void getAllMembersByProjectId() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of();

      // stubs
      when(this.projectMembersRepository.getAllMembersByProjectId(projectId)).thenReturn(membersListEntity);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.getAllMembersByProjectId(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).getAllMembersByProjectId(projectId);

      // no checks
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("getAllMembersByRole() - must throw when members list is empty")
   void getAllMembersByRole() {
      // data - setup
      UUID projectId = UUID.randomUUID();
      RoleEnum role = RoleEnum.MEMBER;

      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of();

      // stubs
      when(this.projectMembersRepository.getAllMembersByRole(projectId, role)).thenReturn(membersListEntity);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.getAllMembersByRole(projectId, role.toString()))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).getAllMembersByRole(projectId, role);

      // no checks
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("getEntityById() - must throw when project member entity not found")
   void getEntityById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.getEntityById(relationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.getEntityById(relationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
   }


   @Test
   @DisplayName("existsById() - must throw when member does not exist")
   void existsById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.existsById(relationId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.existsById(relationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).existsById(relationId);
   }


   @Test
   @DisplayName("existsByProjectIdAndMemberId() - must throw when member does not exist")
   void existsByProjectIdAndMemberId() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.existsByProjectIdAndMemberId(projectId, memberId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.existsByProjectIdAndMemberId(projectId, memberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).userIdValidate(memberId);
      verify(this.projectMembersRepository, times(1)).existsByProjectIdAndMemberId(projectId, memberId);
   }


   @Test
   @DisplayName("checkUserMembership() - must throw when member does not exist")
   void checkUserMembership() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.checkUserMembership(userId, projectId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.checkUserMembership(userId, projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não participa do projeto");

      // checks
      verify(this.projectMembersValidator, times(1)).userIdValidate(userId);
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).checkUserMembership(userId, projectId);
   }


   @Test
   @DisplayName("updateMemberRole() - must throw when the last admin changes the role (should exist at least 1 admin)")
   void updateMemberRole() {
      // data - setup
      UUID relationId = UUID.randomUUID();
      RoleEnum currentRole = RoleEnum.ADMIN;
      RoleEnum roleToUpdate = RoleEnum.MEMBER;

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .project(projectEntity)
         .role(currentRole)
         .build();

      // stubs
      when(this.projectMembersRepository.getEntityById(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersRepository.countAdminsByProject(projectMembersEntity.getProject().getId())).thenReturn(1L);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.updateMemberRole(relationId, roleToUpdate.toString()))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Um projeto deve ter pelo menos 1 administrador");
      
      // checks
      verify(this.projectMembersValidator, times(2)).idValidate(relationId);
      verify(this.projectMembersValidator, times(1)).roleValidate(roleToUpdate.toString());
      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectMembersEntity.getProject().getId());
      verify(this.projectMembersRepository, times(1)).countAdminsByProject(projectMembersEntity.getProject().getId());

      // no checks
      verify(this.projectMembersRepository, never()).updateMemberRole(any(UUID.class), any(RoleEnum.class));
      verifyNoInteractions(this.activityLogService);      
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("removeParticipation() - must throw when the last admin changes the role (should exist at least 1 admin)")
   void removeParticipation() {
      // data - setup
      UUID relationId = UUID.randomUUID();

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.ADMIN)
         .project(projectEntity)
         .build();

      // stubs
      when(this.projectMembersRepository.getEntityById(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersRepository.countAdminsByProject(projectMembersEntity.getProject().getId())).thenReturn(1L);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.removeParticipation(relationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Um projeto deve ter pelo menos 1 administrador");
      
      // checks
      verify(this.projectMembersValidator, times(2)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectMembersEntity.getProject().getId());
      verify(this.projectMembersRepository, times(1)).countAdminsByProject(projectMembersEntity.getProject().getId());

      // no checks
      verify(this.projectMembersRepository, never()).removeParticipation(any(UUID.class));
      verifyNoInteractions(this.activityLogService);      
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.projectMembersMapper);
   }


   @Test
   @DisplayName("isRemoved() - must throw when member does not exist")
   void isRemoved() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.isRemoved(relationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.projectMemberService.isRemoved(relationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Participação não existe");

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).isRemoved(relationId);
   }

}