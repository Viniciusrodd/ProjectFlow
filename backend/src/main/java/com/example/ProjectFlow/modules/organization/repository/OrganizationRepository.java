
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class OrganizationRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // create organization
   @Transactional
   public OrganizationResponseDTO create(OrganizationDTO data, UserEntity owner) {
      OrganizationEntity organization = new OrganizationEntity();

      organization.setName(data.name());
      organization.setDescription(data.description());
      organization.setOwner(owner);

      this.entityManager.persist(organization);

      return OrganizationResponseDTO.get(organization);
   } 

}