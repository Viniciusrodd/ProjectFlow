
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;


public record labelsByTaskResponseDTO (

   LabelsResponseDTO label

) {

   public static labelsByTaskResponseDTO get(TaskLabelsEntity document) {
      return new labelsByTaskResponseDTO(
         LabelsResponseDTO.get(document.getLabel())
      );
   }

}