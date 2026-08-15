
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


public record ProjectDeletedDTO (

   UUID id,
   UUID organizationId,
   UUID ownerId,
   String name,
   String description,
   StatusEnum status,
   LocalDateTime deletedAt

) {

   public static ProjectDeletedDTO get(ProjectEntity document) {
      return new ProjectDeletedDTO(
         document.getId(),
         document.getOrganizationId(),
         document.getOwnerId(),
         document.getName(),
         document.getDescription(),
         document.getStatus(),
         document.getDeletedAt()
      );
   }

}