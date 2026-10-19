
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
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationMembersValidator;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.notification.service.NotificationService;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.MemberByOrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.notification.dto.NotificationDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;
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
      organizationEntity.setMembers(List.of(memberEntity));

      // dto - setup
      OrganizationMembersDTO organizationMembersDTO = new OrganizationMembersDTO(
         organizationEntity.getId(), userEntity.getId(), memberEntity.getRole().toString()
      );

      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
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

      // mocks config
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
      
      verify(this.activityLogService, times(1)).create(activityLog);
      verify(this.notificationService, times(1)).create(notificationDTO);

      verify(this.organizationMembersMapper, times(1)).toOrganizationMembersResponseDTO(memberEntity);
   }


   @Test
   @DisplayName("getAllOrganizationMembers() - must get all members")
   void getAllOrganizationMembers() {
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
      MemberByOrganizationResponseDTO dto1 = new MemberByOrganizationResponseDTO(
         memberEntity1.getId(), userProfileDTO, memberEntity1.getRole(), LocalDateTime.now()
      );

      // organization member 2 - setup
      OrganizationMembersEntity memberEntity2 = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();
      MemberByOrganizationResponseDTO dto2 = new MemberByOrganizationResponseDTO(
         memberEntity2.getId(), userProfileDTO, memberEntity2.getRole(), LocalDateTime.now()
      );

      // list - setup
      List<OrganizationMembersEntity> membersEntities = List.of(memberEntity1, memberEntity2);

      // mocks config
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
   @DisplayName("getOrganizationMemberById() - must get member by relation")
   void getOrganizationMemberById() {
      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .role(RoleEnum.MEMBER)
         .build();

      // dto - setup
      UserProfileDTO userProfileDTO = new UserProfileDTO(
         UUID.randomUUID(),
         "vini@gmail.com",
         "vini",
         "123456789"
      );

      MemberByOrganizationResponseDTO memberByOrganizationResponseDTO = new MemberByOrganizationResponseDTO(
         memberEntity.getId(), userProfileDTO, memberEntity.getRole(), LocalDateTime.now()
      );

      // mocks config
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

}