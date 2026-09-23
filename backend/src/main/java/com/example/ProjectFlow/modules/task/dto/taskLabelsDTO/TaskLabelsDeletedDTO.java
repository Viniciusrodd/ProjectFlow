
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;


public record TaskLabelsDeletedDTO (

   UUID id,
   UUID taskId,
   UUID labelId,
   LocalDateTime createdAt,
   LocalDateTime deletedAt

) {}