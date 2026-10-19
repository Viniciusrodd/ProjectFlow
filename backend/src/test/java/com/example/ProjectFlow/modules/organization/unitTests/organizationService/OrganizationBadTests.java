
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationService;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import repositories
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;

// import entities
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMapper;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;

import jakarta.persistence.NoResultException;

import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - OrganizationService")
public class OrganizationBadTests {
 
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
   @DisplayName("getAll() - must throw when organization list is empty")
   void getAll() {
      // list - setup
      List<OrganizationEntity> organizationsList = List.of();

      // mock config
      when(this.organizationRepository.getAll()).thenReturn(organizationsList);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.getAll())
         .isInstanceOf(RuntimeException.class) // MultiExceptions -> BaseException -> RuntimeException
         .hasMessageContaining("Recurso não encontrado: Organizações não existem");

      // checks
      verify(this.organizationRepository, times(1)).getAll();

      // no checks
      verifyNoInteractions(this.organizationMapper);
   }


   @Test
   @DisplayName("getById() - must throw organization not found")
   void getById() {
      // data
      UUID organizationId = UUID.randomUUID();

      // mocks config
      when(this.organizationRepository.getById(organizationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.getById(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Organização não existe");

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationId);
      verify(this.organizationRepository, times(1)).getById(organizationId);

      // no checks
      verifyNoInteractions(this.organizationMapper);
   }


   @Test
   @DisplayName("getByOwnerId() - must throw owner organizations not found")
   void getByOwnerId() {
      // data
      UUID ownerId = UUID.randomUUID();
      List<OrganizationEntity> organizationsList = List.of();

      // mocks config
      when(this.organizationRepository.getByOwnerId(ownerId)).thenReturn(organizationsList);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.getByOwnerId(ownerId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Organizações não existem");

      // checks
      verify(this.organizationValidator, times(1)).ownerIdValidate(ownerId);
      verify(this.organizationRepository, times(1)).getByOwnerId(ownerId);

      // no checks
      verifyNoInteractions(this.organizationMapper);
   }


   @Test
   @DisplayName("getEntityById() - must throw organization not found")
   void getEntityById() {
      // data
      UUID organizationId = UUID.randomUUID();

      // mocks config
      when(this.organizationRepository.getEntityById(organizationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.getEntityById(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Organização não existe");

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationId);
      verify(this.organizationRepository, times(1)).getEntityById(organizationId);
   }


   @Test
   @DisplayName("existsById() - must throw when organization does not exist")
   void existsById() {
      // data
      UUID organizationId = UUID.randomUUID();

      // mocks config
      when(this.organizationRepository.existsById(organizationId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.existsById(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Organização não existe");

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationId);
      verify(this.organizationRepository, times(1)).existsById(organizationId);
   }


   @Test
   @DisplayName("update() - must throw when user is not a admin")
   void update() {
      // data
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .role(RoleEnum.OWNER)
         .build();

      // DTO - setup
      OrganizationUpdateDTO organizationUpdateDTO = new OrganizationUpdateDTO("new name", null);

      // mocks config
      when(this.organizationRepository.existsById(organizationId)).thenReturn(true);
      when(this.organizationMembersRepository.getMemberByOrganizationId(userId, organizationId)).thenReturn(memberEntity);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.update(organizationId, userId, organizationUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Membro não é um administrador");
      
      // checks
      verify(this.organizationValidator, times(2)).idValidate(organizationId);
      verify(this.organizationValidator, times(1)).updateValidations(organizationUpdateDTO);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationRepository, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userId, organizationId);
   }


   @Test
   @DisplayName("delete() - must throw when user is not a admin")
   void delete() {
      // data
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .role(RoleEnum.OWNER)
         .build();

      // mocks config
      when(this.organizationRepository.existsById(organizationId)).thenReturn(true);
      when(this.organizationMembersRepository.getMemberByOrganizationId(userId, organizationId)).thenReturn(memberEntity);

      // act and assert
      assertThatThrownBy(() -> this.organizationService.delete(organizationId, userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Membro não é um administrador");
      
      // checks
      verify(this.organizationValidator, times(2)).idValidate(organizationId);
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationRepository, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userId, organizationId);
   }


   @Test
   @DisplayName("isDeleted() - must throw when organization does not exist")
   void isDeleted() {
      // data
      UUID organizationId = UUID.randomUUID();

      // mock config
      when(this.organizationRepository.isDeleted(organizationId)).thenThrow(NoResultException.class);
      
      // act and assert
      assertThatThrownBy(() -> this.organizationService.isDeleted(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Organização não existe");

      // checks
      verify(this.organizationValidator, times(1)).idValidate(organizationId);
      verify(this.organizationRepository, times(1)).isDeleted(organizationId);
   }

}