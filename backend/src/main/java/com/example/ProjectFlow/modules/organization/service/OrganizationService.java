
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationResponseDTO;
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

}