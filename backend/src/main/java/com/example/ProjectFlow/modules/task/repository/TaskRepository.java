
// packages
package com.example.ProjectFlow.modules.task.repository;

import java.util.ArrayList;
import java.util.List;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

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

}