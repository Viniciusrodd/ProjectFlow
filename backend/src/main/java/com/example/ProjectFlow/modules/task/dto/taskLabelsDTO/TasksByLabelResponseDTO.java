
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;


public record TasksByLabelResponseDTO (

   UUID id,
   TasksResponseDTO task

) {

   public static TasksByLabelResponseDTO get(TaskLabelsEntity document) {
      return new TasksByLabelResponseDTO(
         document.getId(),
         TasksResponseDTO.get(document.getTask())
      );
   }

}