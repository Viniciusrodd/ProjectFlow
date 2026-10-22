
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationMemberService;

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
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationMembersValidator;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMembersMapper;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - OrganizationMemberService")
public class OrganizationMemberHappyTests {
 
   // mock dependencies
   @Mock
   private OrganizationMembersRepository organizationMembersRepository;
   @Mock
   private OrganizationMembersValidator organizationMembersValidator;
   @Mock
   private UserService userService;
   @Mock
   private OrganizationService organizationService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private NotificationService notificationService;
   @Mock 
   private OrganizationMembersMapper organizationMembersMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private OrganizationMemberService organizationMemberService;


   @Test
   @DisplayName("createMemberParticipation() - must create an organization member participation")
   void createMemberParticipation() {
      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("vini")
         .build();
      
      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization x")
         .build();
      
      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .user(userEntity)
         .organization(organizationEntity)
         .role(RoleEnum.MEMBER)
         .build();

      // set members
      organizationEntity.setMembers(List.of(memberEntity));

      // dto - setup
      OrganizationMembersDTO organizationMembersDTO = new OrganizationMembersDTO(
         organizationEntity.getId(), userEntity.getId(), memberEntity.getRole().toString()
      );

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .userId(userEntity.getId())
         .action(ActivityActionEnum.MEMBRO_ADICIONADO_A_ORGANIZACAO)
         .description(
            "Participação do usuário: " + memberEntity.getUser().getName() + 
            ", adicionada á organização: " + memberEntity.getOrganization().getName() + 
            ", como: " + memberEntity.getRole()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.MEMBRO_ADICIONADO_A_ORGANIZACAO)
         .message("O usuário: " + userEntity.getName() + ", foi adicionado á organização: " + organizationEntity.getName())
         .build();

      OrganizationMembersResponseDTO organizationMembersResponseDTO = new OrganizationMembersResponseDTO(
         memberEntity.getId(), organizationEntity.getId(), userEntity.getId(),
         memberEntity.getRole(), LocalDateTime.now()
      );

      // stubs
      when(this.userService.getEntityById(userEntity.getId())).thenReturn(userEntity);
      when(this.organizationService.getEntityById(organizationEntity.getId())).thenReturn(organizationEntity);
      when(this.organizationMembersRepository.createMemberParticipation(
         organizationMembersDTO, userEntity, organizationEntity
      )).thenReturn(memberEntity);
      when(this.organizationMembersMapper.toOrganizationMembersResponseDTO(memberEntity)).thenReturn(organizationMembersResponseDTO);      

      // act and assert
      OrganizationMembersResponseDTO result = this.organizationMemberService.createMemberParticipation(organizationMembersDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationMembersResponseDTO);

      // checks
      verify(this.organizationMembersValidator, times(1)).userIdValidate(userEntity.getId());
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(organizationEntity.getId());
      verify(this.organizationMembersValidator, times(1)).roleValidate(organizationMembersDTO.role());
      
      verify(this.userService, times(1)).getEntityById(userEntity.getId());
      verify(this.organizationService, times(1)).getEntityById(organizationEntity.getId());
      verify(this.organizationMembersRepository, times(1)).createMemberParticipation(organizationMembersDTO, userEntity, organizationEntity);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);

      verify(this.organizationMembersMapper, times(1)).toOrganizationMembersResponseDTO(memberEntity);
   }


   @Test
   @DisplayName("getAllOrganizationMembers() - must return all members")
   void getAllOrganizationMembers() {
      // organization member 1 - setup
      OrganizationMembersEntity memberEntity1 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();
         
      // organization member 2 - setup
      OrganizationMembersEntity memberEntity2 = new OrganizationMembersEntity.Builder()
      .id(UUID.randomUUID())
      .role(RoleEnum.MEMBER)
      .build();
         
      // DTO's - setup   
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(),
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      MemberByOrganizationResponseDTO dto1 = new MemberByOrganizationResponseDTO(
         memberEntity1.getId(), userProfileDTO, memberEntity1.getRole(), LocalDateTime.now()
      );

      MemberByOrganizationResponseDTO dto2 = new MemberByOrganizationResponseDTO(
         memberEntity2.getId(), userProfileDTO, memberEntity2.getRole(), LocalDateTime.now()
      );
         
      // list - setup
      List<OrganizationMembersEntity> membersEntities = List.of(memberEntity1, memberEntity2);

      // stubs
      when(this.organizationMembersRepository.getAllOrganizationMembers()).thenReturn(membersEntities);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity1)).thenReturn(dto1);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity2)).thenReturn(dto2);

      // act and assert
      List<MemberByOrganizationResponseDTO> result = this.organizationMemberService.getAllOrganizationMembers();
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);

      // checks
      verify(this.organizationMembersRepository, times(1)).getAllOrganizationMembers();
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity1);
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity2);
   }


   @Test
   @DisplayName("getOrganizationMemberById() - must return a organization member")
   void getOrganizationMemberById() {
      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();

      // DTO's - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(),
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      MemberByOrganizationResponseDTO memberByOrganizationResponseDTO = new MemberByOrganizationResponseDTO(
         memberEntity.getId(), userProfileDTO, memberEntity.getRole(), LocalDateTime.now()
      );

      // stubs
      when(this.organizationMembersRepository.getOrganizationMemberById(memberEntity.getId())).thenReturn(memberEntity);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity)).thenReturn(memberByOrganizationResponseDTO);

      // act and assert
      MemberByOrganizationResponseDTO result = this.organizationMemberService.getOrganizationMemberById(memberEntity.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(memberByOrganizationResponseDTO);
      
      // checks
      verify(this.organizationMembersRepository, times(1)).getOrganizationMemberById(memberEntity.getId());
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity);
   }


   @Test
   @DisplayName("getAllMembersByOrganizationId() - must return all members by organization")
   void getAllMembersByOrganizationId() {
      // id - setup
      UUID organizationId = UUID.randomUUID();

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(),
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      // organization member 1 - setup
      OrganizationMembersEntity memberEntity1 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();

      // organization member 2 - setup
      OrganizationMembersEntity memberEntity2 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();
         
      // DTO's - setup
      MemberByOrganizationResponseDTO dto1 = new MemberByOrganizationResponseDTO(
         memberEntity1.getId(), userProfileDTO, memberEntity1.getRole(), LocalDateTime.now()
      );

      MemberByOrganizationResponseDTO dto2 = new MemberByOrganizationResponseDTO(
         memberEntity2.getId(), userProfileDTO, memberEntity2.getRole(), LocalDateTime.now()
      );

      // list - setup
      List<OrganizationMembersEntity> membersEntities = List.of(memberEntity1, memberEntity2);

      // stubs
      when(this.organizationMembersRepository.getAllMembersByOrganizationId(organizationId)).thenReturn(membersEntities);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity1)).thenReturn(dto1);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity2)).thenReturn(dto2);

      // act and assert
      List<MemberByOrganizationResponseDTO> result = this.organizationMemberService.getAllMembersByOrganizationId(organizationId);
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);
      
      // checks
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).getAllMembersByOrganizationId(organizationId);
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity1);
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity2);
   }


   @Test
   @DisplayName("getMemberByOrganizationId() - must return an member by organization")
   void getMemberByOrganizationId() {
      // id's - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();

      // DTO's - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         userId,
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      MemberByOrganizationResponseDTO memberByOrganizationResponseDTO = new MemberByOrganizationResponseDTO(
         memberEntity.getId(), userProfileDTO, memberEntity.getRole(), LocalDateTime.now()
      );

      // stubs
      when(this.organizationMembersRepository.getMemberByOrganizationId(userId, organizationId)).thenReturn(memberEntity);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity)).thenReturn(memberByOrganizationResponseDTO);      

      // act and assert
      MemberByOrganizationResponseDTO result = this.organizationMemberService.getMemberByOrganizationId(userId, organizationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(memberByOrganizationResponseDTO);
      
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userId, organizationId);
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity);
   }


   @Test
   @DisplayName("getAllMembersByRole() - must return all members by role")
   void getAllMembersByRole() {
      // data setup
      UUID organizationId = UUID.randomUUID();
      RoleEnum role = RoleEnum.MEMBER;

      // DTO - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(),
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      // organization member 1 - setup
      OrganizationMembersEntity memberEntity1 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();
         
      // organization member 2 - setup
      OrganizationMembersEntity memberEntity2 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();
         
      // DTO's - setup   
      MemberByOrganizationResponseDTO dto1 = new MemberByOrganizationResponseDTO(
         memberEntity1.getId(), userProfileDTO, memberEntity1.getRole(), LocalDateTime.now()
      );
      
      MemberByOrganizationResponseDTO dto2 = new MemberByOrganizationResponseDTO(
         memberEntity2.getId(), userProfileDTO, memberEntity2.getRole(), LocalDateTime.now()
      );

      // list - setup
      List<OrganizationMembersEntity> membersEntities = List.of(memberEntity1, memberEntity2);

      // stubs
      when(this.organizationMembersRepository.getAllMembersByRole(organizationId, role)).thenReturn(membersEntities);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity1)).thenReturn(dto1);
      when(this.organizationMembersMapper.toMemberByOrganizationResponseDTO(memberEntity2)).thenReturn(dto2);

      // act and assert
      List<MemberByOrganizationResponseDTO> result = this.organizationMemberService.getAllMembersByRole(organizationId, role.toString());
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);
      
      // checks
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity1);
      verify(this.organizationMembersMapper, times(1)).toMemberByOrganizationResponseDTO(memberEntity2);
   }


   @Test
   @DisplayName("getEntityById() - must return organization member entity by id")
   void getEntityById() {
      // id - setup
      UUID organizationId = UUID.randomUUID();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationId)).thenReturn(memberEntity);

      // act and assert
      OrganizationMembersEntity result = this.organizationMemberService.getEntityById(organizationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(memberEntity);

      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationId);
      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationId);
   }


   @Test
   @DisplayName("existsById() - must check if member exists by id")
   void existsById() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID();

      // stubs
      when(this.organizationMembersRepository.existsById(organizationMemberId)).thenReturn(true);

      // act and assert
      boolean result = this.organizationMemberService.existsById(organizationMemberId);
      assertThat(result).isTrue();

      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).existsById(organizationMemberId);
   }


   @Test
   @DisplayName("existsByOrganizationIdAndMemberId() - must check if member exists by organization and member id")
   void existsByOrganizationIdAndMemberId() {
      // id - setup
      UUID organizationId = UUID.randomUUID();
      UUID memberId = UUID.randomUUID();

      // stubs
      when(this.organizationMembersRepository.existsByOrganizationIdAndMemberId(organizationId, memberId)).thenReturn(true);

      // act and assert
      boolean result = this.organizationMemberService.existsByOrganizationIdAndMemberId(organizationId, memberId);
      assertThat(result).isTrue();

      // checks
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(organizationId);
      verify(this.organizationMembersValidator, times(1)).userIdValidate(memberId);
      verify(this.organizationMembersRepository, times(1)).existsByOrganizationIdAndMemberId(organizationId, memberId);
   }


   @Test
   @DisplayName("checkUserMembership() - must check if user is an organization membership")
   void checkUserMembership() {
      // id - setup
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // stubs
      when(this.organizationMembersRepository.checkUserMembership(userId, organizationId)).thenReturn(true);

      // act and assert
      boolean result = this.organizationMemberService.checkUserMembership(userId, organizationId);
      assertThat(result).isTrue();

      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).checkUserMembership(userId, organizationId);
   }


   @Test
   @DisplayName("updateMemberRole() - must update member role")
   void updateMemberRole() {
      // data - setup
      UUID organizationMemberId = UUID.randomUUID();
      RoleEnum roleToUpdate = RoleEnum.ADMIN;
      
      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("vini")
         .build();
      
      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization x")
         .build();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .user(userEntity)
         .organization(organizationEntity)
         .role(roleToUpdate)
         .build();

      // set members
      organizationEntity.setMembers(List.of(memberEntity));

      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .userId(userEntity.getId())
         .action(ActivityActionEnum.PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA)
         .description(
            "Papel do usuário: " + memberEntity.getUser().getName() + 
            ", da organização: " + memberEntity.getOrganization().getName() + 
            ", atualizado para: " + memberEntity.getRole()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA)
         .message(
            "Papel do usuário: " + memberEntity.getUser().getName() + 
            ", foi atualizado para: " + memberEntity.getRole() + 
            ", na organização: " + memberEntity.getOrganization().getName()
         )
         .build();

      OrganizationMembersResponseDTO organizationMembersResponseDTO = new OrganizationMembersResponseDTO(
         memberEntity.getId(), organizationEntity.getId(), userEntity.getId(),
         memberEntity.getRole(), LocalDateTime.now()
      );

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationMemberId)).thenReturn(memberEntity);
      when(this.organizationMembersRepository.updateMemberRole(organizationMemberId, roleToUpdate)).thenReturn(memberEntity);
      when(this.organizationMembersMapper.toOrganizationMembersResponseDTO(memberEntity)).thenReturn(organizationMembersResponseDTO);

      // act and assert
      OrganizationMembersResponseDTO result = this.organizationMemberService.updateMemberRole(organizationMemberId, roleToUpdate.toString());
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationMembersResponseDTO);
      
      // checks
      verify(this.organizationMembersValidator, times(2)).idValidate(organizationMemberId);
      verify(this.organizationMembersValidator, times(1)).roleValidate(roleToUpdate.toString());

      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).updateMemberRole(organizationMemberId, roleToUpdate);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);
      
      verify(this.organizationMembersMapper, times(1)).toOrganizationMembersResponseDTO(memberEntity);
   }


   @Test
   @DisplayName("removeParticipation() - must remove a member participation")
   void removeParticipation() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID();

      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("vini")
         .build();
      
      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization x")
         .build();
      
      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .user(userEntity)
         .organization(organizationEntity)
         .role(RoleEnum.MEMBER)
         .build();

      // set members
      organizationEntity.setMembers(List.of(memberEntity));

      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(memberEntity.getOrganizationId())
         .userId(memberEntity.getUserId())
         .action(ActivityActionEnum.MEMBRO_REMOVIDO_DA_ORGANIZACAO)
         .description(
            "Participação do usuário: " + memberEntity.getUser().getName() + 
            ", removida da organização: " + memberEntity.getOrganization().getName()
         )
         .build();

      NotificationDTO notificationDTO = new NotificationDTO.Builder()
         .userId(userEntity.getId().toString())
         .title(NotificationTitleEnum.MEMBRO_REMOVIDO_DA_ORGANIZACAO)
         .message(
            "Usuário: " + memberEntity.getUser().getName() +  
            ", foi removido da organização: " + memberEntity.getOrganization().getName()
         )
         .build();

      OrganizationMembersDeletedDTO organizationMembersDeletedDTO = new OrganizationMembersDeletedDTO(
         organizationEntity.getId(), userEntity.getId(),
         memberEntity.getRole(), LocalDateTime.now(), LocalDateTime.now()
      );

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationMemberId)).thenReturn(memberEntity);
      when(this.organizationMembersRepository.removeParticipation(organizationMemberId)).thenReturn(memberEntity);
      when(this.organizationMembersMapper.toOrganizationMembersDeletedDTO(memberEntity)).thenReturn(organizationMembersDeletedDTO);

      // act and assert
      OrganizationMembersDeletedDTO result = this.organizationMemberService.removeParticipation(organizationMemberId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationMembersDeletedDTO);

      // checks
      verify(this.organizationMembersValidator, times(2)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationMemberId);

      verify(this.organizationMembersRepository, times(1)).removeParticipation(organizationMemberId);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.notificationService, times(1)).create(notificationDTO);

      verify(this.organizationMembersMapper, times(1)).toOrganizationMembersDeletedDTO(memberEntity);
   }


   @Test
   @DisplayName("isRemoved() - must check if member is removed")
   void isRemoved() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID();

      // stubs
      when(this.organizationMembersRepository.isRemoved(organizationMemberId)).thenReturn(true);

      // act and assert
      boolean result = this.organizationMemberService.isRemoved(organizationMemberId);
      assertThat(result).isTrue();
      
      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).isRemoved(organizationMemberId);
   }

}