
// packages
package com.example.ProjectFlow.modules.labels.repository;

import java.util.ArrayList;
// imports
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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

   
   // task labels creation
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


   // get all task labels by project id
   public List<LabelsResponseDTO> getAllByProjectId(UUID projectId) throws NoResultException {
      List<LabelsEntity> labelsDocument = this.entityManager
         .createQuery(
            "SELECT l FROM LabelsEntity l " + 
            "WHERE l.project.id = :projectId " +
            "ORDER BY l.createdAt ASC ",
            LabelsEntity.class
         )
         .setParameter("projectId", projectId)
         .getResultList();

      List<LabelsResponseDTO> labels = new ArrayList<>();

      for(LabelsEntity label : labelsDocument) {
         labels.add(LabelsResponseDTO.get(label));
      }

      return labels;
   }


   // get label by id
   public LabelsResponseDTO getById(UUID id) throws NoResultException {
      LabelsEntity label = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return LabelsResponseDTO.get(label);
   }

}