
// packages
package com.example.ProjectFlow.modules.organization.repository;

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
import com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO.OrganizationMembersCompleteResponseDTO;
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
   public OrganizationMembersResponseDTO createMemberParticipation(
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

      return OrganizationMembersResponseDTO.get(organizationMembers);
   }


   // get all organization members
   public List<OrganizationMembersResponseDTO> getAllOrganizationMembers() {
      List<OrganizationMembersEntity> membersDocument = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m ORDER BY m.joinedAt DESC", OrganizationMembersEntity.class)
         .getResultList();

      List<OrganizationMembersResponseDTO> members = new ArrayList<>();

      for(OrganizationMembersEntity memberDocument : membersDocument) {
         members.add(OrganizationMembersResponseDTO.get(memberDocument));
      }

      return members;
   }


   // get all members by organization
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByOrganizationId(UUID organizationId) {
      List<OrganizationMembersEntity> membersDocument = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.organization.id = :organizationId " +
            "ORDER BY m.joinedAt DESC", 
            OrganizationMembersEntity.class
         )
         .setParameter("organizationId", organizationId)
         .getResultList();

      List<OrganizationMembersCompleteResponseDTO> members = new ArrayList<>();
      
      for(OrganizationMembersEntity memberDocument : membersDocument) {
         members.add(OrganizationMembersCompleteResponseDTO.get(memberDocument));
      }

      return members;
   }


   // get all members by role
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByRole(
      UUID organizationId,
      RoleEnum role
   ) {
      List<OrganizationMembersEntity> membersDocument = this.entityManager
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

      List<OrganizationMembersCompleteResponseDTO> members = new ArrayList<>();
      
      for(OrganizationMembersEntity memberDocument : membersDocument) {
         members.add(OrganizationMembersCompleteResponseDTO.get(memberDocument));
      }

      return members;
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
   public OrganizationMembersResponseDTO updateMemberRole(UUID id, RoleEnum role) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      member.setRole(role);

      return OrganizationMembersResponseDTO.get(member);
   }


   // remove member participation
   @Transactional
   public OrganizationMembersDeletedDTO removeParticipation(UUID id) throws NoResultException {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      member.setDeletedAt(LocalDateTime.now());

      return OrganizationMembersDeletedDTO.get(member);
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