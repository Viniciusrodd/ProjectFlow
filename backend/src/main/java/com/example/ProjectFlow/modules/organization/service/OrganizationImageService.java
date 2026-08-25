
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
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;


@Service
public class OrganizationImageService {
 
   // properties
   private final OrganizationImageRepository organizationImageRepository;
   private final OrganizationService organizationService;
   private final OrganizationImageValidator organizationImageValidator;
   
   
   // constructor - dependency injection
   public OrganizationImageService(
      OrganizationImageRepository organizationImageRepository,
      OrganizationService organizationService,
      OrganizationImageValidator organizationImageValidator
   ) {
      this.organizationImageRepository = organizationImageRepository;
      this.organizationService = organizationService;
      this.organizationImageValidator = organizationImageValidator;
   }


   // organization image upload
   @Transactional
   public OrganizationImageResponseDTO uploadOrganizationImage(UUID organizationId, MultipartFile file) {
      this.organizationService.existsById(organizationId);
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

         // return saved document
         return OrganizationImageResponseDTO.get(savedDocument);
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
      this.organizationService.existsById(organizationId);

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
   }

}