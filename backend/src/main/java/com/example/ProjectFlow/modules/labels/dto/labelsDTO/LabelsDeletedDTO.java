
// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


public record LabelsDeletedDTO (

   UUID id,
   UUID projectId,
   String name,
   String color,
   LocalDateTime deletedAt

) {
 
   public static LabelsDeletedDTO get(LabelsEntity document) {
      return new LabelsDeletedDTO(
         document.getId(),
         document.getProject().getId(),
         document.getName(),
         document.getColor(),
         document.getDeletedAt()
      );
   }
   
}