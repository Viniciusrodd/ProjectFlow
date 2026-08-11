
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

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
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
            "SELECT om FROM OrganizationMembersEntity om " +
            "JOIN FETCH om.user " +
            "WHERE om.organization.id = :organizationId " +
            "AND om.deletedAt IS NULL " +
            "ORDER BY om.joinedAt DESC", 
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
            "SELECT om FROM OrganizationMembersEntity om " +
            "JOIN FETCH om.user " +
            "WHERE om.organization.id = :organizationId " +
            "AND om.role = :role " +
            "AND om.deletedAt IS NULL",
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


   // check if user is a membership
   public boolean checkUserMembership(UUID userId, UUID organizationId) {
      Long count = this.entityManager
         .createQuery(
            "SELECT COUNT(om) FROM OrganizationMembersEntity om " +
            "WHERE om.user.id = :userId " +
            "AND om.organization.id = :organizationId " +
            "AND om.deletedAt IS NULL",
            Long.class
         )
         .setParameter("userId", userId)
         .setParameter("organizationId", organizationId)
         .getSingleResult();

      return count > 0;
   }


}