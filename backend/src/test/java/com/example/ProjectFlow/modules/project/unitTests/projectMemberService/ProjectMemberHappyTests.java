
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectMemberService;

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
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.MemberByProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectMembersMapper;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ProjectMemberService")
public class ProjectMemberHappyTests {
   
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
   @DisplayName("createMemberParticipation() - must create an project member participation")
   void createMemberParticipation() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      
      // role - setup
      RoleEnum role = RoleEnum.MEMBER;

      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(userId)
         .build();

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .project(projectEntity)
         .user(userEntity)
         .role(role)
         .build();

      // set members
      projectEntity.setMembers(List.of(projectMembersEntity));

      // dto - setup
      ProjectMembersDTO projectMembersDTO = new ProjectMembersDTO(
         projectId, userId, role.toString()
      );

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectMembersEntity.getProjectId())
         .userId(userEntity.getId())
         .action(ActivityActionEnum.MEMBRO_ADICIONADO_AO_PROJETO)
         .description(
            "Participação do usuário: " + projectMembersEntity.getUser().getName() + 
            ", adicionada ao projeto: " + projectMembersEntity.getProject().getName() + 
            ", como: " + projectMembersEntity.getRole()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.MEMBRO_ADICIONADO_AO_PROJETO)
         .message("O usuário: " + userEntity.getName() + ", foi adicionado ao projeto: " + projectEntity.getName())
         .build();
      
      ProjectMembersResponseDTO projectMembersResponseDTO = new ProjectMembersResponseDTO(
         projectId, userId, role, LocalDateTime.now()
      );

      // stubs
      when(this.userService.getEntityById(userId)).thenReturn(userEntity);
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);
      when(this.projectMembersRepository.createMemberParticipation(projectMembersDTO, userEntity, projectEntity)).thenReturn(projectMembersEntity);
      when(this.projectMembersMapper.toProjectMembersResponseDTO(projectMembersEntity)).thenReturn(projectMembersResponseDTO);

      // act and assert
      ProjectMembersResponseDTO result = this.projectMemberService.createMemberParticipation(projectMembersDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectMembersResponseDTO);
      
      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).userIdValidate(userId);
      verify(this.projectMembersValidator, times(1)).roleValidate(role.toString());

      verify(this.userService, times(1)).getEntityById(userId);
      verify(this.projectService, times(1)).getEntityById(projectId);
      verify(this.projectMembersRepository, times(1)).createMemberParticipation(projectMembersDTO, userEntity, projectEntity);
         
      verify(this.activityLogService, times(1)).create(activityLogDTO);      
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.projectMembersMapper, times(1)).toProjectMembersResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("getAllProjectMembers() - must return all members")
   void getAllProjectMembers() {
      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of(projectMembersEntity);

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(), "vini@gmail.com", "vini", "12345" 
      );

      MemberByProjectResponseDTO memberByProjectResponseDTO = new MemberByProjectResponseDTO(
         UUID.randomUUID(), userProfileDTO, RoleEnum.MEMBER, LocalDateTime.now()
      );

      List<MemberByProjectResponseDTO> membersListDTO = List.of(memberByProjectResponseDTO);

      // stubs
      when(this.projectMembersRepository.getAllProjectMembers()).thenReturn(membersListEntity);
      when(this.projectMembersMapper.toMemberByProjectResponseDTO(projectMembersEntity)).thenReturn(memberByProjectResponseDTO);

      // act and assert
      List<MemberByProjectResponseDTO> result = this.projectMemberService.getAllProjectMembers();
      assertThat(result)
         .isNotNull()
         .isEqualTo(membersListDTO);

      // checks
      verify(this.projectMembersRepository, times(1)).getAllProjectMembers();
      verify(this.projectMembersMapper, times(1)).toMemberByProjectResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("getProjectMemberById() - must return a project member")
   void getProjectMemberById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(), "vini@gmail.com", "vini", "12345" 
      );

      MemberByProjectResponseDTO memberByProjectResponseDTO = new MemberByProjectResponseDTO(
         UUID.randomUUID(), userProfileDTO, RoleEnum.MEMBER, LocalDateTime.now()
      );

      // stubs      
      when(this.projectMembersRepository.getProjectMemberById(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersMapper.toMemberByProjectResponseDTO(projectMembersEntity)).thenReturn(memberByProjectResponseDTO);

      // act and assert
      MemberByProjectResponseDTO result = this.projectMemberService.getProjectMemberById(relationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(memberByProjectResponseDTO);

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).getProjectMemberById(relationId);
      verify(this.projectMembersMapper, times(1)).toMemberByProjectResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("getAllMembersByProjectId() - must return all members by project")
   void getAllMembersByProjectId() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of(projectMembersEntity);

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(), "vini@gmail.com", "vini", "12345" 
      );

      MemberByProjectResponseDTO memberByProjectResponseDTO = new MemberByProjectResponseDTO(
         UUID.randomUUID(), userProfileDTO, RoleEnum.MEMBER, LocalDateTime.now()
      );

      List<MemberByProjectResponseDTO> membersListDTO = List.of(memberByProjectResponseDTO);

      // stubs
      when(this.projectMembersRepository.getAllMembersByProjectId(projectId)).thenReturn(membersListEntity);
      when(this.projectMembersMapper.toMemberByProjectResponseDTO(projectMembersEntity)).thenReturn(memberByProjectResponseDTO);

      // act and assert
      List<MemberByProjectResponseDTO> result = this.projectMemberService.getAllMembersByProjectId(projectId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(membersListDTO);

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).getAllMembersByProjectId(projectId);
      verify(this.projectMembersMapper, times(1)).toMemberByProjectResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("getAllMembersByRole() - must return all members by role")
   void getAllMembersByRole() {
      // data - setup
      UUID projectId = UUID.randomUUID();
      RoleEnum role = RoleEnum.MEMBER;

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // list - setup
      List<ProjectMembersEntity> membersListEntity = List.of(projectMembersEntity);

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(), "vini@gmail.com", "vini", "12345" 
      );

      MemberByProjectResponseDTO memberByProjectResponseDTO = new MemberByProjectResponseDTO(
         UUID.randomUUID(), userProfileDTO, RoleEnum.MEMBER, LocalDateTime.now()
      );

      List<MemberByProjectResponseDTO> membersListDTO = List.of(memberByProjectResponseDTO);

      // stubs
      when(this.projectMembersRepository.getAllMembersByRole(projectId, role)).thenReturn(membersListEntity);
      when(this.projectMembersMapper.toMemberByProjectResponseDTO(projectMembersEntity)).thenReturn(memberByProjectResponseDTO);

      // act and assert
      List<MemberByProjectResponseDTO> result = this.projectMemberService.getAllMembersByRole(projectId, role.toString());
      assertThat(result)
         .isNotNull()
         .isEqualTo(membersListDTO);

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).getAllMembersByRole(projectId, role);
      verify(this.projectMembersMapper, times(1)).toMemberByProjectResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("getEntityById() - must return a project member entity")
   void getEntityById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // stubs      
      when(this.projectMembersRepository.getEntityById(relationId)).thenReturn(projectMembersEntity);

      // act and assert
      ProjectMembersEntity result = this.projectMemberService.getEntityById(relationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectMembersEntity);

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
   }


   @Test
   @DisplayName("existsById() - must check if member exists by id")
   void existsById() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.existsById(relationId)).thenReturn(true);

      // act and assert
      boolean result = this.projectMemberService.existsById(relationId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).existsById(relationId);
   }


   @Test
   @DisplayName("existsByProjectIdAndMemberId() - must check if member exists by project and member id")
   void existsByProjectIdAndMemberId() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.existsByProjectIdAndMemberId(projectId, memberId)).thenReturn(true);

      // act and assert
      boolean result = this.projectMemberService.existsByProjectIdAndMemberId(projectId, memberId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.projectMembersValidator, times(1)).userIdValidate(memberId);
      verify(this.projectMembersRepository, times(1)).existsByProjectIdAndMemberId(projectId, memberId);
   }


   @Test
   @DisplayName("checkUserMembership() - must check if user is an project membership")
   void checkUserMembership() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.checkUserMembership(userId, projectId)).thenReturn(true);

      // act and assert
      boolean result = this.projectMemberService.checkUserMembership(userId, projectId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectMembersValidator, times(1)).userIdValidate(userId);
      verify(this.projectMembersValidator, times(1)).projectIdValidate(projectId);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectMembersRepository, times(1)).checkUserMembership(userId, projectId);
   }


   @Test
   @DisplayName("updateMemberRole() - must update member role")
   void updateMemberRole() {
      // id's - setup
      UUID relationId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      
      // role - setup
      RoleEnum role = RoleEnum.MEMBER;

      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(userId)
         .build();

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .project(projectEntity)
         .user(userEntity)
         .role(role)
         .build();

      // set members
      projectEntity.setMembers(List.of(projectMembersEntity));

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectMembersEntity.getProjectId())
         .userId(userEntity.getId())
         .action(ActivityActionEnum.PAPEL_DO_MEMBRO_DO_PROJETO_ATUALIZADA)
         .description(
            "Papel do usuário: " + projectMembersEntity.getUser().getName() + 
            ", do projeto: " + projectMembersEntity.getProject().getName() + 
            ", atualizado para: " + projectMembersEntity.getRole()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.PAPEL_DO_MEMBRO_DO_PROJETO_ATUALIZADA)
         .message(
            "Papel do usuário: " + projectMembersEntity.getUser().getName() + 
            ", foi atualizado para: " + projectMembersEntity.getRole() + 
            ", no projeto: " + projectMembersEntity.getProject().getName()
         )
         .build();
      
      ProjectMembersResponseDTO projectMembersResponseDTO = new ProjectMembersResponseDTO(
         projectId, userId, role, LocalDateTime.now()
      );

      // stubs
      when(this.projectMembersRepository.getEntityById(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersRepository.updateMemberRole(relationId, role)).thenReturn(projectMembersEntity);
      when(this.projectMembersMapper.toProjectMembersResponseDTO(projectMembersEntity)).thenReturn(projectMembersResponseDTO);

      // act and assert
      ProjectMembersResponseDTO result = this.projectMemberService.updateMemberRole(relationId, role.toString());
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectMembersResponseDTO);
      
      // checks
      verify(this.projectMembersValidator, times(2)).idValidate(relationId);
      verify(this.projectMembersValidator, times(1)).roleValidate(role.toString());
      
      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
      verify(this.projectMembersRepository, times(1)).updateMemberRole(relationId, role);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);      
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.projectMembersMapper, times(1)).toProjectMembersResponseDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("removeParticipation() - must remove a member participation")
   void removeParticipation() {
      // id's - setup
      UUID relationId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      
      // role - setup
      RoleEnum role = RoleEnum.MEMBER;

      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(userId)
         .build();

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();

      // project member - setup
      ProjectMembersEntity projectMembersEntity = new ProjectMembersEntity.Builder()
         .project(projectEntity)
         .user(userEntity)
         .role(role)
         .build();

      // set members
      projectEntity.setMembers(List.of(projectMembersEntity));

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectMembersEntity.getProjectId())
         .userId(userEntity.getId())
         .action(ActivityActionEnum.MEMBRO_REMOVIDO_DO_PROJETO)
         .description(
            "Participação do usuário: " + projectMembersEntity.getUser().getName() + 
            ", removida do projeto: " + projectMembersEntity.getProject().getName()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.MEMBRO_REMOVIDO_DO_PROJETO)
         .message(
            "Participação do usuário: " + projectMembersEntity.getUser().getName() + 
            ", removida da organização: " + projectMembersEntity.getProject().getName()
         )
         .build();
      
      ProjectMembersDeletedDTO projectMembersDeletedDTO = new ProjectMembersDeletedDTO(
         projectId, userId, role, LocalDateTime.now(), LocalDateTime.now()
      );

      // stubs
      when(this.projectMembersRepository.getEntityById(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersRepository.removeParticipation(relationId)).thenReturn(projectMembersEntity);
      when(this.projectMembersMapper.toProjectMembersDeletedDTO(projectMembersEntity)).thenReturn(projectMembersDeletedDTO);

      // act and assert
      ProjectMembersDeletedDTO result = this.projectMemberService.removeParticipation(relationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectMembersDeletedDTO);
      
      // checks
      verify(this.projectMembersValidator, times(2)).idValidate(relationId);    

      verify(this.projectMembersRepository, times(1)).getEntityById(relationId);
      verify(this.projectMembersRepository, times(1)).removeParticipation(relationId);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);      
      verify(this.notificationService, times(1)).create(notificationDTO);
      verify(this.projectMembersMapper, times(1)).toProjectMembersDeletedDTO(projectMembersEntity);
   }


   @Test
   @DisplayName("isRemoved() - must check if member is removed")
   void isRemoved() {
      // id - setup
      UUID relationId = UUID.randomUUID();

      // stubs      
      when(this.projectMembersRepository.isRemoved(relationId)).thenReturn(true);

      // act and assert
      boolean result = this.projectMemberService.isRemoved(relationId);
      assertThat(result).isTrue();

      // checks
      verify(this.projectMembersValidator, times(1)).idValidate(relationId);
      verify(this.projectMembersRepository, times(1)).isRemoved(relationId);
   }

}