
// packages
package com.example.ProjectFlow.modules.organization.service;

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
import com.example.ProjectFlow.modules.organization.repository.OrganizationImageRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;

// import services
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationImageMapper;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@Service
public class OrganizationImageService {
 
   // properties
   private final OrganizationImageRepository organizationImageRepository;
   private final OrganizationService organizationService;
   private final ActivityLogService activityLogService;
   private final OrganizationImageValidator organizationImageValidator;
   private final OrganizationImageMapper organizationImageMapper;
   
   
   // constructor - dependency injection
   public OrganizationImageService(
      OrganizationImageRepository organizationImageRepository,
      OrganizationService organizationService,
      ActivityLogService activityLogService,
      OrganizationImageValidator organizationImageValidator,
      OrganizationImageMapper organizationImageMapper
   ) {
      this.organizationImageRepository = organizationImageRepository;
      this.organizationService = organizationService;
      this.activityLogService = activityLogService;
      this.organizationImageValidator = organizationImageValidator;
      this.organizationImageMapper = organizationImageMapper;
   }


   // organization image upload
   @Transactional
   public OrganizationImageResponseDTO uploadOrganizationImage(UUID organizationId, MultipartFile file) {
      this.organizationImageValidator.validate(file);

      try {
         // replace old image for new one
         if(this.organizationImageRepository.existsByOrganizationId(organizationId)) {
            this.organizationImageRepository.deleteByOrganizationId(organizationId);
         }

         // document - setup
         OrganizationImageDocument document = new OrganizationImageDocument.Builder()
            .organizationId(organizationId)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         OrganizationImageDocument savedDocument = this.organizationImageRepository.save(document);
         
         // update organization image id - mysql
         this.organizationService.updateLogoImageId(organizationId, savedDocument.getId());

         // get organization
         OrganizationEntity organizationEntity = this.organizationService.getEntityById(organizationId);

         // activity log - registering
         ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
            .organizationId(organizationEntity.getId())
            .action(ActivityActionEnum.LOGO_DA_ORGANIZACAO_ATUALIZADO)
            .description("Imagem da organização: " + organizationEntity.getName() + ", criada")
            .build();

         this.activityLogService.create(activityLog);

         // return saved document
         return this.organizationImageMapper.toOrganizationImageResponseDTO(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha ao processar imagem da organização: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get organization image
   public OrganizationImageDocument getOrganizationImage(UUID organizationId) {
      this.organizationService.existsById(organizationId);

      OrganizationImageDocument image = this.organizationImageRepository.findByOrganizationId(organizationId);
      if(image == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return image;
   }


   // get all organization images
   public List<OrganizationImageDocument> getAllOrganizationImages() {
      List<OrganizationImageDocument> images = this.organizationImageRepository.findAll();
      if(images.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagens de organizações não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return images;
   }


   // delete organization image
   public void deleteOrganizationImage(UUID organizationId) {
      // organization image existence - validation
      if(this.organizationImageRepository.findByOrganizationId(organizationId) == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de organização não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // delete organization image id - mysql
      this.organizationService.removeLogoImageId(organizationId);

      // delete organization image - mongodb
      this.organizationImageRepository.deleteByOrganizationId(organizationId);

      // get organization
      OrganizationEntity organizationEntity = this.organizationService.getEntityById(organizationId);

      // activity log - registering
      ActivityLogDTO activityLog = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .action(ActivityActionEnum.LOGO_DA_ORGANIZACAO_REMOVIDO)
         .description("Imagem da organização: " + organizationEntity.getName() + ", removida")
         .build();

      this.activityLogService.create(activityLog);
   }

}