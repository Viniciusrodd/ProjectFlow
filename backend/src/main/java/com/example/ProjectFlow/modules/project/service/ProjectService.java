
// packages
package com.example.ProjectFlow.modules.project.service;


// imports
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
// import service
import com.example.ProjectFlow.modules.user.service.UserService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

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


   // get all 
   public List<ProjectResponseDTO> getAll() {
      List<ProjectResponseDTO> projects = this.projectRepository.getAll();

      if(projects.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projetos não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return projects;
   }


   // get by id
   public ProjectResponseDTO getById(UUID id) {
      this.projectValidator.idValidate(id);

      try {
         return this.projectRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public ProjectEntity getEntityById(UUID id) {
      this.projectValidator.idValidate(id);

      try {
         return this.projectRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get all by organization id
   public List<ProjectResponseDTO> getByOrganizationId(UUID organizationId) {
      this.projectValidator.organizationIdValidate(organizationId);

      List<ProjectResponseDTO> projects = this.projectRepository.getByOrganizationId(organizationId);

      if(projects.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projetos não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return projects;
   }  


   // get all by owner id
   public List<ProjectResponseDTO> getByOwnerId(UUID ownerId) {
      this.projectValidator.ownerIdValidate(ownerId);

      List<ProjectResponseDTO> projects = this.projectRepository.getByOwnerId(ownerId);

      if(projects.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projetos não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return projects;
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.projectValidator.idValidate(id);

      boolean exist = this.projectRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }


   // update logo image id
   @Transactional
   public void updateLogoImageId(UUID id, String logoImageId) {
      this.projectValidator.idValidate(id);
      this.projectValidator.logoImageIdValidate(logoImageId);

      try {
         this.projectRepository.updateLogoImageId(id, logoImageId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // remove logo image id
   @Transactional
   public void removeLogoImageId(UUID id) {
      this.projectValidator.idValidate(id);

      try {
         this.projectRepository.removeLogoImageId(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


}