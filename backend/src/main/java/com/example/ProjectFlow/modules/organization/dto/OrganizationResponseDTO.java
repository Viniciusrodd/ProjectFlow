
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;


public record OrganizationResponseDTO (

   UUID id,
   UUID ownerId,
   String name,
   String description

) {
 
   public static OrganizationResponseDTO get(OrganizationEntity document) {
      return new OrganizationResponseDTO(
         document.getId(),
         document.getOwnerId(),
         document.getName(),
         document.getDescription()
      );
   }

}