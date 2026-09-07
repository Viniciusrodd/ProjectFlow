
// packages
package com.example.ProjectFlow.modules.task.dto.taskChecklistDTO;

// imports
import java.util.UUID;


public record TaskChecklistDTO (

   UUID taskId,
   String description,
   Boolean completed,
   Integer position

) {}