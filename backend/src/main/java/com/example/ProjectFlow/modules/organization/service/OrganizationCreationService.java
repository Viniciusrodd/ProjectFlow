
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationMapper;


@Service 
public class OrganizationCreationService {
 
   // properties
   private final OrganizationService organizationService;
   private final OrganizationMemberService organizationMemberService;
   private final ActivityLogService activityLogService;
   private final OrganizationMapper organizationMapper;


   // constructor - dependency injection
   public OrganizationCreationService(
      OrganizationService organizationService,
      OrganizationMemberService organizationMemberService,
      ActivityLogService activityLogService,
      OrganizationMapper organizationMapper
   ) {
      this.organizationService = organizationService;
      this.organizationMemberService = organizationMemberService;
      this.activityLogService = activityLogService;
      this.organizationMapper = organizationMapper;
   }


   // create organization
   @Transactional 
   public OrganizationResponseDTO create(OrganizationDTO data) {
      // organization
      OrganizationEntity organization = this.organizationService.create(data);

      // organization member - owner
      this.organizationMemberService.createMemberParticipation(
         new OrganizationMembersDTO(
            organization.getId(),
            organization.getOwnerId(),
            RoleEnum.OWNER.toString()
         )
      );

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO(
         organization.getId(),
         null, null, null, null,
         organization.getOwnerId(),
         ActivityActionEnum.ORGANIZACAO_CRIADA,
         "Organização: " + organization.getName() + ", criada por: " + organization.getOwner().getName()
      );
      this.activityLogService.create(activityLog);

      return this.organizationMapper.toOrganizationResponseDTO(organization);
   }

}