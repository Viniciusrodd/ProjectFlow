
// packages
package com.example.ProjectFlow.modules.labels.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDeletedDTO;


@Component 
public class LabelsMapper {
 
   // from LabelsEntity to LabelsResponseDTO
   public LabelsResponseDTO toLabelsResponseDTO(LabelsEntity entity) {
      return new LabelsResponseDTO(
         entity.getId(),
         entity.getProject().getId(),
         entity.getName(),
         entity.getColor()
      );
   }


   // from LabelsEntity to LabelsUpdateDTO
   public LabelsUpdateDTO toLabelsUpdateDTO(LabelsEntity entity) {
      return new LabelsUpdateDTO(
         entity.getName(),
         entity.getColor()
      );
   }


   // from LabelsEntity to LabelsDeletedDTO
   public LabelsDeletedDTO toLabelsDeletedDTO(LabelsEntity entity) {
      return new LabelsDeletedDTO(
         entity.getId(),
         entity.getProject().getId(),
         entity.getName(),
         entity.getColor(),
         entity.getDeletedAt()
      );
   }

}