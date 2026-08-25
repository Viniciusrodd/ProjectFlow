
// packages
package com.example.ProjectFlow.modules.task.dto.taskDTO;

// imports
import java.time.LocalDate;


public record TasksUpdateDTO (
 
   String title,
   String description,
   String priority,
   LocalDate dueDate

) {}