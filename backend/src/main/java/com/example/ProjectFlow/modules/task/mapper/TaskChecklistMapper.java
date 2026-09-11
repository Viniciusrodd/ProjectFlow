
// packages
package com.example.ProjectFlow.modules.task.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDeletedDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;


@Component 
public class TaskChecklistMapper {
 
   // from TaskChecklistEntity to TaskChecklistResponseDTO
   public TaskChecklistResponseDTO toTaskChecklistResponseDTO(TaskChecklistEntity entity) {
      return new TaskChecklistResponseDTO(
         entity.getId(),
         entity.getTaskId(),
         entity.getDescription(),
         entity.getCompleted(),
         entity.getPosition()
      );
   }


   // from TaskChecklistEntity to TaskChecklistDeletedDTO
   public TaskChecklistDeletedDTO toTaskChecklistDeletedDTO(TaskChecklistEntity entity) {
      return new TaskChecklistDeletedDTO(
         entity.getId(),
         entity.getTaskId(),
         entity.getDescription(),
         entity.getCompleted(),
         entity.getPosition(),
         entity.getDeletedAt()
      );
   }

}