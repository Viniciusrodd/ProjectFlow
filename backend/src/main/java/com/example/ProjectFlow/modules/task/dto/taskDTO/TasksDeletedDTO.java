
// packages
package com.example.ProjectFlow.modules.task.dto.taskDTO;

// imports
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;


public record TasksDeletedDTO (

   UUID id,
   UUID projectId,
   UUID columnId,
   UUID ownerId,
   String title,
   String description,
   PriorityEnum priority,
   LocalDate due_date,
   LocalDateTime completedAt,
   LocalDateTime deletedAt

) {
   
   public static TasksDeletedDTO get(TasksEntity document) {
      return new TasksDeletedDTO(
         document.getId(),
         document.getProjectId(),
         document.getBoardColumnId(),
         document.getOwnerId(),
         document.getTitle(),
         document.getDescription(),
         document.getPriority(),
         document.getDueDate(),
         document.getCompletedAt(),
         document.getDeletedAt()         
      );
   }

}