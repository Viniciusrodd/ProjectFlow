
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@Service 
public class OrganizationCreationService {
 
   // properties
   private final OrganizationService organizationService;
   private final OrganizationMemberService organizationMemberService;


   // constructor - dependency injection
   public OrganizationCreationService(
      OrganizationService organizationService,
      OrganizationMemberService organizationMemberService
   ) {
      this.organizationService = organizationService;
      this.organizationMemberService = organizationMemberService;
   }


   // create organization
   @Transactional 
   public OrganizationResponseDTO create(OrganizationDTO data) {
      // organization
      OrganizationResponseDTO organization = this.organizationService.create(data);

      // organization member - owner
      this.organizationMemberService.createMemberParticipation(
         new OrganizationMembersDTO(
            organization.id(),
            data.ownerId(),
            RoleEnum.OWNER.toString()
         )
      );

      return organization;
   }

}