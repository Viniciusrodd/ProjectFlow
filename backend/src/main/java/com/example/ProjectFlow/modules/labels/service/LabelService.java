
// packages
package com.example.ProjectFlow.modules.labels.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.NoResultException;
// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.labels.repository.LabelsRepository;

// import services
import com.example.ProjectFlow.modules.project.service.ProjectService;

// import validator
import com.example.ProjectFlow.modules.labels.validator.LabelsValidator;

// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class LabelService {
 
   // properties
   private final LabelsRepository labelsRepository;
   private final LabelsValidator labelsValidator;
   private final ProjectService projectService;

   // constructor - dependency injection
   public LabelService(
      LabelsRepository labelsRepository,
      LabelsValidator labelsValidator,
      ProjectService projectService
   ) {
      this.labelsRepository = labelsRepository;
      this.labelsValidator = labelsValidator;
      this.projectService = projectService;
   }


   // task labels creation
   @Transactional
   public LabelsResponseDTO create(LabelsDTO data) {
      this.labelsValidator.projectIdValidate(data.projectId());
      this.labelsValidator.nameValidate(data.name());
      this.labelsValidator.colorValidate(data.color());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      return this.labelsRepository.create(data, project);
   }


   // get all task labels by project id
   public List<LabelsResponseDTO> getAllByProjectId(UUID projectId) {
      this.labelsValidator.projectIdValidate(projectId);

      List<LabelsResponseDTO> labels = this.labelsRepository.getAllByProjectId(projectId);

      if(labels.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiquetas do projeto não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return labels;
   }


   // get label by id
   public LabelsResponseDTO getById(UUID id) {
      this.labelsValidator.idValidate(id);

      try {
         return this.labelsRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public LabelsEntity getEntityById(UUID id) {
      this.labelsValidator.idValidate(id);

      try {
         return this.labelsRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.labelsValidator.idValidate(id);

      boolean exist = this.labelsRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Etiqueta de projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return exist;
   }

}