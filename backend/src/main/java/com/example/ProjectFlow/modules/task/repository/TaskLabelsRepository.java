
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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
   public TaskLabelsEntity create(TasksEntity tasksEntity, LabelsEntity labelsEntity) {
      TaskLabelsEntity taskLabel = new TaskLabelsEntity.Builder()
         .task(tasksEntity)
         .label(labelsEntity)
         .build();

      this.entityManager.persist(taskLabel);

      return taskLabel;
   }


   // get all labels by task id
   public List<TaskLabelsEntity> getAllByTaskId(UUID taskId) {
      List<TaskLabelsEntity> taskLabelsEntity = this.entityManager
         .createQuery(
            "SELECT tl FROM TaskLabelsEntity tl " +
            "JOIN FETCH tl.label " +
            "WHERE tl.task.id = :taskId " +
            "ORDER BY tl.createdAt ASC",
            TaskLabelsEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();

      return taskLabelsEntity;         
   }


   // get all tasks by label id
   public List<TaskLabelsEntity> getAllByLabelId(UUID labelId) {
      List<TaskLabelsEntity> taskLabelsEntity = this.entityManager
         .createQuery(
            "SELECT tl FROM TaskLabelsEntity tl " +
            "JOIN FETCH tl.task " +
            "WHERE tl.label.id = :labelId " +
            "ORDER BY tl.createdAt ASC",
            TaskLabelsEntity.class
         )
         .setParameter("labelId", labelId)
         .getResultList();

      return taskLabelsEntity;         
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


   // exists by task and label id
   public boolean existsByTaskIdAndLabelId(UUID taskId, UUID labelId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(tl) FROM TaskLabelsEntity tl " +
            "WHERE tl.task.id = :taskId " +
            "AND tl.label.id = :labeld ",
            Long.class
         )
         .setParameter("taskId", taskId)
         .setParameter("labelId", labelId)
         .getSingleResult();

      return count > 0;
   }


   // remove task label relation
   @Transactional
   public TaskLabelsEntity removeRelation(UUID id) throws NoResultException {
      TaskLabelsEntity taskLabel = this.entityManager
         .createQuery("SELECT tl FROM TaskLabelsEntity tl WHERE tl.id = :id", TaskLabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      taskLabel.setDeletedAt(LocalDateTime.now());

      return taskLabel;
   }

}