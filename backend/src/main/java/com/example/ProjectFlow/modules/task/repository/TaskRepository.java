
// packages
package com.example.ProjectFlow.modules.task.repository;

// imports
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.TasksDTO;
import com.example.ProjectFlow.modules.task.dto.TasksResponseDTO;
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

}