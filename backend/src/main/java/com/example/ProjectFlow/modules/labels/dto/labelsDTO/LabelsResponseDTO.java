
// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// imports
import java.util.UUID;


public record LabelsResponseDTO (

   UUID id,
   UUID projectId,
   String name,
   String color

) {}