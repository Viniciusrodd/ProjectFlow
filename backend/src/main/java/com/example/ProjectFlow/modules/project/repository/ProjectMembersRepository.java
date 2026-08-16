
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;


@Repository
public class ProjectMembersRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // create member participation
   @Transactional
   public ProjectMembersResponseDTO createMemberParticipation(
      ProjectMembersDTO data,
      UserEntity user,
      ProjectEntity project
   ) {
      ProjectMembersEntity projectMembers = new ProjectMembersEntity.Builder()
         .user(user)
         .project(project)
         .role(RoleEnum.valueOf(data.role().toUpperCase()))
         .joinedAt(LocalDateTime.now())
         .build();

      this.entityManager.persist(projectMembers);

      return ProjectMembersResponseDTO.get(projectMembers);
   }

}