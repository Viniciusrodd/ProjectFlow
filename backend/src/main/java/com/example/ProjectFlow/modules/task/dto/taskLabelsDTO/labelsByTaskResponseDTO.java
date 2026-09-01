
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;


public record LabelsByTaskResponseDTO (

   UUID id,
   LabelsResponseDTO label

) {

   public static LabelsByTaskResponseDTO get(TaskLabelsEntity document) {
      return new LabelsByTaskResponseDTO(
         document.getId(),
         LabelsResponseDTO.get(document.getLabel())
      );
   }

}