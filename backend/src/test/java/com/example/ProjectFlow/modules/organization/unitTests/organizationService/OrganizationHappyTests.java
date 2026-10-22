
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationService;

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

// import repositories
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMapper;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - OrganizationService")
public class OrganizationHappyTests {
 
   // mock dependencies
   @Mock
   private OrganizationRepository organizationRepository;
   @Mock
   private OrganizationMembersRepository organizationMembersRepository;
   @Mock
   private OrganizationValidator organizationValidator;
   @Mock
   private UserService userService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private OrganizationMapper organizationMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private OrganizationService organizationService;


   @Test
   @DisplayName("getAll() - must return all organizations")
   void getAll() {
      // user - setup
      UserEntity userEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();

      // organization 1 - setup
      OrganizationEntity organization1 = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization 1")
         .description("organization test 1")
         .build();

      // organization 2 - setup
      OrganizationEntity organization2 = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(userEntity)
         .name("organization 2")
         .description("organization test 2")
         .build();
         
      // DTO's - setup
      OrganizationResponseDTO dto1 = new OrganizationResponseDTO(
         organization1.getId(), organization1.getOwnerId(), organization1.getName(), organization1.getDescription()
      );
      
      OrganizationResponseDTO dto2 = new OrganizationResponseDTO(
         organization2.getId(), organization2.getOwnerId(), organization2.getName(), organization2.getDescription()
      );
         
      // list - setup
      List<OrganizationEntity> organizationsList = List.of(organization1, organization2);

      // stubs
      when(this.organizationRepository.getAll()).thenReturn(organizationsList);
      when(this.organizationMapper.toOrganizationResponseDTO(organization1)).thenReturn(dto1);
      when(this.organizationMapper.toOrganizationResponseDTO(organization2)).thenReturn(dto2);

      // act and assert
      List<OrganizationResponseDTO> result = this.organizationService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);

      // checks
      verify(this.organizationRepository, times(1)).getAll();
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organization1);
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organization2);
   }


   @Test
   @DisplayName("getById() - must return organization by id")
   void getById() {
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
         .name("organization")
         .description("organization test")
         .build();
      
      // DTO - setup
      OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO(
         organizationEntity.getId(), organizationEntity.getOwnerId(), organizationEntity.getName(), organizationEntity.getDescription()
      );

      // stubs
      when(this.organizationRepository.getById(organizationEntity.getId())).thenReturn(organizationEntity);
      when(this.organizationMapper.toOrganizationResponseDTO(organizationEntity)).thenReturn(organizationResponseDTO);

      // act and assert
      OrganizationResponseDTO result = this.organizationService.getById(organizationEntity.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationResponseDTO);

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationEntity.getId());
      verify(this.organizationRepository, times(1)).getById(organizationEntity.getId());
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organizationEntity);
   }


   @Test
   @DisplayName("getByOwnerId() - must return all owner organizations by owner id")
   void getByOwnerId() {
      // user - setup
      UserEntity ownerEntity = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();

      // organization 1 - setup
      OrganizationEntity organization1 = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(ownerEntity)
         .name("organization 1")
         .description("organization test 1")
         .build();

      // organization 2 - setup
      OrganizationEntity organization2 = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .owner(ownerEntity)
         .name("organization 2")
         .description("organization test 2")
         .build();

      // DTO's - setup
      OrganizationResponseDTO dto1 = new OrganizationResponseDTO(
         organization1.getId(), organization1.getOwnerId(), organization1.getName(), organization1.getDescription()
      );
      
      OrganizationResponseDTO dto2 = new OrganizationResponseDTO(
         organization2.getId(), organization2.getOwnerId(), organization2.getName(), organization2.getDescription()
      );
         
      // list - setup
      List<OrganizationEntity> organizationsList = List.of(organization1, organization2);

      // stubs
      when(this.organizationRepository.getByOwnerId(ownerEntity.getId())).thenReturn(organizationsList);
      when(this.organizationMapper.toOrganizationResponseDTO(organization1)).thenReturn(dto1);
      when(this.organizationMapper.toOrganizationResponseDTO(organization2)).thenReturn(dto2);

      // act and assert
      List<OrganizationResponseDTO> result = this.organizationService.getByOwnerId(ownerEntity.getId());
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);

      // checks
      verify(this.organizationValidator, times(1)).ownerIdValidate(ownerEntity.getId());
      verify(this.organizationRepository, times(1)).getByOwnerId(ownerEntity.getId());
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organization1);
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organization2);
   }


   @Test
   @DisplayName("getEntityById() - must return organization entity by id")
   void getEntityById() {
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
         .name("organization")
         .description("organization test")
         .build();

      // stubs
      when(this.organizationRepository.getEntityById(organizationEntity.getId())).thenReturn(organizationEntity);

      // act and assert
      OrganizationEntity result = this.organizationService.getEntityById(organizationEntity.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationEntity);

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationEntity.getId());
      verify(this.organizationRepository, times(1)).getEntityById(organizationEntity.getId());
   }


   @Test
   @DisplayName("existsById() - must check if organization exist by id")
   void existsById() {
      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // stubs
      when(this.organizationRepository.existsById(organizationEntity.getId())).thenReturn(true);

      // act and assert
      boolean result = this.organizationService.existsById(organizationEntity.getId());
      assertThat(result).isTrue();

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationEntity.getId());
      verify(this.organizationRepository, times(1)).existsById(organizationEntity.getId());
   }


   @Test
   @DisplayName("update() - must update organization (only the name)")
   void update() {
      // data - setup
      String newName = "new organization name";

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
         .name(newName)
         .description("organization test")
         .build();

      // DTOs - setup
      OrganizationMembersEntity admin = new OrganizationMembersEntity.Builder()
         .user(userEntity)
         .organization(organizationEntity)
         .role(RoleEnum.ADMIN)
         .build();

      OrganizationUpdateDTO organizationUpdateDTO = new OrganizationUpdateDTO(
         newName, null
      );

      OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO(
         organizationEntity.getId(), organizationEntity.getOwnerId(), organizationUpdateDTO.name(), organizationEntity.getDescription()
      );

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .action(ActivityActionEnum.ORGANIZACAO_ATUALIZADA)
         .description("Organização: " + organizationUpdateDTO.name() + " atualizada")
         .build();

      // stubs
      when(this.organizationRepository.existsById(organizationEntity.getId())).thenReturn(true);
      when(this.organizationMembersRepository.getMemberByOrganizationId(userEntity.getId(), organizationEntity.getId())).thenReturn(admin);
      when(this.organizationRepository.update(organizationEntity.getId(), organizationUpdateDTO)).thenReturn(organizationEntity);
      when(this.organizationMapper.toOrganizationResponseDTO(organizationEntity)).thenReturn(organizationResponseDTO);

      // act and assert
      OrganizationResponseDTO result = this.organizationService.update(
         organizationEntity.getId(), userEntity.getId(), organizationUpdateDTO
      );
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationResponseDTO);

      // checks
      verify(this.organizationValidator, times(2)).idValidate(organizationEntity.getId());
      verify(this.organizationValidator, times(1)).updateValidations(organizationUpdateDTO);

      verify(this.userService, times(1)).existsById(userEntity.getId());
      verify(this.organizationRepository, times(1)).existsById(organizationEntity.getId());
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userEntity.getId(), organizationEntity.getId());
      
      verify(this.organizationRepository, times(1)).update(organizationEntity.getId(), organizationUpdateDTO);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.organizationMapper, times(1)).toOrganizationResponseDTO(organizationEntity);
   }


   @Test
   @DisplayName("delete() - must delete an organization")
   void delete() {
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
         .name("organization")
         .description("organization test")
         .build();

      // DTOs - setup
      OrganizationMembersEntity admin = new OrganizationMembersEntity.Builder()
         .user(userEntity)
         .organization(organizationEntity)
         .role(RoleEnum.ADMIN)
         .build();

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .action(ActivityActionEnum.ORGANIZACAO_DELETADA)
         .description("Organização: " + organizationEntity.getName() + " deletada")
         .build();

      OrganizationDeletedDTO organizationDeletedDTO = new OrganizationDeletedDTO(
         organizationEntity.getId(), userEntity.getId(), organizationEntity.getName(), organizationEntity.getDescription(), LocalDateTime.now()
      );
      
      // stubs
      when(this.organizationRepository.existsById(organizationEntity.getId())).thenReturn(true);
      when(this.organizationMembersRepository.getMemberByOrganizationId(userEntity.getId(), organizationEntity.getId())).thenReturn(admin);
      when(this.organizationRepository.delete(organizationEntity.getId())).thenReturn(organizationEntity);
      when(this.organizationMapper.toOrganizationDeletedDTO(organizationEntity)).thenReturn(organizationDeletedDTO);

      // act and assert
      OrganizationDeletedDTO result = this.organizationService.delete(organizationEntity.getId(), userEntity.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationDeletedDTO);

      // checks
      verify(this.organizationValidator, times(2)).idValidate(organizationEntity.getId());
      
      verify(this.userService, times(1)).existsById(userEntity.getId());
      verify(this.organizationRepository, times(1)).existsById(organizationEntity.getId());
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userEntity.getId(), organizationEntity.getId());
      
      verify(this.organizationRepository, times(1)).delete(organizationEntity.getId());
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.organizationMapper, times(1)).toOrganizationDeletedDTO(organizationEntity);
   }


   @Test
   @DisplayName("isDeleted() - must check if organization is deleted")
   void isDeleted() {
      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // stubs
      when(this.organizationRepository.isDeleted(organizationEntity.getId())).thenReturn(true);
      
      // act and assert
      boolean result = this.organizationService.isDeleted(organizationEntity.getId());
      assertThat(result).isTrue();

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationEntity.getId());
      verify(this.organizationRepository, times(1)).isDeleted(organizationEntity.getId());
   }

}