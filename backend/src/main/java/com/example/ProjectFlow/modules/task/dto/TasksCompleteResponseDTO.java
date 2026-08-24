
// packages
package com.example.ProjectFlow.modules.task.dto;

// imports
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;


public record TasksCompleteResponseDTO (

   UUID id,
   UUID projectId,
   UUID columnId,
   UUID ownerId,
   String title,
   String description,
   PriorityEnum priority,
   LocalDate due_date,
   LocalDateTime completedAt

) {

   public static TasksCompleteResponseDTO get(TasksEntity document) {
      return new TasksCompleteResponseDTO(
         document.getId(),
         document.getProjectId(),
         document.getBoardColumnId(),
         document.getOwnerId(),
         document.getTitle(),
         document.getDescription(),
         document.getPriority(),
         document.getDueDate(),
         document.getCompletedAt()
      );
   }

}