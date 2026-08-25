
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;


public record OrganizationMembersDeletedDTO (

   UUID organizationId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt,
   LocalDateTime deletedAt

) {

   public static OrganizationMembersDeletedDTO get(OrganizationMembersEntity document) {
      return new OrganizationMembersDeletedDTO(
         document.getOrganizationId(),
         document.getUserId(),
         document.getRole(),
         document.getJoinedAt(),
         document.getDeletedAt()
      );
   }

}