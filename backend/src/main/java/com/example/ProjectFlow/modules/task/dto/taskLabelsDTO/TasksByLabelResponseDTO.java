
// packages
package com.example.ProjectFlow.modules.task.dto.taskLabelsDTO;

// imports
import java.util.UUID;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskDTO.TasksResponseDTO;


public record TasksByLabelResponseDTO (

   UUID id,
   TasksResponseDTO task

) {}