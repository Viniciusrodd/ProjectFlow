
// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// import entity
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


public record LabelsUpdateDTO (

   String name,
   String color

) {

   public static LabelsUpdateDTO get(LabelsEntity document) {
      return new LabelsUpdateDTO(
         document.getName(),
         document.getColor()
      );
   }
   
}