
// packages
package com.example.ProjectFlow.modules.labels.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;


@Repository
public class LabelsRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;

   
   // labels creation
   @Transactional
   public LabelsResponseDTO create(
      LabelsDTO data, 
      ProjectEntity projectEntity
   ) {
      LabelsEntity labels = new LabelsEntity.Builder()
         .project(projectEntity)
         .name(data.name())
         .color(data.color())
         .build();

      this.entityManager.persist(labels);

      return LabelsResponseDTO.get(labels);
   }

}