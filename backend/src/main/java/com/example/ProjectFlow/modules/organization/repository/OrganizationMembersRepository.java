
// packages
package com.example.ProjectFlow.modules.organization.repository;

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
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersResponseDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@Repository
public class OrganizationMembersRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // create member participation
   @Transactional
   public OrganizationMembersEntity createMemberParticipation(
      OrganizationMembersDTO data,
      UserEntity user,
      OrganizationEntity organization
   ) {
      OrganizationMembersEntity organizationMembers = new OrganizationMembersEntity.Builder()
         .user(user)
         .organization(organization)
         .role(RoleEnum.valueOf(data.role().toUpperCase()))
         .joinedAt(LocalDateTime.now())
         .build();

      this.entityManager.persist(organizationMembers);

      return organizationMembers;
   }


   // get all members
   public List<OrganizationMembersEntity> getAllOrganizationMembers() {
      List<OrganizationMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " + 
            "JOIN FETCH m.user " +
            "ORDER BY m.joinedAt DESC ", 
            OrganizationMembersEntity.class
         )
         .getResultList();

      return membersEntity;
   }


   // get member by relation id
   public OrganizationMembersEntity getOrganizationMemberById(UUID id) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return member;
   }


   // get all members by organization
   public List<OrganizationMembersEntity> getAllMembersByOrganizationId(UUID organizationId) {
      List<OrganizationMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.organization.id = :organizationId " +
            "ORDER BY m.joinedAt DESC", 
            OrganizationMembersEntity.class
         )
         .setParameter("organizationId", organizationId)
         .getResultList();

      return membersEntity;
   }


   // get all members by role
   public List<OrganizationMembersEntity> getAllMembersByRole(
      UUID organizationId,
      RoleEnum role
   ) {
      List<OrganizationMembersEntity> membersEntity = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.organization.id = :organizationId " +
            "AND m.role = :role " +
            "ORDER BY m.joinedAt DESC",
            OrganizationMembersEntity.class
         )
         .setParameter("organizationId", organizationId)
         .setParameter("role", role)
         .getResultList();

      return membersEntity;
   }


   // get entity by id
   public OrganizationMembersEntity getEntityById(UUID id) throws NoResultException {
      OrganizationMembersEntity entity = this.entityManager
         .createQuery("SELECT e FROM OrganizationMembersEntity e WHERE e.id = :id ", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return entity;
   }


   // exists by id
   public boolean existsById(UUID id) throws NoResultException {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(m) FROM OrganizationMembersEntity m WHERE m.id = :id ", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // exists by organization and member id
   public boolean existsByOrganizationIdAndMemberId(UUID organizationId, UUID memberId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM OrganizationMembersEntity m " +
            "WHERE m.organization.id = :organizationId " +
            "AND m.user.id = :memberId ",
            Long.class
         )
         .setParameter("organizationId", organizationId)
         .setParameter("memberId", memberId)
         .getSingleResult();

      return count > 0;
   }


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID organizationId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM OrganizationMembersEntity m " +
            "WHERE m.user.id = :userId " +
            "AND m.organization.id = :organizationId ",
            Long.class
         )
         .setParameter("userId", userId)
         .setParameter("organizationId", organizationId)
         .getSingleResult();

      return count > 0;
   }


   // count admins members by organization
   public Long countAdminsByOrganization(UUID organizationId) {
      Long admins = this.entityManager
         .createQuery(
            "SELECT COUNT(m) FROM OrganizationMembersEntity m " +
            "WHERE m.organization.id = :organizationId " +
            "AND m.role = :role ",
            Long.class
         )
         .setParameter("organizationId", organizationId)
         .setParameter("role", RoleEnum.ADMIN)
         .getSingleResult();

      return admins;
   }


   // update member role
   @Transactional
   public OrganizationMembersEntity updateMemberRole(UUID id, RoleEnum role) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      member.setRole(role);

      return member;
   }


   // remove member participation
   @Transactional
   public OrganizationMembersEntity removeParticipation(UUID id) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      member.setDeletedAt(LocalDateTime.now());

      return member;
   }


   // is removed
   public boolean isRemoved(UUID id) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return member.isDeleted();
   }

}