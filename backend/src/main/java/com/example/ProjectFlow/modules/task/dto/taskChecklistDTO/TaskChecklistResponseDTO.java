
// packages
package com.example.ProjectFlow.modules.task.dto.taskChecklistDTO;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;


public record TaskChecklistResponseDTO (

   UUID id,
   UUID taskId,
   String description,
   Boolean completed,
   Integer position

) {

   public static TaskChecklistResponseDTO get(TaskChecklistEntity document) {
      return new TaskChecklistResponseDTO(
         document.getId(),
         document.getTaskId(),
         document.getDescription(),
         document.getCompleted(),
         document.getPosition()
      );
   }

}