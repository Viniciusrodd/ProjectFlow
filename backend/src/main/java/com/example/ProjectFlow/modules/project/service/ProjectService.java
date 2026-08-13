
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions

// import constants

// import enum



@Service
public class ProjectService {
 
   // properties
   private final ProjectRepository projectRepository;
   private final ProjectValidator projectValidator;
   private final UserService userService;
   private final OrganizationService organizationService;


   // constructor - dependency injection
   public ProjectService(
      ProjectRepository projectRepository,
      ProjectValidator projectValidator,
      UserService userService,
      OrganizationService organizationService
   ) {
      this.projectRepository = projectRepository;
      this.projectValidator = projectValidator;
      this.userService = userService;
      this.organizationService = organizationService;
   }


   // project creation
   @Transactional
   public ProjectResponseDTO create(ProjectDTO data) {
      this.projectValidator.organizationIdValidate(data.organizationId());
      this.projectValidator.ownerIdValidate(data.ownerId());
      this.projectValidator.nameValidate(data.name());
      this.projectValidator.descriptionValidate(data.description());

      // get organization data
      OrganizationEntity organization = this.organizationService.getEntityById(data.organizationId());

      // get owner data
      UserEntity owner = this.userService.getEntityById(data.ownerId());

      // set project member - admin...

      return this.projectRepository.create(data, organization, owner);
   }

}