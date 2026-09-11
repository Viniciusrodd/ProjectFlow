
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectUpdateDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;


@Repository
public class ProjectRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // project creation
   @Transactional
   public ProjectEntity create(ProjectDTO data, OrganizationEntity organizationEntity, UserEntity ownerEntity) {
      ProjectEntity project = new ProjectEntity.Builder()
         .organization(organizationEntity)
         .owner(ownerEntity)
         .name(data.name())
         .description(data.description())
         .status(StatusEnum.ACTIVE)
         .build();
         
      this.entityManager.persist(project);

      return project;
   }


   // get all 
   public List<ProjectEntity> getAll() {
      List<ProjectEntity> projectsEntity = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p ORDER BY p.createdAt ASC", ProjectEntity.class)
         .getResultList();

      return projectsEntity;
   }


   // get by id
   public ProjectEntity getById(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return project;
   }


   // get entity by id
   public ProjectEntity getEntityById(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return project;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(p) FROM ProjectEntity p WHERE p.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // get all by organization id
   public List<ProjectEntity> getByOrganizationId(UUID organizationId) {
      List<ProjectEntity> projectsEntity = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.organization.id = :organizationId ORDER BY p.createdAt ASC", ProjectEntity.class)
         .setParameter("organizationId", organizationId)
         .getResultList();

      return projectsEntity;
   }


   // get all by owner id
   public List<ProjectEntity> getByOwnerId(UUID ownerId) {
      List<ProjectEntity> projectsEntity = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.owner.id = :ownerId ORDER BY p.createdAt ASC", ProjectEntity.class)
         .setParameter("ownerId", ownerId)
         .getResultList();

      return projectsEntity;
   }


   // update logo image id
   @Transactional
   public void updateLogoImageId(UUID id, String logoImageId) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      project.setLogoImageId(logoImageId);
   }


   // remove logo image id
   @Transactional
   public void removeLogoImageId(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // remove
      project.setLogoImageId(null);
   }


   // update project
   @Transactional
   public ProjectEntity update(UUID id, ProjectUpdateDTO data) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.name()).ifPresent(name -> project.setName(name));
      Optional.ofNullable(data.description()).ifPresent(description -> project.setDescription(description));
      Optional.ofNullable(data.status()).ifPresent(status -> 
         project.setStatus(StatusEnum.valueOf(status.toUpperCase()))
      );

      return project;
   }


   // delete project
   @Transactional
   public ProjectEntity delete(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      project.setDeletedAt(LocalDateTime.now());

      return project;
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return project.isDeleted();
   }


}