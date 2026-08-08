
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;


public record OrganizationDTO (

   UUID id,
   String name,
   String description,
   UUID ownerId

) {
 
   public static OrganizationDTO get(OrganizationEntity document) {
      return new OrganizationDTO(
         document.getId(),
         document.getName(),
         document.getDescription(),
         document.getOwnerId()
      );
   }

}