
// packages
package com.example.ProjectFlow.modules.task.dto.taskChecklistDTO;

// imports
import java.util.UUID;


public record TaskChecklistResponseDTO (

   UUID id,
   UUID taskId,
   String description,
   boolean completed,
   Integer position

) {}