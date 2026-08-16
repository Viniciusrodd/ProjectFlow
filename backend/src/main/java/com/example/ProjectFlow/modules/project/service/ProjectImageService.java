
// packages
package com.example.ProjectFlow.modules.project.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectImageRepository;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectImageResponseDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;


@Service
public class ProjectImageService {
 
   // properties
   private final ProjectService projectService;
   private final ProjectImageRepository projectImageRepository;
   private final ProjectImageValidator projectImageValidator;


   // constructor - dependency injection
   public ProjectImageService(
      ProjectService projectService,
      ProjectImageRepository projectImageRepository,
      ProjectImageValidator projectImageValidator
   ) {
      this.projectService = projectService;
      this.projectImageRepository = projectImageRepository;
      this.projectImageValidator = projectImageValidator;
   }


   // project image upload
   @Transactional
   public ProjectImageResponseDTO uploadProjectImage(UUID projectId, MultipartFile file) {
      this.projectService.existsById(projectId);
      this.projectImageValidator.validate(file);

      try {
         // replace old image for new one
         if(this.projectImageRepository.existsByProjectId(projectId)) {
            this.projectImageRepository.deleteByProjectId(projectId);
         }

         // document - setup
         ProjectImageDocument document = new ProjectImageDocument.Builder()
            .projectId(projectId)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         ProjectImageDocument savedDocument = this.projectImageRepository.save(document);

         // update project image id - mysql
         this.projectService.updateLogoImageId(projectId, savedDocument.getId());

         // return saved document
         return ProjectImageResponseDTO.get(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha ao processar imagem do projeto: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      } 
   }


   // get project image
   public ProjectImageDocument getProjectImage(UUID projectId) {
      this.projectService.existsById(projectId);

      ProjectImageDocument image = this.projectImageRepository.findByProjectId(projectId);
      if(image == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem do projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return image;
   }


   // get all project images
   public List<ProjectImageDocument> getAllProjectImages() {
      List<ProjectImageDocument> images = this.projectImageRepository.findAll();
      if(images.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagens de projetos não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return images;
   }

}