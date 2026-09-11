
// packages
package com.example.ProjectFlow.modules.task.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksCompleteResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksDeletedDTO;


@Component 
public class TaskMapper {
 
   // from TasksEntity to TasksResponseDTO
   public TasksResponseDTO toTasksResponseDTO(TasksEntity entity) {
      return new TasksResponseDTO(
         entity.getId(),
         entity.getProjectId(),
         entity.getBoardColumnId(),
         entity.getOwnerId(),
         entity.getTitle(),
         entity.getDescription(),
         entity.getPriority(),
         entity.getDueDate()
      );
   }


   // from TasksEntity to TasksDeletedDTO
   public TasksDeletedDTO toTasksDeletedDTO(TasksEntity entity) {
      return new TasksDeletedDTO(
         entity.getId(),
         entity.getProjectId(),
         entity.getBoardColumnId(),
         entity.getOwnerId(),
         entity.getTitle(),
         entity.getDescription(),
         entity.getPriority(),
         entity.getDueDate(),
         entity.getCompletedAt(),
         entity.getDeletedAt()
      );
   }


   // from TasksEntity to TasksCompleteResponseDTO
   public TasksCompleteResponseDTO toTasksCompleteResponseDTO(TasksEntity entity) {
      return new TasksCompleteResponseDTO(
         entity.getId(),
         entity.getProjectId(),
         entity.getBoardColumnId(),
         entity.getOwnerId(),
         entity.getTitle(),
         entity.getDescription(),
         entity.getPriority(),
         entity.getDueDate(),
         entity.getCompletedAt()
      );
   }

}