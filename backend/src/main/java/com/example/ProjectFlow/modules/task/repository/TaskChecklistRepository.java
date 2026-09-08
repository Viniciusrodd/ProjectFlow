
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskChecklistEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;


@Repository
public class TaskChecklistRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // creating task checklist item
   @Transactional
   public TaskChecklistResponseDTO create(
      TasksEntity tasksEntity,
      TaskChecklistDTO data
   ) {
      TaskChecklistEntity taskChecklist = new TaskChecklistEntity.Builder()
         .task(tasksEntity)
         .description(data.description())
         .completed(data.completed())
         .position(data.position())
         .build();

      this.entityManager.persist(taskChecklist);

      return TaskChecklistResponseDTO.get(taskChecklist);
   }

}