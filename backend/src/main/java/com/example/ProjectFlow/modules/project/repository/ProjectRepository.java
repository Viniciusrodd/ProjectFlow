
// packages
package com.example.ProjectFlow.modules.project.repository;

import java.util.ArrayList;
import java.util.List;
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
import com.example.ProjectFlow.modules.project.dto.ProjectResponseDTO;

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


}