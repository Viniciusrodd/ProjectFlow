
// packages
package com.example.ProjectFlow.modules.organization.repository;

// imports
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationUpdateDTO;

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
   public OrganizationEntity create(OrganizationDTO data, UserEntity ownerEntity) {
      OrganizationEntity organization = new OrganizationEntity.Builder()
         .owner(ownerEntity)
         .name(data.name())
         .description(data.description())
         .build();

      this.entityManager.persist(organization);

      return organization;
   }


   // get all
   public List<OrganizationEntity> getAll() {
      List<OrganizationEntity> organizationsEntity = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o ORDER BY o.createdAt ASC", OrganizationEntity.class)
         .getResultList();

      return organizationsEntity;
   }


   // get by id
   public OrganizationEntity getById(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return organization;
   }


   // get all by owner id
   public List<OrganizationEntity> getByOwnerId(UUID ownerId) {
      List<OrganizationEntity> organizationsEntity = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.owner.id = :ownerId ORDER BY o.createdAt ASC", OrganizationEntity.class)
         .setParameter("ownerId", ownerId)
         .getResultList();

      return organizationsEntity;
   }


   // get entity by id
   public OrganizationEntity getEntityById(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return organization;
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
   public OrganizationEntity update(
      UUID id, 
      OrganizationUpdateDTO data
   ) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.name()).ifPresent(name -> organization.setName(name));
      Optional.ofNullable(data.description()).ifPresent(description -> organization.setDescription(description));
   
      return organization;
   }


   // delete organization
   @Transactional
   public OrganizationEntity delete(UUID id) throws NoResultException {
      OrganizationEntity organization = this.entityManager
         .createQuery("SELECT o FROM OrganizationEntity o WHERE o.id = :id", OrganizationEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      organization.setDeletedAt(LocalDateTime.now());

      return organization;
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