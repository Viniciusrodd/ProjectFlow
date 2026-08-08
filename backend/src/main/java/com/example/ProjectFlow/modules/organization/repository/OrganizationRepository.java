
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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


   // get all
   public List<OrganizationResponseDTO> getAll() {
      List<OrganizationEntity> organizationsDocument = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o ORDER BY o.createdAt ASC", OrganizationEntity.class)
         .getResultList();

      List<OrganizationResponseDTO> organizations = new ArrayList<>();

      for(OrganizationEntity organization : organizationsDocument) {
         organizations.add(OrganizationResponseDTO.get(organization));
      }

      return organizations;
   }


   // get by id
   public OrganizationResponseDTO getById(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return OrganizationResponseDTO.get(organization);
   }

}