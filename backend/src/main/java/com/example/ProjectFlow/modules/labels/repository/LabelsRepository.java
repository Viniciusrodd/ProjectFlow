
// packages
package com.example.ProjectFlow.modules.labels.repository;

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
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;

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
   public LabelsEntity create(
      LabelsDTO data, 
      ProjectEntity projectEntity
   ) {
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .project(projectEntity)
         .name(data.name())
         .color(data.color())
         .build();

      this.entityManager.persist(labelEntity);

      return labelEntity;
   }


   // get all task labels by project id
   public List<LabelsEntity> getAllByProjectId(UUID projectId) throws NoResultException {
      List<LabelsEntity> labelsEntity = this.entityManager
         .createQuery(
            "SELECT l FROM LabelsEntity l " + 
            "WHERE l.project.id = :projectId " +
            "ORDER BY l.createdAt ASC ",
            LabelsEntity.class
         )
         .setParameter("projectId", projectId)
         .getResultList();

      return labelsEntity;
   }


   // get label by id
   public LabelsEntity getById(UUID id) throws NoResultException {
      LabelsEntity labelEntity = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return labelEntity;
   }


   // get entity by id
   public LabelsEntity getEntityById(UUID id) throws NoResultException {
      LabelsEntity label = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return label;
   }


   // exists by id
   public boolean existsById(UUID id) {
      Long count = this.entityManager
         .createQuery("SELECT COUNT(l) FROM LabelsEntity l WHERE l.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }


   // update label
   @Transactional
   public LabelsEntity update(UUID id, LabelsUpdateDTO data) throws NoResultException {
      LabelsEntity labelEntity = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // update
      Optional.ofNullable(data.name()).ifPresent(name -> labelEntity.setName(name));
      Optional.ofNullable(data.color()).ifPresent(color -> labelEntity.setColor(color));

      return labelEntity;
   }


   // delete label
   @Transactional
   public LabelsEntity delete(UUID id) throws NoResultException {
      LabelsEntity labelEntity = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      // delete
      labelEntity.setDeletedAt(LocalDateTime.now());

      return labelEntity;
   }


   // is deleted
   public boolean isDeleted(UUID id) throws NoResultException {
      LabelsEntity label = this.entityManager
         .createQuery("SELECT l FROM LabelsEntity l WHERE l.id = :id", LabelsEntity.class)
         .setParameter("id", id)
         .getSingleResult();
      
      return label.isDeleted();
   }

}