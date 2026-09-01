
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;


public record TaskLabelsDeletedDTO (

   UUID id,
   UUID taskId,
   UUID labelId,
   LocalDateTime createdAt,
   LocalDateTime deletedAt

) {

   public static TaskLabelsDeletedDTO get(TaskLabelsEntity document) {
      return new TaskLabelsDeletedDTO(
         document.getId(),
         document.getTask().getId(),
         document.getLabel().getId(),
         document.getCreatedAt(),
         document.getDeletedAt()
      );
   }

}