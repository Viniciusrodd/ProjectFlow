
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.labelsByTaskResponseDTO;

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


   // get all labels by task id
   public List<labelsByTaskResponseDTO> getAllByTaskId(UUID taskId) {
      List<TaskLabelsEntity> taskLabelsDocument = this.entityManager
         .createQuery(
            "SELECT tl FROM TaskLabelsEntity tl " +
            "JOIN FETCH tl.label " +
            "WHERE tl.task.id = :taskId " +
            "ORDER BY tl.createdAt ASC",
            TaskLabelsEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();
         
      List<labelsByTaskResponseDTO> taskLabels = new ArrayList<>();

      for(TaskLabelsEntity taskLabel : taskLabelsDocument) {
         taskLabels.add(labelsByTaskResponseDTO.get(taskLabel));
      }

      return taskLabels;
   }

}