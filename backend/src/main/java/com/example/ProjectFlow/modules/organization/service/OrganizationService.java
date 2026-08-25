
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@Service
public class OrganizationService {
 
   // properties
   private final OrganizationRepository organizationRepository;
   private final OrganizationValidator organizationValidator;
   private final UserService userService;
   private final OrganizationMemberService organizationMemberService;

   // constructor - dependency injection
   public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationValidator organizationValidator,
      UserService userService,
      OrganizationMemberService organizationMemberService
   ) {
      this.organizationRepository = organizationRepository;
      this.organizationValidator = organizationValidator;
      this.userService = userService; 
      this.organizationMemberService = organizationMemberService;
   }


   // create organization
   @Transactional
   public OrganizationResponseDTO create(OrganizationDTO data) {
      this.organizationValidator.idValidate(data.ownerId());
      this.organizationValidator.nameValidate(data.name());
      if(!data.description().trim().isEmpty()) this.organizationValidator.descriptionValidate(data.description());
   
      // get owner data
      UserEntity owner = this.userService.getEntityById(data.ownerId());

      // organization creation
      OrganizationResponseDTO organizationCreated = this.organizationRepository.create(data, owner);

      // set organization member - owner
      OrganizationMembersDTO organizationMembersData = new OrganizationMembersDTO(
         organizationCreated.id(),
         owner.getId(),
         RoleEnum.OWNER.toString()
      );
      this.organizationMemberService.createMemberParticipation(organizationMembersData);

      return organizationCreated;
   }


   // get all
   public List<OrganizationResponseDTO> getAll() {
      List<OrganizationResponseDTO> organizations = this.organizationRepository.getAll();

      if(organizations.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organizações não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return organizations;
   }


   // get by id
   public OrganizationResponseDTO getById(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         return this.organizationRepository.getById(id);
      } 
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public OrganizationEntity getEntityById(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         return this.organizationRepository.getEntityById(id);
      } 
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get by owner id
   public List<OrganizationResponseDTO> getByOwnerId(UUID ownerId) {
      this.organizationValidator.ownerIdValidate(ownerId);

      List<OrganizationResponseDTO> organizations = this.organizationRepository.getByOwnerId(ownerId);

      if(organizations.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organizações não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return organizations;
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.organizationValidator.idValidate(id);

      boolean exist = this.organizationRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
      
      return exist;
   }


   // update logo image id
   @Transactional
   public void updateLogoImageId(UUID id, String logoImageId) {
      this.organizationValidator.idValidate(id);
      this.organizationValidator.logoImageIdValidate(logoImageId);

      try {
         this.organizationRepository.updateLogoImageId(id, logoImageId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // remove logo image id
   @Transactional
   public void removeLogoImageId(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         this.organizationRepository.removeLogoImageId(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update organization
   @Transactional
   public OrganizationResponseDTO update(UUID id, OrganizationUpdateDTO data) {
      this.organizationValidator.idValidate(id);
      this.organizationValidator.updateValidations(data);
      
      try {
         return this.organizationRepository.update(id, data);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete organization
   @Transactional
   public OrganizationDeletedDTO delete(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         return this.organizationRepository.delete(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         return this.organizationRepository.isDeleted(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}