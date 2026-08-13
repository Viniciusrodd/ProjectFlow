
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


public record ProjectResponseDTO (

   UUID id,
   UUID organizationId,
   UUID ownerId,
   String name,
   String description,
   StatusEnum status

) {

   public static ProjectResponseDTO get(ProjectEntity document) {
      return new ProjectResponseDTO(
         document.getId(),
         document.getOrganizationId(),
         document.getOwnerId(),
         document.getName(),
         document.getDescription(),
         document.getStatus()
      );
   }

}