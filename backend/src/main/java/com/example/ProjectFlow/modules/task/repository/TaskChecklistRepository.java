
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistDTO;
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;

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
   public TaskChecklistEntity create(
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

      return taskChecklist;
   }


   // check if position already exist
   public boolean checkPositionExistence(int position, UUID taskId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(c) FROM TaskChecklistEntity c " + 
            "WHERE c.position = :position " +
            "AND c.task.id = :taskId ",
            Long.class
         )
         .setParameter("position", position)
         .setParameter("taskId", taskId)
         .getSingleResult();

      return count > 0;
   }


   // get all checklist items by task id
   public List<TaskChecklistEntity> getAllByTaskId(UUID taskId) {
      List<TaskChecklistEntity> itemsEntity = this.entityManager
         .createQuery(
            "SELECT i FROM TaskChecklistEntity i " +
            "WHERE i.task.id = :taskId " +
            "ORDER BY i.createdAt ASC",
            TaskChecklistEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();
      
      return itemsEntity;
   }


   // get checklist item by id
   public TaskChecklistEntity getById(UUID id) throws NoResultException {
      TaskChecklistEntity itemEntity = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return itemEntity;
   }


   // get all checklist items
   public List<TaskChecklistEntity> getAll() {
      List<TaskChecklistEntity> itemsEntity = this.entityManager
         .createQuery(
            "SELECT i FROM TaskChecklistEntity i " +
            "ORDER BY i.createdAt ASC ", 
            TaskChecklistEntity.class
         )
         .getResultList();

      return itemsEntity;      
   }


   // get entity by id
   public TaskChecklistEntity getEntityById(UUID id) throws NoResultException {
      TaskChecklistEntity itemEntity = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return itemEntity;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(i) FROM TaskChecklistEntity i WHERE i.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // update task checklist item
   @Transactional
   public TaskChecklistEntity update(UUID id, TaskChecklistUpdateDTO data) throws NoResultException {
      TaskChecklistEntity itemEntity = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.description()).ifPresent(description -> itemEntity.setDescription(description));
      Optional.ofNullable(data.position()).ifPresent(position -> itemEntity.setPosition(position));

      return itemEntity;
   }


   // update completed field of task checklist item
   @Transactional 
   public TaskChecklistEntity setCompleted(UUID id, boolean completed) throws NoResultException {
      TaskChecklistEntity itemEntity = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      itemEntity.setCompleted(completed);

      return itemEntity;
   }


   // delete task checklist item
   @Transactional 
   public TaskChecklistEntity delete(UUID id) throws NoResultException {
      TaskChecklistEntity itemEntity = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      itemEntity.setDeletedAt(LocalDateTime.now());

      return itemEntity;
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      TaskChecklistEntity item = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return item.isDeleted();
   }

}