
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
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDeletedDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersCompleteResponseDTO;

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
      OrganizationMembersEntity organizationMembers = new OrganizationMembersEntity();

      organizationMembers.setUser(user);
      organizationMembers.setOrganization(organization);
      organizationMembers.setRole(RoleEnum.valueOf(data.role().toUpperCase()));
      organizationMembers.setJoinedAt(LocalDateTime.now());

      this.entityManager.persist(organizationMembers);

      return OrganizationMembersResponseDTO.get(organizationMembers);
   }


   // get all organization members
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByOrganizationId(UUID organizationId) {
      List<OrganizationMembersEntity> members = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.organization.id = :organizationId " +
            "ORDER BY m.joinedAt DESC", 
            OrganizationMembersEntity.class
         )
         .setParameter("organizationId", organizationId)
         .getResultList();

      List<OrganizationMembersCompleteResponseDTO> allMembers = new ArrayList<>();
      
      for(OrganizationMembersEntity member : members) {
         allMembers.add(OrganizationMembersCompleteResponseDTO.get(member));
      }

      return allMembers;
   }


   // get all members by role
   public List<OrganizationMembersCompleteResponseDTO> getAllMembersByRole(
      UUID organizationId,
      RoleEnum role
   ) {
      List<OrganizationMembersEntity> members = this.entityManager
         .createQuery(
            "SELECT m FROM OrganizationMembersEntity m " +
            "JOIN FETCH m.user " +
            "WHERE m.organization.id = :organizationId " +
            "AND m.role = :role ",
            OrganizationMembersEntity.class
         )
         .setParameter("organizationId", organizationId)
         .setParameter("role", role)
         .getResultList();

      List<OrganizationMembersCompleteResponseDTO> allMembers = new ArrayList<>();
      
      for(OrganizationMembersEntity member : members) {
         allMembers.add(OrganizationMembersCompleteResponseDTO.get(member));
      }

      return allMembers;
   }


   // get entity by id
   public OrganizationMembersEntity getEntityById(UUID id) {
      OrganizationMembersEntity member = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id ", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return member;
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


   // delete member participation
   @Transactional
   public OrganizationMembersDeletedDTO delete(UUID id) throws NoResultException {
      OrganizationMembersEntity participation = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      participation.setDeletedAt(LocalDateTime.now());

      return OrganizationMembersDeletedDTO.get(participation);
   }


   // is deleted
   public boolean isDeleted(UUID id) {
      OrganizationMembersEntity participation = this.entityManager
         .createQuery("SELECT m FROM OrganizationMembersEntity m WHERE m.id = :id", OrganizationMembersEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return participation.isDeleted();
   }

}