
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

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

}