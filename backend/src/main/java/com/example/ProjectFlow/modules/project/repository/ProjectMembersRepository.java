
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectMembersDTO.ProjectMembersDTO;

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
   public ProjectMembersEntity createMemberParticipation(
      ProjectMembersDTO data,
      UserEntity userEntity,
      ProjectEntity projectEntity
   ) {
      ProjectMembersEntity projectMembers = new ProjectMembersEntity.Builder()
         .user(userEntity)
         .project(projectEntity)
         .role(RoleEnum.valueOf(data.role().toUpperCase()))
         .joinedAt(LocalDateTime.now())
         .build();

      this.entityManager.persist(projectMembers);

      return projectMembers;
   }


   // get all members
   public List<ProjectMembersEntity> getAllProjectMembers() {
      List<ProjectMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM ProjectMembersEntity m " +
            "JOIN FETCH m.user " +
            "ORDER BY m.joinedAt DESC ", 
            ProjectMembersEntity.class
         )
         .getResultList();
         
      return membersEntity;
   }


   // get member by relation id
   public ProjectMembersEntity getProjectMemberById(UUID id) throws NoResultException {
      ProjectMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM ProjectMembersEntity m WHERE m.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return member;
   }


   // get all members by project
   public List<ProjectMembersEntity> getAllMembersByProjectId(UUID projectId) {
      List<ProjectMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM ProjectMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.project.id = :projectId " +
            "ORDER BY m.joinedAt DESC",
            ProjectMembersEntity.class
         )
         .setParameter("projectId", projectId)
         .getResultList();

      return membersEntity;
   }


   // get all members by role
   public List<ProjectMembersEntity> getAllMembersByRole(
      UUID projectId,
      RoleEnum role
   ) {
      List<ProjectMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM ProjectMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.project.id = :projectId " +
            "AND m.role = :role " +
            "ORDER BY m.joinedAt DESC",
            ProjectMembersEntity.class
         )
         .setParameter("projectId", projectId)
         .setParameter("role", role)
         .getResultList();
         
      return membersEntity;
   }


   // get entity by id
   public ProjectMembersEntity getEntityById(UUID id) throws NoResultException {
      ProjectMembersEntity entity = this.entityManager
         .createQuery("SELECT e FROM ProjectMembersEntity e WHERE e.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();
         
      return entity;
   }


   // exists by id
   public boolean existsById(UUID id) throws NoResultException {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(m) FROM ProjectMembersEntity m WHERE m.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // exists by project and member id
   public boolean existsByProjectIdAndMemberId(UUID projectId, UUID memberId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM ProjectMembersEntity m " +
            "WHERE m.project.id = :projectId " +
            "AND m.user.id = :memberId ",
            Long.class
         )
         .setParameter("projectId", projectId)
         .setParameter("memberId", memberId)
         .getSingleResult();

      return count > 0;
   }


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID projectId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM ProjectMembersEntity m " +
            "WHERE m.user.id = :userId " +
            "AND m.project.id = :projectId ",
            Long.class
         )
         .setParameter("userId", userId)
         .setParameter("projectId", projectId)
         .getSingleResult();

      return count > 0;
   }


   // count admins members by project
   public Long countAdminsByProject(UUID projectId) {
      Long admins = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM ProjectMembersEntity m " +
            "WHERE m.project.id = :projectId " +
            "AND m.role = :role ",
            Long.class
         )
         .setParameter("projectId", projectId)
         .setParameter("role", RoleEnum.ADMIN)
         .getSingleResult();

      return admins;
   }


   // update member role
   @Transactional
   public ProjectMembersEntity updateMemberRole(UUID id, RoleEnum role) throws NoResultException {
      ProjectMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM ProjectMembersEntity m WHERE m.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      member.setRole(role);

      return member;
   }


   // remove member participation
   @Transactional
   public ProjectMembersEntity removeParticipation(UUID id) throws NoResultException {
      ProjectMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM ProjectMembersEntity m WHERE m.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      member.setDeletedAt(LocalDateTime.now());

      return member;
   }


   // is removed
   public boolean isRemoved(UUID id) throws NoResultException {
      ProjectMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM ProjectMembersEntity m WHERE m.id = :id", ProjectMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return member.isDeleted();
   }

}