
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

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectImageValidator;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;

// import DTO
import com.example.ProjectFlow.modules.project.dto.projectImageDTO.ProjectImageResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectImageMapper;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@Service
public class ProjectImageService {
 
   // properties
   private final ProjectService projectService;
   private final ActivityLogService activityLogService;
   private final ProjectImageRepository projectImageRepository;
   private final ProjectImageValidator projectImageValidator;
   private final ProjectImageMapper projectImageMapper;


   // constructor - dependency injection
   public ProjectImageService(
      ProjectService projectService,
      ActivityLogService activityLogService,
      ProjectImageRepository projectImageRepository,
      ProjectImageValidator projectImageValidator,
      ProjectImageMapper projectImageMapper
   ) {
      this.projectService = projectService;
      this.activityLogService = activityLogService;
      this.projectImageRepository = projectImageRepository;
      this.projectImageValidator = projectImageValidator;
      this.projectImageMapper = projectImageMapper;
   }


   // project image upload
   @Transactional
   public ProjectImageResponseDTO uploadProjectImage(UUID projectId, MultipartFile file) {
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

         // get project
         ProjectEntity projectEntity = this.projectService.getEntityById(projectId);

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO(
            null,
            projectEntity.getId(), 
            null, null, null, null,
            ActivityActionEnum.PROJECT_LOGO_UPDATED,
            "Imagem do projeto: " + projectEntity.getName() + ", atualizada"
         );
         this.activityLogService.create(activityLog);

         // return saved document
         return this.projectImageMapper.toProjectImageResponseDTO(savedDocument);
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
            "%s: Imagem de projeto não existe",
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


   // delete project image
   public void deleteProjectImage(UUID projectId) {
      // project image existence - validation
      if(this.projectImageRepository.findByProjectId(projectId) == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de projeto não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // delete project image id - mysql
      this.projectService.removeLogoImageId(projectId);

      // delete project image - mongodb
      this.projectImageRepository.deleteByProjectId(projectId);

      // get project
      ProjectEntity projectEntity = this.projectService.getEntityById(projectId);

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO(
         null,
         projectEntity.getId(), 
         null, null, null, null,
         ActivityActionEnum.PROJECT_LOGO_REMOVED,
         "Imagem do projeto: " + projectEntity.getName() + ", removida"
      );
      this.activityLogService.create(activityLog);
   }

}