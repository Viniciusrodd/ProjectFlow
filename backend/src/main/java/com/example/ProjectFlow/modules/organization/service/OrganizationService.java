
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationValidator;

// import DTOs
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

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMapper;


@Service
public class OrganizationService {
 
   // properties
   private final OrganizationRepository organizationRepository;
   private final OrganizationValidator organizationValidator;
   private final UserService userService;
   private final OrganizationMemberService organizationMemberService;
   private final OrganizationMapper organizationMapper;


   // constructor - dependency injection
   public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationValidator organizationValidator,
      UserService userService,
      OrganizationMemberService organizationMemberService,
      OrganizationMapper organizationMapper
   ) {
      this.organizationRepository = organizationRepository;
      this.organizationValidator = organizationValidator;
      this.userService = userService; 
      this.organizationMemberService = organizationMemberService;
      this.organizationMapper = organizationMapper;
   }


   // create organization
   @Transactional
   public OrganizationResponseDTO create(OrganizationDTO data) {
      this.organizationValidator.idValidate(data.ownerId());
      this.organizationValidator.nameValidate(data.name());
      if(!data.description().trim().isEmpty()) this.organizationValidator.descriptionValidate(data.description());
   
      // get owner data
      UserEntity owner = this.userService.getEntityById(data.ownerId());

      // creation
      OrganizationEntity organizationEntity = this.organizationRepository.create(data, owner);

      // mapping
      OrganizationResponseDTO organization = this.organizationMapper.toOrganizationResponseDTO(organizationEntity);

      // set organization member - owner
      OrganizationMembersDTO organizationMembersData = new OrganizationMembersDTO(
         organization.id(),
         owner.getId(),
         RoleEnum.OWNER.toString()
      );
      this.organizationMemberService.createMemberParticipation(organizationMembersData);

      return organization;
   }


   // get all
   public List<OrganizationResponseDTO> getAll() {
      List<OrganizationEntity> organizationsEntity = this.organizationRepository.getAll();

      if(organizationsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organizações não existem",
            ResponseMessages.NOT_FOUND
         ));
      }
      
      // mapping
      List<OrganizationResponseDTO> organizations = new ArrayList<>();
      for(OrganizationEntity organization : organizationsEntity) {
         organizations.add(this.organizationMapper.toOrganizationResponseDTO(organization));
      }

      return organizations;
   }


   // get by id
   public OrganizationResponseDTO getById(UUID id) {
      this.organizationValidator.idValidate(id);

      try {
         OrganizationEntity organizationEntity = this.organizationRepository.getById(id);

         return this.organizationMapper.toOrganizationResponseDTO(organizationEntity);
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

      List<OrganizationEntity> organizationsEntity = this.organizationRepository.getByOwnerId(ownerId);

      if(organizationsEntity.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Organizações não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      // mapping
      List<OrganizationResponseDTO> organizations = new ArrayList<>();
      for(OrganizationEntity organization : organizationsEntity) {
         organizations.add(this.organizationMapper.toOrganizationResponseDTO(organization));
      }

      return organizations;
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
         OrganizationEntity organizationEntity = this.organizationRepository.update(id, data);

         return this.organizationMapper.toOrganizationResponseDTO(organizationEntity);
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
         OrganizationEntity organizationEntity = this.organizationRepository.delete(id);

         return this.organizationMapper.toOrganizationDeletedDTO(organizationEntity);
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