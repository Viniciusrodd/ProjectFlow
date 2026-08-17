
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationMembersValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersCompleteResponseDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class OrganizationMemberService {

   // properties
   private final OrganizationMembersRepository organizationMembersRepository;
   private final OrganizationMembersValidator organizationMembersValidator;
   private final UserService userService;
   private final OrganizationService organizationService;
   
   // constructor - dependency injection
   public OrganizationMemberService(
      OrganizationMembersRepository organizationMembersRepository,
      OrganizationMembersValidator organizationMembersValidator,
      UserService userService,
      @Lazy OrganizationService organizationService // intentional cycle of circular dependency - (OrganizationService) needs to create "members", (OrganizationMemberService) needs to get "organizations".
   ) {
      this.organizationMembersRepository = organizationMembersRepository;
      this.organizationMembersValidator = organizationMembersValidator;
      this.userService = userService;
      this.organizationService = organizationService;
   }


   // create member participation
   @Transactional
   public OrganizationMembersResponseDTO createMemberParticipation(OrganizationMembersDTO data) {
      this.organizationMembersValidator.userIdValidate(data.userId());
      this.organizationMembersValidator.organizationIdValidate(data.organizationId());
      this.organizationMembersValidator.roleValidate(data.role());

      // get user data
      UserEntity user = this.userService.getEntityById(data.userId());
      
      // get organization data
      OrganizationEntity organization = this.organizationService.getEntityById(data.organizationId());

      return this.organizationMembersRepository.createMemberParticipation(data, user, organization);
   }


   // get all organization members
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByOrganizationId(UUID organizationId) {
      this.organizationMembersValidator.organizationIdValidate(organizationId);
      
      // organization existence - check
      this.organizationService.existsById(organizationId);

      List<OrganizationMembersCompleteResponseDTO> members = this.organizationMembersRepository.getAllMembersByOrganizationId(organizationId);

      if(members.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return members;
   }


   // get all members by role
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByRole(
      UUID organizationId,
      String role
   ) {
      this.organizationMembersValidator.organizationIdValidate(organizationId);
      this.organizationMembersValidator.roleValidate(role);
      
      // organization existence - check
      this.organizationService.existsById(organizationId);

      List<OrganizationMembersCompleteResponseDTO> members = this.organizationMembersRepository.getAllMembersByRole(organizationId, RoleEnum.valueOf(role.toUpperCase()));

      if(members.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Membros não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return members;
   }


   // get entity by id
   public OrganizationMembersEntity getEntityById(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         return this.organizationMembersRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participante não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID organizationId) {
      this.organizationMembersValidator.userIdValidate(userId);
      this.organizationMembersValidator.organizationIdValidate(organizationId);
      
      // user existence - check
      this.userService.existsById(userId);
      
      // organization existence - check
      this.organizationService.existsById(organizationId);

      boolean exist = this.organizationMembersRepository.checkUserMembership(userId, organizationId);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não participa da organização",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // count admins members by organization
   public Long countAdminsByOrganization(UUID organizationId) {
      this.organizationMembersValidator.organizationIdValidate(organizationId);

      return this.organizationMembersRepository.countAdminsByOrganization(organizationId);
   }


   // last admin member by organization - validate
   public void validateLastAdmin(UUID organizationId) {
      long admins = this.countAdminsByOrganization(organizationId);

      if(admins <= 1) {
         throw MultiExceptions.unauthorized(String.format(
            "%s: Uma organização deve ter pelo menos 1 administrador",
            ResponseMessages.UNAUTHORIZED
         ));
      }
   }


   // update member role
   @Transactional
   public OrganizationMembersResponseDTO updateMemberRole(UUID id, String role) {
      this.organizationMembersValidator.idValidate(id);
      this.organizationMembersValidator.roleValidate(role); // not allow update to "owner"

      try {
         // last admin - check
         OrganizationMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN && !role.equalsIgnoreCase("ADMIN")) {
            this.validateLastAdmin(member.getOrganization().getId());
         }

         return this.organizationMembersRepository.updateMemberRole(id, RoleEnum.valueOf(role.toUpperCase()));
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete member participation
   @Transactional
   public OrganizationMembersDeletedDTO delete(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         // last admin - check
         OrganizationMembersEntity member = this.getEntityById(id);
         if(member.getRole() == RoleEnum.ADMIN) {
            this.validateLastAdmin(member.getOrganization().getId());
         }

         return this.organizationMembersRepository.delete(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.organizationMembersValidator.idValidate(id);

      try {
         return this.organizationMembersRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Participação não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}