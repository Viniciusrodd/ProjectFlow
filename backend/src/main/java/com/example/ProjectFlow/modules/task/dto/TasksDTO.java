
// packages
package com.example.ProjectFlow.modules.task.dto;

// imports
import java.util.UUID;
import java.time.LocalDate;


public record TasksDTO (

   UUID projectId,
   UUID columnId,
   UUID ownerId,
   String title,
   String description,
   String priority,
   LocalDate due_date

) {}