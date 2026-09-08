
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
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
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistResponseDTO;
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
   public List<TaskChecklistResponseDTO> getAllByTaskId(UUID taskId) {
      List<TaskChecklistEntity> itemsDocument = this.entityManager
         .createQuery(
            "SELECT i FROM TaskChecklistEntity i " +
            "WHERE i.task.id = :taskId " +
            "ORDER BY i.createdAt ASC",
            TaskChecklistEntity.class
         )
         .setParameter("taskId", taskId)
         .getResultList();
      
      List<TaskChecklistResponseDTO> items = new ArrayList<>();

      for(TaskChecklistEntity item : itemsDocument) {
         items.add(TaskChecklistResponseDTO.get(item));
      }

      return items;
   }


   // get checklist item by id
   public TaskChecklistResponseDTO getById(UUID id) throws NoResultException {
      TaskChecklistEntity item = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return TaskChecklistResponseDTO.get(item);
   }


   // get all checklist items
   public List<TaskChecklistResponseDTO> getAll() {
      List<TaskChecklistEntity> itemsDocument = this.entityManager
         .createQuery(
            "SELECT i FROM TaskChecklistEntity i " +
            "ORDER BY i.createdAt ASC ", 
            TaskChecklistEntity.class
         )
         .getResultList();
      
      List<TaskChecklistResponseDTO> items = new ArrayList<>();

      for(TaskChecklistEntity item : itemsDocument) {
         items.add(TaskChecklistResponseDTO.get(item));
      }

      return items;
   }


   // get entity by id
   public TaskChecklistEntity getEntityById(UUID id) throws NoResultException {
      TaskChecklistEntity item = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return item;
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
   public TaskChecklistResponseDTO update(UUID id, TaskChecklistUpdateDTO data) throws NoResultException {
      TaskChecklistEntity item = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.description()).ifPresent(description -> item.setDescription(description));
      Optional.ofNullable(data.position()).ifPresent(position -> item.setPosition(position));

      return TaskChecklistResponseDTO.get(item);
   }


   // update completed field of task checklist item
   @Transactional 
   public TaskChecklistResponseDTO setCompleted(UUID id, boolean completed) throws NoResultException {
      TaskChecklistEntity item = this.entityManager
         .createQuery("SELECT i FROM TaskChecklistEntity i WHERE i.id = :id", TaskChecklistEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      item.setCompleted(completed);

      return TaskChecklistResponseDTO.get(item);
   }

}