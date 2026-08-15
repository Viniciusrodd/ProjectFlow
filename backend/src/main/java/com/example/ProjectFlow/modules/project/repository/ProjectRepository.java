
// packages
package com.example.ProjectFlow.modules.project.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.ProjectUpdateDTO;
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
   public ProjectResponseDTO create(ProjectDTO data, OrganizationEntity organizationEntity, UserEntity ownerEntity) {
      ProjectEntity project = new ProjectEntity.Builder()
         .organization(organizationEntity)
         .owner(ownerEntity)
         .name(data.name())
         .description(data.description())
         .status(StatusEnum.ACTIVE)
         .build();
         
      this.entityManager.persist(project);

      return ProjectResponseDTO.get(project);
   }


   // get all 
   public List<ProjectResponseDTO> getAll() {
      List<ProjectEntity> projectsDocument = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p ORDER BY p.createdAt ASC", ProjectEntity.class)
         .getResultList();

      List<ProjectResponseDTO> projects = new ArrayList<>();

      for(ProjectEntity project : projectsDocument) {
         projects.add(ProjectResponseDTO.get(project));
      }

      return projects;
   }


   // get by id
   public ProjectResponseDTO getById(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return ProjectResponseDTO.get(project);
   }


   // get entity by id
   public ProjectEntity getEntityById(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return project;
   }


   // get all by organization id
   public List<ProjectResponseDTO> getByOrganizationId(UUID organizationId) {
      List<ProjectEntity> projectsDocument = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.organization.id = :organizationId ORDER BY p.createdAt ASC", ProjectEntity.class)
         .setParameter("organizationId", organizationId)
         .getResultList();

      List<ProjectResponseDTO> projects = new ArrayList<>();

      for(ProjectEntity project : projectsDocument) {
         projects.add(ProjectResponseDTO.get(project));
      }

      return projects;
   }


   // get all by owner id
   public List<ProjectResponseDTO> getByOwnerId(UUID ownerId) {
      List<ProjectEntity> projectsDocument = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.owner.id = :ownerId ORDER BY p.createdAt ASC", ProjectEntity.class)
         .setParameter("ownerId", ownerId)
         .getResultList();

      List<ProjectResponseDTO> projects = new ArrayList<>();

      for(ProjectEntity project : projectsDocument) {
         projects.add(ProjectResponseDTO.get(project));
      }

      return projects;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(p) FROM ProjectEntity p WHERE p.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
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
   public ProjectResponseDTO update(UUID id, ProjectUpdateDTO data) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.name()).ifPresent(name -> project.setName(name));
      Optional.ofNullable(data.description()).ifPresent(description -> project.setDescription(description));

      return ProjectResponseDTO.get(project);
   }


   // update status of project
   @Transactional
   public ProjectResponseDTO updateStatus(UUID id, StatusEnum status) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      project.setStatus(status);

      return ProjectResponseDTO.get(project);
   }


   // delete project
   @Transactional
   public ProjectDeletedDTO delete(UUID id) throws NoResultException {
      ProjectEntity project = this.entityManager
         .createQuery("SELECT p FROM ProjectEntity p WHERE p.id = :id", ProjectEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      project.setDeletedAt(LocalDateTime.now());

      return ProjectDeletedDTO.get(project);
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