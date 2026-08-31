
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;


public record TaskLabelsResponseDTO (

   UUID taskId,
   UUID labelId

) {

   public static TaskLabelsResponseDTO get(TaskLabelsEntity document) {
      return new TaskLabelsResponseDTO(
         document.getTask().getId(),
         document.getLabel().getId()
      );
   }

}