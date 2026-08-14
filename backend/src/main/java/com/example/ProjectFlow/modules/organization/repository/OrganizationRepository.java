
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationResponseDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationUpdateDTO;
import com.example.ProjectFlow.modules.organization.dto.OrganizationDeletedDTO;

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
   public OrganizationResponseDTO create(OrganizationDTO data, UserEntity ownerEntity) {
      OrganizationEntity organization = new OrganizationEntity();

      organization.setName(data.name());
      organization.setDescription(data.description());
      organization.setOwner(ownerEntity);

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


   // get entity by id
   public OrganizationEntity getEntityById(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return organization;
   }


   // get all by owner id
   public List<OrganizationResponseDTO> getByOwnerId(UUID ownerId) {
      List<OrganizationEntity> organizationsDocument = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.owner.id = :ownerId ORDER BY o.createdAt ASC", OrganizationEntity.class)
         .setParameter("ownerId", ownerId)
         .getResultList();

      List<OrganizationResponseDTO> organizations = new ArrayList<>();

      for(OrganizationEntity organization : organizationsDocument) {
         organizations.add(OrganizationResponseDTO.get(organization));
      }

      return organizations;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(o) FROM OrganizationEntity o WHERE o.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // update logo image id
   @Transactional
   public void updateLogoImageId(UUID id, String logoImageId) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      organization.setLogoImageId(logoImageId);
   }


   // remove logo image id
   @Transactional
   public void removeLogoImageId(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // remove
      organization.setLogoImageId(null);
   }


   // update organization
   @Transactional
   public OrganizationResponseDTO update(
      UUID id, 
      OrganizationUpdateDTO data, 
      Optional<UserEntity> owner
   ) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      owner.ifPresent(ownerId -> organization.setOwner(ownerId));
      Optional.ofNullable(data.name()).ifPresent(name -> organization.setName(name));
      Optional.ofNullable(data.description()).ifPresent(description -> organization.setDescription(description));
   
      return OrganizationResponseDTO.get(organization);
   }


   // delete organization
   @Transactional
   public OrganizationDeletedDTO delete(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      organization.setDeletedAt(LocalDateTime.now());

      return OrganizationDeletedDTO.get(organization);
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return organization.isDeleted();
   }

}