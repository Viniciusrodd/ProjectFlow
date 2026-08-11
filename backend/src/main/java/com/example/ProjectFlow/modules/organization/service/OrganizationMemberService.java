
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationMembersRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationMembersValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersResponseDTO;

// import service
import com.example.ProjectFlow.modules.user.service.UserService;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import exceptions

// import constants


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
      OrganizationService organizationService
   ) {
      this.organizationMembersRepository = organizationMembersRepository;
      this.organizationMembersValidator = organizationMembersValidator;
      this.userService = userService;
      this.organizationService = organizationService;
   }


   // create member participation
   @Transactional
   public OrganizationMembersResponseDTO createMemberParticipation(OrganizationMembersDTO data) {
      this.organizationMembersValidator.organizationIdValidate(data.organizationId());
      this.organizationMembersValidator.userIdValidate(data.userId());
      this.organizationMembersValidator.roleValidate(data.role().toString());

      // get organization data
      OrganizationEntity organization = this.organizationService.getEntityById(data.organizationId());
      
      // get user data
      UserEntity user = this.userService.getEntityById(data.userId());

      return this.organizationMembersRepository.createMemberParticipation(data, user, organization);
   }

}