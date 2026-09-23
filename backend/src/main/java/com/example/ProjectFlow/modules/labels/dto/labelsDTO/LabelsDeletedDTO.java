
// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record LabelsDeletedDTO (

   UUID id,
   UUID projectId,
   String name,
   String color,
   LocalDateTime deletedAt

) {}