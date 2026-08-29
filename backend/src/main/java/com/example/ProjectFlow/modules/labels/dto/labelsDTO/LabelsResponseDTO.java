

// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


public record LabelsResponseDTO (

   UUID id,
   UUID projectId,
   String name,
   String color

) {

   public static LabelsResponseDTO get(LabelsEntity document) {
      return new LabelsResponseDTO(
         document.getId(),
         document.getProject().getId(),
         document.getName(),
         document.getColor()
      );
   }

}