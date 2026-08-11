
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationMembersResponseDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.entity.UserEntity;


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
      organizationMembers.setRole(RoleEnum.valueOf(data.role()));
      organizationMembers.setJoinedAt(LocalDateTime.now());

      this.entityManager.persist(organizationMembers);

      return OrganizationMembersResponseDTO.get(organizationMembers);
   }

}