
// packages
package com.example.ProjectFlow.modules.labels.dto.labelsDTO;

// imports
import java.util.UUID;


public record LabelsDTO (

   UUID projectId,
   String name,
   String color

) {}