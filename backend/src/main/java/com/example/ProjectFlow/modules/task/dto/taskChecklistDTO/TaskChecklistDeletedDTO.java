
// packages
package com.example.ProjectFlow.modules.task.dto.taskChecklistDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;


public record TaskChecklistDeletedDTO (

   UUID id,
   UUID taskId,
   String description,
   boolean completed,
   Integer position,
   LocalDateTime deletedAt

) {

   public static TaskChecklistDeletedDTO get(TaskChecklistEntity document) {
      return new TaskChecklistDeletedDTO(
         document.getId(),
         document.getTaskId(),
         document.getDescription(),
         document.getCompleted(),
         document.getPosition(),
         document.getDeletedAt()
      );
   }

}