
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationDTO;

import java.time.LocalDateTime;
// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;


public record OrganizationDeletedDTO (

   UUID id,
   UUID ownerId,
   String name,
   String description,
   LocalDateTime deletedAt

) {

   public static OrganizationDeletedDTO get(OrganizationEntity document) {
      return new OrganizationDeletedDTO(
         document.getId(),
         document.getOwnerId(),
         document.getName(),
         document.getDescription(),
         document.getDeletedAt()
      );
   }

}