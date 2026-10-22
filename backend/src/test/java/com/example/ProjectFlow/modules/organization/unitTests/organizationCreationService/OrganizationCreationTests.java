
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationCreationService;

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

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMapper;

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.organization.service.OrganizationCreationService;
import com.example.ProjectFlow.modules.organization.service.OrganizationMemberService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - OrganizationCreationService")
public class OrganizationCreationTests {
 
   // mock dependencies
   @Mock
   private OrganizationService organizationService;
   @Mock
   private OrganizationMemberService organizationMemberService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private OrganizationMapper organizationMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private OrganizationCreationService organizationCreationService;


   @Test
   @DisplayName("create() - should creates an organization")
   void create() {
      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();

      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization x")
         .description("organization test")
         .build();

      // dto - setup
      OrganizationDTO organizationDTO = new OrganizationDTO(
         organizationEntity.getOwnerId(),
         organizationEntity.getName(),
         organizationEntity.getDescription()
      );
      OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO(
         organizationEntity.getId(),
         organizationEntity.getOwnerId(),
         organizationEntity.getName(),
         organizationEntity.getDescription()
      );
      OrganizationMembersDTO organizationMembersDTO = new OrganizationMembersDTO(
         organizationEntity.getId(),
         organizationEntity.getOwnerId(),
         RoleEnum.OWNER.toString()
      );
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .userId(organizationEntity.getOwnerId())
         .action(ActivityActionEnum.ORGANIZACAO_CRIADA)
         .description("Organização: " + organizationEntity.getName() + ", criada por: " + userEntity.getName())
         .build();

      // stubs
      when(this.organizationService.create(organizationDTO)).thenReturn(organizationEntity);
      when(this.organizationMapper.toOrganizationResponseDTO(organizationEntity)).thenReturn(organizationResponseDTO);

      // act and assert
      OrganizationResponseDTO result = this.organizationCreationService.create(organizationDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationResponseDTO);

      // checks
      verify(this.organizationService, times(1)).create(organizationDTO);
      verify(this.organizationMemberService, times(1)).createMemberParticipation(organizationMembersDTO);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organizationEntity);
   }

}