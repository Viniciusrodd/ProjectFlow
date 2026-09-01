
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
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


@Repository
public class TaskLabelsRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // creating task label relation
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
   public List<LabelsByTaskResponseDTO> getAllByTaskId(UUID taskId) {
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
         
      List<LabelsByTaskResponseDTO> labels = new ArrayList<>();

      for(TaskLabelsEntity taskLabel : taskLabelsDocument) {
         labels.add(LabelsByTaskResponseDTO.get(taskLabel));
      }

      return labels;
   }


   // get all tasks by label id
   public List<TasksByLabelResponseDTO> getAllByLabelId(UUID labelId) {
      List<TaskLabelsEntity> taskLabelsDocument = this.entityManager
         .createQuery(
            "SELECT tl FROM TaskLabelsEntity tl " +
            "JOIN FETCH tl.task " +
            "WHERE tl.label.id = :labelId " +
            "ORDER BY tl.createdAt ASC",
            TaskLabelsEntity.class
         )
         .setParameter("labelId", labelId)
         .getResultList();
         
      List<TasksByLabelResponseDTO> tasks = new ArrayList<>();

      for(TaskLabelsEntity taskLabel : taskLabelsDocument) {
         tasks.add(TasksByLabelResponseDTO.get(taskLabel));
      }

      return tasks;
   }


   // get entity by id
   public TaskLabelsEntity getEntityById(UUID id) throws NoResultException {
      TaskLabelsEntity taskLabel = this.entityManager
         .createQuery("SELECT tl FROM TaskLabelsEntity tl WHERE tl.id = :id", TaskLabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return taskLabel;
   }


   // exists by id
   public boolean existsById(UUID id) throws NoResultException {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(tl) FROM TaskLabelsEntity tl WHERE tl.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }

}