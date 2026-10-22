
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationMemberService;

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
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMembersMapper;

// jakarta imports
import jakarta.persistence.NoResultException;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - OrganizationMemberService")
public class OrganizationMemberBadTests {
 
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
   @DisplayName("createMemberParticipation() - must throw that user does not exist")
   void createMemberParticipation() {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      RoleEnum role = RoleEnum.MEMBER;

      // message error - setup
      String errorMessage = "Recurso não encontrado: Usuário não existe";

      // DTO - setup
      OrganizationMembersDTO organizationMembersDTO = new OrganizationMembersDTO(
         organizationId, userId, role.toString()
      );      
      
      // stubs
      when(this.userService.getEntityById(userId))
         .thenThrow(MultiExceptions.notFound(errorMessage));

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.createMemberParticipation(organizationMembersDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
      
      // checks
      verify(this.organizationMembersValidator, times(1)).userIdValidate(userId);
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(organizationId);
      verify(this.organizationMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.userService, times(1)).getEntityById(userId);

      // no checks
      verifyNoInteractions(this.organizationService);
      verifyNoInteractions(this.organizationMembersRepository);
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getAllOrganizationMembers() - must throw when members list is empty")
   void getAllOrganizationMembers() {
      // list - setup
      List<OrganizationMembersEntity> members = List.of();

      // stubs
      when(this.organizationMembersRepository.getAllOrganizationMembers()).thenReturn(members);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getAllOrganizationMembers())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");
      
      // checks
      verify(this.organizationMembersRepository, times(1)).getAllOrganizationMembers();

      // no checks
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getOrganizationMemberById() - must throw when organization member not found")
   void getOrganizationMemberById() {      
      // id - setup
      UUID organizationMemberId = UUID.randomUUID(); 

      // stubs
      when(this.organizationMembersRepository.getOrganizationMemberById(organizationMemberId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getOrganizationMemberById(organizationMemberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");
      
      // checks
      verify(this.organizationMembersRepository, times(1)).getOrganizationMemberById(organizationMemberId);

      // no checks
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getAllMembersByOrganizationId() - must throw when members list is empty")
   void getAllMembersByOrganizationId() {
      // id - setup
      UUID organizationId = UUID.randomUUID(); 

      // list - setup
      List<OrganizationMembersEntity> members = List.of();

      // stubs
      when(this.organizationMembersRepository.getAllMembersByOrganizationId(organizationId)).thenReturn(members);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getAllMembersByOrganizationId(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");
      
      // checks
      verify(this.organizationMembersRepository, times(1)).getAllMembersByOrganizationId(organizationId);

      // no checks
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getMemberByOrganizationId() - must throw when member not found")
   void getMemberByOrganizationId() {      
      // id - setup
      UUID userId = UUID.randomUUID(); 
      UUID organizationId = UUID.randomUUID(); 

      // stubs
      when(this.organizationMembersRepository.getMemberByOrganizationId(userId, organizationId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getMemberByOrganizationId(userId, organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");
      
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).getMemberByOrganizationId(userId, organizationId);

      // no checks
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getAllMembersByRole() - must throw when members list is empty")
   void getAllMembersByRole() {
      // id - setup
      UUID organizationId = UUID.randomUUID(); 
      RoleEnum role = RoleEnum.MEMBER;

      // list - setup
      List<OrganizationMembersEntity> members = List.of();

      // stubs
      when(this.organizationMembersRepository.getAllMembersByRole(organizationId, role)).thenReturn(members);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getAllMembersByRole(organizationId, role.toString()))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membros não existem");
      
      // checks
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersValidator, times(1)).roleValidate(role.toString());
      verify(this.organizationMembersRepository, times(1)).getAllMembersByRole(organizationId, role);

      // no checks
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("getEntityById() - must throw when entity not found")
   void getEntityById() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID(); 

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationMemberId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.getEntityById(organizationMemberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");
      
      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationMemberId);
   }


   @Test
   @DisplayName("existsById() - must throw when member does not exist")
   void existsById() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID(); 

      // stubs
      when(this.organizationMembersRepository.existsById(organizationMemberId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.existsById(organizationMemberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");
      
      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).existsById(organizationMemberId);
   }


   @Test
   @DisplayName("existsByOrganizationIdAndMemberId() - must throw when member does not exist")
   void existsByOrganizationIdAndMemberId() {
      // id - setup
      UUID memberId = UUID.randomUUID(); 
      UUID organizationId = UUID.randomUUID();  

      // stubs
      when(this.organizationMembersRepository.existsByOrganizationIdAndMemberId(organizationId, memberId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.existsByOrganizationIdAndMemberId(organizationId, memberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Membro não existe");
      
      // checks
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(organizationId);
      verify(this.organizationMembersValidator, times(1)).userIdValidate(memberId);
      verify(this.organizationMembersRepository, times(1)).existsByOrganizationIdAndMemberId(organizationId, memberId);
   }


   @Test
   @DisplayName("checkUserMembership() - must throw when user is not a organization membership")
   void checkUserMembership() {
      // id - setup
      UUID userId = UUID.randomUUID(); 
      UUID organizationId = UUID.randomUUID();  

      // stubs
      when(this.organizationMembersRepository.checkUserMembership(userId, organizationId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.checkUserMembership(userId, organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não participa da organização");
      
      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationMembersRepository, times(1)).checkUserMembership(userId, organizationId);
   }


   @Test
   @DisplayName("updateMemberRole() - must throw when the last admin changes the role (should exist at least 1 admin)")
   void updateMemberRole() {
      // data - setup
      UUID organizationMemberId = UUID.randomUUID();
      RoleEnum currentRole = RoleEnum.ADMIN;
      RoleEnum roleToUpdate = RoleEnum.MEMBER;

      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .organization(organizationEntity)
         .role(currentRole)
         .build();

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationMemberId)).thenReturn(memberEntity);
      when(this.organizationMembersRepository.countAdminsByOrganization(memberEntity.getOrganization().getId())).thenReturn(1L);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.updateMemberRole(organizationMemberId, roleToUpdate.toString()))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Uma organização deve ter pelo menos 1 administrador");
      
      // checks
      verify(this.organizationMembersValidator, times(2)).idValidate(organizationMemberId);
      verify(this.organizationMembersValidator, times(1)).roleValidate(roleToUpdate.toString());
      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationMemberId);
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(memberEntity.getOrganization().getId());
      verify(this.organizationMembersRepository, times(1)).countAdminsByOrganization(memberEntity.getOrganization().getId());

      // no checks
      verify(this.organizationMembersRepository, never()).updateMemberRole(any(UUID.class), any(RoleEnum.class));
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("removeParticipation() - must throw when rest the last admin (should exist at least 1 admin)")
   void removeParticipation() {
      // data - setup
      UUID organizationMemberId = UUID.randomUUID();
      RoleEnum currentRole = RoleEnum.ADMIN;

      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // organization member - setup
      OrganizationMembersEntity memberEntity = new OrganizationMembersEntity.Builder()
         .id(UUID.randomUUID())
         .organization(organizationEntity)
         .role(currentRole)
         .build();

      // stubs
      when(this.organizationMembersRepository.getEntityById(organizationMemberId)).thenReturn(memberEntity);
      when(this.organizationMembersRepository.countAdminsByOrganization(memberEntity.getOrganization().getId())).thenReturn(1L);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.removeParticipation(organizationMemberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Uma organização deve ter pelo menos 1 administrador");
      
      // checks
      verify(this.organizationMembersValidator, times(2)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).getEntityById(organizationMemberId);
      verify(this.organizationMembersValidator, times(1)).organizationIdValidate(memberEntity.getOrganization().getId());
      verify(this.organizationMembersRepository, times(1)).countAdminsByOrganization(memberEntity.getOrganization().getId());

      // no checks
      verify(this.organizationMembersRepository, never()).removeParticipation(any(UUID.class));
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.notificationService);
      verifyNoInteractions(this.organizationMembersMapper);
   }


   @Test
   @DisplayName("isRemoved() - must throw when member does not exist")
   void isRemoved() {
      // id - setup
      UUID organizationMemberId = UUID.randomUUID();

      // stubs
      when(this.organizationMembersRepository.isRemoved(organizationMemberId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.organizationMemberService.isRemoved(organizationMemberId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Participação não existe");

      // checks
      verify(this.organizationMembersValidator, times(1)).idValidate(organizationMemberId);
      verify(this.organizationMembersRepository, times(1)).isRemoved(organizationMemberId);
   }

}