
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.dto.OrganizationDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class OrganizationService {
 
   // properties
   private final OrganizationRepository organizationRepository;
   private final OrganizationValidator organizationValidator;
   private final UserService userService;

   // constructor - dependency injection
   public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationValidator organizationValidator,
      UserService userService
   ) {
      this.organizationRepository = organizationRepository;
      this.organizationValidator = organizationValidator;
      this.userService = userService; 
   }


   // create organization
   @Transactional
   public OrganizationResponseDTO create(OrganizationDTO data) {
      this.organizationValidator.idValidate(data.ownerId());
      this.organizationValidator.nameValidate(data.name());
      if(!data.description().trim().isEmpty()) this.organizationValidator.descriptionValidate(data.description());
   
      // get owner data
      UserEntity user = this.userService.getEntityById(data.ownerId());

      return this.organizationRepository.create(data, user);
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


   // exists by owner id
   public boolean existsByOwnerId(UUID ownerId) {
      this.organizationValidator.ownerIdValidate(ownerId);

      boolean exist = this.organizationRepository.existsByOwnerId(ownerId);
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

}