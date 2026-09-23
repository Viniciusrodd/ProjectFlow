
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;


public record LabelsByTaskResponseDTO (

   UUID id,
   LabelsResponseDTO label

) {}