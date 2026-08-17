
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectMembersResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectMembersCompleteResponseDTO;
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


   // get all project members
   public List<ProjectMembersCompleteResponseDTO> getAllMembersByProjectId(UUID projectId) {
      List<ProjectMembersEntity> members = this.entityManager
         .createQuery(
            "SELECT m FROM ProjectMembersEntity m " +
            "JOIN FETCH m.user " +
            "JOIN FETCH m.project " +
            "WHERE m.project.id = :projectId " +
            "ORDER BY m.joinedAt DESC",
            ProjectMembersEntity.class
         )
         .setParameter("projectId", projectId)
         .getResultList();

      List<ProjectMembersCompleteResponseDTO> allMembers = new ArrayList<>();

      for(ProjectMembersEntity member : members) {
         allMembers.add(ProjectMembersCompleteResponseDTO.get(member));
      }

      return allMembers;
   }


   // get all members by role
   public List<ProjectMembersCompleteResponseDTO> getAllMembersByRole(
      UUID projectId,
      RoleEnum role
   ) {
      List<ProjectMembersEntity> members = this.entityManager
         .createQuery(
            "SELECT m FROM ProjectMembersEntity m " +
            "JOIN FETCH m.user " +
            "JOIN FETCH m.project " +
            "WHERE m.project.id = :projectId " +
            "AND m.role = :role " +
            "ORDER BY m.joinedAt DESC",
            ProjectMembersEntity.class
         )
         .setParameter("projectId", projectId)
         .setParameter("role", role)
         .getResultList();

      List<ProjectMembersCompleteResponseDTO> allMembers = new ArrayList<>();

      for(ProjectMembersEntity member : members) {
         allMembers.add(ProjectMembersCompleteResponseDTO.get(member));
      }

      return allMembers;
   }


   // get entity by id
   public ProjectMembersEntity getEntityById(UUID id) throws NoResultException {
      ProjectMembersEntity entity = this.entityManager
         .createQuery("SELECT e FROM ProjectMembersEntity e WHERE e.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();
         
      return entity;
   }

}