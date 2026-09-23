
// packages
package com.example.ProjectFlow.modules.task.dto.taskChecklistDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;


public record TaskChecklistDeletedDTO (

   UUID id,
   UUID taskId,
   String description,
   boolean completed,
   Integer position,
   LocalDateTime deletedAt

) {}