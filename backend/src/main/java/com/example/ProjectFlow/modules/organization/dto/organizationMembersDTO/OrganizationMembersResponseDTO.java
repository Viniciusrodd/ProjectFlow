
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;


public record OrganizationMembersResponseDTO (

   UUID organizationId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt

) {

   public static OrganizationMembersResponseDTO get(OrganizationMembersEntity document) {
      return new OrganizationMembersResponseDTO(
         document.getOrganizationId(),
         document.getUserId(),
         document.getRole(),
         document.getJoinedAt()
      );
   } 

}