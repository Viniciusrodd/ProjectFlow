
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
import com.example.ProjectFlow.modules.task.dto.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.TasksResponseDTO;
import com.example.ProjectFlow.modules.task.dto.TasksUpdateDTO;
import com.example.ProjectFlow.modules.task.dto.TasksCompleteResponseDTO;

// import entity
import com.example.ProjectFlow.modules.task.entity.TasksEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.board.entity.BoardColumnsEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;


@Repository
public class TaskRepository {
   
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // task creation
   @Transactional
   public TasksResponseDTO create(
      TasksDTO data,
      ProjectEntity projectEntity,
      BoardColumnsEntity boardColumnsEntity,
      UserEntity ownerEntity
   ) {
      TasksEntity task = new TasksEntity.Builder()
         .project(projectEntity)
         .boardColumn(boardColumnsEntity)
         .owner(ownerEntity)
         .title(data.title())
         .description(data.description())
         .priority(PriorityEnum.valueOf(data.priority().toUpperCase()))
         .dueDate(data.dueDate())
         .build();

      this.entityManager.persist(task);

      return TasksResponseDTO.get(task);
   }


   // get all
   public List<TasksCompleteResponseDTO> getAll() {
      List<TasksEntity> tasksEntities = this.entityManager
         .createQuery("SELECT t FROM TasksEntity t ORDER BY t.createdAt ASC", TasksEntity.class)
         .getResultList();
      
      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();

      for(TasksEntity task : tasksEntities) {
         tasks.add(TasksCompleteResponseDTO.get(task));
      }

      return tasks;
   }


   // get by id
   public TasksCompleteResponseDTO getById(UUID id) throws NoResultException {
      TasksEntity task = this.entityManager
         .createQuery("SELECT t FROM TasksEntity t WHERE t.id = :id", TasksEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return TasksCompleteResponseDTO.get(task);
   }


   // get entity by id
   public TasksEntity getEntityById(UUID id) throws NoResultException {
      TasksEntity task = this.entityManager
         .createQuery("SELECT t FROM TasksEntity t WHERE t.id = :id", TasksEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return task;
   }


   // get tasks by project id
   public List<TasksCompleteResponseDTO> getByProjectId(UUID projectId) {
      List<TasksEntity> tasksEntities = this.entityManager
         .createQuery(
            "SELECT t FROM TasksEntity t " + 
            "WHERE t.project.id = :projectId " + 
            "ORDER BY t.createdAt ASC ", 
            TasksEntity.class
         )
         .setParameter("projectId", projectId)
         .getResultList();

      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();

      for(TasksEntity task : tasksEntities) {
         tasks.add(TasksCompleteResponseDTO.get(task));
      }

      return tasks;
   }


   // get tasks by board column id
   public List<TasksCompleteResponseDTO> getByColumnId(UUID columnId) {
      List<TasksEntity> tasksEntities = this.entityManager
         .createQuery(
            "SELECT t FROM TasksEntity t " + 
            "WHERE t.boardColumn.id = :columnId " + 
            "ORDER BY t.createdAt ASC ", 
            TasksEntity.class
         )
         .setParameter("columnId", columnId)
         .getResultList();

      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();

      for(TasksEntity task : tasksEntities) {
         tasks.add(TasksCompleteResponseDTO.get(task));
      }

      return tasks;
   }


   // get tasks by owner id
   public List<TasksCompleteResponseDTO> getByOwnerId(UUID ownerId) {
      List<TasksEntity> tasksEntities = this.entityManager
         .createQuery(
            "SELECT t FROM TasksEntity t " + 
            "WHERE t.owner.id = :ownerId " + 
            "ORDER BY t.createdAt ASC ", 
            TasksEntity.class
         )
         .setParameter("ownerId", ownerId)
         .getResultList();

      List<TasksCompleteResponseDTO> tasks = new ArrayList<>();

      for(TasksEntity task : tasksEntities) {
         tasks.add(TasksCompleteResponseDTO.get(task));
      }

      return tasks;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(t) FROM TasksEntity t WHERE t.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // update column id - task position
   @Transactional
   public TasksCompleteResponseDTO updateColumn(UUID id, BoardColumnsEntity boardColumnEntity) throws NoResultException {
      TasksEntity task = this.entityManager
         .createQuery("SELECT t FROM TasksEntity t WHERE t.id = :id ", TasksEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      task.setBoardColumn(boardColumnEntity);

      return TasksCompleteResponseDTO.get(task);
   }


   // update task
   @Transactional
   public TasksCompleteResponseDTO update(UUID id, TasksUpdateDTO data) throws NoResultException {
      TasksEntity task = this.entityManager
         .createQuery("SELECT t FROM TasksEntity t WHERE t.id = :id ", TasksEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.title()).ifPresent(title -> task.setTitle(title));
      Optional.ofNullable(data.description()).ifPresent(description -> task.setDescription(description));
      Optional.ofNullable(data.priority()).ifPresent(priority -> 
         task.setPriority(PriorityEnum.valueOf(priority.toUpperCase()))
      );
      Optional.ofNullable(data.dueDate()).ifPresent(dueDate -> task.setDueDate(dueDate));

      return TasksCompleteResponseDTO.get(task);
   }

}