

// packages
package com.example.ProjectFlow.modules.task.dto.taskDTO;

// imports
import java.util.UUID;
import java.time.LocalDate;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;


public record TasksResponseDTO (

   UUID id,
   UUID projectId,
   UUID columnId,
   UUID ownerId,
   String title,
   String description,
   PriorityEnum priority,
   LocalDate due_date

) {

   public static TasksResponseDTO get(TasksEntity document) {
      return new TasksResponseDTO(
         document.getId(),
         document.getProjectId(),
         document.getBoardColumnId(),
         document.getOwnerId(),
         document.getTitle(),
         document.getDescription(),
         document.getPriority(),
         document.getDueDate()
      );
   }

}