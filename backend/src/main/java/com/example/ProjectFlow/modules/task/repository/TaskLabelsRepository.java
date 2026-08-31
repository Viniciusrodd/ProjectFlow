
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


@Repository
public class TaskLabelsRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // creating labels for tasks
   @Transactional
   public TaskLabelsResponseDTO create(TasksEntity tasksEntity, LabelsEntity labelsEntity) {
      TaskLabelsEntity taskLabel = new TaskLabelsEntity.Builder()
         .task(tasksEntity)
         .label(labelsEntity)
         .build();

      this.entityManager.persist(taskLabel);

      return TaskLabelsResponseDTO.get(taskLabel);
   }

}