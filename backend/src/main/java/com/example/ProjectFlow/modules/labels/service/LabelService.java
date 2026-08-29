
// packages
package com.example.ProjectFlow.modules.labels.service;

// imports
import org.springframework.stereotype.Service;

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

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import exceptions

// import constants


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


   // labels creation
   @Transactional
   public LabelsResponseDTO create(LabelsDTO data) {
      this.labelsValidator.projectIdValidate(data.projectId());
      this.labelsValidator.nameValidate(data.name());
      this.labelsValidator.colorValidate(data.color());

      // get project data
      ProjectEntity project = this.projectService.getEntityById(data.projectId());

      return this.labelsRepository.create(data, project);
   }

}